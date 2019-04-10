package com.yejf.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by tony on 2019/3/14.
 */
@Service
public class CacheService {

    @Cacheable("address")
    public String getSomeStr(String name){
        System.out.println("invoke getSomeStr...");
        return "hi "+name;
    }
}
