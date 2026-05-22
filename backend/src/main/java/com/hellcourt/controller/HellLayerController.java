package com.hellcourt.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hellcourt.common.result.ApiResponse;
import com.hellcourt.entity.HellLayer;
import com.hellcourt.mapper.HellLayerMapper;
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
    private final HellLayerMapper hellLayerMapper;

    @GetMapping
    @Operation(summary = "查询全部十八层地狱")
    public ApiResponse<List<HellLayer>> list() {
        return ApiResponse.success(hellLayerMapper.selectList(
                new LambdaQueryWrapper<HellLayer>().orderByAsc(HellLayer::getLayerNum)));
    }
}
