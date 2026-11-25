package com.example.medicalsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 科室新增/修改请求。校验规则放在请求对象上，
 * 与持久层实体分离，代码生成器重写 entity 时不会影响校验逻辑。
 */
@Data
public class DepartmentReq {

    @NotBlank(message = "科室编码不能为空")
    @Size(max = 32, message = "科室编码不能超过 32 个字符")
    private String deptCode;

    @NotBlank(message = "科室名称不能为空")
    @Size(max = 64, message = "科室名称不能超过 64 个字符")
    private String deptName;

    @Size(max = 128, message = "位置不能超过 128 个字符")
    private String location;

    @Pattern(regexp = "^[0-9-]{7,20}$", message = "科室电话只能包含数字与连字符，长度 7~20")
    private String phone;

    @Size(max = 255, message = "科室简介不能超过 255 个字符")
    private String description;
}
