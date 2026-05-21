package com.hellcourt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hellcourt.common.result.ApiResponse;
import com.hellcourt.dto.request.JudgmentRequest;
import com.hellcourt.entity.Judgment;
import com.hellcourt.service.JudgmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "审判管理")
@RestController
@RequestMapping("/api/judgments")
@RequiredArgsConstructor
public class JudgmentController {
    private final JudgmentService judgmentService;

    @GetMapping
    @Operation(summary = "分页查询审判")
    public ApiResponse<IPage<Judgment>> page(@RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(required = false) Integer status) {
        return ApiResponse.success(judgmentService.page(page, size, status));
    }

    @PostMapping
    @Operation(summary = "创建审判")
    public ApiResponse<Void> create(@Valid @RequestBody JudgmentRequest request,
                                    Authentication auth) {
        judgmentService.create(request, (Long) auth.getPrincipal());
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改审判")
    public ApiResponse<Void> update(@PathVariable Long id, @Valid @RequestBody JudgmentRequest request,
                                    Authentication auth) {
        judgmentService.update(id, request, (Long) auth.getPrincipal());
        return ApiResponse.success();
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "提交复核")
    public ApiResponse<Void> submitReview(@PathVariable Long id, Authentication auth) {
        judgmentService.submitReview(id, (Long) auth.getPrincipal());
        return ApiResponse.success();
    }
}
