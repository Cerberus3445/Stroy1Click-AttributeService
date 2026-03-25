package ru.stroy1click.attribute.validator.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.stroy1click.attribute.dto.AttributeDto;
import ru.stroy1click.attribute.service.AttributeService;
import ru.stroy1click.attribute.validator.AttributeCreateValidator;
import ru.stroy1click.common.util.ExceptionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttributeCreateValidatorImpl implements AttributeCreateValidator {

    private final AttributeService attributeService;

    @Override
    public void validate(AttributeDto attributeDto) {
        log.info("validate {}", attributeDto);

        if(this.attributeService.getByTitle(attributeDto.getTitle()).isPresent()){
            throw ExceptionUtils.alreadyExists("error.attribute.create.validate", null);
        }
    }
}
