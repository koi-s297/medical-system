package com.example.medicalsystem.service.impl;

import com.example.medicalsystem.entity.Registration;
import com.example.medicalsystem.mapper.RegistrationMapper;
import com.example.medicalsystem.service.RegistrationService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 挂号记录表 服务实现类
 * </p>
 *
 * @author koi
 * @since 2026-11-12
 */
@Service
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements RegistrationService {

}
