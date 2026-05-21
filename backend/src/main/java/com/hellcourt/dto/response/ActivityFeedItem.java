package com.hellcourt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFeedItem {
    private String action;
    private String detail;
    private String createdAt;
}
