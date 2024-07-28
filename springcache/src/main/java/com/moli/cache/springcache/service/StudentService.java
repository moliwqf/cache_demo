package com.moli.cache.springcache.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.moli.cache.entity.Student;

/**
 * @author moli
 * @time 2024-07-28 09:34:25
 */
public interface StudentService extends IService<Student> {

    Student getByName(String name);
}
