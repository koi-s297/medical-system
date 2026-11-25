package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.entity.Prescription;
import com.example.medicalsystem.mapper.PrescriptionMapper;
import com.example.medicalsystem.service.PrescriptionService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电子处方表 服务实现类
 * </p>
 *
 * @author koi
 * @since 2026-11-12
 */
@Service
public class PrescriptionServiceImpl extends ServiceImpl<PrescriptionMapper, Prescription> implements PrescriptionService {

}
