package com.example.medicalsystem.controller;

import com.example.medicalsystem.common.R;
import com.example.medicalsystem.dto.RegisterReq;
import com.example.medicalsystem.service.biz.RegistrationBizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 在线挂号：走业务编排层，事务内扣号源 + 写挂号单。 */
@RestController
@RequestMapping("/api/registration")
public class RegisterController {

    @Autowired
    private RegistrationBizService registrationBizService;

    /** 提交挂号。请求体可带 "simulateFail": true 演示事务回滚。 */
    @PostMapping("/book")
    public R<Long> book(@Valid @RequestBody RegisterReq req) {
        return R.ok("挂号成功", registrationBizService.register(req));
    }
}
