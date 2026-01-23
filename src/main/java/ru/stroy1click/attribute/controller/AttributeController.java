package ru.stroy1click.attribute.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.attribute.dto.AttributeDto;
import ru.stroy1click.attribute.exception.ValidationException;
import ru.stroy1click.attribute.service.AttributeService;
import ru.stroy1click.attribute.util.ValidationErrorUtils;
import ru.stroy1click.attribute.validator.AttributeCreateValidator;
import ru.stroy1click.attribute.validator.AttributeUpdateValidator;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attributes")
@Tag(name = "Attribute Controller", description = "Взаимодействие с атрибутами")
@RateLimiter(name = "attributeLimiter")
public class AttributeController {

    private final AttributeService attributeService;

    private final AttributeCreateValidator createValidator;

    private final AttributeUpdateValidator updateValidator;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    @Operation(summary = "Получение атрибута")
    public AttributeDto get(@PathVariable("id") Integer id){
        return this.attributeService.get(id);
    }

    @GetMapping
    @Operation(summary = "Получение всех атрибутов")
    public List<AttributeDto> getAll(){
        return this.attributeService.getAll();
    }

    @PostMapping
    @Operation(summary = "Создание атрибута")
    public ResponseEntity<AttributeDto> create(@RequestBody @Valid AttributeDto attributeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.createValidator.validate(attributeDto);

        AttributeDto createdAttribute = this.attributeService.create(attributeDto);

        return ResponseEntity
                .created(URI.create("/api/v1/attributes/"+ createdAttribute.getId()))
                .body(createdAttribute);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление атрибута")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid AttributeDto attributeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        attributeDto.setId(id); //for update validator
        this.updateValidator.validate(attributeDto);

        this.attributeService.update(id, attributeDto);
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.attribute.update",
                        null,
                        Locale.getDefault()
                )
        );
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление атрибута")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.attributeService.delete(id);
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.attribute.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
