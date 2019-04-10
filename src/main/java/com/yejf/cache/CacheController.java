package com.yejf.cache;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by tony on 2019/3/14.
 */
@RestController
public class CacheController {
    @Resource
    private CacheService cacheService;

    @RequestMapping("/sayHi")
    public String getSomeStr(@RequestParam String name){

        return cacheService.getSomeStr(name);
    }
}
