<template>
  <div class="demon-page">
    <!-- 卷轴头部 -->
    <div class="scroll-header">
      <div class="scroll-rod"></div>
      <div class="scroll-title-area">
        <div class="scroll-skull"></div>
        <h2 class="scroll-title">百 鬼 录</h2>
        <p class="scroll-sub">阴司群像 · 鬼神图鉴 · 见之则不祥</p>
      </div>
      <div class="scroll-rod"></div>
    </div>

    <!-- 卷轴内容 — 错落卡片 -->
    <div class="scroll-body">
      <div class="demon-masonry">
        <div v-for="(d, i) in demons" :key="d.id"
          class="demon-card"
          :class="[cardSizes[i % 5]]"
          :style="{ '--accent': accentColors[i % 8], '--delay': i * 0.07 + 's' }"
        >
          <!-- CSS 场景 -->
          <div class="demon-visual">
            <div class="demon-scene" :style="{ background: sceneGradients[i % 6] }">
              <span class="demon-symbol">{{ demonSymbols[i % demonSymbols.length] }}</span>
            </div>
          </div>

          <div class="demon-info">
            <div class="demon-top">
              <span class="demon-name">{{ d.name }}</span>
              <span class="demon-cat" :style="{ borderColor: 'var(--accent)', color: 'var(--accent)' }">{{ d.category }}</span>
            </div>
            <p class="demon-alias" v-if="d.alias">{{ d.alias }}</p>
            <p class="demon-desc">{{ d.description }}</p>
            <div class="demon-traits">
              <span v-if="d.ability" class="trait trait-ability">
                <span class="trait-mark">能</span>{{ d.ability }}
              </span>
              <span v-if="d.weakness" class="trait trait-weakness">
                <span class="trait-mark">忌</span>{{ d.weakness }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 卷轴尾部 -->
    <div class="scroll-footer">
      <div class="scroll-rod"></div>
      <span class="scroll-end">— 卷 终 —</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDemons } from '@/api/demon'

const demons = ref([])
const cardSizes = ['card-std', 'card-wide', 'card-std', 'card-tall', 'card-std']
const accentColors = [
  '#c9a84c', '#a01020', '#8b5a2b', '#6a3050',
  '#b05a20', '#5a4040', '#9a4030', '#7a3030'
]
const demonSymbols = ['👹', '👺', '👻', '💀', '👁️', '🕯️', '🦇', '🕷️', '🐍', '🔥', '🌑', '⚡']
const sceneGradients = [
  'radial-gradient(ellipse at 40% 60%, rgba(160,16,32,0.5) 0%, rgba(0,0,0,0.7) 50%, rgba(13,10,8,0.9) 100%)',
  'radial-gradient(ellipse at 60% 40%, rgba(139,90,43,0.4) 0%, rgba(0,0,0,0.6) 50%, rgba(13,10,8,0.85) 100%)',
  'radial-gradient(ellipse at 50% 30%, rgba(106,48,80,0.45) 0%, rgba(0,0,0,0.65) 50%, rgba(13,10,8,0.9) 100%)',
  'radial-gradient(ellipse at 30% 50%, rgba(100,40,40,0.4) 0%, rgba(0,0,0,0.7) 45%, rgba(13,10,8,0.85) 100%)',
  'radial-gradient(ellipse at 45% 55%, rgba(160,50,20,0.45) 0%, rgba(0,0,0,0.6) 50%, rgba(13,10,8,0.9) 100%)',
  'radial-gradient(ellipse at 55% 45%, rgba(90,40,40,0.5) 0%, rgba(0,0,0,0.65) 50%, rgba(13,10,8,0.85) 100%)'
]

onMounted(async () => {
  const res = await getDemons()
  demons.value = res.data || []
})
</script>

<style scoped>
.demon-page {
  padding-bottom: 60px;
}

/* 卷轴头部 */
.scroll-header {
  display: flex;
  align-items: flex-start;
  gap: 0;
  margin-bottom: 32px;
}
.scroll-rod {
  width: 100%;
  height: 6px;
  background: linear-gradient(180deg, #3a2020 0%, #1a1010 50%, #0d0a08 100%);
  border-radius: 3px;
  flex: 1;
  margin-top: 30px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.5);
}
.scroll-title-area {
  text-align: center;
  padding: 16px 40px 10px;
  flex-shrink: 0;
}
.scroll-skull {
  width: 32px; height: 38px;
  margin: 0 auto 12px;
  position: relative;
}
.scroll-skull::before {
  content: '';
  position: absolute;
  top: 0; left: 5px;
  width: 22px; height: 22px;
  background: var(--gold);
  border-radius: 52% 52% 25% 25%;
  box-shadow: 0 0 14px rgba(201,168,76,0.35);
}
.scroll-skull::after {
  content: '';
  position: absolute;
  bottom: 3px; left: 0;
  width: 32px; height: 12px;
  border: 1.5px solid var(--gold);
  border-top: none;
  border-radius: 0 0 30% 30%;
  box-shadow: 0 0 8px rgba(201,168,76,0.25);
}
.scroll-title {
  font-family: var(--font-display);
  font-size: 28px;
  color: var(--gold);
  letter-spacing: 10px;
  font-weight: 400;
  animation: candleFlicker 4s ease-in-out infinite;
  text-shadow: 0 0 15px rgba(201,168,76,0.3), 0 2px 4px rgba(0,0,0,0.6);
}
.scroll-sub {
  font-size: 12px;
  color: #5a4030;
  letter-spacing: 4px;
  margin-top: 6px;
}

/* 错落 Masonry 网格 */
.demon-masonry {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 16px;
}
@media (max-width: 900px) {
  .demon-masonry { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 550px) {
  .demon-masonry { grid-template-columns: 1fr; }
}

/* 不同尺寸 */
.card-std { grid-column: span 1; grid-row: span 1; }
.card-wide { grid-column: span 2; grid-row: span 1; }
.card-tall { grid-column: span 1; grid-row: span 2; }

@media (max-width: 900px) {
  .card-wide { grid-column: span 2; }
  .card-tall { grid-column: span 1; grid-row: span 2; }
}
@media (max-width: 550px) {
  .card-wide, .card-tall { grid-column: span 1; grid-row: span 1; }
}

/* 卡片 */
.demon-card {
  background: linear-gradient(170deg, rgba(26,21,16,0.9), rgba(13,10,8,0.95));
  border: 1px solid rgba(122,0,0,0.12);
  border-radius: 3px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  opacity: 0;
  animation: apparition 0.6s ease both;
  animation-delay: var(--delay);
  transition: all 0.3s;
  position: relative;
}
.demon-card:hover {
  border-color: var(--accent);
  box-shadow: 0 10px 32px rgba(0,0,0,0.5), 0 0 0 4px rgba(122,0,0,0.05);
  transform: translateY(-3px);
}
/* 卡片边缘烧灼效果 */
.demon-card::before {
  content: '';
  position: absolute;
  inset: -1px;
  border-radius: 3px;
  opacity: 0;
  transition: opacity 0.3s;
  background: linear-gradient(135deg, transparent 40%, rgba(122,0,0,0.15) 40%, rgba(122,0,0,0.15) 60%, transparent 60%);
  pointer-events: none;
}
.demon-card:hover::before { opacity: 1; }

.demon-visual {
  height: 120px;
  overflow: hidden;
  position: relative;
}
.card-tall .demon-visual { height: 180px; }
.card-wide .demon-visual { height: 140px; }

.demon-scene {
  width: 100%; height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
.demon-scene::after {
  content: '';
  position: absolute;
  inset: 0;
  pointer-events: none;
  background:
    repeating-linear-gradient(0deg, transparent, transparent 2px, rgba(255,255,255,0.01) 2px, rgba(255,255,255,0.01) 3px);
}
.demon-symbol {
  font-size: 42px;
  position: relative;
  z-index: 1;
  filter: drop-shadow(0 0 12px rgba(0,0,0,0.7));
}
.card-tall .demon-symbol { font-size: 56px; }

.demon-info {
  padding: 16px 20px;
  flex: 1;
}

.demon-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  margin-bottom: 4px;
}
.demon-name {
  font-size: 17px;
  color: var(--gold);
  font-weight: 700;
  letter-spacing: 2px;
}
.demon-cat {
  font-size: 10px;
  border: 1px solid;
  padding: 2px 8px;
  border-radius: 2px;
  letter-spacing: 2px;
  flex-shrink: 0;
}

.demon-alias {
  font-size: 11px;
  color: #5a4030;
  margin-bottom: 8px;
  font-style: italic;
}
.demon-desc {
  font-size: 13px;
  color: #908070;
  line-height: 1.65;
  margin-bottom: 12px;
}

.demon-traits {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.trait {
  font-size: 11px;
  color: #706050;
  display: flex;
  align-items: center;
  gap: 6px;
}
.trait-mark {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 18px; height: 18px;
  font-size: 10px;
  border-radius: 50%;
  flex-shrink: 0;
}
.trait-ability .trait-mark {
  background: rgba(122,0,0,0.2);
  color: var(--blood-bright);
  border: 1px solid rgba(122,0,0,0.3);
}
.trait-weakness .trait-mark {
  background: rgba(201,168,76,0.1);
  color: var(--gold);
  border: 1px solid rgba(201,168,76,0.2);
}

/* 卷轴尾 */
.scroll-footer {
  text-align: center;
  padding: 40px 0;
}
.scroll-footer .scroll-rod { margin-bottom: 16px; }
.scroll-end {
  font-size: 13px;
  color: #4a3030;
  letter-spacing: 6px;
}
</style>
