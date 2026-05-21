package com.hellcourt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hellcourt.dto.request.UserRequest;
import com.hellcourt.entity.User;

public interface UserService {
    IPage<User> page(int page, int size);
    void create(UserRequest request);
    void update(Long id, UserRequest request);
    void delete(Long id);
}
