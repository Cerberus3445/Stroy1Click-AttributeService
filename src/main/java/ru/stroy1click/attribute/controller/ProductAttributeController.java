package ru.stroy1click.attribute.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.stroy1click.attribute.dto.ProductAttributeDto;
import ru.stroy1click.attribute.entity.ProductAttribute;
import ru.stroy1click.attribute.exception.ValidationException;
import ru.stroy1click.attribute.model.PageResponse;
import ru.stroy1click.attribute.model.ProductAttributeValueFilter;
import ru.stroy1click.attribute.service.ProductAttributeService;
import ru.stroy1click.attribute.util.ValidationErrorUtils;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-attributes")
@Tag(name = "Product Attribute Value Controller", description = "Взаимодействие со значениями атрибута продукта")
@RateLimiter(name = "productAttributeValueLimiter")
public class ProductAttributeController {

    private final ProductAttributeService productAttributeService;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    @Operation(summary = "Получение значения атрибута продукта")
    public ResponseEntity<ProductAttributeDto> get(@PathVariable("id") Integer id){
        return ResponseEntity.ok(this.productAttributeService.get(id));
    }

    @PostMapping("/filter")
    public PageResponse<ProductAttributeDto> getProductIdsByAttributes(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                       @RequestParam(value = "size", defaultValue = "20") Integer size,
                                                                       @RequestBody @Valid ProductAttributeValueFilter filter,
                                                                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        return this.productAttributeService.getProductIdsByAttributes(filter,
                PageRequest.of(page, size));
    }

    @GetMapping("/{id}/attribute-values")
    @Operation(summary = "Получить значение атрибутов продукта")
    public List<ProductAttributeDto> getAttributesValue(@PathVariable("id") Integer id){
        return this.productAttributeService.getAllByProductId(id);
    }

    @PostMapping
    @Operation(summary = "Создание значения атрибута продукта")
    public ResponseEntity<ProductAttributeDto> create(@RequestBody @Valid ProductAttributeDto productAttributeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        ProductAttributeDto createdProductAttribute = this.productAttributeService.create(productAttributeDto);

        return ResponseEntity
                .created(URI.create("/api/v1/product-attributes/" + createdProductAttribute.getId()))
                .body(createdProductAttribute);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление значения атрибута продукта")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid ProductAttributeDto productAttributeDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.productAttributeService.update(id, productAttributeDto);
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_attribute_value.update",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление значения атрибута продукта")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.productAttributeService.delete(id);
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_attribute_value.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
