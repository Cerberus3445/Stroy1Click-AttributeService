package ru.stroy1click.attribute.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.stroy1click.attribute.dto.ProductTypeAttributeValueDto;
import ru.stroy1click.attribute.entity.ProductTypeAttributeValue;


import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductTypeAttributeValueMapper implements Mappable<ProductTypeAttributeValue, ProductTypeAttributeValueDto>{

    private final ModelMapper modelMapper;

    @Override
    public ProductTypeAttributeValue toEntity(ProductTypeAttributeValueDto productTypeAttributeValueDto) {
        return this.modelMapper.map(productTypeAttributeValueDto, ProductTypeAttributeValue.class);
    }

    @Override
    public ProductTypeAttributeValueDto toDto(ProductTypeAttributeValue productTypeAttributeValue) {
        return this.modelMapper.map(productTypeAttributeValue, ProductTypeAttributeValueDto.class);
    }

    @Override
    public List<ProductTypeAttributeValueDto> toDto(List<ProductTypeAttributeValue> e) {
        return e.stream()
                .map(productAttributeValue -> this.modelMapper.map(productAttributeValue, ProductTypeAttributeValueDto.class))
                .toList();
    }
}
