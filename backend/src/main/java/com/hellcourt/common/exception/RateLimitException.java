package com.hellcourt.common.exception;

/**
 * 限流异常 — 由 RateLimitAspect 抛出，GlobalExceptionHandler 统一处理返回 429
 */
public class RateLimitException extends RuntimeException {
    public RateLimitException(String message) {
        super(message);
    }
}
