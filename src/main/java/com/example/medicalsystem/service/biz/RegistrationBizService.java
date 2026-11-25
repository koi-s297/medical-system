package com.example.medicalsystem.service.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.medicalsystem.common.BusinessException;
import com.example.medicalsystem.dto.RegisterReq;
import com.example.medicalsystem.entity.DoctorSchedule;
import com.example.medicalsystem.entity.Registration;
import com.example.medicalsystem.mapper.DoctorScheduleMapper;
import com.example.medicalsystem.mapper.RegistrationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 挂号业务编排层：把"校验 → 扣号源 → 写挂号单"串成一个事务。
 *
 * <p>分层职责：Controller 只做参数接收与结果封装；本层负责业务规则与事务边界；
 * 基础 Service / Mapper 只做单表读写。这样业务逻辑集中、可复用、易测试（高内聚低耦合）。</p>
 */
@Service
public class RegistrationBizService {

    @Autowired
    private RegistrationMapper registrationMapper;

    @Autowired
    private DoctorScheduleMapper doctorScheduleMapper;

    /**
     * 在线挂号：四步在同一事务内完成，任何一步失败整体回滚。
     *
     * @return 新增挂号单主键
     */
    @Transactional(rollbackFor = Exception.class)
    public Long register(RegisterReq req) {
        // 1. 查排班，校验存在与可约
        DoctorSchedule schedule = doctorScheduleMapper.selectById(req.getScheduleId());
        if (schedule == null) {
            throw new BusinessException(404, "所选排班不存在");
        }
        if (schedule.getStatus() == null || schedule.getStatus() != 1) {
            throw new BusinessException(409, "该排班已停诊，无法挂号");
        }

        // 2. 防重复挂号：同一患者 + 同一排班只允许一条（数据库唯一键是最后兜底）
        Long exists = registrationMapper.selectCount(
                new LambdaQueryWrapper<Registration>()
                        .eq(Registration::getPatientId, req.getPatientId())
                        .eq(Registration::getScheduleId, req.getScheduleId()));
        if (exists != null && exists > 0) {
            throw new BusinessException(409, "重复挂号：该患者已预约过此排班");
        }

        // 3. 带条件扣号源：WHERE remaining > 0，受影响行数为 0 说明号源已满
        int deducted = doctorScheduleMapper.deductQuota(req.getScheduleId());
        if (deducted == 0) {
            throw new BusinessException(409, "号源已满，请选择其他排班");
        }

        // 4. 模拟异常开关：扣完号源后抛错，用来现场验证事务回滚（号源应恢复）
        if (Boolean.TRUE.equals(req.getSimulateFail())) {
            throw new RuntimeException("模拟异常：触发事务回滚，号源应保持不变");
        }

        // 5. 写挂号单
        Registration reg = new Registration();
        reg.setRegNo(nextNo("REG"));
        reg.setPatientId(req.getPatientId());
        reg.setDoctorId(schedule.getDoctorId());
        reg.setScheduleId(req.getScheduleId());
        reg.setRegTime(LocalDateTime.now());
        reg.setStatus((byte) 0);
        reg.setFee(schedule.getFee());
        reg.setSymptom(req.getSymptom());
        registrationMapper.insert(reg);
        return reg.getId();
    }

    /** 生成单号：前缀 + 时间戳，保证唯一且便于识别。 */
    private String nextNo(String prefix) {
        return prefix + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
