package ru.stroy1click.attribute.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.stroy1click.attribute.dto.ProductAttributeDto;
import ru.stroy1click.attribute.model.PageResponse;
import ru.stroy1click.attribute.model.ProductAttributeValueFilter;

import java.util.List;

public interface ProductAttributeService extends BaseService<Integer, ProductAttributeDto> {

    List<ProductAttributeDto> getAllByProductId(Integer id);

    PageResponse<ProductAttributeDto> getProductIdsByAttributes(ProductAttributeValueFilter filter, Pageable pageable);
}
