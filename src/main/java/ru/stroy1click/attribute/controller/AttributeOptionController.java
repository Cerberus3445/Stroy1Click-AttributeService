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
import ru.stroy1click.attribute.dto.AttributeOptionDto;
import ru.stroy1click.attribute.exception.ValidationException;
import ru.stroy1click.attribute.service.AttributeOptionService;
import ru.stroy1click.attribute.util.ValidationErrorUtils;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attribute-options")
@Tag(name = "Product Type Attribute Controller", description = "Взаимодействие со значениями атрибута типа продукта")
@RateLimiter(name = "attributeOptionLimiter")
public class AttributeOptionController {

    private final AttributeOptionService attributeOptionService;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    @Operation(summary = "Получение значения атрибута типа продукта")
    public AttributeOptionDto get(@PathVariable("id") Integer id){
        return this.attributeOptionService.get(id);
    }

    @GetMapping
    @Operation(summary = "Получение атрибутов (опционально по типу продукта)")
    public List<AttributeOptionDto> getAll(
            @RequestParam(required = false) Integer productTypeId
    ) {
        if (productTypeId == null) {
            return attributeOptionService.getAll();
        }
        return attributeOptionService.getAllByProductTypeId(productTypeId);
    }


    @PostMapping
    @Operation(summary = "Создание значения атрибута типа продукта")
    public ResponseEntity<AttributeOptionDto> create(@RequestBody @Valid AttributeOptionDto attributeOptionDto,
                                                     BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        AttributeOptionDto createdProductTypeAttributeValue =
                this.attributeOptionService.create(attributeOptionDto);

        return ResponseEntity
                .created(URI.create("/api/v1/attribute-options/" + createdProductTypeAttributeValue.getId()))
                .body(createdProductTypeAttributeValue);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление значения атрибута типа продукта")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid AttributeOptionDto attributeOptionDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.attributeOptionService.update(id, attributeOptionDto);
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_type_attribute_value.update",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление значения атрибута типа продукта")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.attributeOptionService.delete(id);
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_type_attribute_value.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}