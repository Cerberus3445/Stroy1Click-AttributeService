package ru.stroy1click.attribute.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.stroy1click.attribute.entity.ProductAttributeValue;
import ru.stroy1click.attribute.model.ProductAttributeValueFilter;
import ru.stroy1click.attribute.specification.ProductSpecification;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Integer>,
        JpaSpecificationExecutor<ProductAttributeValue> {

    List<ProductAttributeValue> findByProductId(Integer productId);

    Optional<ProductAttributeValue> findByValue(String value);

    default Page<ProductAttributeValue> findProductIdsByAttributes(ProductAttributeValueFilter filter, Pageable pageable) {
        return findAll(ProductSpecification.filterByAttributes(filter), pageable);
    }
}
