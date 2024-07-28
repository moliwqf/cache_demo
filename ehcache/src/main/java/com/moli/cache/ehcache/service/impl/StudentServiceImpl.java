package com.moli.cache.ehcache.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moli.cache.ehcache.mapper.StudentMapper;
import com.moli.cache.entity.Student;
import com.moli.cache.ehcache.service.StudentService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-07-28 09:34:48
 */
@Service
@CacheConfig(cacheNames = "users")
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    private CacheManager cacheManager;

    @Override
    @Cacheable(key = "#name")
    public Student getByName(String name) {
        return this.getOne(
                new LambdaQueryWrapper<Student>()
                        .like(Student::getName, name)
        );
    }
}
