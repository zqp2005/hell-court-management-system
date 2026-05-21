package com.hellcourt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SoulRequest {
    @NotBlank(message = "姓名不能为空")
    private String name;
    private Integer gender;
    @NotNull(message = "出生日期不能为空")
    private LocalDateTime birthDate;
    private LocalDateTime deathDate;
    private String description;
}
