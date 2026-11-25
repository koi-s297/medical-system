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
 * 药品表
 * </p>
 *
 * @author koi
 * @since 2026-11-19
 */
@Getter
@Setter
@ToString
@TableName("medicine")
public class Medicine implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 药品主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 药品编码
     */
    @TableField("medicine_no")
    private String medicineNo;

    /**
     * 药品名称
     */
    @TableField("name")
    private String name;

    /**
     * 规格
     */
    @TableField("spec")
    private String spec;

    /**
     * 单价
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 库存数量
     */
    @TableField("stock")
    private Integer stock;

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
