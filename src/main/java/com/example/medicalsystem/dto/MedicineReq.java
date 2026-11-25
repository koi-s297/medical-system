package com.example.medicalsystem.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/** 药品新增/修改请求。 */
@Data
public class MedicineReq {

    @NotBlank(message = "药品编码不能为空")
    private String medicineNo;

    @NotBlank(message = "药品名称不能为空")
    private String name;

    private String spec;

    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0.0", message = "单价不能为负")
    private java.math.BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负")
    private Integer stock;
}
