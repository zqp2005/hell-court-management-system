package com.hellcourt.service.impl;

import com.hellcourt.common.util.JwtUtil;
import com.hellcourt.dto.request.LoginRequest;
import com.hellcourt.dto.response.LoginResponse;
import com.hellcourt.dto.response.UserInfoResponse;
import com.hellcourt.entity.Role;
import com.hellcourt.entity.User;
import com.hellcourt.mapper.RoleMapper;
import com.hellcourt.mapper.UserMapper;
import com.hellcourt.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .eq(User::getUsername, request.getUsername()));
        Role role = roleMapper.selectById(user.getRoleId());
        String roleCode = role != null ? role.getCode() : "UNKNOWN";
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRoleId(), roleCode);
        return new LoginResponse(token, UserInfoResponse.from(user, role));
    }

    @Override
    public UserInfoResponse getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        Role role = roleMapper.selectById(user.getRoleId());
        return UserInfoResponse.from(user, role);
    }
}
