package com.hellcourt.controller;

import com.hellcourt.common.annotation.RateLimit;
import com.hellcourt.common.result.ApiResponse;
import com.hellcourt.dto.request.LoginRequest;
import com.hellcourt.dto.response.LoginResponse;
import com.hellcourt.dto.response.UserInfoResponse;
import com.hellcourt.entity.Role;
import com.hellcourt.entity.User;
import com.hellcourt.mapper.RoleMapper;
import com.hellcourt.mapper.UserMapper;
import com.hellcourt.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @RateLimit(limit = 5, window = 60, message = "登录过于频繁，请60秒后再试")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public ApiResponse<UserInfoResponse> info(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        User user = userMapper.selectById(userId);
        Role role = roleMapper.selectById(user.getRoleId());
        return ApiResponse.success(UserInfoResponse.from(user, role));
    }
}
