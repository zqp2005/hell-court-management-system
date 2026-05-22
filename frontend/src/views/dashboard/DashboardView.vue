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
            <p class="stat-value" :class="{ 'num-glow': card.justUpdated }">{{ animated[card.key] ?? card.value }}</p>
            <p class="stat-label">{{ card.label }}</p>
          </div>
          <div class="stat-glow" :style="{ background: `radial-gradient(ellipse, ${card.color}30, transparent)` }"></div>
        </div>
      </el-col>
    </el-row>

    <div class="chart-card" style="margin-bottom: 20px; animation-delay: 0.32s">
      <div class="chart-header"><span class="chart-title">地府动态</span></div>
      <div class="feed-list" v-if="activityFeed && activityFeed.length">
        <div v-for="(item, idx) in activityFeed" :key="idx" class="feed-item" :style="{ animationDelay: idx * 0.04 + 's' }">
          <span class="feed-time">{{ formatDateTime(item.createdAt) }}</span>
          <span class="feed-action">{{ item.action }}</span>
          <span class="feed-detail">{{ item.detail }}</span>
        </div>
      </div>
      <div v-else class="empty-state">
        <span class="empty-icon">📜</span>
        <span class="empty-text">暂无动态</span>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="14">
        <div class="chart-card" style="animation-delay: 0.4s">
          <div class="chart-header"><span class="chart-title">魂魄月度趋势</span></div>
          <div ref="trendChart" class="chart-box"></div>
        </div>
      </el-col>
      <el-col :span="10">
        <div class="chart-card" style="animation-delay: 0.48s">
          <div class="chart-header"><span class="chart-title">六道分布</span></div>
          <div ref="pieChart" class="chart-box"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { getStatistics } from '@/api/dashboard'
import { formatDateTime } from '@/utils'
import { initChart, disposeChart, makeLineOption, makePieOption } from './chartOptions'

const activityFeed = ref([])
const animated = reactive({})

const cards = ref([
  { key: 'totalSouls', label: '魂魄总数', value: 0, icon: 'User', color: '#ffd700', justUpdated: false },
  { key: 'arrivedSouls', label: '待审判', value: 0, icon: 'Edit', color: '#ff6b35', justUpdated: false },
  { key: 'judgedSouls', label: '已判决', value: 0, icon: 'Checked', color: '#e67e22', justUpdated: false },
  { key: 'rebirthedSouls', label: '已轮回', value: 0, icon: 'Refresh', color: '#2ecc71', justUpdated: false }
])

const trendChart = ref(null)
const pieChart = ref(null)
let trendInst = null
let pieInst = null

function animateValue(key, from, to) {
  const card = cards.value.find(c => c.key === key)
  if (card) card.justUpdated = true
  const duration = 600
  const start = performance.now()
  function step(ts) {
    const p = Math.min((ts - start) / duration, 1)
    const eased = 1 - Math.pow(1 - p, 3)
    animated[key] = Math.round(from + (to - from) * eased)
    if (p < 1) requestAnimationFrame(step)
    else { animated[key] = to; if (card) setTimeout(() => card.justUpdated = false, 800) }
  }
  requestAnimationFrame(step)
}

function handleResize() {
  trendInst?.resize()
  pieInst?.resize()
}

onMounted(async () => {
  const res = await getStatistics()
  const d = res.data

  for (const card of cards.value) {
    const to = d[card.key] ?? 0
    animateValue(card.key, animated[card.key] || 0, to)
    card.value = to
  }
  activityFeed.value = d.activityFeed || []

  nextTick(() => {
    trendInst = initChart(trendChart.value, makeLineOption(d.monthlyTrend || []))
    pieInst = initChart(pieChart.value, makePieOption(d.rebirthDistribution || []))
  })

  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  disposeChart(trendInst)
  disposeChart(pieInst)
})
</script>

<style scoped>
.dashboard { padding: 0; }
.page-header { margin-bottom: 24px; display: flex; align-items: center; gap: 16px; flex-wrap: wrap; }
.page-header .page-title { margin-bottom: 0; }
.page-header .page-desc { margin: 0; margin-right: auto; }

.btn-screen {
  background: transparent; border: 1px solid #2ecc71; color: #2ecc71; letter-spacing: 2px;
}
.btn-screen:hover { background: rgba(46,204,113,0.1); border-color: #2ecc71; color: #2ecc71; }

.stats-row { margin-bottom: 24px; }

.stat-card {
  position: relative; overflow: hidden;
  background: linear-gradient(170deg, var(--parchment), var(--parchment-dark));
  border: 1px solid; border-radius: 6px; padding: 24px;
  display: flex; align-items: center; gap: 16px;
  transition: all 0.3s; cursor: default;
  opacity: 0; animation: apparition 0.5s ease both;
}
.stat-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.4); }

.stat-icon { width: 52px; height: 52px; border-radius: 12px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.stat-info { flex: 1; }
.stat-value { font-size: 28px; font-weight: bold; color: #e0d5c0; transition: all 0.3s; }
.stat-value.num-glow { animation: numberGlow 0.6s ease-in-out; }
.stat-label { font-size: 13px; color: #888; margin-top: 4px; }

.stat-glow {
  position: absolute; width: 120px; height: 120px; border-radius: 50%;
  right: -30px; top: -30px; pointer-events: none;
}

.chart-card {
  background: var(--parchment); border: 1px solid rgba(122,0,0,0.15);
  border-radius: 6px; padding: 20px; margin-bottom: 20px;
  opacity: 0; animation: apparition 0.5s ease both;
}
.chart-header { margin-bottom: 16px; }
.chart-title { color: #ffd700; font-size: 15px; letter-spacing: 2px; }
.chart-box { height: 300px; }

.feed-list { max-height: 200px; overflow-y: auto; }
.feed-item {
  display: flex; align-items: center; gap: 12px;
  padding: 8px 0; border-bottom: 1px solid rgba(255,255,255,0.04);
  font-size: 13px;
  opacity: 0; animation: apparition 0.35s ease both;
}
.feed-time { color: #555; white-space: nowrap; min-width: 130px; }
.feed-action { color: #8b0000; font-weight: 600; white-space: nowrap; }
.feed-detail { color: #999; flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
</style>
