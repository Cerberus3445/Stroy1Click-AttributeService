package ru.stroy1click.attribute.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.stroy1click.attribute.cache.CacheClear;
import ru.stroy1click.attribute.dto.AttributeOptionDto;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.entity.AttributeOption;
import ru.stroy1click.attribute.mapper.AttributeOptionMapper;
import ru.stroy1click.attribute.repository.AttributeOptionRepository;
import ru.stroy1click.attribute.service.impl.AttributeOptionServiceImpl;
import ru.stroy1click.common.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttributeOptionServiceTest {

    @Mock
    private AttributeOptionRepository attributeOptionRepository;

    @Mock
    private AttributeOptionMapper attributeOptionMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CacheClear cacheClear;

    @Mock
    private AttributeService attributeService;

    @InjectMocks
    private AttributeOptionServiceImpl attributeOptionService;

    private AttributeOption attributeOption;

    private AttributeOptionDto attributeOptionDto;

    private Integer productTypeId;

    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        productTypeId = 10;

        attribute = Attribute.builder()
                .id(20)
                .title("Color")
                .build();

        attributeOption = AttributeOption.builder()
                .id(1)
                .value("White")
                .attribute(attribute)
                .productTypeId(10)
                .build();

        attributeOptionDto = new AttributeOptionDto(
                1, attribute.getId(), productTypeId, "White"
        );
    }

    @Test
    public void get_WhenAttributeOptionExists_ShouldReturnDto() {
        //Arrange
        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.of(attributeOption));
        when(this.attributeOptionMapper.toDto(attributeOption)).thenReturn(attributeOptionDto);

        //Act
        AttributeOptionDto result = this.attributeOptionService.get(1);

        //Assert
        assertEquals(attributeOptionDto, result);
        verify(this.attributeOptionRepository).findById(1);
        verify(this.attributeOptionMapper).toDto(attributeOption);
    }

    @Test
    public void get_WhenAttributeOptionDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.empty());
        when(this.messageSource.getMessage(anyString(), any(), any()))
                .thenReturn("Not found");

        //Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.attributeOptionService.get(1));

        //Assert
        assertEquals("Not found", exception.getMessage());
        verify(this.attributeOptionRepository).findById(1);
        verify(this.attributeOptionMapper, never()).toDto(anyList());
    }

    @Test
    public void getAllByProductId_WhenAttributeOptionsExist_ShouldReturnDtos() {
        //Arrange
        when(this.attributeOptionRepository.findByProductTypeId(10))
                .thenReturn(List.of(attributeOption));
        when(this.attributeOptionMapper.toDto(attributeOption)).thenReturn(attributeOptionDto);

        //Act
        List<AttributeOptionDto> result =
                this.attributeOptionService.getAllByProductTypeId(10);

        //Assert
        assertEquals(1, result.size());
        assertEquals(attributeOptionDto, result.get(0));
        verify(this.attributeOptionRepository).findByProductTypeId(10);
        verify(this.attributeOptionMapper).toDto(attributeOption);
    }

    @Test
    public void getAllByProductId_WhenAttributeOptionsDoNotExist_ShouldReturnEmptyList() {
        //Arrange
        when(this.attributeOptionRepository.findByProductTypeId(10))
                .thenReturn(List.of());

        //Act
        List<AttributeOptionDto> result =
                this.attributeOptionService.getAllByProductTypeId(10);

        //Assert
        assertTrue(result.isEmpty());
        verify(this.attributeOptionRepository).findByProductTypeId(10);
        verify(this.attributeOptionMapper, never()).toDto(anyList());
    }

    @Test
    public void getByValue_WhenAttributeOptionExists_ShouldReturnOptional() {
        //Arrange
        when(this.attributeOptionRepository.findByValue("White"))
                .thenReturn(Optional.of(attributeOption));

        //Act
        Optional<AttributeOption> result =
                this.attributeOptionService.getByValue("White");

        //Assert
        assertTrue(result.isPresent());
        assertEquals(attributeOption, result.get());
        verify(this.attributeOptionRepository).findByValue("White");
    }

    @Test
    public void getByValue_WhenAttributeOptionDoesNotExist_ShouldReturnEmptyOptional() {
        //Arrange
        when(this.attributeOptionRepository.findByValue("NonExistent"))
                .thenReturn(Optional.empty());;

        //Act
        Optional<AttributeOption> result =
                this.attributeOptionService.getByValue("NonExistent");

        //Assert
        assertFalse(result.isPresent());
        verify(this.attributeOptionRepository).findByValue("NonExistent");
    }

    @Test
    public void create_WhenValidDataProvided_ShouldSaveAndReturnCreatedEntity() {
        //Arrange
        when(this.attributeOptionMapper.toEntity(attributeOptionDto)).thenReturn(attributeOption);
        when(this.attributeOptionRepository.save(attributeOption)).thenReturn(attributeOption);
        when(this.attributeOptionMapper.toDto(attributeOption)).thenReturn(attributeOptionDto);

        //Act
        AttributeOptionDto createdAttributeOption = this.attributeOptionService.create(attributeOptionDto);

        //Assert
        assertNotNull(createdAttributeOption.getId());
        assertEquals("White", createdAttributeOption.getValue());
        verify(this.attributeOptionMapper).toEntity(attributeOptionDto);
        verify(this.attributeOptionRepository).save(attributeOption);
        verify(this.attributeService).get(attributeOptionDto.getAttributeId());
    }

    @Test
    public void create_WhenAttributeDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        when(this.attributeService.get(attributeOptionDto.getAttributeId())).thenThrow(
                new NotFoundException("Атрибут не найден")
        );

        //Act
        NotFoundException notFoundException = assertThrows(
                NotFoundException.class, () -> this.attributeOptionService.create(attributeOptionDto)
        );

        //Assert
        assertEquals("Атрибут не найден", notFoundException.getMessage());
    }

    @Test
    public void delete_WhenAttributeOptionExists_ShouldRemoveEntity() {
        //Arrange
        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.of(attributeOption));

        //Act
        this.attributeOptionService.delete(1);

        //Act
        verify(this.attributeOptionRepository).findById(1);
        verify(this.cacheClear).clearAllAttributeOptionsByProductTypeId(10);
        verify(this.attributeOptionRepository).delete(attributeOption);
    }

    @Test
    public void delete_WhenAttributeOptionDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.empty());
        when(this.messageSource.getMessage(anyString(), any(), any()))
                .thenReturn("Not found");

        //Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.attributeOptionService.delete(1));

        //Assert
        assertEquals("Not found", exception.getMessage());
        verify(this.attributeOptionRepository).findById(1);
        verify(this.attributeOptionRepository, never()).delete(any());
        verify(this.cacheClear, never()).clearAllAttributeOptionsByProductTypeId(anyInt());
    }

    @Test
    public void update_WhenAttributeOptionExists_ShouldUpdateAttributeOption() {
        //Arrange
        AttributeOptionDto updateDto = new AttributeOptionDto(
                1, 5, 10, "Red"
        );
        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.of(this.attributeOption));

        //Act
        this.attributeOptionService.update(1, updateDto);

        //Assert
        verify(this.attributeOptionRepository).findById(1);
        ArgumentCaptor<AttributeOption> captor =
                ArgumentCaptor.forClass(AttributeOption.class);
        verify(this.attributeOptionRepository).save(captor.capture());

        AttributeOption saved = captor.getValue();
        assertEquals("Red", saved.getValue());
        assertEquals(this.attributeOption.getProductTypeId(), saved.getProductTypeId());
        assertEquals(this.attributeOption.getAttribute(), saved.getAttribute());
    }

    @Test
    public void update_WhenAttributeOptionDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        AttributeOptionDto updateDto = new AttributeOptionDto(
                1, 5, 10, "Black"
        );
        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.empty());
        when(this.messageSource.getMessage(anyString(), any(), any()))
                .thenReturn("Not found");

        //Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.attributeOptionService.update(1, updateDto));

        //Assert
        assertEquals("Not found", exception.getMessage());
        verify(this.attributeOptionRepository, never()).save(any());
        verify(this.cacheClear, never()).clearAllAttributeOptionsByProductTypeId(anyInt());
    }
}