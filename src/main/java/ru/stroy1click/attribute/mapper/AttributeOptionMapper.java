package ru.stroy1click.attribute.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.stroy1click.attribute.dto.AttributeOptionDto;
import ru.stroy1click.attribute.entity.AttributeOption;


import java.util.List;

@Component
@RequiredArgsConstructor
public class AttributeOptionMapper implements Mappable<AttributeOption, AttributeOptionDto>{

    private final ModelMapper modelMapper;

    @Override
    public AttributeOption toEntity(AttributeOptionDto attributeOptionDto) {
        return this.modelMapper.map(attributeOptionDto, AttributeOption.class);
    }

    @Override
    public AttributeOptionDto toDto(AttributeOption attributeOption) {
        return this.modelMapper.map(attributeOption, AttributeOptionDto.class);
    }

    @Override
    public List<AttributeOptionDto> toDto(List<AttributeOption> e) {
        return e.stream()
                .map(productAttributeValue -> this.modelMapper.map(productAttributeValue, AttributeOptionDto.class))
                .toList();
    }
}
