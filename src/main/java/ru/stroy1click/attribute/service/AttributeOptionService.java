package ru.stroy1click.attribute.service;

import ru.stroy1click.attribute.dto.AttributeOptionDto;
import ru.stroy1click.attribute.entity.AttributeOption;

import java.util.List;
import java.util.Optional;

public interface AttributeOptionService extends CrudOperations<Integer, AttributeOptionDto> {

    List<AttributeOptionDto> getAllByProductTypeId(Integer id);

    Optional<AttributeOption> getByValue(String value);
}
