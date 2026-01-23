package ru.stroy1click.attribute.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheClear {

    private final CacheManager cacheManager;

    public void clearAllProductAttributeValuesByProductId(Integer productId){
        log.info("allProductAttributeValuesByProductTypeId {}", productId);
        deleteCache("allProductAttributeValuesByProductId", productId);
    }

    public void clearAllAttributeOptionsByProductTypeId(Integer productTypeId){
        log.info("clearAllAttributeOptionsByProductTypeId {}", productTypeId);
        deleteCache("allAttributeOptionsByProductTypeId", productTypeId);
    }

    private void deleteCache(String key, Integer value){
        Cache cache = this.cacheManager.getCache(key);
        if(cache != null){
            cache.evict(value);
        }
    }
}