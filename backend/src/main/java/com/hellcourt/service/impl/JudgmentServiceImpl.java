package com.hellcourt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hellcourt.common.annotation.OpLog;
import com.hellcourt.common.constant.SystemConstant;
import com.hellcourt.common.exception.BusinessException;
import com.hellcourt.dto.request.JudgmentRequest;
import com.hellcourt.entity.Judgment;
import com.hellcourt.entity.Soul;
import com.hellcourt.mapper.JudgmentMapper;
import com.hellcourt.mapper.SoulMapper;
import com.hellcourt.service.JudgmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JudgmentServiceImpl implements JudgmentService {
    private final JudgmentMapper judgmentMapper;
    private final SoulMapper soulMapper;

    @Override
    public IPage<Judgment> page(int page, int size, Integer status) {
        LambdaQueryWrapper<Judgment> wrapper = new LambdaQueryWrapper<>();
        if (status != null) wrapper.eq(Judgment::getStatus, status);
        wrapper.orderByDesc(Judgment::getCreatedAt);
        return judgmentMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    @OpLog(action = "录入审判", targetType = "judgment")
    @Transactional
    public void create(JudgmentRequest request, Long judgeId) {
        Soul soul = soulMapper.selectById(request.getSoulId());
        if (soul == null) throw new BusinessException("魂魄不存在");
        soul.setSoulStatus(SystemConstant.SOUL_STATUS_JUDGING);
        soulMapper.updateById(soul);

        Judgment judgment = new Judgment();
        judgment.setSoulId(request.getSoulId());
        judgment.setJudgeId(judgeId);
        judgment.setJudgmentDetail(request.getJudgmentDetail());
        judgment.setGoodReward(request.getGoodReward());
        judgment.setEvilPunishment(request.getEvilPunishment());
        judgment.setStatus(SystemConstant.JUDGMENT_STATUS_PENDING);
        judgmentMapper.insert(judgment);
    }

    @Override
    @OpLog(action = "修改审判", targetType = "judgment")
    public void update(Long id, JudgmentRequest request, Long userId) {
        Judgment judgment = judgmentMapper.selectById(id);
        if (judgment == null) throw new BusinessException("审判记录不存在");
        if (judgment.getStatus() != SystemConstant.JUDGMENT_STATUS_PENDING) {
            throw new BusinessException("只能修改待复核的审判");
        }
        judgment.setJudgmentDetail(request.getJudgmentDetail());
        judgment.setGoodReward(request.getGoodReward());
        judgment.setEvilPunishment(request.getEvilPunishment());
        judgmentMapper.updateById(judgment);
    }

    @Override
    @OpLog(action = "提交复核", targetType = "judgment")
    public void submitReview(Long id, Long judgeId) {
        Judgment judgment = judgmentMapper.selectById(id);
        if (judgment == null) throw new BusinessException("审判记录不存在");
        if (!judgment.getJudgeId().equals(judgeId)) {
            throw new BusinessException("只能提交自己的审判");
        }
        judgment.setStatus(SystemConstant.JUDGMENT_STATUS_PENDING);
        judgmentMapper.updateById(judgment);
    }
}
