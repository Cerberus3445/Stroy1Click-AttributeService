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
import ru.stroy1click.attribute.dto.ProductAttributeDto;
import ru.stroy1click.attribute.dto.ProductTypeAttributeValueDto;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.entity.ProductAttribute;
import ru.stroy1click.attribute.entity.ProductTypeAttributeValue;
import ru.stroy1click.attribute.exception.NotFoundException;
import ru.stroy1click.attribute.mapper.ProductAttributeMapper;
import ru.stroy1click.attribute.repository.ProductAttributeRepository;
import ru.stroy1click.attribute.service.ProductTypeAttributeValueService;
import ru.stroy1click.attribute.service.impl.ProductAttributeServiceImpl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductAttributeTest {

    @Mock
    private ProductAttributeRepository productAttributeRepository;

    @Mock
    private ProductAttributeMapper mapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CacheClear cacheClear;

    @Mock
    private ProductTypeAttributeValueService productTypeAttributeValueService;

    @InjectMocks
    private ProductAttributeServiceImpl productAttributeValueService;

    private ProductAttribute productAttribute;
    private ProductAttributeDto productAttributeDto;
    private ProductTypeAttributeValue productTypeAttributeValue;
    private ProductTypeAttributeValueDto productTypeAttributeValueDto;
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

        this.productAttribute = ProductAttribute.builder()
                .id(1)
                .productId(10)
                .productTypeAttributeValue(this.productTypeAttributeValue)
                .build();

        this.productTypeAttributeValue = ProductTypeAttributeValue.builder()
                .id(1)
                .value("Test Value")
                .attribute(attribute)
                .productTypeId(1)
                .productAttributes(List.of(this.productAttribute))
                .build();

        this.attributeDto = AttributeDto.builder()
                .id(20)
                .title("Color")
                .build();

        this.productTypeAttributeValueDto = ProductTypeAttributeValueDto.builder()
                .id(1)
                .attributeId(this.attribute.getId())
                .productTypeId(1)
                .value("Test Value")
                .build();

        this.productAttributeDto = ProductAttributeDto.builder()
                .id(1)
                .productTypeAttributeValueId(1)
                .productId(10)
                .build();

        when(this.messageSource.getMessage(anyString(), any(), any(Locale.class)))
                .thenReturn("Product attribute value not found");
    }

    @Test
    public void get_ShouldReturnDto_WhenExists() {
        when(this.productAttributeRepository.findById(1)).thenReturn(Optional.of(this.productAttribute));
        when(this.mapper.toDto(this.productAttribute)).thenReturn(this.productAttributeDto);

        ProductAttributeDto result = this.productAttributeValueService.get(1);

        assertNotNull(result);
        verify(this.productAttributeRepository).findById(1);
        verify(this.mapper).toDto(this.productAttribute);
    }

    @Test
    public void get_ShouldThrowNotFoundException_WhenNotExists() {
        when(this.productAttributeRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> this.productAttributeValueService.get(1));

        assertEquals("Product attribute value not found", exception.getMessage());
        verify(this.productAttributeRepository).findById(1);
        verify(this.mapper, never()).toDto(anyList());
    }

    @Test
    public void getAllByProductId_ShouldReturnList_WhenValuesExist() {
        when(this.productAttributeRepository.findByProductId(10))
                .thenReturn(List.of(this.productAttribute));
        when(this.mapper.toDto(this.productAttribute)).thenReturn(this.productAttributeDto);

        List<ProductAttributeDto> result = this.productAttributeValueService.getAllByProductId(10);

        assertEquals(1, result.size());
        verify(this.productAttributeRepository).findByProductId(10);
        verify(this.mapper).toDto(this.productAttribute);
    }

    @Test
    public void getAllByProductId_ShouldReturnEmptyList_WhenNoValuesExist() {
        when(this.productAttributeRepository.findByProductId(10)).thenReturn(List.of());

        List<ProductAttributeDto> result = this.productAttributeValueService.getAllByProductId(10);

        assertTrue(result.isEmpty());
        verify(this.productAttributeRepository).findByProductId(10);
        verify(this.mapper, never()).toDto(anyList());
    }

    @Test
    public void create_ShouldSaveEntity_WhenDtoIsValid() {
        ProductAttribute entity = this.productAttribute;
        when(this.mapper.toEntity(this.productAttributeDto)).thenReturn(entity);
        when(this.productTypeAttributeValueService.get(this.productAttributeDto.getProductTypeAttributeValueId()))
                .thenReturn(this.productTypeAttributeValueDto);

        this.productAttributeValueService.create(this.productAttributeDto);

        verify(this.mapper).toEntity(this.productAttributeDto);
        verify(this.productAttributeRepository).save(entity);
        verify(this.productTypeAttributeValueService).get(this.productAttributeDto.getProductTypeAttributeValueId());
    }

    @Test
    public void create_ShouldThrowNotFoundException_WhenAttributeNotExists(){
        ProductAttribute entity = this.productAttribute;
        when(this.mapper.toEntity(this.productAttributeDto)).thenReturn(entity);
        when(this.productTypeAttributeValueService.get(this.productAttributeDto.getProductTypeAttributeValueId()))
                .thenThrow(new NotFoundException("Атрибут не найден"));

        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            this.productAttributeValueService.create(this.productAttributeDto);
        });

        assertEquals("Атрибут не найден", notFoundException.getMessage());
    }

    @Test
    public void delete_ShouldDeleteEntityAndClearCache_WhenExists() {
        when(this.productAttributeRepository.findById(1)).thenReturn(Optional.of(this.productAttribute));

        this.productAttributeValueService.delete(1);

        verify(this.cacheClear).clearAllProductAttributeValuesByProductId(10);
        verify(this.productAttributeRepository).delete(this.productAttribute);
    }

    @Test
    public void delete_ShouldThrowNotFoundException_WhenNotExists() {
        when(this.productAttributeRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> this.productAttributeValueService.delete(1));

        assertEquals("Product attribute value not found", exception.getMessage());
        verify(this.productAttributeRepository).findById(1);
        verify(this.productAttributeRepository, never()).delete(any(ProductAttribute.class));
        verify(this.cacheClear, never()).clearAllProductAttributeValuesByProductId(any());
    }

    @Test
    public void update_ShouldSaveUpdatedEntity_WhenExists() {
        ProductAttribute existing = this.productAttribute;
        ProductAttributeDto updateDto = new ProductAttributeDto(1,555,1);

        when(this.productAttributeRepository.findById(1)).thenReturn(Optional.of(existing));

        this.productAttributeValueService.update(1, updateDto);

        ArgumentCaptor<ProductAttribute> captor = ArgumentCaptor.forClass(ProductAttribute.class);
        verify(this.productAttributeRepository).save(captor.capture());

        ProductAttribute saved = captor.getValue();
        assertEquals(555, saved.getProductId());
    }

    @Test
    public void update_ShouldThrowNotFoundException_WhenNotExists() {
        when(this.productAttributeRepository.findById(100)).thenReturn(Optional.empty());
        ProductAttributeDto updateDto = new ProductAttributeDto(100,555,1);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> this.productAttributeValueService.update(100, updateDto));

        assertEquals("Product attribute value not found", exception.getMessage());
        verify(this.productAttributeRepository).findById(100);
        verify(this.productAttributeRepository, never()).save(any());
        verify(this.cacheClear, never()).clearAllProductAttributeValuesByProductId(any());
    }
}