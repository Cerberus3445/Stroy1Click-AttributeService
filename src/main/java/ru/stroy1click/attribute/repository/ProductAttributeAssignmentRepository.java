package ru.stroy1click.attribute.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.stroy1click.attribute.entity.ProductAttributeAssignment;
import ru.stroy1click.attribute.dto.ProductAttributeValueFilter;
import ru.stroy1click.attribute.specification.ProductSpecification;

import java.util.List;

@Repository
public interface ProductAttributeAssignmentRepository extends JpaRepository<ProductAttributeAssignment, Integer>,
        JpaSpecificationExecutor<ProductAttributeAssignment> {

    List<ProductAttributeAssignment> findByProductId(Integer productId);

    default Page<ProductAttributeAssignment> findProductIdsByAttributes(ProductAttributeValueFilter filter, Pageable pageable) {
        return findAll(ProductSpecification.filterByAttributes(filter), pageable);
    }
}
