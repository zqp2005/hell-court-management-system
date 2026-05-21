package com.hellcourt.common.aspect;

import com.hellcourt.common.annotation.OpLog;
import com.hellcourt.entity.OperationLog;
import com.hellcourt.mapper.OperationLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 自动操作日志切面 — 拦截 @OpLog 注解的方法，自动写入 operation_log 表
 */
@Aspect
@Component
@RequiredArgsConstructor
public class OpLogAspect {

    private final OperationLogMapper operationLogMapper;

    @Around("@annotation(com.hellcourt.common.annotation.OpLog)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 执行原方法
        Object result = point.proceed();

        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            OpLog opLog = method.getAnnotation(OpLog.class);
            Object[] args = point.getArgs();
            Parameter[] params = method.getParameters();

            OperationLog log = new OperationLog();

            // 操作人 — 从 SecurityContext 或方法参数中取
            Long userId = extractUserId(args, params);
            log.setUserId(userId);

            // 操作动作 & 目标类型
            log.setAction(opLog.action());
            log.setTargetType(opLog.targetType());

            // 目标 ID — 优先使用注解指定的参数名，否则取第一个 Long 参数
            Long targetId = extractTargetId(opLog, args, params);
            log.setTargetId(targetId);

            // 详情
            String detail = opLog.detail();
            if (detail == null || detail.isEmpty()) {
                detail = buildDefaultDetail(method.getName(), opLog.action(), targetId);
            }
            log.setDetail(detail);

            // IP
            log.setIpAddress(getClientIp());

            operationLogMapper.insert(log);
        } catch (Exception e) {
            // 日志记录失败不影响业务
        }

        return result;
    }

    private Long extractUserId(Object[] args, Parameter[] params) {
        // 先从 SecurityContext 取
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Long userId) {
                return userId;
            }
        } catch (Exception ignored) {}

        // 从参数中找 userId 或 operatorId
        for (int i = 0; i < params.length; i++) {
            String name = params[i].getName();
            if ("userId".equals(name) || "operatorId".equals(name)) {
                if (args[i] instanceof Long id) return id;
            }
        }
        // 兜底：最后一个 Long 参数
        for (int i = args.length - 1; i >= 0; i--) {
            if (args[i] instanceof Long id) return id;
        }
        return null;
    }

    private Long extractTargetId(OpLog opLog, Object[] args, Parameter[] params) {
        // 注解指定了参数名
        String targetParam = opLog.targetIdParam();
        if (targetParam != null && !targetParam.isEmpty()) {
            for (int i = 0; i < params.length; i++) {
                if (targetParam.equals(params[i].getName()) && args[i] instanceof Long) {
                    return (Long) args[i];
                }
            }
        }
        // 取第一个 Long 参数作为 targetId（通常是 id）
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Long && !isUserIdParam(params[i].getName())) {
                return (Long) args[i];
            }
        }
        return null;
    }

    private boolean isUserIdParam(String name) {
        return "userId".equals(name) || "operatorId".equals(name)
                || "judgeId".equals(name) || "kingId".equals(name);
    }

    private String buildDefaultDetail(String methodName, String action, Long targetId) {
        return action + (targetId != null ? " [ID:" + targetId + "]" : "");
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("X-Real-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                return ip;
            }
        } catch (Exception ignored) {}
        return "unknown";
    }
}
