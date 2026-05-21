package com.hellcourt.common.annotation;

import java.lang.annotation.*;

/**
 * 接口限流 — 基于 Redis 滑动窗口计数
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    /** 时间窗口内允许的最大请求数 */
    int limit() default 10;

    /** 时间窗口，单位秒 */
    int window() default 60;

    /** 限流提示信息 */
    String message() default "请求过于频繁，请稍后再试";
}
