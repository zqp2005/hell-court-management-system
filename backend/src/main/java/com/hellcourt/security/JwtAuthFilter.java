package com.hellcourt.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hellcourt.common.util.JwtUtil;
import com.hellcourt.entity.Role;
import com.hellcourt.entity.RolePermission;
import com.hellcourt.mapper.RoleMapper;
import com.hellcourt.mapper.RolePermissionMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (token != null && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.parseToken(token);
            Long userId = Long.parseLong(claims.getSubject());
            Long roleId = claims.get("roleId", Long.class);

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (roleId != null) {
                Role role = roleMapper.selectById(roleId);
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
                }
                List<RolePermission> rpList = rolePermissionMapper.selectList(
                        new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId));
                for (RolePermission rp : rpList) {
                    authorities.add(new SimpleGrantedAuthority(rp.getPermissionCode()));
                }
            }

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);
            auth.setDetails(claims.get("username"));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        // 导出等场景支持 URL 参数传 token
        String param = request.getParameter("token");
        if (StringUtils.hasText(param)) {
            return param;
        }
        return null;
    }
}
