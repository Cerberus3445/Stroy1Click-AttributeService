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
import ru.stroy1click.attribute.dto.ProductAttributeDto;
import ru.stroy1click.attribute.entity.ProductAttribute;
import ru.stroy1click.attribute.entity.ProductTypeAttributeValue;
import ru.stroy1click.attribute.exception.NotFoundException;
import ru.stroy1click.attribute.mapper.ProductAttributeMapper;
import ru.stroy1click.attribute.model.PageResponse;
import ru.stroy1click.attribute.model.ProductAttributeValueFilter;
import ru.stroy1click.attribute.repository.ProductAttributeRepository;
import ru.stroy1click.attribute.service.ProductAttributeService;
import ru.stroy1click.attribute.service.ProductTypeAttributeValueService;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;

    private final MessageSource messageSource;

    private final ProductAttributeMapper productAttributeMapper;

    private final CacheClear cacheClear;

    private final ProductTypeAttributeValueService productTypeAttributeValueService;

    @Override
    @Cacheable(cacheNames = "productAttributeValue", key = "#id")
    public ProductAttributeDto get(Integer id) {
        log.info("get {}", id);

         return this.productAttributeMapper.toDto(this.productAttributeRepository.findById(id).orElseThrow(
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
    @Cacheable(cacheNames = "allProductAttributeValuesByProductId", key = "#id")
    public List<ProductAttributeDto> getAllByProductId(Integer id) {
        log.info("getAllByProductId {}", id);

         return this.productAttributeRepository.findByProductId(id).stream()
                 .map(this.productAttributeMapper::toDto)
                 .toList();
    }

    @Override
    public PageResponse<ProductAttributeDto> getProductIdsByAttributes(ProductAttributeValueFilter filter, Pageable pageable) {
        Page<ProductAttribute> page = this.productAttributeRepository.findProductIdsByAttributes(filter, pageable);

        List<ProductAttributeDto> productAttributeDtos = page.stream()
                .map(this.productAttributeMapper::toDto)
                .toList();

        return new PageResponse<>(
                productAttributeDtos, pageable.getPageNumber(), pageable.getPageSize(),
                page.getTotalElements(), page.getTotalPages(),
                page.isLast()
        );
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "allProductAttributeValuesByProductId", key = "#productTypeAttributeValueDto.productId")
    public ProductAttributeDto create(ProductAttributeDto productTypeAttributeValueDto) {
        log.info("create {}", productTypeAttributeValueDto);

        // проверка на существование
        this.productTypeAttributeValueService.get(productTypeAttributeValueDto.getProductTypeAttributeValueId());

        ProductAttribute createdProductAttribute = this.productAttributeRepository.save(
                this.productAttributeMapper.toEntity(productTypeAttributeValueDto)
        );

        return this.productAttributeMapper.toDto(createdProductAttribute);
    }

    @Override
    @Transactional
    @CacheEvict(value = "productAttributeValue", key = "#id")
    public void delete(Integer id) {
        log.info("delete {}", id);

        ProductAttribute productAttribute = this.productAttributeRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        this.messageSource.getMessage(
                                "error.product_type_attribute_value.not_found",
                                null,
                                Locale.getDefault()
                        )
                )
        );

        this.cacheClear.clearAllProductAttributeValuesByProductId(productAttribute.getProductId());
        this.productAttributeRepository.delete(productAttribute);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "productAttributeValue", key = "#id"),
            @CacheEvict(value = "allProductAttributeValuesByProductId", key = "#productAttributeDto.productId")
    })
    public void update(Integer id, ProductAttributeDto productAttributeDto) {
        this.productAttributeRepository.findById(id).ifPresentOrElse(productAttributeValue -> {
            ProductAttribute updatedProductTypeAttributeValue = ProductAttribute.builder()
                    .id(id)
                    .productId(productAttributeDto.getProductId())
                    .productTypeAttributeValue(productAttributeValue.getProductTypeAttributeValue())
                    .build();
            this.productAttributeRepository.save(updatedProductTypeAttributeValue);
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