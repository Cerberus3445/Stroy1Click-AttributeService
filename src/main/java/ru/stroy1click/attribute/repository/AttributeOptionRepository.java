package ru.stroy1click.attribute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stroy1click.attribute.entity.AttributeOption;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeOptionRepository extends JpaRepository<AttributeOption, Integer> {

    List<AttributeOption> findByProductTypeId(Integer productId);

    Optional<AttributeOption> findByValue(String value);
}
