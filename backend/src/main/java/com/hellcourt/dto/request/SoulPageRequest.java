package com.hellcourt.dto.request;

import lombok.Data;

@Data
public class SoulPageRequest {
    private int page = 1;
    private int size = 10;
    private String name;
    private Integer soulStatus;
}
