package com.example.medicalsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 电子处方表
 * </p>
 *
 * @author koi
 * @since 2026-11-12
 */
@Getter
@Setter
@ToString
@TableName("prescription")
public class Prescription implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 处方主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 处方编号
     */
    @TableField("presc_no")
    private String prescNo;

    /**
     * 关联挂号记录外键
     */
    @TableField("registration_id")
    private Long registrationId;

    /**
     * 诊断结论
     */
    @TableField("diagnosis")
    private String diagnosis;

    /**
     * 医嘱（单值文本）
     */
    @TableField("advice")
    private String advice;

    /**
     * 状态：0-草稿，1-已开具，2-已作废
     */
    @TableField("status")
    private Byte status;

    /**
     * 开具时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
