package ru.stroy1click.attribute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stroy1click.attribute.entity.ProductTypeAttributeValue;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTypeAttributeValueRepository extends JpaRepository<ProductTypeAttributeValue, Integer> {

    List<ProductTypeAttributeValue> findByProductTypeId(Integer productId);

    Optional<ProductTypeAttributeValue> findByValue(String value);
}
