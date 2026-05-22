package com.hellcourt.controller;

import com.hellcourt.common.result.ApiResponse;
import com.hellcourt.entity.Demon;
import com.hellcourt.service.DemonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "百鬼录")
@RestController
@RequestMapping("/api/demons")
@RequiredArgsConstructor
public class DemonController {
    private final DemonService demonService;

    @GetMapping
    @Operation(summary = "查询全部鬼怪")
    public ApiResponse<List<Demon>> list() {
        return ApiResponse.success(demonService.listAll());
    }
}
