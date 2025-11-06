package ru.stroy1click.attribute.service.product;

import ru.stroy1click.attribute.dto.ProductAttributeValueDto;
import ru.stroy1click.attribute.entity.ProductAttributeValue;
import ru.stroy1click.attribute.service.BaseService;

import java.util.List;
import java.util.Optional;

public interface ProductAttributeValueService extends BaseService<Integer, ProductAttributeValueDto> {

    List<ProductAttributeValueDto> getAllByProductId(Integer id);

    Optional<ProductAttributeValue> getByValue(String value);
}
