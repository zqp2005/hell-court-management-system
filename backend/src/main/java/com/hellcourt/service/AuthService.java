package com.hellcourt.service;

import com.hellcourt.dto.request.LoginRequest;
import com.hellcourt.dto.response.LoginResponse;
import com.hellcourt.dto.response.UserInfoResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    UserInfoResponse getCurrentUser(Long userId);
}
