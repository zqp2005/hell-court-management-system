package com.hellcourt.dto.response;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DashboardResponse {
    private long totalSouls;        // 魂魄总数
    private long pendingSummon;     // 待勾魂 (status=0)
    private long arrivedSouls;      // 已到殿 (status=1)
    private long judgedSouls;       // 已判决 (status=3)
    private long rebirthedSouls;    // 已轮回 (status=4)
    private long pendingReviews;    // 待复核审判数 (judgment status=0)
    private List<Map<String, Object>> rebirthDistribution;
    private List<Map<String, Object>> monthlyTrend;
    private List<ActivityFeedItem> activityFeed;       // 最近操作动态
    private List<Map<String, Object>> judgeRanking;     // 判官排行榜
}
