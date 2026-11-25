package com.example.medicalsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.medicalsystem.common.R;
import com.example.medicalsystem.dto.DoctorReq;
import com.example.medicalsystem.entity.Doctor;
import com.example.medicalsystem.service.DoctorService;
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

/** 医生基础 CRUD：支持姓名、手机号、身份证、科室、职称多条件组合分页。 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public R<Page<Doctor>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "5") long size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String idCard,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) String title) {
        LambdaQueryWrapper<Doctor> query = new LambdaQueryWrapper<>();
        // 所有条件均以参数绑定方式传入，等价于 PreparedStatement 的 ? 占位符
        query.like(StringUtils.hasText(name), Doctor::getName, name)
             .eq(StringUtils.hasText(phone), Doctor::getPhone, phone)
             .eq(StringUtils.hasText(idCard), Doctor::getIdCard, idCard)
             .eq(deptId != null, Doctor::getDeptId, deptId)
             .eq(StringUtils.hasText(title), Doctor::getTitle, title)
             .orderByAsc(Doctor::getId);
        return R.ok(doctorService.page(new Page<>(current, size), query));
    }

    @PostMapping
    public R<Long> add(@Validated @RequestBody DoctorReq req) {
        Doctor entity = new Doctor();
        BeanUtils.copyProperties(req, entity);
        doctorService.save(entity);
        return R.ok("新增医生成功", entity.getId());
    }

    @PutMapping("/{id}")
    public R<Boolean> update(@PathVariable Long id, @Validated @RequestBody DoctorReq req) {
        Doctor entity = new Doctor();
        BeanUtils.copyProperties(req, entity);
        entity.setId(id);
        if (!doctorService.updateById(entity)) {
            throw new IllegalArgumentException("医生不存在，id=" + id);
        }
        return R.ok("修改医生成功", true);
    }

    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        if (!doctorService.removeById(id)) {
            throw new IllegalArgumentException("医生不存在或已被挂号记录引用，id=" + id);
        }
        return R.ok("删除医生成功", true);
    }
}
