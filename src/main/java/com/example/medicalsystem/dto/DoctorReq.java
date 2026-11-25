package com.example.medicalsystem.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** 医生新增/修改请求：身份证、手机号为医疗敏感字段，必须做格式校验。 */
@Data
public class DoctorReq {

    @NotNull(message = "所属科室不能为空")
    private Long deptId;

    @NotBlank(message = "医生工号不能为空")
    @Size(max = 32, message = "医生工号不能超过 32 个字符")
    private String doctorNo;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 32, message = "姓名不能超过 32 个字符")
    private String name;

    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^\\d{17}[\\dXx]$", message = "身份证必须为 18 位，末位可为数字或 X")
    private String idCard;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号必须为 1 开头的 11 位数字")
    private String phone;

    @NotNull(message = "性别不能为空")
    @Min(value = 1, message = "性别只能是 1-男 或 2-女")
    @Max(value = 2, message = "性别只能是 1-男 或 2-女")
    private Byte gender;

    @Size(max = 32, message = "职称不能超过 32 个字符")
    private String title;
}
