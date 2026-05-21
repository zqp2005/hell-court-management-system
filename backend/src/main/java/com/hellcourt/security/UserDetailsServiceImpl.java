package com.hellcourt.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hellcourt.entity.Role;
import com.hellcourt.entity.RolePermission;
import com.hellcourt.entity.User;
import com.hellcourt.mapper.RoleMapper;
import com.hellcourt.mapper.RolePermissionMapper;
import com.hellcourt.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        if (user.getStatus() == 0) {
            throw new UsernameNotFoundException("用户已被禁用");
        }

        Role role = roleMapper.selectById(user.getRoleId());
        String roleCode = role != null ? role.getCode() : "UNKNOWN";

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleCode));

        List<RolePermission> rpList = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, user.getRoleId()));
        for (RolePermission rp : rpList) {
            authorities.add(new SimpleGrantedAuthority(rp.getPermissionCode()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getId().toString(),
                user.getPassword(),
                authorities);
    }
}
