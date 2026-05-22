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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PERM_CACHE_PREFIX = "auth:permissions:";
    private static final long PERM_CACHE_TTL_MINUTES = 30;

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
                Role role = getCachedRole(roleId);
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
                }
                Set<String> permissions = getCachedPermissions(roleId);
                for (String perm : permissions) {
                    authorities.add(new SimpleGrantedAuthority(perm));
                }
            }

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);
            auth.setDetails(claims.get("username"));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    @SuppressWarnings("unchecked")
    private Role getCachedRole(Long roleId) {
        String key = PERM_CACHE_PREFIX + "role:" + roleId;
        Role cached = (Role) redisTemplate.opsForValue().get(key);
        if (cached != null) return cached;
        Role role = roleMapper.selectById(roleId);
        if (role != null) {
            redisTemplate.opsForValue().set(key, role, PERM_CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        }
        return role;
    }

    @SuppressWarnings("unchecked")
    private Set<String> getCachedPermissions(Long roleId) {
        String key = PERM_CACHE_PREFIX + roleId;
        Set<String> cached = (Set<String>) (Set<?>) redisTemplate.opsForSet().members(key);
        if (cached != null && !cached.isEmpty()) return cached;

        List<RolePermission> rpList = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId));
        Set<String> permissions = rpList.stream()
                .map(RolePermission::getPermissionCode)
                .collect(Collectors.toSet());
        if (!permissions.isEmpty()) {
            redisTemplate.opsForSet().add(key, permissions.toArray(new Object[0]));
            redisTemplate.expire(key, PERM_CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        }
        return permissions;
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
