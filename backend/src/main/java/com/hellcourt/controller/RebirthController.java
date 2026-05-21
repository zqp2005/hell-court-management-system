package com.hellcourt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hellcourt.common.result.ApiResponse;
import com.hellcourt.dto.export.RebirthExportDTO;
import com.hellcourt.dto.request.RebirthRequest;
import com.hellcourt.entity.Rebirth;
import com.hellcourt.entity.Soul;
import com.hellcourt.entity.User;
import com.hellcourt.mapper.SoulMapper;
import com.hellcourt.mapper.UserMapper;
import com.hellcourt.service.RebirthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "轮回管理")
@RestController
@RequestMapping("/api/rebirths")
@RequiredArgsConstructor
public class RebirthController {
    private final RebirthService rebirthService;
    private final SoulMapper soulMapper;
    private final UserMapper userMapper;

    private static final Map<Integer, String> PATH_NAMES = Map.of(
            1, "天道", 2, "阿修罗道", 3, "人道",
            4, "畜生道", 5, "饿鬼道", 6, "地狱道"
    );
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping
    @Operation(summary = "分页查询轮回记录")
    public ApiResponse<IPage<Rebirth>> page(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(rebirthService.page(page, size));
    }

    @PostMapping
    @Operation(summary = "创建轮回记录")
    public ApiResponse<Void> create(@Valid @RequestBody RebirthRequest request, Authentication auth) {
        rebirthService.create(request, (Long) auth.getPrincipal());
        return ApiResponse.success();
    }

    @GetMapping("/export")
    @Operation(summary = "导出轮回记录Excel")
    public void export(HttpServletResponse response) throws IOException {
        List<Rebirth> list = rebirthService.listAll();
        List<RebirthExportDTO> exportList = new ArrayList<>();
        for (Rebirth r : list) {
            RebirthExportDTO dto = new RebirthExportDTO();
            dto.setId(r.getId());
            dto.setSoulId(r.getSoulId());
            dto.setPathName(PATH_NAMES.getOrDefault(r.getPath(), "未知"));
            dto.setDestination(r.getDestination());
            dto.setRebirthDate(r.getRebirthDate() != null ? r.getRebirthDate().format(DTF) : "");
            dto.setCreatedAt(r.getCreatedAt() != null ? r.getCreatedAt().format(DTF) : "");

            Soul soul = soulMapper.selectById(r.getSoulId());
            dto.setSoulName(soul != null ? soul.getName() : "未知");

            User operator = userMapper.selectById(r.getOperatorId());
            dto.setOperatorName(operator != null ? operator.getRealName() : "未知");

            exportList.add(dto);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-disposition", "attachment;filename=" +
                URLEncoder.encode("轮回记录.xlsx", StandardCharsets.UTF_8.name()));
        com.alibaba.excel.EasyExcel.write(response.getOutputStream(), RebirthExportDTO.class)
                .sheet("轮回记录").doWrite(exportList);
    }
}
