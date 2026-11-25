package com.example.medicalsystem.controller;

import com.example.medicalsystem.common.R;
import com.example.medicalsystem.dto.PrescriptionReq;
import com.example.medicalsystem.service.biz.PrescriptionBizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 开具处方：走业务编排层，事务内写处方 + 扣库存 + 写明细。 */
@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionBizService prescriptionBizService;

    /** 开具处方。请求体可带 "simulateFail": true 演示事务回滚。 */
    @PostMapping("/issue")
    public R<Long> issue(@Valid @RequestBody PrescriptionReq req) {
        return R.ok("开方成功", prescriptionBizService.issue(req));
    }
}
