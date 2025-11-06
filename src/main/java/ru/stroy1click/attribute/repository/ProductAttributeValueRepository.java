package ru.stroy1click.attribute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stroy1click.attribute.entity.ProductAttributeValue;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Integer> {

    List<ProductAttributeValue> findByProductId(Integer productId);

    Optional<ProductAttributeValue> findByValue(String value);
}
