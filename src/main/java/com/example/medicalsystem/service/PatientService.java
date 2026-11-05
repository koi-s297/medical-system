package com.example.medicalsystem.service;

import com.example.medicalsystem.entity.Patient;

import java.util.List;

/**
 * 业务逻辑层：对外提供患者相关业务能力。
 */
public interface PatientService {

    List<Patient> listPatients();
}
