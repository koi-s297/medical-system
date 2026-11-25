package com.example.medicalsystem.common;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理：把参数校验、唯一键冲突等异常转成中文提示，
 * 避免框架默认的英文堆栈直接暴露给调用方。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleBodyValid(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return R.fail(400, msg);
    }

    @ExceptionHandler({BindException.class, ConstraintViolationException.class})
    public R<Void> handleParamValid(Exception ex) {
        return R.fail(400, "参数格式不正确: " + ex.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R<Void> handleDuplicate(DuplicateKeyException ex) {
        return R.fail(409, "数据重复：工号、手机号或身份证已存在");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public R<Void> handleIntegrity(DataIntegrityViolationException ex) {
        return R.fail(409, "违反数据完整性约束：请检查外键关联或非空字段");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public R<Void> handleIllegalArgument(IllegalArgumentException ex) {
        return R.fail(400, ex.getMessage());
    }

    /**
     * 实验三业务异常：重复挂号、号源已满、库存不足等，使用异常自带的状态码与中文提示。
     */
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusiness(BusinessException ex) {
        return R.fail(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleOther(Exception ex) {
        return R.fail(500, "服务器内部错误: " + ex.getMessage());
    }
}
