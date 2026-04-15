package ru.stroy1click.attribute.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stroy1click.attribute.cache.CacheClear;
import ru.stroy1click.attribute.dto.PageResponse;
import ru.stroy1click.attribute.dto.ProductAttributeAssignmentDto;
import ru.stroy1click.attribute.dto.ProductAttributeValueFilter;
import ru.stroy1click.attribute.entity.ProductAttributeAssignment;
import ru.stroy1click.attribute.mapper.ProductAttributeAssignmentMapper;
import ru.stroy1click.attribute.repository.ProductAttributeAssignmentRepository;
import ru.stroy1click.attribute.service.AttributeOptionService;
import ru.stroy1click.attribute.service.ProductAttributeAssignmentService;
import ru.stroy1click.common.util.ExceptionUtils;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductAttributeAssignmentServiceImpl implements ProductAttributeAssignmentService {

    private final ProductAttributeAssignmentRepository productAttributeAssignmentRepository;

    private final ProductAttributeAssignmentMapper productAttributeAssignmentMapper;

    private final CacheClear cacheClear;

    private final AttributeOptionService attributeOptionService;

    @Override
    @Cacheable(cacheNames = "productAttributeAssignment", key = "#id")
    public ProductAttributeAssignmentDto get(Integer id) {
        log.info("get {}", id);

        ProductAttributeAssignment productAttributeAssignment = this.productAttributeAssignmentRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.product_attribute_assignment.not_found", id));

         return this.productAttributeAssignmentMapper.toDto(productAttributeAssignment);
    }

    @Override
    @Cacheable(value = "allProductAttributeAssignments")
    public List<ProductAttributeAssignmentDto> getAll() {
        log.info("getAll");

        return this.productAttributeAssignmentMapper.toDto(
                this.productAttributeAssignmentRepository.findAll()
        );
    }

    @Override
    @Cacheable(cacheNames = "allProductAttributeAssignmentsByProductId", key = "#id")
    public List<ProductAttributeAssignmentDto> getAllByProductId(Integer id) {
        log.info("getAllByProductId {}", id);

         return this.productAttributeAssignmentRepository.findByProductId(id).stream()
                 .map(this.productAttributeAssignmentMapper::toDto)
                 .toList();
    }

    @Override
    public PageResponse<ProductAttributeAssignmentDto> getProductIdsByAttributes(ProductAttributeValueFilter filter, Pageable pageable) {
        log.info("getProductIdsByAttributes {}", filter);

        Page<ProductAttributeAssignment> page = this.productAttributeAssignmentRepository.findProductIdsByAttributes(filter, pageable);

        List<ProductAttributeAssignmentDto> productAttributeAssignmentDtos = page.stream()
                .map(this.productAttributeAssignmentMapper::toDto)
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
                this.productAttributeAssignmentMapper.toEntity(productTypeAttributeValueDto)
        );

        return this.productAttributeAssignmentMapper.toDto(createdProductAttributeAssignment);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "productAttributeAssignment", key = "#id"),
            @CacheEvict(value = "allProductAttributeAssignments", allEntries = true)
    })
    public void delete(Integer id) {
        log.info("delete {}", id);

        ProductAttributeAssignment productAttributeAssignment = this.productAttributeAssignmentRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.product_attribute_assignment.not_found", id));

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
        log.info("update {} {}", id, productAttributeAssignmentDto);

        this.productAttributeAssignmentRepository.findById(id).ifPresentOrElse(productAttributeAssignment -> {
            productAttributeAssignment.setProductId(productAttributeAssignmentDto.getProductId());
        }, () -> {
            throw ExceptionUtils.notFound("error.product_attribute_assignment.not_found", id);
        });
    }
}