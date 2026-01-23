package ru.stroy1click.attribute.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.stroy1click.attribute.dto.ProductAttributeAssignmentDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductAttributeAssignment implements Mappable<ru.stroy1click.attribute.entity.ProductAttributeAssignment, ProductAttributeAssignmentDto>{

    private final ModelMapper modelMapper;

    @Override
    public ru.stroy1click.attribute.entity.ProductAttributeAssignment toEntity(ProductAttributeAssignmentDto productAttributeAssignmentDto) {
        return this.modelMapper.map(productAttributeAssignmentDto, ru.stroy1click.attribute.entity.ProductAttributeAssignment.class);
    }

    @Override
    public ProductAttributeAssignmentDto toDto(ru.stroy1click.attribute.entity.ProductAttributeAssignment productAttributeAssignment) {
        return this.modelMapper.map(productAttributeAssignment, ProductAttributeAssignmentDto.class);
    }

    @Override
    public List<ProductAttributeAssignmentDto> toDto(List<ru.stroy1click.attribute.entity.ProductAttributeAssignment> e) {
        return e.stream()
                .map(productAttributeValue -> this.modelMapper.map(productAttributeValue, ProductAttributeAssignmentDto.class))
                .toList();
    }
}
