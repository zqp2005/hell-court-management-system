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
    @Cacheable(value = "dashboard", key = "'v3'")
    public DashboardResponse getStatistics() {
        DashboardResponse resp = new DashboardResponse();
        // 魂魄总数
        resp.setTotalSouls(soulMapper.selectCount(null));
        // 待勾魂 (status=0)
        resp.setPendingSummon(soulMapper.selectCount(
                new LambdaQueryWrapper<Soul>().eq(Soul::getSoulStatus, 0)));
        // 已到殿 (status=1)
        resp.setArrivedSouls(soulMapper.selectCount(
                new LambdaQueryWrapper<Soul>().eq(Soul::getSoulStatus, 1)));
        // 已判决 (status=3)
        resp.setJudgedSouls(soulMapper.selectCount(
                new LambdaQueryWrapper<Soul>().eq(Soul::getSoulStatus, 3)));
        // 已轮回 (status=4)
        resp.setRebirthedSouls(soulMapper.selectCount(
                new LambdaQueryWrapper<Soul>().eq(Soul::getSoulStatus, 4)));
        // 待复核审判数 (judgment status=0)
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
        // 最近操作动态 (最新10条)
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
