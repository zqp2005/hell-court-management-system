package com.hellcourt.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("rebirth")
public class Rebirth {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long soulId;
    private Integer path;
    private LocalDateTime rebirthDate;
    private String destination;
    private Long operatorId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
