package com.example.medicalsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.medicalsystem.common.R;
import com.example.medicalsystem.dto.PatientReq;
import com.example.medicalsystem.entity.Patient;
import com.example.medicalsystem.service.PatientService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 患者基础 CRUD：支持姓名、就诊卡号、手机号、身份证、性别多条件组合分页。
 * 所有查询条件经 LambdaQueryWrapper 参数绑定，等价于 PreparedStatement 的 ? 占位符。
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public R<Page<Patient>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "5") long size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String patientNo,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String idCard,
            @RequestParam(required = false) Byte gender) {
        LambdaQueryWrapper<Patient> query = new LambdaQueryWrapper<>();
        query.like(StringUtils.hasText(name), Patient::getName, name)
             .eq(StringUtils.hasText(patientNo), Patient::getPatientNo, patientNo)
             .eq(StringUtils.hasText(phone), Patient::getPhone, phone)
             .eq(StringUtils.hasText(idCard), Patient::getIdCard, idCard)
             .eq(gender != null, Patient::getGender, gender)
             .orderByAsc(Patient::getId);
        return R.ok(patientService.page(new Page<>(current, size), query));
    }

    @PostMapping
    public R<Long> add(@Validated @RequestBody PatientReq req) {
        Patient entity = new Patient();
        BeanUtils.copyProperties(req, entity);
        patientService.save(entity);
        return R.ok("新增患者成功", entity.getId());
    }

    @PutMapping("/{id}")
    public R<Boolean> update(@PathVariable Long id, @Validated @RequestBody PatientReq req) {
        Patient entity = new Patient();
        BeanUtils.copyProperties(req, entity);
        entity.setId(id);
        if (!patientService.updateById(entity)) {
            throw new IllegalArgumentException("患者不存在，id=" + id);
        }
        return R.ok("修改患者成功", true);
    }

    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        if (!patientService.removeById(id)) {
            throw new IllegalArgumentException("患者不存在或已被挂号记录引用，id=" + id);
        }
        return R.ok("删除患者成功", true);
    }
}
