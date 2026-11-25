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
import java.time.LocalDateTime;

/**
 * <p>
 * 挂号记录表
 * </p>
 *
 * @author koi
 * @since 2026-11-12
 */
@Getter
@Setter
@ToString
@TableName("registration")
public class Registration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 挂号记录主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 挂号单号
     */
    @TableField("reg_no")
    private String regNo;

    /**
     * 患者外键
     */
    @TableField("patient_id")
    private Long patientId;

    /**
     * 医生外键
     */
    @TableField("doctor_id")
    private Long doctorId;

    /**
     * 排班外键（实验三新增：挂号关联到具体排班，用于号源扣减与防重复挂号）
     */
    @TableField("schedule_id")
    private Long scheduleId;

    /**
     * 挂号时间
     */
    @TableField("reg_time")
    private LocalDateTime regTime;

    /**
     * 状态：0-已挂号，1-已就诊，2-已取消
     */
    @TableField("status")
    private Byte status;

    /**
     * 挂号费
     */
    @TableField("fee")
    private BigDecimal fee;

    /**
     * 主诉
     */
    @TableField("symptom")
    private String symptom;

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
