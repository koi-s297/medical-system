package com.example.medicalsystem.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 开具处方请求：一个处方带多条药品明细，扣库存与写明细在同一事务内。
 */
@Data
public class PrescriptionReq {

    @NotNull(message = "挂号记录不能为空")
    private Long registrationId;

    @NotBlank(message = "诊断结论不能为空")
    @Size(max = 255, message = "诊断结论不能超过 255 个字符")
    private String diagnosis;

    @Size(max = 500, message = "医嘱不能超过 500 个字符")
    private String advice;

    @NotEmpty(message = "药品明细不能为空")
    private java.util.List<PrescriptionItemReq> items;

    private Boolean simulateFail;

    /** 处方明细：药品 + 数量。 */
    @Data
    public static class PrescriptionItemReq {

        @NotNull(message = "药品不能为空")
        private Long medicineId;

        @NotNull(message = "数量不能为空")
        @Min(value = 1, message = "数量至少为 1")
        private Integer quantity;
    }
}
