package ru.stroy1click.attribute.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import ru.stroy1click.attribute.cache.CacheClear;
import ru.stroy1click.attribute.dto.ProductTypeAttributeValueDto;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.entity.ProductTypeAttributeValue;
import ru.stroy1click.attribute.exception.NotFoundException;
import ru.stroy1click.attribute.mapper.ProductTypeAttributeValueMapper;
import ru.stroy1click.attribute.repository.ProductTypeAttributeValueRepository;
import ru.stroy1click.attribute.service.product.type.impl.ProductTypeAttributeValueServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductTypeAttributeValueTest {

    @Mock
    private ProductTypeAttributeValueRepository productTypeAttributeValueRepository;

    @Mock
    private ProductTypeAttributeValueMapper mapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CacheClear cacheClear;

    @InjectMocks
    private ProductTypeAttributeValueServiceImpl productTypeAttributeValueService;

    private ProductTypeAttributeValue entity;
    private ProductTypeAttributeValueDto dto;
    private Integer productTypeId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.productTypeId = 10;

        Attribute attribute = Attribute.builder()
                .id(5)
                .title("Color")
                .build();

        this.entity = ProductTypeAttributeValue.builder()
                .id(1)
                .value("White")
                .attribute(attribute)
                .productTypeId(10)
                .build();

        this.dto = new ProductTypeAttributeValueDto(
                1, attribute.getId(), this.productTypeId, "White"
        );

        when(this.messageSource.getMessage(anyString(), any(), any()))
                .thenReturn("Not found");
    }

    @Test
    public void get_ShouldReturnDto_WhenEntityExists() {
        when(this.productTypeAttributeValueRepository.findById(1))
                .thenReturn(Optional.of(this.entity));
        when(this.mapper.toDto(this.entity)).thenReturn(this.dto);

        ProductTypeAttributeValueDto result = this.productTypeAttributeValueService.get(1);

        assertEquals(this.dto, result);
        verify(this.productTypeAttributeValueRepository).findById(1);
        verify(this.mapper).toDto(this.entity);
    }

    @Test
    public void get_ShouldThrowNotFoundException_WhenEntityDoesNotExist() {
        when(this.productTypeAttributeValueRepository.findById(1))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.productTypeAttributeValueService.get(1));

        assertEquals("Not found", exception.getMessage());
        verify(this.productTypeAttributeValueRepository).findById(1);
        verify(this.mapper, never()).toDto(anyList());
    }

    @Test
    public void getAllByProductId_ShouldReturnMappedDtos_WhenEntitiesExist() {
        when(this.productTypeAttributeValueRepository.findByProductTypeId(10))
                .thenReturn(List.of(this.entity));
        when(this.mapper.toDto(this.entity)).thenReturn(this.dto);

        List<ProductTypeAttributeValueDto> result =
                this.productTypeAttributeValueService.getAllByProductId(10);

        assertEquals(1, result.size());
        assertEquals(this.dto, result.get(0));
        verify(this.productTypeAttributeValueRepository).findByProductTypeId(10);
        verify(this.mapper).toDto(this.entity);
    }

    @Test
    public void getAllByProductId_ShouldReturnEmptyList_WhenNoEntitiesExist() {
        when(this.productTypeAttributeValueRepository.findByProductTypeId(10))
                .thenReturn(List.of());

        List<ProductTypeAttributeValueDto> result =
                this.productTypeAttributeValueService.getAllByProductId(10);

        assertTrue(result.isEmpty());
        verify(this.productTypeAttributeValueRepository).findByProductTypeId(10);
        verify(this.mapper, never()).toDto(anyList());
    }

    @Test
    public void getByValue_ShouldReturnOptional_WhenExists() {
        when(this.productTypeAttributeValueRepository.findByValue("White"))
                .thenReturn(Optional.of(this.entity));

        Optional<ProductTypeAttributeValue> result =
                this.productTypeAttributeValueService.getByValue("White");

        assertTrue(result.isPresent());
        assertEquals(this.entity, result.get());
        verify(this.productTypeAttributeValueRepository).findByValue("White");
    }

    @Test
    public void getByValue_ShouldReturnEmptyOptional_WhenNotExists() {
        when(this.productTypeAttributeValueRepository.findByValue("NonExistent"))
                .thenReturn(Optional.empty());

        Optional<ProductTypeAttributeValue> result =
                this.productTypeAttributeValueService.getByValue("NonExistent");

        assertFalse(result.isPresent());
        verify(this.productTypeAttributeValueRepository).findByValue("NonExistent");
    }

    @Test
    public void create_ShouldSaveEntity_WhenDtoIsValid() {
        when(this.mapper.toEntity(this.dto)).thenReturn(this.entity);

        this.productTypeAttributeValueService.create(this.dto);

        verify(this.mapper).toEntity(this.dto);
        verify(this.productTypeAttributeValueRepository).save(this.entity);
    }

    @Test
    public void delete_ShouldRemoveEntity_WhenExists() {
        when(this.productTypeAttributeValueRepository.findById(1))
                .thenReturn(Optional.of(this.entity));

        this.productTypeAttributeValueService.delete(1);

        verify(this.productTypeAttributeValueRepository).findById(1);
        verify(this.cacheClear).clearAllProductTypeAttributeValuesByProductTypeId(10);
        verify(this.productTypeAttributeValueRepository).delete(this.entity);
    }

    @Test
    public void delete_ShouldThrowNotFoundException_WhenEntityDoesNotExist() {
        when(this.productTypeAttributeValueRepository.findById(1))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.productTypeAttributeValueService.delete(1));

        assertEquals("Not found", exception.getMessage());
        verify(this.productTypeAttributeValueRepository).findById(1);
        verify(this.productTypeAttributeValueRepository, never()).delete(any());
        verify(this.cacheClear, never()).clearAllProductTypeAttributeValuesByProductTypeId(anyInt());
    }

    @Test
    public void update_ShouldSaveUpdatedEntity_WhenExists() {
        ProductTypeAttributeValueDto updateDto = new ProductTypeAttributeValueDto(
                1, 5, 10, "Red"
        );

        when(this.productTypeAttributeValueRepository.findById(1))
                .thenReturn(Optional.of(this.entity));

        this.productTypeAttributeValueService.update(1, updateDto);

        verify(this.productTypeAttributeValueRepository).findById(1);
        ArgumentCaptor<ProductTypeAttributeValue> captor =
                ArgumentCaptor.forClass(ProductTypeAttributeValue.class);
        verify(this.productTypeAttributeValueRepository).save(captor.capture());

        ProductTypeAttributeValue saved = captor.getValue();
        assertEquals("Red", saved.getValue());
        assertEquals(this.entity.getProductTypeId(), saved.getProductTypeId());
        assertEquals(this.entity.getAttribute(), saved.getAttribute());
    }

    @Test
    public void update_ShouldNotChangeProductTypeAndAttribute_WhenUpdatingValue() {
        ProductTypeAttributeValueDto updateDto = new ProductTypeAttributeValueDto(
                1, 999, 888, "Red"
        );

        when(this.productTypeAttributeValueRepository.findById(1))
                .thenReturn(Optional.of(this.entity));

        this.productTypeAttributeValueService.update(1, updateDto);

        ArgumentCaptor<ProductTypeAttributeValue> captor =
                ArgumentCaptor.forClass(ProductTypeAttributeValue.class);
        verify(this.productTypeAttributeValueRepository).save(captor.capture());

        ProductTypeAttributeValue saved = captor.getValue();
        assertEquals("Red", saved.getValue());
        assertEquals(10, saved.getProductTypeId());
        assertEquals(5, saved.getAttribute().getId());
    }

    @Test
    public void update_ShouldThrowNotFoundException_WhenEntityDoesNotExist() {
        ProductTypeAttributeValueDto updateDto = new ProductTypeAttributeValueDto(
                1, 5, 10, "Black"
        );

        when(this.productTypeAttributeValueRepository.findById(1))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.productTypeAttributeValueService.update(1, updateDto));

        assertEquals("Not found", exception.getMessage());
        verify(this.productTypeAttributeValueRepository, never()).save(any());
        verify(this.cacheClear, never()).clearAllProductTypeAttributeValuesByProductTypeId(anyInt());
    }

    @Test
    public void update_ShouldThrowException_WhenDtoIsNull() {
        when(this.productTypeAttributeValueRepository.findById(1))
                .thenReturn(Optional.of(this.entity));

        assertThrows(NullPointerException.class,
                () -> this.productTypeAttributeValueService.update(1, null));
    }
}

