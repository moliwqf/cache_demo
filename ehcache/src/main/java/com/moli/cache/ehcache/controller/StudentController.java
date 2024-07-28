package com.moli.cache.ehcache.controller;

import com.moli.cache.ehcache.service.StudentService;
import com.moli.cache.entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author moli
 * @time 2024-07-28 09:38:37
 */
@RestController
@RequestMapping("student")
public class StudentController {

    @Resource
    private StudentService studentService;

    @GetMapping("getByName")
    public Student getByName(String name) {
        return studentService.getByName(name);
    }
}
