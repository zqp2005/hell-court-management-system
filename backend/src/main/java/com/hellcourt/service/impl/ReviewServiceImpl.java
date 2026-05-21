package com.hellcourt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hellcourt.common.annotation.OpLog;
import com.hellcourt.common.constant.SystemConstant;
import com.hellcourt.common.exception.BusinessException;
import com.hellcourt.dto.request.ReviewRequest;
import com.hellcourt.entity.Judgment;
import com.hellcourt.entity.Review;
import com.hellcourt.entity.Soul;
import com.hellcourt.mapper.JudgmentMapper;
import com.hellcourt.mapper.ReviewMapper;
import com.hellcourt.mapper.SoulMapper;
import com.hellcourt.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewMapper reviewMapper;
    private final JudgmentMapper judgmentMapper;
    private final SoulMapper soulMapper;

    @Override
    public IPage<Review> page(int page, int size) {
        return reviewMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<Review>().orderByDesc(Review::getCreatedAt));
    }

    @Override
    @OpLog(action = "复核审批", targetType = "judgment")
    public void approve(ReviewRequest request, Long kingId) {
        Judgment judgment = judgmentMapper.selectById(request.getJudgmentId());
        if (judgment == null) throw new BusinessException("审判记录不存在");
        if (judgment.getStatus() != SystemConstant.JUDGMENT_STATUS_PENDING) {
            throw new BusinessException("该审判已被复核");
        }

        if (request.getResult() == SystemConstant.REVIEW_PASS) {
            judgment.setStatus(SystemConstant.JUDGMENT_STATUS_APPROVED);
        } else {
            judgment.setStatus(SystemConstant.JUDGMENT_STATUS_REJECTED);
        }
        judgmentMapper.updateById(judgment);

        Soul soul = soulMapper.selectById(judgment.getSoulId());
        if (soul != null) {
            soul.setSoulStatus(SystemConstant.SOUL_STATUS_JUDGED);
            soul.setGoodDeeds(request.getResult() == SystemConstant.REVIEW_PASS ?
                    soul.getGoodDeeds() + 100 : soul.getGoodDeeds());
            soulMapper.updateById(soul);
        }

        Review review = new Review();
        review.setJudgmentId(request.getJudgmentId());
        review.setKingId(kingId);
        review.setReviewOpinion(request.getReviewOpinion());
        review.setResult(request.getResult());
        reviewMapper.insert(review);
    }
}
