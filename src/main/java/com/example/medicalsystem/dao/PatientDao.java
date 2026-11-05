package com.example.medicalsystem.dao;

import com.example.medicalsystem.entity.Patient;

import java.util.List;

/**
 * 数据访问层：定义患者数据读写契约。
 * 实验一不接入数据库，JDBC / MyBatis 实现留待后续实验。
 */
public interface PatientDao {

    List<Patient> findAll();

    Patient findById(Long id);
}
