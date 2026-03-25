package ru.stroy1click.attribute.validator.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.stroy1click.attribute.dto.AttributeDto;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.service.AttributeService;
import ru.stroy1click.attribute.validator.AttributeUpdateValidator;
import ru.stroy1click.common.util.ExceptionUtils;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttributeUpdateValidatorImpl implements AttributeUpdateValidator {

    private final AttributeService attributeService;

    @Override
    public void validate(AttributeDto dto) {
        log.info("validate {}", dto);

        Optional<Attribute> foundAttribute= this.attributeService.getByTitle(dto.getTitle());

        if(foundAttribute.isPresent() && !Objects.equals(dto.getId(), foundAttribute.get().getId())
                && dto.getTitle().equalsIgnoreCase(foundAttribute.get().getTitle())){
            throw ExceptionUtils.alreadyExists("error.attribute.update.validate", null);
        }
    }
}
