package ru.stroy1click.attribute.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.stroy1click.attribute.dto.AttributeDto;
import ru.stroy1click.attribute.entity.Attribute;


import java.util.List;

@Component
@RequiredArgsConstructor
public class AttributeMapper implements Mappable<Attribute, AttributeDto>{

    private final ModelMapper modelMapper;

    @Override
    public Attribute toEntity(AttributeDto dto) {
        return this.modelMapper.map(dto, Attribute.class);
    }

    @Override
    public AttributeDto toDto(Attribute attribute) {
        return this.modelMapper.map(attribute, AttributeDto.class);
    }

    @Override
    public List<AttributeDto> toDto(List<Attribute> e) {
        return e.stream()
                .map(attribute -> this.modelMapper.map(attribute,AttributeDto.class))
                .toList();
    }
}
