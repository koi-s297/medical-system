package com.example.medicalsystem.entity;

/**
 * 实体层：患者业务对象。
 * 实验一仅完成工程初始化，保留最简字段结构。
 */
public class Patient {

    private Long id;
    private String name;
    private Integer age;

    public Patient() {
    }

    public Patient(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
