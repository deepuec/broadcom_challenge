
package com.broadcom.challenge.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfig {
    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);
    public static final String USER_COUNT_CACHE = "usercountcache";


    private CacheManager cacheManager;

    @Bean
    public CacheManager cacheManager() {
        //Not mandatory to add the cache names during compile time
        cacheManager =  new ConcurrentMapCacheManager(USER_COUNT_CACHE);
        return cacheManager;
    }


    @Scheduled(
        cron = "${app.cache.cron:0  0/10 * * * ?}"
    )
    public void scheduledCacheEvict() {
        cacheManager.getCacheNames().forEach(this::cacheEvict);
    }


    /**
     * Clear all entries from {@code cacheName}
     * @param cacheName
     */
    public void cacheEvict(String cacheName) {
        log.info("************CLEARING CACHE for::::{}", cacheName);
        if(StringUtils.hasText(cacheName)){
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        }

    }
}
