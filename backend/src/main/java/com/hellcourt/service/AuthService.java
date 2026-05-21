package com.hellcourt.service;

import com.hellcourt.dto.request.LoginRequest;
import com.hellcourt.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
