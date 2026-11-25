package com.example.medicalsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.medicalsystem.common.R;
import com.example.medicalsystem.dto.MedicineReq;
import com.example.medicalsystem.entity.Medicine;
import com.example.medicalsystem.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 药品管理：分页查询、新增、修改库存。 */
@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping
    public R<Page<Medicine>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String name) {
        var query = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Medicine>()
                .like(org.springframework.util.StringUtils.hasText(name), Medicine::getName, name)
                .orderByAsc(Medicine::getId);
        return R.ok(medicineService.page(new Page<>(current, size), query));
    }

    @PostMapping
    public R<Long> create(@Valid @RequestBody MedicineReq req) {
        Medicine m = new Medicine();
        org.springframework.beans.BeanUtils.copyProperties(req, m);
        medicineService.save(m);
        return R.ok("新增药品成功", m.getId());
    }

    @PutMapping("/{id}")
    public R<Boolean> update(@PathVariable Long id, @Valid @RequestBody MedicineReq req) {
        Medicine m = new Medicine();
        org.springframework.beans.BeanUtils.copyProperties(req, m);
        m.setId(id);
        return R.ok("修改药品成功", medicineService.updateById(m));
    }
}
