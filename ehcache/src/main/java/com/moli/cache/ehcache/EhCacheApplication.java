package com.moli.cache.ehcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author moli
 * @time 2024-07-28 09:16:59
 */
@MapperScan("com.moli.cache.ehcache.mapper")
@SpringBootApplication
public class EhCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(EhCacheApplication.class, args);
    }
}
