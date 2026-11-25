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
 * 处方明细表
 * </p>
 *
 * @author koi
 * @since 2026-11-19
 */
@Getter
@Setter
@ToString
@TableName("prescription_item")
public class PrescriptionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 明细主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 处方外键
     */
    @TableField("presc_id")
    private Long prescId;

    /**
     * 药品外键
     */
    @TableField("medicine_id")
    private Long medicineId;

    /**
     * 数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 开方时单价快照
     */
    @TableField("unit_price")
    private BigDecimal unitPrice;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
