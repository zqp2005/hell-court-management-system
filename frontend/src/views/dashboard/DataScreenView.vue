<template>
  <div class="screen" @keydown.esc="goBack">
    <div class="particles" aria-hidden="true">
      <span v-for="i in 30" :key="i" class="particle"
        :style="{
          left: rand(i*7) + '%', top: rand(i*13) + '%',
          animationDelay: rand(i*3) + 's', animationDuration: (8 + rand(i*5)) + 's',
          fontSize: (14 + rand(i*11)) + 'px', opacity: 0.03 + rand(i*7) * 0.01
        }"
      >{{ symbols[i % symbols.length] }}</span>
    </div>

    <div class="top-bar">
      <div class="title-section">
        <span class="skull">💀</span>
        <span class="main-title">地 府 数 据 大 屏</span>
        <span class="subtitle">· 酆都司政实时监控 ·</span>
      </div>
      <div class="clock-section">
        <span class="clock-date">{{ dateStr }}</span>
        <span class="clock-time">{{ timeStr }}</span>
      </div>
    </div>

    <div class="main-area">
      <div class="left-col">
        <div class="panel panel-feed">
          <div class="panel-title"><span class="title-dot"></span>地府动态</div>
          <div class="feed-wrap">
            <div class="feed-scroll" ref="feedScroll">
              <div v-for="(item, i) in activityFeed" :key="i" class="feed-row" :style="{ animationDelay: i * 0.05 + 's' }">
                <span class="feed-dot"></span>
                <div class="feed-body">
                  <span class="feed-action">{{ item.action }}</span>
                  <span class="feed-detail">{{ item.detail }}</span>
                  <span class="feed-time">{{ item.createdAt }}</span>
                </div>
              </div>
              <p v-if="!activityFeed.length" class="empty-hint">暂无动态</p>
            </div>
          </div>
        </div>
      </div>

      <div class="center-col">
        <div class="stats-grid">
          <div class="stat-item" v-for="s in statsCards" :key="s.label" :style="{ borderColor: s.color }">
            <span class="stat-icon" :style="{ color: s.color }">{{ s.icon }}</span>
            <div class="stat-body">
              <span class="stat-num" :style="{ color: s.color }">{{ animated[s.key] ?? s.value }}</span>
              <span class="stat-label">{{ s.label }}</span>
            </div>
            <div class="stat-bar" :style="{ background: s.color }"></div>
          </div>
        </div>

        <div class="panel panel-chart">
          <div class="panel-title"><span class="title-dot"></span>魂魄月度趋势</div>
          <div ref="trendChart" class="chart-box"></div>
        </div>
      </div>

      <div class="right-col">
        <div class="panel panel-chart panel-sm">
          <div class="panel-title"><span class="title-dot"></span>判官业绩排行</div>
          <div ref="rankChart" class="chart-box-sm"></div>
        </div>
        <div class="panel panel-chart panel-sm">
          <div class="panel-title"><span class="title-dot"></span>六道分布</div>
          <div ref="pieChart" class="chart-box-sm"></div>
        </div>
      </div>
    </div>

    <div class="ticker-bar">
      <span class="ticker-text">{{ tickerMsg }}</span>
    </div>

    <button class="exit-btn" @click="goBack" title="返回工作台">✕</button>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { getStatistics } from '@/api/dashboard'
import echarts from '@/utils/echarts'

const router = useRouter()
const symbols = ['💀', '☠', '👻', '🕯', '⚰', '🦴', '🩸', '🔮', '🕷', '🦇']

const dateStr = ref('')
const timeStr = ref('')
const activityFeed = ref([])
let trendChartInst = null, pieChartInst = null, rankChartInst = null
let clockTimer = null, refreshTimer = null
const feedScroll = ref(null)
const animated = reactive({})

const statsCards = ref([
  { key: 'totalSouls', label: '魂魄总数', value: 0, icon: '👥', color: '#ffd700' },
  { key: 'pendingSummon', label: '待勾魂', value: 0, icon: '⏳', color: '#9b59b6' },
  { key: 'arrivedSouls', label: '已到殿', value: 0, icon: '🚶', color: '#3498db' },
  { key: 'judgedSouls', label: '已判决', value: 0, icon: '⚖', color: '#e67e22' },
  { key: 'pendingReviews', label: '待复核', value: 0, icon: '📋', color: '#e74c3c' },
  { key: 'rebirthedSouls', label: '已轮回', value: 0, icon: '🔄', color: '#2ecc71' }
])

const tickerMsg = ref('')

function rand(seed) { return ((seed * 7919 + 1049) % 100) }
function goBack() { router.push('/dashboard') }

function updateClock() {
  const now = new Date()
  dateStr.value = `${now.getFullYear()}年${String(now.getMonth() + 1).padStart(2, '0')}月${String(now.getDate()).padStart(2, '0')}日`
  timeStr.value = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}:${String(now.getSeconds()).padStart(2, '0')}`
}

function animateValue(key, from, to) {
  const duration = 800, start = performance.now()
  function step(ts) {
    const p = Math.min((ts - start) / duration, 1)
    animated[key] = Math.round(from + (to - from) * (1 - Math.pow(1 - p, 3)))
    if (p < 1) requestAnimationFrame(step)
    else animated[key] = to
  }
  requestAnimationFrame(step)
}

async function loadData() {
  try {
    const res = await getStatistics()
    const d = res.data
    statsCards.value[0].value = d.totalSouls
    statsCards.value[1].value = d.pendingSummon
    statsCards.value[2].value = d.arrivedSouls
    statsCards.value[3].value = d.judgedSouls
    statsCards.value[4].value = d.pendingReviews
    statsCards.value[5].value = d.rebirthedSouls

    for (const s of statsCards.value) {
      animateValue(s.key, animated[s.key] || 0, s.value)
    }

    activityFeed.value = d.activityFeed || []

    tickerMsg.value = [
      `当前地府共收录魂魄 ${d.totalSouls} 条`,
      `待勾魂魄 ${d.pendingSummon} 条，无常速往`,
      `待复核审判 ${d.pendingReviews} 件，请阎王审阅`,
      `已入轮回 ${d.rebirthedSouls} 魂`,
      '善恶到头终有报，人间私语天闻若雷'
    ].join('  ╱  ')

    nextTick(() => {
      updateTrend(d.monthlyTrend || [])
      updatePie(d.rebirthDistribution || [])
      updateRank(d.judgeRanking || [])
    })
  } catch (_) { /* ignore */ }
}

function updateTrend(data) {
  if (!trendChartInst) {
    const el = document.querySelector('[ref="trendChart"]')
    if (el) trendChartInst = echarts.init(el)
  }
  if (!trendChartInst) return
  trendChartInst.setOption({
    backgroundColor: 'transparent',
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(10,10,15,0.9)', borderColor: '#8b0000', textStyle: { color: '#ccc', fontSize: 13 } },
    grid: { left: '3%', right: '4%', top: 20, bottom: 20, containLabel: true },
    xAxis: { type: 'category', data: data.map(m => m.month), axisLine: { lineStyle: { color: '#333' } }, axisLabel: { color: '#888' } },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#1a1a2e', type: 'dashed' } }, axisLabel: { color: '#888' } },
    series: [{
      data: data.map(m => m.count), type: 'line', smooth: true, symbol: 'circle', symbolSize: 10,
      lineStyle: { color: '#ffd700', width: 3, shadowBlur: 10, shadowColor: 'rgba(255,215,0,0.4)' },
      itemStyle: { color: '#ffd700', borderColor: '#8b0000', borderWidth: 2 },
      areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(255,215,0,0.25)' }, { offset: 1, color: 'rgba(139,0,0,0.05)' }]) }
    }]
  })
}

function updatePie(data) {
  if (!pieChartInst) {
    const el = document.querySelector('[ref="pieChart"]')
    if (el) pieChartInst = echarts.init(el)
  }
  if (!pieChartInst) return
  const names = ['天道', '阿修罗道', '人道', '畜生道', '饿鬼道', '地狱道']
  const colors = ['#ffd700', '#ff6b35', '#2ecc71', '#95a5a6', '#e74c3c', '#8b0000']
  pieChartInst.setOption({
    backgroundColor: 'transparent',
    tooltip: { trigger: 'item', backgroundColor: 'rgba(10,10,15,0.9)', borderColor: '#8b0000', textStyle: { color: '#ccc', fontSize: 13 }, formatter: '{b}: {c} ({d}%)' },
    series: [{
      type: 'pie', radius: ['45%', '75%'], center: ['50%', '55%'], roseType: 'area',
      data: data.map((d, i) => ({ name: names[d.path - 1] || '未知', value: d.count, itemStyle: { color: colors[d.path - 1] || '#666', borderRadius: 6, borderColor: '#0a0a0f', borderWidth: 3 } })),
      label: { color: '#aaa', fontSize: 11 }, labelLine: { lineStyle: { color: '#444' } }
    }]
  })
}

function updateRank(data) {
  if (!rankChartInst) {
    const el = document.querySelector('[ref="rankChart"]')
    if (el) rankChartInst = echarts.init(el)
  }
  if (!rankChartInst) return
  rankChartInst.setOption({
    backgroundColor: 'transparent',
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(10,10,15,0.9)', borderColor: '#8b0000', textStyle: { color: '#ccc', fontSize: 13 } },
    grid: { left: '3%', right: '8%', top: 15, bottom: 15, containLabel: true },
    xAxis: { type: 'value', splitLine: { lineStyle: { color: '#1a1a2e', type: 'dashed' } }, axisLabel: { color: '#888' }, name: '审判数', nameTextStyle: { color: '#666', fontSize: 11 } },
    yAxis: { type: 'category', data: (data || []).map(r => '判官 #' + r.judgeId), axisLine: { lineStyle: { color: '#333' } }, axisLabel: { color: '#aaa', fontSize: 12 }, inverse: true },
    series: [{
      data: (data || []).map(r => ({ value: r.count, itemStyle: { color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{ offset: 0, color: '#8b0000' }, { offset: 1, color: '#ffd700' }]), borderRadius: [0, 4, 4, 0] } })),
      type: 'bar', barWidth: 18, label: { show: true, position: 'right', color: '#aaa', fontSize: 11 }
    }]
  })
}

function handleResize() {
  trendChartInst?.resize()
  pieChartInst?.resize()
  rankChartInst?.resize()
}

function onKeydown(e) { if (e.key === 'Escape') goBack() }

onMounted(() => {
  updateClock()
  clockTimer = setInterval(updateClock, 1000)
  loadData()
  refreshTimer = setInterval(loadData, 10000)
  window.addEventListener('resize', handleResize)
  document.addEventListener('keydown', onKeydown)
})

onUnmounted(() => {
  clearInterval(clockTimer)
  clearInterval(refreshTimer)
  trendChartInst?.dispose()
  pieChartInst?.dispose()
  rankChartInst?.dispose()
  window.removeEventListener('resize', handleResize)
  document.removeEventListener('keydown', onKeydown)
})
</script>

<style scoped>
.screen {
  position: fixed; inset: 0; z-index: 9999;
  background: radial-gradient(ellipse at 50% 30%, #0d0d1a 0%, #06060c 60%, #020208 100%);
  color: #ccc; font-family: 'Noto Serif SC', 'SimSun', serif;
  display: flex; flex-direction: column; overflow: hidden;
}
.particles { position: absolute; inset: 0; pointer-events: none; overflow: hidden; }
.particle { position: absolute; animation: floatUp linear infinite; user-select: none; }
@keyframes floatUp {
  0% { transform: translateY(0) rotate(0deg); opacity: 0; }
  10% { opacity: 0.06; } 90% { opacity: 0.04; }
  100% { transform: translateY(-100vh) rotate(180deg); opacity: 0; }
}
.top-bar { display: flex; align-items: center; justify-content: space-between; padding: 20px 40px 12px; flex-shrink: 0; border-bottom: 1px solid rgba(139,0,0,0.15); position: relative; z-index: 1; }
.title-section { display: flex; align-items: center; gap: 14px; }
.skull { font-size: 38px; animation: skullStare 2s ease-in-out infinite; filter: drop-shadow(0 0 12px rgba(139,0,0,0.6)); }
@keyframes skullStare {
  0%, 100% { text-shadow: 0 0 10px rgba(139,0,0,0.4); }
  50% { text-shadow: 0 0 25px rgba(139,0,0,0.8), 0 0 50px rgba(139,0,0,0.3); }
}
.main-title { font-family: var(--font-display, 'ZCOOL XiaoWei', serif); font-size: 36px; font-weight: 400; color: #ffd700; letter-spacing: 12px; text-shadow: 0 0 20px rgba(255,215,0,0.3); }
.subtitle { font-size: 14px; color: #666; letter-spacing: 4px; }
.clock-section { display: flex; flex-direction: column; align-items: flex-end; gap: 2px; }
.clock-date { font-size: 14px; color: #666; letter-spacing: 2px; }
.clock-time { font-size: 42px; font-weight: bold; color: #ffd700; letter-spacing: 6px; font-variant-numeric: tabular-nums; text-shadow: 0 0 15px rgba(255,215,0,0.25); }
.main-area { flex: 1; display: flex; gap: 18px; padding: 16px 24px 12px; position: relative; z-index: 1; min-height: 0; }
.panel { background: rgba(26,26,46,0.6); border: 1px solid rgba(255,255,255,0.06); border-radius: 10px; backdrop-filter: blur(4px); }
.panel-title { font-size: 14px; color: #ffd700; letter-spacing: 3px; padding: 14px 18px 10px; border-bottom: 1px solid rgba(139,0,0,0.15); display: flex; align-items: center; gap: 10px; }
.title-dot { width: 8px; height: 8px; border-radius: 50%; background: #8b0000; box-shadow: 0 0 8px #8b0000; animation: pulse 2s ease-in-out infinite; }
@keyframes pulse {
  0%, 100% { box-shadow: 0 0 6px #8b0000; }
  50% { box-shadow: 0 0 16px #8b0000, 0 0 24px rgba(139,0,0,0.5); }
}
.left-col { width: 260px; flex-shrink: 0; display: flex; flex-direction: column; }
.panel-feed { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
.feed-wrap { flex: 1; overflow: hidden; padding: 8px 14px; }
.feed-scroll { height: 100%; overflow-y: auto; }
.feed-row { display: flex; align-items: flex-start; gap: 10px; padding: 8px 0; border-bottom: 1px solid rgba(255,255,255,0.03); animation: fadeInRight 0.4s ease both; }
@keyframes fadeInRight {
  from { opacity: 0; transform: translateX(-10px); }
  to { opacity: 1; transform: translateX(0); }
}
.feed-dot { width: 6px; height: 6px; border-radius: 50%; background: #8b0000; margin-top: 6px; flex-shrink: 0; }
.feed-body { flex: 1; min-width: 0; }
.feed-action { font-size: 12px; color: #8b0000; font-weight: 600; display: block; }
.feed-detail { font-size: 11px; color: #888; display: block; margin-top: 2px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.feed-time { font-size: 10px; color: #444; display: block; margin-top: 2px; }
.empty-hint { color: #555; text-align: center; padding: 20px 0; font-size: 13px; }
.center-col { flex: 1; min-width: 0; display: flex; flex-direction: column; gap: 16px; }
.stats-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 14px; }
.stat-item { background: rgba(26,26,46,0.6); border: 1px solid; border-radius: 10px; padding: 18px 16px; display: flex; align-items: center; gap: 14px; position: relative; overflow: hidden; backdrop-filter: blur(4px); }
.stat-icon { font-size: 32px; flex-shrink: 0; }
.stat-body { flex: 1; }
.stat-num { font-size: 30px; font-weight: bold; display: block; font-variant-numeric: tabular-nums; }
.stat-label { font-size: 12px; color: #666; letter-spacing: 2px; display: block; margin-top: 2px; }
.stat-bar { position: absolute; bottom: 0; left: 0; height: 3px; width: 100%; opacity: 0.25; }
.panel-chart { flex: 1; display: flex; flex-direction: column; }
.chart-box { flex: 1; min-height: 0; }
.right-col { width: 280px; flex-shrink: 0; display: flex; flex-direction: column; gap: 16px; }
.panel-sm { flex: 1; display: flex; flex-direction: column; }
.chart-box-sm { flex: 1; min-height: 0; }
.ticker-bar { flex-shrink: 0; height: 36px; background: rgba(139,0,0,0.1); border-top: 1px solid rgba(139,0,0,0.15); display: flex; align-items: center; overflow: hidden; position: relative; z-index: 1; padding: 0 20px; }
.ticker-text { white-space: nowrap; animation: ticker 30s linear infinite; color: #666; font-size: 12px; letter-spacing: 2px; }
@keyframes ticker {
  0% { transform: translateX(0); }
  100% { transform: translateX(-50%); }
}
.exit-btn { position: absolute; top: 20px; right: 24px; z-index: 2; background: rgba(139,0,0,0.15); border: 1px solid rgba(139,0,0,0.3); color: #888; width: 36px; height: 36px; border-radius: 8px; font-size: 18px; cursor: pointer; display: flex; align-items: center; justify-content: center; transition: all 0.3s; }
.exit-btn:hover { background: rgba(139,0,0,0.3); color: #ffd700; border-color: #8b0000; }
.feed-scroll::-webkit-scrollbar { width: 3px; }
.feed-scroll::-webkit-scrollbar-track { background: transparent; }
.feed-scroll::-webkit-scrollbar-thumb { background: rgba(139,0,0,0.3); border-radius: 2px; }
</style>
