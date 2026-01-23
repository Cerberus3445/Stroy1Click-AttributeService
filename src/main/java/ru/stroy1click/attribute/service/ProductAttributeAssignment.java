package ru.stroy1click.attribute.service;

import org.springframework.data.domain.Pageable;
import ru.stroy1click.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.attribute.model.PageResponse;
import ru.stroy1click.attribute.model.ProductAttributeValueFilter;

import java.util.List;

public interface ProductAttributeAssignment extends CrudOperations<Integer, ProductAttributeAssignmentDto> {

    List<ProductAttributeAssignmentDto> getAllByProductId(Integer id);

    PageResponse<ProductAttributeAssignmentDto> getProductIdsByAttributes(ProductAttributeValueFilter filter, Pageable pageable);
}
