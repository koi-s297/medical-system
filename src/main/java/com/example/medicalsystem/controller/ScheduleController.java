package com.example.medicalsystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.medicalsystem.common.R;
import com.example.medicalsystem.dto.ScheduleReq;
import com.example.medicalsystem.entity.DoctorSchedule;
import com.example.medicalsystem.service.DoctorScheduleService;
import com.example.medicalsystem.service.biz.ScheduleBizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 医生排班管理：分页查询、新增排班、停诊。 */
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private DoctorScheduleService scheduleService;

    @Autowired
    private ScheduleBizService scheduleBizService;

    @GetMapping
    public R<Page<DoctorSchedule>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) Long doctorId) {
        var query = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<DoctorSchedule>()
                .eq(doctorId != null, DoctorSchedule::getDoctorId, doctorId)
                .orderByAsc(DoctorSchedule::getWorkDate, DoctorSchedule::getTimeSlot);
        return R.ok(scheduleService.page(new Page<>(current, size), query));
    }

    @PostMapping
    public R<Long> create(@Valid @RequestBody ScheduleReq req) {
        return R.ok("新增排班成功", scheduleBizService.create(req));
    }

    @PutMapping("/{id}/close")
    public R<Boolean> close(@PathVariable Long id) {
        scheduleBizService.close(id);
        return R.ok("停诊成功", true);
    }
}
