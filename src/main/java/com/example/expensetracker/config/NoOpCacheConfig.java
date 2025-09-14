package com.example.expensetracker.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fallback cache configuration when Redis is not available
 * Uses in-memory caching instead of Redis
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "none")
public class NoOpCacheConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(NoOpCacheConfig.class);
    
    @Bean
    public CacheManager cacheManager() {
        logger.info("Using in-memory cache manager (Redis disabled)");
        return new ConcurrentMapCacheManager();
    }
}


