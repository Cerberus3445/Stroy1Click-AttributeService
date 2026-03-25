package ru.stroy1click.attribute.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.stroy1click.attribute.cache.CacheClear;
import ru.stroy1click.attribute.dto.AttributeOptionDto;
import ru.stroy1click.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.entity.AttributeOption;
import ru.stroy1click.attribute.entity.ProductAttributeAssignment;
import ru.stroy1click.attribute.mapper.ProductAttributeAssignmentMapper;
import ru.stroy1click.attribute.repository.ProductAttributeAssignmentRepository;
import ru.stroy1click.attribute.service.impl.ProductAttributeAssignmentServiceImpl;
import ru.stroy1click.common.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAttributeAssignmentServiceTest {

    @Mock
    private ProductAttributeAssignmentRepository productAttributeAssignmentRepository;

    @Mock
    private ProductAttributeAssignmentMapper productAttributeAssignmentMapper;

    @Mock
    private CacheClear cacheClear;

    @Mock
    private AttributeOptionService attributeOptionService;

    @InjectMocks
    private ProductAttributeAssignmentServiceImpl productAttributeValueService;

    private ProductAttributeAssignment productAttributeAssignment;

    private ProductAttributeAssignmentDto productAttributeAssignmentDto;

    private AttributeOption attributeOption;

    private AttributeOptionDto attributeOptionDto;

    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        attribute = Attribute.builder()
                .id(20)
                .title("Color")
                .build();

        productAttributeAssignment = ProductAttributeAssignment.builder()
                .id(1)
                .productId(10)
                .attributeOption(attributeOption)
                .build();

        attributeOption = AttributeOption.builder()
                .id(1)
                .value("Test Value")
                .attribute(attribute)
                .productTypeId(1)
                .productAttributeAssignments(List.of(productAttributeAssignment))
                .build();

        attributeOptionDto = AttributeOptionDto.builder()
                .id(1)
                .attributeId(attribute.getId())
                .productTypeId(1)
                .value("Test Value")
                .build();

        productAttributeAssignmentDto = ProductAttributeAssignmentDto.builder()
                .id(1)
                .attributeOptionId(1)
                .productId(10)
                .build();
    }

    @Test
    public void get_WhenProductAttributeAssignmentExists_ShouldReturnProductAttributeAssignmentDto() {
        //Arrange
        when(this.productAttributeAssignmentRepository.findById(1)).thenReturn(Optional.of(productAttributeAssignment));
        when(this.productAttributeAssignmentMapper.toDto(productAttributeAssignment)).thenReturn(productAttributeAssignmentDto);

        //Act
        ProductAttributeAssignmentDto result = this.productAttributeValueService.get(1);

        //Assert
        assertNotNull(result);
        verify(this.productAttributeAssignmentRepository).findById(1);
        verify(this.productAttributeAssignmentMapper).toDto(productAttributeAssignment);
    }

    @Test
    public void get_WhenProductAttributeAssignmentDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        when(this.productAttributeAssignmentRepository.findById(1)).thenReturn(Optional.empty());

        //Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.productAttributeValueService.get(1));

        //Assert
        assertEquals("error.product_attribute_assignment.not_found", exception.getMessage());
        verify(this.productAttributeAssignmentRepository).findById(1);
        verify(this.productAttributeAssignmentMapper, never()).toDto(anyList());
    }

    @Test
    public void getAllByProductId_WhenProductAttributeAssignmentsExist_ShouldReturnList() {
        //Arrange
        when(this.productAttributeAssignmentRepository.findByProductId(10))
                .thenReturn(List.of(productAttributeAssignment));
        when(this.productAttributeAssignmentMapper.toDto(productAttributeAssignment)).thenReturn(productAttributeAssignmentDto);

        //Act
        List<ProductAttributeAssignmentDto> result = this.productAttributeValueService.getAllByProductId(10);

        //Assert
        assertEquals(1, result.size());
        verify(this.productAttributeAssignmentRepository).findByProductId(10);
        verify(this.productAttributeAssignmentMapper).toDto(productAttributeAssignment);
    }

    @Test
    public void getAllByProductId_WhenProductAttributeAssignmentsDoesNotExist_ShouldReturnEmptyLis() {
        //Arrange
        when(this.productAttributeAssignmentRepository.findByProductId(10)).thenReturn(List.of());

        //Act
        List<ProductAttributeAssignmentDto> result = this.productAttributeValueService.getAllByProductId(10);

        //Assert
        assertTrue(result.isEmpty());
        verify(this.productAttributeAssignmentRepository).findByProductId(10);
        verify(this.productAttributeAssignmentMapper, never()).toDto(anyList());
    }

    @Test
    public void create_WhenValidDataProvided_ShouldSaveAndReturnCreatedProductAttributeAssignment() {
        //Arrange
        when(this.productAttributeAssignmentMapper.toEntity(productAttributeAssignmentDto)).thenReturn(productAttributeAssignment);
        when(this.attributeOptionService.get(productAttributeAssignmentDto.getAttributeOptionId()))
                .thenReturn(attributeOptionDto);
        when(this.productAttributeAssignmentMapper.toDto(productAttributeAssignment)).thenReturn(productAttributeAssignmentDto);
        when(this.productAttributeAssignmentRepository.save(productAttributeAssignment)).thenReturn(productAttributeAssignment);

        //Act
        ProductAttributeAssignmentDto createdDto =
                this.productAttributeValueService.create(productAttributeAssignmentDto);

        //Assert
        assertNotNull(createdDto.getId());
        verify(this.productAttributeAssignmentMapper).toEntity(productAttributeAssignmentDto);
        verify(this.productAttributeAssignmentMapper).toDto(productAttributeAssignment);
        verify(this.productAttributeAssignmentRepository).save(productAttributeAssignment);
        verify(this.attributeOptionService).get(productAttributeAssignmentDto.getAttributeOptionId());
    }

    @Test
    public void create_WhenAttributeDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        when(this.attributeOptionService.get(productAttributeAssignmentDto.getAttributeOptionId()))
                .thenThrow(new NotFoundException("Атрибут не найден"));

        //Act
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
            this.productAttributeValueService.create(this.productAttributeAssignmentDto);
        });

        //Assert
        assertEquals("Атрибут не найден", notFoundException.getMessage());
    }

    @Test
    public void delete_WhenProductAttributeAssignmentExists_ShouldDeleteProductAttributeAssignment() {
        //Arrange
        when(this.productAttributeAssignmentRepository.findById(1)).thenReturn(Optional.of(this.productAttributeAssignment));

        //Act
        this.productAttributeValueService.delete(1);

        //Assert
        verify(this.cacheClear).clearAllProductAttributeValuesByProductId(10);
        verify(this.productAttributeAssignmentRepository).delete(this.productAttributeAssignment);
    }

    @Test
    public void delete_WhenProductAttributeAssignmentDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        when(this.productAttributeAssignmentRepository.findById(1)).thenReturn(Optional.empty());

        //Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.productAttributeValueService.delete(1));

        //Assert
        assertEquals("error.product_attribute_assignment.not_found", exception.getMessage());
        verify(this.productAttributeAssignmentRepository).findById(1);
        verify(this.productAttributeAssignmentRepository, never()).delete(any(ProductAttributeAssignment.class));
        verify(this.cacheClear, never()).clearAllProductAttributeValuesByProductId(any());
    }

    @Test
    public void update_ShouldSaveUpdatedEntity_WhenExists() {
        //Arrange
        ProductAttributeAssignmentDto updateDto
                = new ProductAttributeAssignmentDto(1,555,1);
        when(this.productAttributeAssignmentRepository.findById(1))
                .thenReturn(Optional.of(productAttributeAssignment));

        //Act
        this.productAttributeValueService.update(1, updateDto);

        //Assert
        assertEquals(555, productAttributeAssignment.getProductId());
    }

    @Test
    public void update_WhenProductAttributeAssignmentDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        when(this.productAttributeAssignmentRepository.findById(100)).thenReturn(Optional.empty());
        ProductAttributeAssignmentDto updateDto = new ProductAttributeAssignmentDto(100,555,1);

        //Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.productAttributeValueService.update(100, updateDto));

        //Assert
        assertEquals("error.product_attribute_assignment.not_found", exception.getMessage());
        verify(this.productAttributeAssignmentRepository).findById(100);
        verify(this.productAttributeAssignmentRepository, never()).save(any());
        verify(this.cacheClear, never()).clearAllProductAttributeValuesByProductId(any());
    }
}