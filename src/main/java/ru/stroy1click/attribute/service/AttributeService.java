package ru.stroy1click.attribute.service;

import ru.stroy1click.attribute.dto.AttributeDto;
import ru.stroy1click.attribute.entity.Attribute;

import java.util.List;
import java.util.Optional;

public interface AttributeService extends CrudOperations<Integer, AttributeDto> {

    List<AttributeDto> getAll();

    Optional<Attribute> getByTitle(String title);
}
