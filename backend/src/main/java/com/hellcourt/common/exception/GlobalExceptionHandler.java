package com.hellcourt.common.exception;

import com.hellcourt.common.result.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RateLimitException.class)
    public ApiResponse<Void> handleRateLimitException(RateLimitException e) {
        return ApiResponse.error(429, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        return ApiResponse.error("服务器内部错误: " + e.getMessage());
    }
}
