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
 * 患者表
 * </p>
 *
 * @author koi
 * @since 2026-11-12
 */
@Getter
@Setter
@ToString
@TableName("patient")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 患者主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 就诊卡号
     */
    @TableField("patient_no")
    private String patientNo;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 身份证号（18 位）
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 手机号（11 位）
     */
    @TableField("phone")
    private String phone;

    /**
     * 性别：1-男，2-女
     */
    @TableField("gender")
    private Byte gender;

    /**
     * 联系地址
     */
    @TableField("address")
    private String address;

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
