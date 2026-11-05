package com.example.medicalsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HelloController {
    @GetMapping("/api/hello")
    public Map<String, Object> hello() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", 200);
        data.put("msg", "医疗系统 Hello World");
        data.put("system", "medical-system");
        return data;
    }
}