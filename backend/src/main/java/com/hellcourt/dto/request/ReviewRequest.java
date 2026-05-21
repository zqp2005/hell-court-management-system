package com.hellcourt.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull(message = "审判ID不能为空")
    private Long judgmentId;
    private String reviewOpinion;
    @NotNull(message = "审核结果不能为空")
    private Integer result;
}
