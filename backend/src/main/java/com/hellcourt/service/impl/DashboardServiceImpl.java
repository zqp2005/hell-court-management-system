package com.hellcourt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hellcourt.dto.response.ActivityFeedItem;
import com.hellcourt.dto.response.DashboardResponse;
import com.hellcourt.entity.Judgment;
import com.hellcourt.entity.OperationLog;
import com.hellcourt.entity.Rebirth;
import com.hellcourt.entity.Soul;
import com.hellcourt.mapper.*;
import com.hellcourt.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final SoulMapper soulMapper;
    private final JudgmentMapper judgmentMapper;
    private final ReviewMapper reviewMapper;
    private final RebirthMapper rebirthMapper;
    private final OperationLogMapper operationLogMapper;

    @Override
    @Cacheable(value = "dashboard", key = "'v4'")
    public DashboardResponse getStatistics() {
        DashboardResponse resp = new DashboardResponse();

        // 一次性 GROUP BY 查询替代 5 次独立 COUNT
        List<Map<String, Object>> statusCounts = soulMapper.selectMaps(
                new QueryWrapper<Soul>().select("soul_status, COUNT(*) as count").groupBy("soul_status"));
        long totalSouls = 0;
        for (Map<String, Object> row : statusCounts) {
            long count = ((Number) row.get("count")).longValue();
            totalSouls += count;
            int status = ((Number) row.get("soul_status")).intValue();
            switch (status) {
                case 0 -> resp.setPendingSummon(count);
                case 1 -> resp.setArrivedSouls(count);
                case 3 -> resp.setJudgedSouls(count);
                case 4 -> resp.setRebirthedSouls(count);
            }
        }
        resp.setTotalSouls(totalSouls);

        // 待复核审判数
        resp.setPendingReviews(judgmentMapper.selectCount(
                new LambdaQueryWrapper<Judgment>().eq(Judgment::getStatus, 0)));

        // 六道分布
        List<Map<String, Object>> distribution = rebirthMapper.selectMaps(
                new QueryWrapper<Rebirth>().select("path, COUNT(*) as count").groupBy("path"));
        resp.setRebirthDistribution(distribution);

        // 月度趋势
        List<Map<String, Object>> trend = soulMapper.selectMaps(
                new QueryWrapper<Soul>().select("DATE_FORMAT(created_at, '%Y-%m') as month, COUNT(*) as count")
                        .groupBy("month").orderByAsc("month").last("LIMIT 12"));
        resp.setMonthlyTrend(trend);

        // 最近操作动态
        List<OperationLog> logs = operationLogMapper.selectList(
                new LambdaQueryWrapper<OperationLog>()
                        .orderByDesc(OperationLog::getCreatedAt).last("LIMIT 10"));
        List<ActivityFeedItem> feed = new java.util.ArrayList<>();
        for (OperationLog log : logs) {
            feed.add(new ActivityFeedItem(
                    log.getAction(),
                    log.getDetail(),
                    log.getCreatedAt() != null ? log.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : ""));
        }
        resp.setActivityFeed(feed);

        // 判官排行榜
        List<Map<String, Object>> ranking = judgmentMapper.selectMaps(
                new QueryWrapper<Judgment>().select("judge_id as judgeId, COUNT(*) as count")
                        .groupBy("judge_id").orderByDesc("count"));
        resp.setJudgeRanking(ranking);

        return resp;
    }
}
