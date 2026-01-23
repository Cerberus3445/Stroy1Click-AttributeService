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
import ru.stroy1click.attribute.dto.AttributeOptionDto;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.entity.AttributeOption;
import ru.stroy1click.attribute.exception.NotFoundException;
import ru.stroy1click.attribute.mapper.AttributeOptionMapper;
import ru.stroy1click.attribute.repository.AttributeOptionRepository;
import ru.stroy1click.attribute.service.AttributeService;
import ru.stroy1click.attribute.service.impl.AttributeOptionServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AttributeOptionTest {

    @Mock
    private AttributeOptionRepository attributeOptionRepository;

    @Mock
    private AttributeOptionMapper mapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private CacheClear cacheClear;

    @Mock
    private AttributeService attributeService;

    @InjectMocks
    private AttributeOptionServiceImpl productTypeAttributeValueService;

    private AttributeOption entity;
    private AttributeOptionDto dto;
    private Integer productTypeId;
    private Attribute attribute;
    private AttributeDto attributeDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.productTypeId = 10;

        this.attribute = Attribute.builder()
                .id(20)
                .title("Color")
                .build();

        this.attributeDto = AttributeDto.builder()
                .id(20)
                .title("Color")
                .build();

        this.entity = AttributeOption.builder()
                .id(1)
                .value("White")
                .attribute(this.attribute)
                .productTypeId(10)
                .build();

        this.dto = new AttributeOptionDto(
                1, this.attribute.getId(), this.productTypeId, "White"
        );

        when(this.messageSource.getMessage(anyString(), any(), any()))
                .thenReturn("Not found");
    }

    @Test
    public void get_ShouldReturnDto_WhenEntityExists() {
        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.of(this.entity));
        when(this.mapper.toDto(this.entity)).thenReturn(this.dto);

        AttributeOptionDto result = this.productTypeAttributeValueService.get(1);

        assertEquals(this.dto, result);
        verify(this.attributeOptionRepository).findById(1);
        verify(this.mapper).toDto(this.entity);
    }

    @Test
    public void get_ShouldThrowNotFoundException_WhenEntityDoesNotExist() {
        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.productTypeAttributeValueService.get(1));

        assertEquals("Not found", exception.getMessage());
        verify(this.attributeOptionRepository).findById(1);
        verify(this.mapper, never()).toDto(anyList());
    }

    @Test
    public void getAllByProductId_ShouldReturnMappedDtos_WhenEntitiesExist() {
        when(this.attributeOptionRepository.findByProductTypeId(10))
                .thenReturn(List.of(this.entity));
        when(this.mapper.toDto(this.entity)).thenReturn(this.dto);

        List<AttributeOptionDto> result =
                this.productTypeAttributeValueService.getAllByProductTypeId(10);

        assertEquals(1, result.size());
        assertEquals(this.dto, result.get(0));
        verify(this.attributeOptionRepository).findByProductTypeId(10);
        verify(this.mapper).toDto(this.entity);
    }

    @Test
    public void getAllByProductId_ShouldReturnEmptyList_WhenNoEntitiesExist() {
        when(this.attributeOptionRepository.findByProductTypeId(10))
                .thenReturn(List.of());

        List<AttributeOptionDto> result =
                this.productTypeAttributeValueService.getAllByProductTypeId(10);

        assertTrue(result.isEmpty());
        verify(this.attributeOptionRepository).findByProductTypeId(10);
        verify(this.mapper, never()).toDto(anyList());
    }

    @Test
    public void getByValue_ShouldReturnOptional_WhenExists() {
        when(this.attributeOptionRepository.findByValue("White"))
                .thenReturn(Optional.of(this.entity));

        Optional<AttributeOption> result =
                this.productTypeAttributeValueService.getByValue("White");

        assertTrue(result.isPresent());
        assertEquals(this.entity, result.get());
        verify(this.attributeOptionRepository).findByValue("White");
    }

    @Test
    public void getByValue_ShouldReturnEmptyOptional_WhenNotExists() {
        when(this.attributeOptionRepository.findByValue("NonExistent"))
                .thenReturn(Optional.empty());

        Optional<AttributeOption> result =
                this.productTypeAttributeValueService.getByValue("NonExistent");

        assertFalse(result.isPresent());
        verify(this.attributeOptionRepository).findByValue("NonExistent");
    }

    @Test
    public void create_ShouldSaveEntity_WhenDtoIsValid() {
        when(this.mapper.toEntity(this.dto)).thenReturn(this.entity);
        when(this.attributeService.get(this.dto.getAttributeId())).thenReturn(this.attributeDto);

        this.productTypeAttributeValueService.create(this.dto);

        verify(this.mapper).toEntity(this.dto);
        verify(this.attributeOptionRepository).save(this.entity);
        verify(this.attributeService).get(this.dto.getAttributeId());
    }

    @Test
    public void create_ShouldThrowNotFountException_WhenAttributeNotExists() {
        when(this.mapper.toEntity(this.dto)).thenReturn(this.entity);
        when(this.attributeService.get(this.dto.getAttributeId())).thenThrow(
                new NotFoundException("Атрибут не найден")
        );

        NotFoundException notFoundException = assertThrows(
                NotFoundException.class, () -> this.productTypeAttributeValueService.create(this.dto)
        );

        assertEquals("Атрибут не найден", notFoundException.getMessage());
    }

    @Test
    public void delete_ShouldRemoveEntity_WhenExists() {
        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.of(this.entity));

        this.productTypeAttributeValueService.delete(1);

        verify(this.attributeOptionRepository).findById(1);
        verify(this.cacheClear).clearAllAttributeOptionsByProductTypeId(10);
        verify(this.attributeOptionRepository).delete(this.entity);
    }

    @Test
    public void delete_ShouldThrowNotFoundException_WhenEntityDoesNotExist() {
        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.productTypeAttributeValueService.delete(1));

        assertEquals("Not found", exception.getMessage());
        verify(this.attributeOptionRepository).findById(1);
        verify(this.attributeOptionRepository, never()).delete(any());
        verify(this.cacheClear, never()).clearAllAttributeOptionsByProductTypeId(anyInt());
    }

    @Test
    public void update_ShouldSaveUpdatedEntity_WhenExists() {
        AttributeOptionDto updateDto = new AttributeOptionDto(
                1, 5, 10, "Red"
        );

        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.of(this.entity));

        this.productTypeAttributeValueService.update(1, updateDto);

        verify(this.attributeOptionRepository).findById(1);
        ArgumentCaptor<AttributeOption> captor =
                ArgumentCaptor.forClass(AttributeOption.class);
        verify(this.attributeOptionRepository).save(captor.capture());

        AttributeOption saved = captor.getValue();
        assertEquals("Red", saved.getValue());
        assertEquals(this.entity.getProductTypeId(), saved.getProductTypeId());
        assertEquals(this.entity.getAttribute(), saved.getAttribute());
    }

    @Test
    public void update_ShouldNotChangeProductTypeAndAttribute_WhenUpdatingValue() {
        AttributeOptionDto updateDto = new AttributeOptionDto(
                1, 999, 888, "Red"
        );

        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.of(this.entity));

        this.productTypeAttributeValueService.update(1, updateDto);

        ArgumentCaptor<AttributeOption> captor =
                ArgumentCaptor.forClass(AttributeOption.class);
        verify(this.attributeOptionRepository).save(captor.capture());

        AttributeOption saved = captor.getValue();
        assertEquals("Red", saved.getValue());
        assertEquals(10, saved.getProductTypeId());
        assertEquals(20, saved.getAttribute().getId());
    }

    @Test
    public void update_ShouldThrowNotFoundException_WhenEntityDoesNotExist() {
        AttributeOptionDto updateDto = new AttributeOptionDto(
                1, 5, 10, "Black"
        );

        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> this.productTypeAttributeValueService.update(1, updateDto));

        assertEquals("Not found", exception.getMessage());
        verify(this.attributeOptionRepository, never()).save(any());
        verify(this.cacheClear, never()).clearAllAttributeOptionsByProductTypeId(anyInt());
    }

    @Test
    public void update_ShouldThrowException_WhenDtoIsNull() {
        when(this.attributeOptionRepository.findById(1))
                .thenReturn(Optional.of(this.entity));

        assertThrows(NullPointerException.class,
                () -> this.productTypeAttributeValueService.update(1, null));
    }
}

