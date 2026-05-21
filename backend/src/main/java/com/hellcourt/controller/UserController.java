package com.hellcourt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hellcourt.common.result.ApiResponse;
import com.hellcourt.dto.request.UserRequest;
import com.hellcourt.entity.User;
import com.hellcourt.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "分页查询用户")
    public ApiResponse<IPage<User>> page(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(userService.page(page, size));
    }

    @PostMapping
    @Operation(summary = "新增用户")
    public ApiResponse<Void> create(@Valid @RequestBody UserRequest request) {
        userService.create(request);
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改用户")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        userService.update(id, request);
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ApiResponse.success();
    }
}
