package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.entity.Doctor;
import com.example.medicalsystem.mapper.DoctorMapper;
import com.example.medicalsystem.service.DoctorService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 医生表 服务实现类
 * </p>
 *
 * @author koi
 * @since 2026-11-12
 */
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {

}
