package ru.stroy1click.attribute.service;

import ru.stroy1click.attribute.dto.AttributeDto;
import ru.stroy1click.attribute.entity.Attribute;

import java.util.Optional;

public interface AttributeService extends BaseService<Integer, AttributeDto> {

    Optional<Attribute> getByTitle(String title);
}
