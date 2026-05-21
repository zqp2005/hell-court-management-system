package com.hellcourt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hellcourt.dto.request.ReviewRequest;
import com.hellcourt.entity.Review;

public interface ReviewService {
    IPage<Review> page(int page, int size);
    void approve(ReviewRequest request, Long kingId);
}
