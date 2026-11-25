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
 * 医生表
 * </p>
 *
 * @author koi
 * @since 2026-11-12
 */
@Getter
@Setter
@ToString
@TableName("doctor")
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 医生主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属科室外键
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 医生工号
     */
    @TableField("doctor_no")
    private String doctorNo;

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
     * 职称：主任医师/副主任医师/主治医师/住院医师
     */
    @TableField("title")
    private String title;

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
