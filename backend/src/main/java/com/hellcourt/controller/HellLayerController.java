package com.hellcourt.controller;

import com.hellcourt.common.result.ApiResponse;
import com.hellcourt.entity.HellLayer;
import com.hellcourt.service.HellLayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "十八层地狱")
@RestController
@RequestMapping("/api/hell-layers")
@RequiredArgsConstructor
public class HellLayerController {
    private final HellLayerService hellLayerService;

    @GetMapping
    @Operation(summary = "查询全部十八层地狱")
    public ApiResponse<List<HellLayer>> list() {
        return ApiResponse.success(hellLayerService.listAll());
    }
}
