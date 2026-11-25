package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.entity.Medicine;
import com.example.medicalsystem.mapper.MedicineMapper;
import com.example.medicalsystem.service.MedicineService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 药品表 服务实现类
 *
 * @author koi
 * @since 2026-11-19
 */
@Service
public class MedicineServiceImpl extends ServiceImpl<MedicineMapper, Medicine> implements MedicineService {

}
