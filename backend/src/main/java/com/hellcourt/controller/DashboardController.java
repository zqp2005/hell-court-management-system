package com.hellcourt.controller;

import com.hellcourt.common.result.ApiResponse;
import com.hellcourt.dto.response.DashboardResponse;
import com.hellcourt.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "仪表盘")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    @Operation(summary = "获取统计数据")
    public ApiResponse<DashboardResponse> statistics() {
        return ApiResponse.success(dashboardService.getStatistics());
    }
}
