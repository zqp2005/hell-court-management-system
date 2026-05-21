package com.hellcourt.common.annotation;

import java.lang.annotation.*;

/**
 * 自动操作日志 — 标注在 Service 方法上，AOP 自动记录 operation_log
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpLog {
    /** 操作动作，如 "勾魂" "录入审判" */
    String action();

    /** 目标类型，如 "soul" "judgment" */
    String targetType();

    /** 目标 ID 在方法参数中的名称，默认取第一个 Long 类型参数 */
    String targetIdParam() default "";

    /** 详情模板，支持 SpEL 表达式，如 "'魂魄:' + #result.name" */
    String detail() default "";
}
