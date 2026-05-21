package com.hellcourt.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("soul")
public class Soul {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer gender;
    private LocalDateTime birthDate;
    private LocalDateTime deathDate;
    private Integer lifespan;
    private Integer goodDeeds;
    private Integer evilDeeds;
    private Integer soulStatus;
    private LocalDateTime arrivalDate;
    private String description;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
