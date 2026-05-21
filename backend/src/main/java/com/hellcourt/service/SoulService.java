package com.hellcourt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hellcourt.dto.request.SoulPageRequest;
import com.hellcourt.dto.request.SoulRequest;
import com.hellcourt.entity.Soul;

public interface SoulService {
    IPage<Soul> page(SoulPageRequest request);
    Soul getById(Long id);
    void create(SoulRequest request, Long userId);
    void update(Long id, SoulRequest request, Long userId);
    void delete(Long id);
    void updateStatus(Long id, Integer status, Long userId);
}
