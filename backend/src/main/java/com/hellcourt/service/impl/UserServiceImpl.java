package com.hellcourt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hellcourt.common.annotation.OpLog;
import com.hellcourt.dto.request.UserRequest;
import com.hellcourt.entity.User;
import com.hellcourt.mapper.UserMapper;
import com.hellcourt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public IPage<User> page(int page, int size) {
        return userMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<User>().orderByDesc(User::getCreatedAt));
    }

    @Override
    @OpLog(action = "新增用户", targetType = "user")
    public void create(UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setRoleId(request.getRoleId());
        user.setStatus(1);
        userMapper.insert(user);
    }

    @Override
    @OpLog(action = "修改用户", targetType = "user")
    public void update(Long id, UserRequest request) {
        User user = userMapper.selectById(id);
        if (user == null) throw new RuntimeException("用户不存在");
        user.setRealName(request.getRealName());
        user.setRoleId(request.getRoleId());
        if (request.getStatus() != null) user.setStatus(request.getStatus());
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userMapper.updateById(user);
    }

    @Override
    @OpLog(action = "删除用户", targetType = "user")
    public void delete(Long id) {
        userMapper.deleteById(id);
    }
}
