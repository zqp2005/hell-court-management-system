package com.hellcourt.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("judgment")
public class Judgment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long soulId;
    private Long judgeId;
    private String judgmentDetail;
    private String goodReward;
    private String evilPunishment;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
