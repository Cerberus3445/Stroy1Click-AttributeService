package ru.stroy1click.attribute.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stroy1click.attribute.cache.CacheClear;
import ru.stroy1click.attribute.dto.AttributeOptionDto;
import ru.stroy1click.attribute.entity.AttributeOption;
import ru.stroy1click.attribute.exception.NotFoundException;
import ru.stroy1click.attribute.mapper.AttributeOptionMapper;
import ru.stroy1click.attribute.repository.AttributeOptionRepository;
import ru.stroy1click.attribute.service.AttributeService;
import ru.stroy1click.attribute.service.AttributeOptionService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttributeOptionServiceImpl implements AttributeOptionService {

    private final AttributeOptionRepository attributeOptionRepository;

    private final MessageSource messageSource;

    private final AttributeOptionMapper attributeOptionMapper;

    private final CacheClear cacheClear;

    private final AttributeService attributeService;

    @Override
    @Cacheable(cacheNames = "attributeOption", key = "#id")
    public AttributeOptionDto get(Integer id) {
        log.info("get {}", id);

        return this.attributeOptionMapper.toDto(this.attributeOptionRepository.findById(id).orElseThrow(
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
    @Cacheable(value = "allAttributeOptions")
    public List<AttributeOptionDto> getAll() {
        return this.attributeOptionMapper.toDto(
                this.attributeOptionRepository.findAll()
        );
    }

    @Override
    @Cacheable(cacheNames = "allAttributeOptionsByProductTypeId", key = "#productTypeId")
    public List<AttributeOptionDto> getAllByProductTypeId(Integer productTypeId) {
        log.info("getAllByProductId {}", productTypeId);

         return this.attributeOptionRepository.findByProductTypeId(productTypeId).stream()
                 .map(this.attributeOptionMapper::toDto)
                 .toList();
    }

    @Override
    public Optional<AttributeOption> getByValue(String value) {
        log.info("getByValue {}", value);

        return this.attributeOptionRepository.findByValue(value);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allAttributeOptions", allEntries = true),
            @CacheEvict(value = "allAttributeOptionsByProductTypeId", key = "#attributeOptionDto.productTypeId")
    })
    public AttributeOptionDto create(AttributeOptionDto attributeOptionDto) {
        log.info("create {}", attributeOptionDto);

        //Проверка на существование атрибута
        this.attributeService.get(attributeOptionDto.getAttributeId());

        AttributeOption createdAttributeOption =  this.attributeOptionRepository.save(
                this.attributeOptionMapper.toEntity(attributeOptionDto)
        );

        return this.attributeOptionMapper.toDto(createdAttributeOption);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allAttributeOptions", allEntries = true),
            @CacheEvict(value = "attributeOption", key = "#id")
    })
    public void delete(Integer id) {
        log.info("delete {}", id);

        AttributeOption attributeOption = this.attributeOptionRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        this.messageSource.getMessage(
                                "error.product_type_attribute_value.not_found",
                                null,
                                Locale.getDefault()
                        )
                )
        );

        this.cacheClear.clearAllAttributeOptionsByProductTypeId(attributeOption.getProductTypeId());
        this.attributeOptionRepository.delete(attributeOption);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "attributeOption", key = "#id"),
            @CacheEvict(value = "allAttributeOptions", allEntries = true),
            @CacheEvict(value = "allAttributeOptionsByProductTypeId", key = "#attributeOptionDto.productTypeId")
    })
    public void update(Integer id, AttributeOptionDto attributeOptionDto) {
        log.info("update {}, {}", id, attributeOptionDto);

        this.attributeOptionRepository.findById(id).ifPresentOrElse(productAttributeValue -> {
            AttributeOption updatedAttributeOption = AttributeOption.builder()
                    .id(id)
                    .value(attributeOptionDto.getValue())
                    .productTypeId(productAttributeValue.getProductTypeId())
                    .attribute(productAttributeValue.getAttribute())
                    .build();

            this.attributeOptionRepository.save(updatedAttributeOption);
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