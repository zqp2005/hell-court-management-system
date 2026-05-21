package com.hellcourt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hellcourt.dto.request.JudgmentRequest;
import com.hellcourt.entity.Judgment;

public interface JudgmentService {
    IPage<Judgment> page(int page, int size, Integer status);
    void create(JudgmentRequest request, Long judgeId);
    void update(Long id, JudgmentRequest request, Long userId);
    void submitReview(Long id, Long judgeId);
}
