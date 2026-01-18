package ru.stroy1click.attribute.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.stroy1click.attribute.entity.ProductAttribute;
import ru.stroy1click.attribute.model.ProductAttributeValueFilter;
import ru.stroy1click.attribute.specification.ProductSpecification;

import java.util.List;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Integer>,
        JpaSpecificationExecutor<ProductAttribute> {

    List<ProductAttribute> findByProductId(Integer productId);

    default Page<ProductAttribute> findProductIdsByAttributes(ProductAttributeValueFilter filter, Pageable pageable) {
        return findAll(ProductSpecification.filterByAttributes(filter), pageable);
    }
}
