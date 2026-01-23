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
import ru.stroy1click.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.attribute.exception.ValidationException;
import ru.stroy1click.attribute.model.PageResponse;
import ru.stroy1click.attribute.model.ProductAttributeValueFilter;
import ru.stroy1click.attribute.service.ProductAttributeAssignment;
import ru.stroy1click.attribute.util.ValidationErrorUtils;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-attribute-assignments")
@Tag(name = "Product Attribute Value Controller", description = "Взаимодействие со значениями атрибута продукта")
@RateLimiter(name = "productAttributeAssignmentLimiter")
public class ProductAttributeAssignmentController {

    private final ProductAttributeAssignment productAttributeAssignment;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    @Operation(summary = "Получение атрибута продукта")
    public ProductAttributeAssignmentDto get(@PathVariable("id") Integer id){
        return this.productAttributeAssignment.get(id);
    }

    @GetMapping
    @Operation(summary = "Получение всех атрибутов продукта")
    public List<ProductAttributeAssignmentDto> getAll(){
        return this.productAttributeAssignment.getAll();
    }

    @PostMapping("/filter")
    public PageResponse<ProductAttributeAssignmentDto> getProductIdsByAttributes(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                 @RequestParam(value = "size", defaultValue = "20") Integer size,
                                                                                 @RequestBody @Valid ProductAttributeValueFilter filter,
                                                                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        return this.productAttributeAssignment.getProductIdsByAttributes(filter,
                PageRequest.of(page, size));
    }

    @GetMapping("/{id}/attribute-values")
    @Operation(summary = "Получить все атрибуты продукта по id атрибута")
    public List<ProductAttributeAssignmentDto> getAttributesValue(@PathVariable("id") Integer id){
        return this.productAttributeAssignment.getAllByProductId(id);
    }

    @PostMapping
    @Operation(summary = "Создание атрибут продукта")
    public ResponseEntity<ProductAttributeAssignmentDto> create(@RequestBody @Valid ProductAttributeAssignmentDto productAttributeAssignmentDto,
                                                                BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        ProductAttributeAssignmentDto createdProductAttribute = this.productAttributeAssignment.create(productAttributeAssignmentDto);

        return ResponseEntity
                .created(URI.create("/api/v1/product-attribute-assignments/" + createdProductAttribute.getId()))
                .body(createdProductAttribute);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление атрибута продукта")
    public ResponseEntity<String> update(@PathVariable("id") Integer id,
                                         @RequestBody @Valid ProductAttributeAssignmentDto productAttributeAssignmentDto,
                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.productAttributeAssignment.update(id, productAttributeAssignmentDto);
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_attribute_value.update",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление атрибута продукта")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        this.productAttributeAssignment.delete(id);
        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.product_attribute_value.delete",
                        null,
                        Locale.getDefault()
                )
        );
    }
}
