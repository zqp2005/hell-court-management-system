<template>
  <div class="dashboard">
    <div class="page-header">
      <h2 class="page-title">酆都司政</h2>
      <p class="page-desc">阴司事务总览</p>
      <el-button class="btn-screen" @click="$router.push('/screen')">
        <el-icon><FullScreen /></el-icon>
        数据大屏
      </el-button>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :span="6" v-for="(card, idx) in cards" :key="card.label">
        <div class="stat-card" :style="{ borderColor: card.color, animationDelay: idx * 0.08 + 's' }">
          <div class="stat-icon" :style="{ color: card.color, background: card.color + '15' }">
            <el-icon :size="28"><component :is="card.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <p class="stat-value">{{ card.value }}</p>
            <p class="stat-label">{{ card.label }}</p>
          </div>
          <div class="stat-glow" :style="{ background: `radial-gradient(ellipse, ${card.color}30, transparent)` }"></div>
        </div>
      </el-col>
    </el-row>

    <div class="chart-card" style="margin-bottom: 20px; animation-delay: 0.32s">
      <div class="chart-header"><span class="chart-title">地府动态</span></div>
      <div class="feed-list">
        <div v-for="(item, idx) in (activityFeed || [])" :key="idx" class="feed-item">
          <span class="feed-time">{{ formatDateTime(item.createdAt) }}</span>
          <span class="feed-action">{{ item.action }}</span>
          <span class="feed-detail">{{ item.detail }}</span>
        </div>
        <p v-if="!activityFeed || activityFeed.length === 0" style="color:#666;text-align:center;padding:20px">暂无动态</p>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="14">
        <div class="chart-card" style="animation-delay: 0.4s">
          <div class="chart-header">
            <span class="chart-title">魂魄月度趋势</span>
          </div>
          <div ref="trendChart" class="chart-box"></div>
        </div>
      </el-col>
      <el-col :span="10">
        <div class="chart-card" style="animation-delay: 0.48s">
          <div class="chart-header">
            <span class="chart-title">六道分布</span>
          </div>
          <div ref="pieChart" class="chart-box"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { getStatistics } from '@/api/dashboard'
import { formatDateTime } from '@/utils'
import * as echarts from 'echarts'

const activityFeed = ref([])

const cards = ref([
  { label: '魂魄总数', value: 0, icon: 'User', color: '#ffd700' },
  { label: '待审判', value: 0, icon: 'Edit', color: '#ff6b35' },
  { label: '已判决', value: 0, icon: 'Checked', color: '#e67e22' },
  { label: '已轮回', value: 0, icon: 'Refresh', color: '#2ecc71' }
])

const trendChart = ref(null)
const pieChart = ref(null)

onMounted(async () => {
  const res = await getStatistics()
  const data = res.data
  cards.value[0].value = data.totalSouls
  cards.value[1].value = data.arrivedSouls
  cards.value[2].value = data.judgedSouls
  cards.value[3].value = data.rebirthedSouls
  activityFeed.value = data.activityFeed || []

  nextTick(() => {
    const trend = echarts.init(trendChart.value)
    trend.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'axis', theme: 'dark' },
      xAxis: { type: 'category', data: data.monthlyTrend?.map(m => m.month) || [], axisLine: { lineStyle: { color: '#444' } } },
      yAxis: { type: 'value', splitLine: { lineStyle: { color: '#222', type: 'dashed' } } },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      series: [{
        data: data.monthlyTrend?.map(m => m.count) || [],
        type: 'line',
        smooth: true,
        lineStyle: { color: '#ffd700', width: 3 },
        areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(255,215,0,0.3)' }, { offset: 1, color: 'rgba(255,215,0,0)' }]) },
        symbol: 'circle',
        symbolSize: 8,
        itemStyle: { color: '#ffd700' }
      }]
    })

    const pie = echarts.init(pieChart.value)
    const pathNames = ['天道', '阿修罗道', '人道', '畜生道', '饿鬼道', '地狱道']
    const pathColors = ['#ffd700', '#ff6b35', '#2ecc71', '#95a5a6', '#e74c3c', '#8b0000']
    pie.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '50%'],
        roseType: 'area',
        data: data.rebirthDistribution?.map((d, i) => ({ name: pathNames[d.path - 1] || '未知', value: d.count, itemStyle: { color: pathColors[d.path - 1] || '#666' } })) || [],
        label: { color: '#ccc', fontSize: 12 },
        labelLine: { lineStyle: { color: '#555' } },
        itemStyle: { borderRadius: 4, borderColor: '#1a1a2e', borderWidth: 2 }
      }]
    })
  })
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.page-header {
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}
.page-header .page-title { margin-bottom: 0; }
.page-header .page-desc { margin: 0; margin-right: auto; }
.btn-screen {
  background: transparent;
  border: 1px solid #2ecc71;
  color: #2ecc71;
  letter-spacing: 2px;
}
.btn-screen:hover {
  background: rgba(46,204,113,0.1);
  border-color: #2ecc71;
  color: #2ecc71;
}

.page-title {
  font-size: 22px;
  color: #ffd700;
  letter-spacing: 4px;
}

.page-desc {
  font-size: 12px;
  color: #666;
  letter-spacing: 2px;
  margin-top: 4px;
}

.stats-row {
  margin-bottom: 24px;
}

.stat-card {
  position: relative;
  overflow: hidden;
  background: linear-gradient(170deg, var(--parchment), var(--parchment-dark));
  border: 1px solid;
  border-radius: 6px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  transition: all 0.3s;
  cursor: default;
  opacity: 0;
  animation: apparition 0.5s ease both;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.4);
}

.stat-icon {
  width: 52px;
  height: 52px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #e0d5c0;
}

.stat-label {
  font-size: 13px;
  color: #888;
  margin-top: 4px;
}

.stat-glow {
  position: absolute;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  right: -30px;
  top: -30px;
  pointer-events: none;
}

.chart-card {
  background: var(--parchment);
  border: 1px solid rgba(122,0,0,0.15);
  border-radius: 6px;
  padding: 20px;
  margin-bottom: 20px;
  opacity: 0;
  animation: apparition 0.5s ease both;
}

.chart-header {
  margin-bottom: 16px;
}

.chart-title {
  color: #ffd700;
  font-size: 15px;
  letter-spacing: 2px;
}

.chart-box {
  height: 300px;
}

.feed-list { max-height: 200px; overflow-y: auto; }
.feed-item {
  display: flex; align-items: center; gap: 12px;
  padding: 8px 0; border-bottom: 1px solid rgba(255,255,255,0.04);
  font-size: 13px;
}
.feed-time { color: #555; white-space: nowrap; min-width: 130px; }
.feed-action { color: #8b0000; font-weight: 600; white-space: nowrap; }
.feed-detail { color: #999; flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
</style>
