package ru.stroy1click.attribute.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import ru.stroy1click.attribute.cache.CacheClear;
import ru.stroy1click.attribute.dto.AttributeDto;
import ru.stroy1click.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.attribute.dto.AttributeOptionDto;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.entity.AttributeOption;
import ru.stroy1click.attribute.exception.NotFoundException;
import ru.stroy1click.attribute.mapper.ProductAttributeAssignment;
import ru.stroy1click.attribute.repository.ProductAttributeAssignmentRepository;
import ru.stroy1click.attribute.service.AttributeOptionService;
import ru.stroy1click.attribute.service.impl.ProductAttributeAssignmentImpl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductAttributeAssignmentTest {

    @Mock
    private ProductAttributeAssignmentRepository productAttributeAssignmentRepository;

    @Mock
    private ProductAttributeAssignment mapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CacheClear cacheClear;

    @Mock
    private AttributeOptionService attributeOptionService;

    @InjectMocks
    private ProductAttributeAssignmentImpl productAttributeValueService;

    private ru.stroy1click.attribute.entity.ProductAttributeAssignment productAttributeAssignment;
    private ProductAttributeAssignmentDto productAttributeAssignmentDto;
    private AttributeOption attributeOption;
    private AttributeOptionDto attributeOptionDto;
    private Integer productId;
    private Attribute attribute;
    private AttributeDto attributeDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.productId = 10;

        this.attribute = Attribute.builder()
                .id(20)
                .title("Color")
                .build();

        this.productAttributeAssignment = ru.stroy1click.attribute.entity.ProductAttributeAssignment.builder()
                .id(1)
                .productId(10)
                .attributeOption(this.attributeOption)
                .build();

        this.attributeOption = AttributeOption.builder()
                .id(1)
                .value("Test Value")
                .attribute(attribute)
                .productTypeId(1)
                .productAttributeAssignments(List.of(this.productAttributeAssignment))
                .build();

        this.attributeDto = AttributeDto.builder()
                .id(20)
                .title("Color")
                .build();

        this.attributeOptionDto = AttributeOptionDto.builder()
                .id(1)
                .attributeId(this.attribute.getId())
                .productTypeId(1)
                .value("Test Value")
                .build();

        this.productAttributeAssignmentDto = ProductAttributeAssignmentDto.builder()
                .id(1)
                .productTypeAttributeValueId(1)
                .productId(10)
                .build();

        when(this.messageSource.getMessage(anyString(), any(), any(Locale.class)))
                .thenReturn("Product attribute value not found");
    }

    @Test
    public void get_ShouldReturnDto_WhenExists() {
        when(this.productAttributeAssignmentRepository.findById(1)).thenReturn(Optional.of(this.productAttributeAssignment));
        when(this.mapper.toDto(this.productAttributeAssignment)).thenReturn(this.productAttributeAssignmentDto);

        ProductAttributeAssignmentDto result = this.productAttributeValueService.get(1);

        assertNotNull(result);
        verify(this.productAttributeAssignmentRepository).findById(1);
        verify(this.mapper).toDto(this.productAttributeAssignment);
    }

    @Test
    public void get_ShouldThrowNotFoundException_WhenNotExists() {
        when(this.productAttributeAssignmentRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> this.productAttributeValueService.get(1));

        assertEquals("Product attribute value not found", exception.getMessage());
        verify(this.productAttributeAssignmentRepository).findById(1);
        verify(this.mapper, never()).toDto(anyList());
    }

    @Test
    public void getAllByProductId_ShouldReturnList_WhenValuesExist() {
        when(this.productAttributeAssignmentRepository.findByProductId(10))
                .thenReturn(List.of(this.productAttributeAssignment));
        when(this.mapper.toDto(this.productAttributeAssignment)).thenReturn(this.productAttributeAssignmentDto);

        List<ProductAttributeAssignmentDto> result = this.productAttributeValueService.getAllByProductId(10);

        assertEquals(1, result.size());
        verify(this.productAttributeAssignmentRepository).findByProductId(10);
        verify(this.mapper).toDto(this.productAttributeAssignment);
    }

    @Test
    public void getAllByProductId_ShouldReturnEmptyList_WhenNoValuesExist() {
        when(this.productAttributeAssignmentRepository.findByProductId(10)).thenReturn(List.of());

        List<ProductAttributeAssignmentDto> result = this.productAttributeValueService.getAllByProductId(10);

        assertTrue(result.isEmpty());
        verify(this.productAttributeAssignmentRepository).findByProductId(10);
        verify(this.mapper, never()).toDto(anyList());
    }

    @Test
    public void create_ShouldSaveEntity_WhenDtoIsValid() {
        ru.stroy1click.attribute.entity.ProductAttributeAssignment entity = this.productAttributeAssignment;
        when(this.mapper.toEntity(this.productAttributeAssignmentDto)).thenReturn(entity);
        when(this.attributeOptionService.get(this.productAttributeAssignmentDto.getProductTypeAttributeValueId()))
                .thenReturn(this.attributeOptionDto);

        this.productAttributeValueService.create(this.productAttributeAssignmentDto);

        verify(this.mapper).toEntity(this.productAttributeAssignmentDto);
        verify(this.productAttributeAssignmentRepository).save(entity);
        verify(this.attributeOptionService).get(this.productAttributeAssignmentDto.getProductTypeAttributeValueId());
    }

    @Test
    public void create_ShouldThrowNotFoundException_WhenAttributeNotExists(){
        ru.stroy1click.attribute.entity.ProductAttributeAssignment entity = this.productAttributeAssignment;
        when(this.mapper.toEntity(this.productAttributeAssignmentDto)).thenReturn(entity);
        when(this.attributeOptionService.get(this.productAttributeAssignmentDto.getProductTypeAttributeValueId()))
                .thenThrow(new NotFoundException("Атрибут не найден"));

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            this.productAttributeValueService.create(this.productAttributeAssignmentDto);
        });

        assertEquals("Атрибут не найден", notFoundException.getMessage());
    }

    @Test
    public void delete_ShouldDeleteEntityAndClearCache_WhenExists() {
        when(this.productAttributeAssignmentRepository.findById(1)).thenReturn(Optional.of(this.productAttributeAssignment));

        this.productAttributeValueService.delete(1);

        verify(this.cacheClear).clearAllProductAttributeValuesByProductId(10);
        verify(this.productAttributeAssignmentRepository).delete(this.productAttributeAssignment);
    }

    @Test
    public void delete_ShouldThrowNotFoundException_WhenNotExists() {
        when(this.productAttributeAssignmentRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> this.productAttributeValueService.delete(1));

        assertEquals("Product attribute value not found", exception.getMessage());
        verify(this.productAttributeAssignmentRepository).findById(1);
        verify(this.productAttributeAssignmentRepository, never()).delete(any(ru.stroy1click.attribute.entity.ProductAttributeAssignment.class));
        verify(this.cacheClear, never()).clearAllProductAttributeValuesByProductId(any());
    }

    @Test
    public void update_ShouldSaveUpdatedEntity_WhenExists() {
        ru.stroy1click.attribute.entity.ProductAttributeAssignment existing = this.productAttributeAssignment;
        ProductAttributeAssignmentDto updateDto = new ProductAttributeAssignmentDto(1,555,1);

        when(this.productAttributeAssignmentRepository.findById(1)).thenReturn(Optional.of(existing));

        this.productAttributeValueService.update(1, updateDto);

        ArgumentCaptor<ru.stroy1click.attribute.entity.ProductAttributeAssignment> captor = ArgumentCaptor.forClass(ru.stroy1click.attribute.entity.ProductAttributeAssignment.class);
        verify(this.productAttributeAssignmentRepository).save(captor.capture());

        ru.stroy1click.attribute.entity.ProductAttributeAssignment saved = captor.getValue();
        assertEquals(555, saved.getProductId());
    }

    @Test
    public void update_ShouldThrowNotFoundException_WhenNotExists() {
        when(this.productAttributeAssignmentRepository.findById(100)).thenReturn(Optional.empty());
        ProductAttributeAssignmentDto updateDto = new ProductAttributeAssignmentDto(100,555,1);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> this.productAttributeValueService.update(100, updateDto));

        assertEquals("Product attribute value not found", exception.getMessage());
        verify(this.productAttributeAssignmentRepository).findById(100);
        verify(this.productAttributeAssignmentRepository, never()).save(any());
        verify(this.cacheClear, never()).clearAllProductAttributeValuesByProductId(any());
    }
}