package ru.stroy1click.attribute.service.product.type;

import ru.stroy1click.attribute.dto.ProductTypeAttributeValueDto;
import ru.stroy1click.attribute.entity.ProductTypeAttributeValue;
import ru.stroy1click.attribute.service.BaseService;

import java.util.List;
import java.util.Optional;

public interface ProductTypeAttributeValueService extends BaseService<Integer, ProductTypeAttributeValueDto> {

    List<ProductTypeAttributeValueDto> getAllByProductId(Integer id);

    Optional<ProductTypeAttributeValue> getByValue(String value);
}
