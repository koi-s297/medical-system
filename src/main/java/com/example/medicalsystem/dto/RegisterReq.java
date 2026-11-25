package com.example.medicalsystem.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 在线挂号请求。simulateFail 仅用于实验演示事务回滚：
 * 为 true 时业务层在扣减号源后故意抛异常，用来验证号源能否随事务回滚。
 */
@Data
public class RegisterReq {

    @NotNull(message = "患者不能为空")
    private Long patientId;

    @NotNull(message = "排班不能为空")
    private Long scheduleId;

    @Size(max = 255, message = "主诉不能超过 255 个字符")
    private String symptom;

    private Boolean simulateFail;
}
