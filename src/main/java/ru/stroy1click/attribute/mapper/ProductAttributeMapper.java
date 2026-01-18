package ru.stroy1click.attribute.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.stroy1click.attribute.dto.ProductAttributeDto;
import ru.stroy1click.attribute.entity.ProductAttribute;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductAttributeMapper implements Mappable<ProductAttribute, ProductAttributeDto>{

    private final ModelMapper modelMapper;

    @Override
    public ProductAttribute toEntity(ProductAttributeDto productAttributeDto) {
        return this.modelMapper.map(productAttributeDto, ProductAttribute.class);
    }

    @Override
    public ProductAttributeDto toDto(ProductAttribute productAttribute) {
        return this.modelMapper.map(productAttribute, ProductAttributeDto.class);
    }

    @Override
    public List<ProductAttributeDto> toDto(List<ProductAttribute> e) {
        return e.stream()
                .map(productAttributeValue -> this.modelMapper.map(productAttributeValue, ProductAttributeDto.class))
                .toList();
    }
}
