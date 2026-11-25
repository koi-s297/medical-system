package com.example.medicalsystem.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/** 排班新增/修改请求。 */
@Data
public class ScheduleReq {

    @NotNull(message = "医生不能为空")
    private Long doctorId;

    @NotNull(message = "出诊日期不能为空")
    private java.time.LocalDate workDate;

    @NotNull(message = "时段不能为空")
    @Min(value = 1, message = "时段只能是 1-上午 或 2-下午")
    @Max(value = 2, message = "时段只能是 1-上午 或 2-下午")
    private Byte timeSlot;

    @NotNull(message = "总号源不能为空")
    @Min(value = 1, message = "总号源至少为 1")
    private Integer totalQuota;

    @NotNull(message = "挂号费不能为空")
    @DecimalMin(value = "0.0", message = "挂号费不能为负")
    private java.math.BigDecimal fee;
}
