package com.hellcourt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("hell_layer")
public class HellLayer {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer layerNum;
    private String name;
    private String crime;
    private String punishment;
    private String durationDesc;
    private LocalDateTime createdAt;
}
