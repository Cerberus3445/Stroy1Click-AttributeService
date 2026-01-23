package ru.stroy1click.attribute.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stroy1click.attribute.cache.CacheClear;
import ru.stroy1click.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.attribute.exception.NotFoundException;
import ru.stroy1click.attribute.model.PageResponse;
import ru.stroy1click.attribute.model.ProductAttributeValueFilter;
import ru.stroy1click.attribute.repository.ProductAttributeAssignmentRepository;
import ru.stroy1click.attribute.service.ProductAttributeAssignment;
import ru.stroy1click.attribute.service.AttributeOptionService;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductAttributeAssignmentImpl implements ProductAttributeAssignment {

    private final ProductAttributeAssignmentRepository productAttributeAssignmentRepository;

    private final MessageSource messageSource;

    private final ru.stroy1click.attribute.mapper.ProductAttributeAssignment productAttributeAssignment;

    private final CacheClear cacheClear;

    private final AttributeOptionService attributeOptionService;

    @Override
    @Cacheable(cacheNames = "productAttributeAssignment", key = "#id")
    public ProductAttributeAssignmentDto get(Integer id) {
        log.info("get {}", id);

         return this.productAttributeAssignment.toDto(this.productAttributeAssignmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        this.messageSource.getMessage(
                                "error.product_type_attribute_value.not_found",
                                null,
                                Locale.getDefault()
                        )
                )
        ));
    }

    @Override
    @Cacheable(value = "allProductAttributeAssignments")
    public List<ProductAttributeAssignmentDto> getAll() {
        return this.productAttributeAssignment.toDto(
                this.productAttributeAssignmentRepository.findAll()
        );
    }

    @Override
    @Cacheable(cacheNames = "allProductAttributeAssignmentsByProductId", key = "#id")
    public List<ProductAttributeAssignmentDto> getAllByProductId(Integer id) {
        log.info("getAllByProductId {}", id);

         return this.productAttributeAssignmentRepository.findByProductId(id).stream()
                 .map(this.productAttributeAssignment::toDto)
                 .toList();
    }

    @Override
    public PageResponse<ProductAttributeAssignmentDto> getProductIdsByAttributes(ProductAttributeValueFilter filter, Pageable pageable) {
        Page<ru.stroy1click.attribute.entity.ProductAttributeAssignment> page = this.productAttributeAssignmentRepository.findProductIdsByAttributes(filter, pageable);

        List<ProductAttributeAssignmentDto> productAttributeAssignmentDtos = page.stream()
                .map(this.productAttributeAssignment::toDto)
                .toList();

        return new PageResponse<>(
                productAttributeAssignmentDtos, pageable.getPageNumber(), pageable.getPageSize(),
                page.getTotalElements(), page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allProductAttributeAssignmentsByProductId", key = "#productTypeAttributeValueDto.productId"),
            @CacheEvict(value = "allProductAttributeAssignments", allEntries = true)
    })
    public ProductAttributeAssignmentDto create(ProductAttributeAssignmentDto productTypeAttributeValueDto) {
        log.info("create {}", productTypeAttributeValueDto);

        // проверка на существование
        this.attributeOptionService.get(productTypeAttributeValueDto.getAttributeOptionId());

        ru.stroy1click.attribute.entity.ProductAttributeAssignment createdProductAttributeAssignment = this.productAttributeAssignmentRepository.save(
                this.productAttributeAssignment.toEntity(productTypeAttributeValueDto)
        );

        return this.productAttributeAssignment.toDto(createdProductAttributeAssignment);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "productAttributeAssignment", key = "#id"),
            @CacheEvict(value = "allProductAttributeAssignments", allEntries = true)
    })
    public void delete(Integer id) {
        log.info("delete {}", id);

        ru.stroy1click.attribute.entity.ProductAttributeAssignment productAttributeAssignment = this.productAttributeAssignmentRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        this.messageSource.getMessage(
                                "error.product_type_attribute_value.not_found",
                                null,
                                Locale.getDefault()
                        )
                )
        );

        this.cacheClear.clearAllProductAttributeValuesByProductId(productAttributeAssignment.getProductId());
        this.productAttributeAssignmentRepository.delete(productAttributeAssignment);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "productAttributeAssignment", key = "#id"),
            @CacheEvict(value = "allProductAttributeAssignmentsByProductId", key = "#productAttributeAssignmentDto.productId"),
            @CacheEvict(value = "allProductAttributeAssignments", allEntries = true)
    })
    public void update(Integer id, ProductAttributeAssignmentDto productAttributeAssignmentDto) {
        this.productAttributeAssignmentRepository.findById(id).ifPresentOrElse(productAttributeValue -> {
            ru.stroy1click.attribute.entity.ProductAttributeAssignment updatedProductTypeAttributeValue = ru.stroy1click.attribute.entity.ProductAttributeAssignment.builder()
                    .id(id)
                    .productId(productAttributeAssignmentDto.getProductId())
                    .attributeOption(productAttributeValue.getAttributeOption())
                    .build();
            this.productAttributeAssignmentRepository.save(updatedProductTypeAttributeValue);
        }, () -> {
            throw new NotFoundException(
                    this.messageSource.getMessage(
                            "error.product_type_attribute_value.not_found",
                            null,
                            Locale.getDefault()
                    )
            );
        });
    }
}