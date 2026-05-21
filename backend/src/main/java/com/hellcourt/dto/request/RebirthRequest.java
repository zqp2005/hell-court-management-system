package com.hellcourt.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RebirthRequest {
    @NotNull(message = "魂魄ID不能为空")
    private Long soulId;
    @NotNull(message = "轮回道不能为空")
    private Integer path;
    @NotNull(message = "投胎日期不能为空")
    private LocalDateTime rebirthDate;
    private String destination;
}
