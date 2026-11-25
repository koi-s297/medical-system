package com.example.medicalsystem.common;

/**
 * 业务异常：把"重复挂号、号源已满、库存不足"这类可预期的业务规则违规
 * 与系统异常区分开，带上业务状态码，由全局异常处理器转成友好中文提示。
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
