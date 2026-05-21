package com.hellcourt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hellcourt.common.result.ApiResponse;
import com.hellcourt.dto.request.SoulPageRequest;
import com.hellcourt.dto.request.SoulRequest;
import com.hellcourt.entity.Soul;
import com.hellcourt.service.SoulService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "魂魄管理")
@RestController
@RequestMapping("/api/souls")
@RequiredArgsConstructor
public class SoulController {
    private final SoulService soulService;

    @GetMapping
    @Operation(summary = "分页查询魂魄")
    public ApiResponse<IPage<Soul>> page(SoulPageRequest request) {
        return ApiResponse.success(soulService.page(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询魂魄详情")
    public ApiResponse<Soul> getById(@PathVariable Long id) {
        return ApiResponse.success(soulService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增魂魄")
    public ApiResponse<Void> create(@Valid @RequestBody SoulRequest request,
                                    Authentication auth) {
        soulService.create(request, (Long) auth.getPrincipal());
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改魂魄")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody SoulRequest request,
                                    Authentication auth) {
        soulService.update(id, request, (Long) auth.getPrincipal());
        return ApiResponse.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除魂魄")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        soulService.delete(id);
        return ApiResponse.success();
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新魂魄状态(勾魂)")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status,
                                          Authentication auth) {
        soulService.updateStatus(id, status, (Long) auth.getPrincipal());
        return ApiResponse.success();
    }
}
