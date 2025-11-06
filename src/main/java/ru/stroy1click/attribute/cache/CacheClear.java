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

    public void clearAllProductTypeAttributeValuesByProductTypeId(Integer productTypeId){
        log.info("clearAllProductTypeAttributeValuesByProductTypeId {}", productTypeId);
        deleteCache("allProductTypeAttributeValuesByProductTypeId", productTypeId);
    }

    private void deleteCache(String key, Integer value){
        Cache cache = this.cacheManager.getCache(key);
        if(cache != null){
            cache.evict(value);
        }
    }
}