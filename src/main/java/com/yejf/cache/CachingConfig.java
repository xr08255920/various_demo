package com.yejf.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tony on 2019/3/14.
 */

@EnableCaching
@Configuration
public class CachingConfig {
    @Bean
    public CacheManager cacheManager(){
//        ConcurrentMapCacheManager address = new ConcurrentMapCacheManager("address");
        CaffeineCacheManager address = new CaffeineCacheManager("address");
        return address;
    }
}
