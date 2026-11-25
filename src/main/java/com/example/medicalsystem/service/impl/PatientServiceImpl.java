package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.entity.Patient;
import com.example.medicalsystem.mapper.PatientMapper;
import com.example.medicalsystem.service.PatientService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 患者表 服务实现类
 * </p>
 *
 * @author koi
 * @since 2026-11-12
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

}
