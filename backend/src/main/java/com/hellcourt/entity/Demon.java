package com.hellcourt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("demon")
public class Demon {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String alias;
    private String category;
    private String description;
    private String ability;
    private String weakness;
    private String imageIcon;
    private LocalDateTime createdAt;
}
