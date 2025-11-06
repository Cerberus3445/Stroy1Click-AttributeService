package ru.stroy1click.attribute.service.attribute;

import ru.stroy1click.attribute.dto.AttributeDto;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.service.BaseService;

import java.util.Optional;

public interface AttributeService extends BaseService<Integer, AttributeDto> {

    Optional<Attribute> getByTitle(String title);
}
