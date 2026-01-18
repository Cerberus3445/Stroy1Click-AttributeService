package ru.stroy1click.attribute.service;

import ru.stroy1click.attribute.dto.ProductTypeAttributeValueDto;
import ru.stroy1click.attribute.entity.ProductTypeAttributeValue;

import java.util.List;
import java.util.Optional;

public interface ProductTypeAttributeValueService extends BaseService<Integer, ProductTypeAttributeValueDto> {

    List<ProductTypeAttributeValueDto> getAllByProductTypeId(Integer id);

    Optional<ProductTypeAttributeValue> getByValue(String value);
}
