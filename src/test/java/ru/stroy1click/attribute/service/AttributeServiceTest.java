package ru.stroy1click.attribute.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.stroy1click.attribute.dto.AttributeDto;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.mapper.AttributeMapper;
import ru.stroy1click.attribute.repository.AttributeRepository;
import ru.stroy1click.attribute.service.impl.AttributeServiceImpl;
import ru.stroy1click.common.exception.NotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @InjectMocks
    private AttributeServiceImpl attributeService;

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private AttributeMapper attributeMapper;

    private Attribute attribute;

    private AttributeDto attributeDto;

    @BeforeEach
    void setUp() {
        attribute = Attribute.builder()
                .id(1)
                .title("Color")
                .build();

        attributeDto = new AttributeDto();
        attributeDto.setTitle("Color");
    }

    @Test
    void get_WhenAttributeExists_ShouldReturnAttributeDto() {
        //Arrange
        when(this.attributeRepository.findById(1)).thenReturn(Optional.of(attribute));
        when(this.attributeMapper.toDto(attribute)).thenReturn(attributeDto);

        //Act
        AttributeDto result = this.attributeService.get(1);

        //Assert
        assertNotNull(result);
        assertEquals("Color", result.getTitle());
        verify(this.attributeRepository).findById(1);
        verify(this.attributeMapper).toDto(attribute);
    }

    @Test
    void get_WhenAttributeDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        when(this.attributeRepository.findById(1)).thenReturn(Optional.empty());

        //Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> this.attributeService.get(1));

        //Assert
        assertEquals("error.attribute.not_found", exception.getMessage());
        verify(this.attributeRepository).findById(1);
    }

    @Test
    void create_WhenValidDataProvided_ShouldSaveAndReturnCreated() {
        //Arrange
        Attribute attributeToSave = Attribute.builder().title("Color").build();
        when(this.attributeMapper.toEntity(attributeDto)).thenReturn(attributeToSave);

        //Act
        this.attributeService.create(attributeDto);

        //Assert
        verify(this.attributeMapper).toEntity(attributeDto);
        verify(this.attributeRepository).save(attributeToSave);
    }

    @Test
    void delete_WhenAttributeExists_ShouldDeleteAttribute() {
        //Arrange
        when(this.attributeRepository.findById(1)).thenReturn(Optional.of(this.attribute));

        //Act
        this.attributeService.delete(1);

        //Assert
        verify(this.attributeRepository).delete(this.attribute);
    }

    @Test
    void delete_WhenAttributeDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        when(this.attributeRepository.findById(1)).thenReturn(Optional.empty());

        //Act
        NotFoundException exception = assertThrows(NotFoundException.class, () -> this.attributeService.delete(1));

        //Assert
        assertEquals("error.attribute.not_found", exception.getMessage());
        verify(this.attributeRepository).findById(1);
        verify(this.attributeRepository, never()).delete(any());
    }

    @Test
    void update_WhenAttributeExists_ShouldUpdateAttribute() {
        //Arrange
        Attribute existing = Attribute.builder().id(1).title("Old").build();
        AttributeDto dto = new AttributeDto();
        dto.setTitle("New");
        when(this.attributeRepository.findById(1)).thenReturn(Optional.of(existing));

        //Act
        this.attributeService.update(1, dto);

        //Assert
        assertEquals(1, existing.getId());
        assertEquals("New", existing.getTitle());
    }

    @Test
    void update_WhenAttributeDoesNotExist_ShouldThrowNotFoundException() {
        //Arrange
        AttributeDto dto = new AttributeDto();
        dto.setTitle("New");
        when(this.attributeRepository.findById(1)).thenReturn(Optional.empty());

        //Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.attributeService.update(1, dto));

        //Assert
        assertEquals("error.attribute.not_found", exception.getMessage());
        verify(this.attributeRepository).findById(1);
        verify(this.attributeRepository, never()).save(any());
    }

    @Test
    void getByTitle_WhenAttributeExists_ShouldReturnOptionalAttribute() {
        //Arrange
        when(this.attributeRepository.findByTitle("Color")).thenReturn(Optional.of(this.attribute));

        //Act
        Optional<Attribute> result = this.attributeService.getByTitle("Color");

        //Assert
        assertTrue(result.isPresent());
        assertEquals(this.attribute, result.get());
    }

    @Test
    void getByTitle_WhenAttributeDoesNotExist_ShouldReturnEmptyOptional() {
        //Arrange
        when(this.attributeRepository.findByTitle("NonExisting")).thenReturn(Optional.empty());

        //Act
        Optional<Attribute> result = this.attributeService.getByTitle("NonExisting");

        //Assert
        assertFalse(result.isPresent());
    }
}

