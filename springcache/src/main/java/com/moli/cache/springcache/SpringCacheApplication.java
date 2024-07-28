package com.moli.cache.springcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author moli
 * @time 2024-07-28 09:16:59
 */
@MapperScan("com.moli.cache.springcache.mapper")
@SpringBootApplication
public class SpringCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCacheApplication.class, args);
    }
}
