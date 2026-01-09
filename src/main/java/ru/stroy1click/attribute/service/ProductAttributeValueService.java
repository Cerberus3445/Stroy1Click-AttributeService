package ru.stroy1click.attribute.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.stroy1click.attribute.dto.ProductAttributeValueDto;
import ru.stroy1click.attribute.entity.ProductAttributeValue;
import ru.stroy1click.attribute.model.ProductAttributeValueFilter;

import java.util.List;
import java.util.Optional;

public interface ProductAttributeValueService extends BaseService<Integer, ProductAttributeValueDto> {

    List<ProductAttributeValueDto> getAllByProductId(Integer id);

    Optional<ProductAttributeValue> getByValue(String value);

    Page<ProductAttributeValueDto> getProductIdsByAttributes(ProductAttributeValueFilter filter, Pageable pageable);
}
