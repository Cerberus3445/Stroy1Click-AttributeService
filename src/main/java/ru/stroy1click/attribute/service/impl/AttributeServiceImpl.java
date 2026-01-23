package ru.stroy1click.attribute.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stroy1click.attribute.dto.AttributeDto;
import ru.stroy1click.attribute.entity.Attribute;
import ru.stroy1click.attribute.exception.NotFoundException;
import ru.stroy1click.attribute.mapper.AttributeMapper;
import ru.stroy1click.attribute.repository.AttributeRepository;
import ru.stroy1click.attribute.service.AttributeService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;

    private final AttributeMapper attributeMapper;

    private final MessageSource messageSource;

    @Override
    @Cacheable(cacheNames = "attribute", key = "#id")
    public AttributeDto get(Integer id) {
        log.info("get {}", id);
        return this.attributeMapper.toDto(this.attributeRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                this.messageSource.getMessage(
                                        "error.attribute.not_found",
                                        null,
                                        Locale.getDefault()
                                )
                        )
                ));
    }

    @Override
    @Transactional
    @CacheEvict(value = "allAttributes", allEntries = true)
    public AttributeDto create(AttributeDto attributeDto) {
        log.info("create {}", attributeDto);

        Attribute createdAttribute = this.attributeRepository.save(this.attributeMapper.toEntity(
                attributeDto
        ));

        return this.attributeMapper.toDto(createdAttribute);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "attribute", key = "#id"),
            @CacheEvict(cacheNames = "allAttributes", allEntries = true)
    })
    public void delete(Integer id) {
        log.info("delete {}", id);
        Attribute attribute = this.attributeRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                this.messageSource.getMessage(
                                        "error.attribute.not_found",
                                        null,
                                        Locale.getDefault()
                                )
                        )
                );
        this.attributeRepository.delete(attribute);
    }


    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "attribute", key = "#id"),
            @CacheEvict(cacheNames = "allAttributes", allEntries = true)
    })
    public void update(Integer id, AttributeDto attributeDto) {
        log.info("update {} {}", id, attributeDto);
        this.attributeRepository.findById(id).ifPresentOrElse(attribute -> {
            Attribute updatedAttribute = Attribute.builder()
                    .id(id)
                    .title(attributeDto.getTitle())
                    .attributeOptions(attribute.getAttributeOptions())
                    .build();
            this.attributeRepository.save(updatedAttribute);
        }, () -> {
            throw new NotFoundException(
                    this.messageSource.getMessage(
                            "error.attribute.not_found",
                            null,
                            Locale.getDefault()
                    )
            );
        });

    }

    @Override
    @Cacheable(value = "allAttributes")
    public List<AttributeDto> getAll() {
        return this.attributeMapper.toDto(
                this.attributeRepository.findAll()
        );
    }

    @Override
    public Optional<Attribute> getByTitle(String title) {
        log.info("getByTitle {}", title);
        return this.attributeRepository.findByTitle(title);
    }
}