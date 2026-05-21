package com.hellcourt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hellcourt.common.result.ApiResponse;
import com.hellcourt.dto.request.ReviewRequest;
import com.hellcourt.entity.Review;
import com.hellcourt.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "复核管理")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    @Operation(summary = "分页查询复核记录")
    public ApiResponse<IPage<Review>> page(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(reviewService.page(page, size));
    }

    @PostMapping
    @Operation(summary = "复核审批")
    public ApiResponse<Void> approve(@Valid @RequestBody ReviewRequest request, Authentication auth) {
        reviewService.approve(request, (Long) auth.getPrincipal());
        return ApiResponse.success();
    }
}
