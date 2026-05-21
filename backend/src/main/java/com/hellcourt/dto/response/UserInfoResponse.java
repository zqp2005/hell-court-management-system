package com.hellcourt.dto.response;

import lombok.Data;
import com.hellcourt.entity.Role;
import com.hellcourt.entity.User;

@Data
public class UserInfoResponse {
    private Long id;
    private String username;
    private String realName;
    private Long roleId;
    private String roleCode;
    private String roleName;
    private Integer status;

    public static UserInfoResponse from(User user, Role role) {
        UserInfoResponse resp = new UserInfoResponse();
        resp.setId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setRealName(user.getRealName());
        resp.setRoleId(user.getRoleId());
        if (role != null) {
            resp.setRoleCode(role.getCode());
            resp.setRoleName(role.getName());
        }
        resp.setStatus(user.getStatus());
        return resp;
    }
}
