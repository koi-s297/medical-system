package com.example.medicalsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 医生排班表
 * </p>
 *
 * @author koi
 * @since 2026-11-19
 */
@Getter
@Setter
@ToString
@TableName("doctor_schedule")
public class DoctorSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 排班主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 医生外键
     */
    @TableField("doctor_id")
    private Long doctorId;

    /**
     * 出诊日期
     */
    @TableField("work_date")
    private LocalDate workDate;

    /**
     * 时段：1-上午，2-下午
     */
    @TableField("time_slot")
    private Byte timeSlot;

    /**
     * 总号源
     */
    @TableField("total_quota")
    private Integer totalQuota;

    /**
     * 剩余号源
     */
    @TableField("remaining")
    private Integer remaining;

    /**
     * 该排班挂号费
     */
    @TableField("fee")
    private BigDecimal fee;

    /**
     * 状态：1-可约，0-停诊
     */
    @TableField("status")
    private Byte status;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
