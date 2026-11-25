package com.example.medicalsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.medicalsystem.common.R;
import com.example.medicalsystem.dto.DepartmentReq;
import com.example.medicalsystem.entity.Department;
import com.example.medicalsystem.service.DepartmentService;
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
 * 科室基础 CRUD。
 * 查询条件全部通过 LambdaQueryWrapper 以参数绑定（#{} / PreparedStatement）方式拼装，
 * 不做任何 SQL 字符串拼接，从根本上避免 SQL 注入。
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /** 多条件组合分页查询：科室名称模糊、编码精确，条件可任意组合。 */
    @GetMapping
    public R<Page<Department>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "5") long size,
            @RequestParam(required = false) String deptName,
            @RequestParam(required = false) String deptCode) {
        LambdaQueryWrapper<Department> query = new LambdaQueryWrapper<>();
        query.like(StringUtils.hasText(deptName), Department::getDeptName, deptName)
             .eq(StringUtils.hasText(deptCode), Department::getDeptCode, deptCode)
             .orderByAsc(Department::getId);
        return R.ok(departmentService.page(new Page<>(current, size), query));
    }

    @PostMapping
    public R<Long> add(@Validated @RequestBody DepartmentReq req) {
        Department entity = new Department();
        BeanUtils.copyProperties(req, entity);
        departmentService.save(entity);
        return R.ok("新增科室成功", entity.getId());
    }

    @PutMapping("/{id}")
    public R<Boolean> update(@PathVariable Long id, @Validated @RequestBody DepartmentReq req) {
        Department entity = new Department();
        BeanUtils.copyProperties(req, entity);
        entity.setId(id);
        if (!departmentService.updateById(entity)) {
            throw new IllegalArgumentException("科室不存在，id=" + id);
        }
        return R.ok("修改科室成功", true);
    }

    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        if (!departmentService.removeById(id)) {
            throw new IllegalArgumentException("科室不存在或已被医生引用，id=" + id);
        }
        return R.ok("删除科室成功", true);
    }
}
