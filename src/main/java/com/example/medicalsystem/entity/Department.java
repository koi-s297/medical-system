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
 * 科室表
 * </p>
 *
 * @author koi
 * @since 2026-11-12
 */
@Getter
@Setter
@ToString
@TableName("department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 科室主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 科室编码
     */
    @TableField("dept_code")
    private String deptCode;

    /**
     * 科室名称
     */
    @TableField("dept_name")
    private String deptName;

    /**
     * 所在楼栋/楼层
     */
    @TableField("location")
    private String location;

    /**
     * 科室联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 科室简介
     */
    @TableField("description")
    private String description;

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
