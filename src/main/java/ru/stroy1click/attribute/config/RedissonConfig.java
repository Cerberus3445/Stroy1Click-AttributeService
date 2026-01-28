package ru.stroy1click.attribute.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedissonConfig {

    @Value(value = "${redisson.host}")
    private String host;

    @Value(value = "${redisson.port}")
    private Integer port;

    @Value(value = "${redisson.username}")
    private String username;

    @Value(value = "${redisson.password}")
    private String password;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        config.useSingleServer()
                .setAddress("redis://%s:%d".formatted(this.host, this.port))
                .setDatabase(0)
                .setUsername(this.username)
                .setPassword(this.password);

        return Redisson.create(config);
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();

        long oneDayMillis = 24 * 60 * 60 * 1000L; // 1 день

        config.put("productAttributeValue", new CacheConfig(oneDayMillis, 0));
        config.put("allProductAttributeValuesByProductId", new CacheConfig(oneDayMillis, 0));
        config.put("attribute", new CacheConfig(oneDayMillis, 0));
        config.put("allAttributes", new CacheConfig(oneDayMillis, 0));
        config.put("allProductAttributeAssignments", new CacheConfig(oneDayMillis, 0));
        config.put("allAttributeOptions", new CacheConfig(oneDayMillis, 0));
        config.put("attributesByProductType", new CacheConfig(oneDayMillis, 0));

        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
