package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.entity.Patient;
import com.example.medicalsystem.service.PatientService;

import java.util.Collections;
import java.util.List;

/**
 * 业务逻辑层实现：实验一仅作分层占位，暂返回空集合。
 * 不注入数据访问层，避免引入尚未实现的 Bean 依赖。
 */
public class PatientServiceImpl implements PatientService {

    @Override
    public List<Patient> listPatients() {
        return Collections.emptyList();
    }
}
