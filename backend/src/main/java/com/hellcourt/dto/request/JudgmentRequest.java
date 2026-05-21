package com.hellcourt.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JudgmentRequest {
    @NotNull(message = "魂魄ID不能为空")
    private Long soulId;
    private String judgmentDetail;
    private String goodReward;
    private String evilPunishment;
}
