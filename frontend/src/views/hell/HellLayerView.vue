<template>
  <div class="hell-page">
    <!-- 深渊入口 -->
    <div class="hell-hero">
      <div class="hell-hero-icon"></div>
      <h2 class="hell-hero-title">十八层地狱</h2>
      <p class="hell-hero-sub">善恶终有报 · 天道好轮回</p>
      <div class="hell-hero-descend">
        <span class="descend-line"></span>
        <span class="descend-text">往下即是深渊</span>
        <span class="descend-line"></span>
      </div>
    </div>

    <!-- 地狱层级 zigzag -->
    <div class="hell-descent">
      <div v-for="(layer, i) in layers" :key="layer.id"
        class="hell-layer"
        :class="[i % 2 === 0 ? 'layer-left' : 'layer-right']"
        :style="{ '--layer-color': layerColors[i % 8], '--layer-delay': i * 0.06 + 's' }"
      >
        <!-- 层级数字装饰 -->
        <div class="layer-num-col">
          <span class="layer-num">{{ layer.layerNum }}</span>
          <span class="layer-num-line"></span>
        </div>

        <!-- 内容卡片 -->
        <div class="layer-card">
          <!-- CSS 绘制的场景插画 -->
          <div class="layer-scene">
            <span class="scene-icon">{{ hellIcons[layer.layerNum - 1] }}</span>
            <div class="scene-texture"></div>
          </div>

          <div class="layer-body">
            <div class="layer-hd">
              <span class="layer-name">{{ layer.name }}</span>
            </div>

            <div class="layer-fields">
              <div class="layer-field">
                <span class="field-tag">罪行</span>
                <p>{{ layer.crime }}</p>
              </div>
              <div class="layer-field">
                <span class="field-tag">刑罚</span>
                <p>{{ layer.punishment }}</p>
              </div>
            </div>

            <div class="layer-duration">
              <span class="duration-mark"></span>
              {{ layer.durationDesc }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部 -->
    <div class="hell-abyss">
      <span class="abyss-text">第 十 八 层 之 下 · 无 间 地 狱</span>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getHellLayers } from '@/api/hell'

const layers = ref([])
const hellIcons = [
  '👅', '✂️', '🌳', '🪞', '🌫',
  '🔥', '🗡️', '❄️', '🍳', '🐃',
  '🪨', '💥', '🩸', '👻', '⛱️',
  '🌋', '⚙️', '🪓'
]
const layerColors = [
  '#6a2020', '#8b2020', '#6a2030', '#7a2030',
  '#8a2020', '#6a2530', '#7a2530', '#8a2530'
]

onMounted(async () => {
  const res = await getHellLayers()
  layers.value = res.data || []
})
</script>

<style scoped>
.hell-page {
  padding-bottom: 80px;
}

/* 深渊入口 */
.hell-hero {
  text-align: center;
  padding: 40px 0 30px;
  position: relative;
}
.hell-hero-icon {
  width: 48px; height: 56px;
  margin: 0 auto 16px;
  position: relative;
}
.hell-hero-icon::before {
  content: '';
  position: absolute;
  top: 0; left: 8px;
  width: 32px; height: 32px;
  background: var(--gold);
  border-radius: 52% 52% 25% 25%;
  box-shadow: 0 0 22px rgba(201,168,76,0.4), 0 0 50px rgba(122,0,0,0.25);
  animation: candleFlicker 3s ease-in-out infinite;
}
.hell-hero-icon::after {
  content: '';
  position: absolute;
  bottom: 4px; left: 0;
  width: 48px; height: 18px;
  border: 2px solid var(--gold);
  border-top: none;
  border-radius: 0 0 32% 32%;
  box-shadow: 0 0 12px rgba(201,168,76,0.3);
}
.hell-hero-title {
  font-family: var(--font-display);
  font-size: 32px;
  color: var(--gold);
  letter-spacing: 10px;
  font-weight: 400;
  animation: candleFlicker 4s ease-in-out infinite;
  text-shadow: 0 0 15px rgba(201,168,76,0.35), 0 2px 6px rgba(0,0,0,0.7);
}
.hell-hero-sub {
  font-size: 14px;
  color: #6a5a50;
  letter-spacing: 6px;
  margin-top: 8px;
}
.hell-hero-descend {
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 14px;
}
.descend-line {
  width: 60px; height: 1px;
  background: linear-gradient(90deg, transparent, rgba(122,0,0,0.5), transparent);
}
.descend-text {
  font-size: 11px;
  color: #4a3030;
  letter-spacing: 4px;
}

/* 层级 descent */
.hell-descent {
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px;
}

.hell-layer {
  display: flex;
  align-items: stretch;
  gap: 0;
  margin-bottom: 0;
  position: relative;
  opacity: 0;
  animation: apparition 0.7s ease both;
  animation-delay: var(--layer-delay);
}

/* 连接线 */
.hell-layer::after {
  content: '';
  position: absolute;
  width: 1px;
  background: linear-gradient(180deg, rgba(122,0,0,0.35), rgba(122,0,0,0.05));
  left: 47px;
  top: 50%;
  height: calc(100% + 0px);
  z-index: -1;
}
.hell-layer:last-child::after { display: none; }

/* 左右交替 */
.layer-left { flex-direction: row; }
.layer-right { flex-direction: row-reverse; }

/* 层级数字 */
.layer-num-col {
  width: 94px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 24px;
}
.layer-num {
  font-size: 32px;
  font-weight: 900;
  font-family: 'Georgia', 'Times New Roman', serif;
  color: var(--layer-color, #7a2020);
  text-shadow: 0 0 14px rgba(160,16,32,0.4);
  line-height: 1;
}
.layer-num-line {
  width: 2px;
  flex: 1;
  min-height: 20px;
  background: linear-gradient(180deg, var(--layer-color, #7a2020), transparent);
  margin-top: 4px;
  opacity: 0.4;
}

/* 内容卡片 */
.layer-card {
  flex: 1;
  margin-bottom: 20px;
  background:
    linear-gradient(170deg, rgba(26,21,16,0.9), rgba(13,10,8,0.95));
  border: 1px solid rgba(122,0,0,0.15);
  border-radius: 3px;
  display: flex;
  overflow: hidden;
  transition: all 0.3s;
}
.layer-card:hover {
  border-color: rgba(122,0,0,0.4);
  box-shadow: 0 8px 30px rgba(0,0,0,0.5), 0 0 10px rgba(122,0,0,0.08);
  transform: translateY(-2px);
}

/* 场景插画 — CSS 渐变替代图片 */
.layer-scene {
  width: 110px;
  flex-shrink: 0;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  background:
    radial-gradient(ellipse at 30% 70%, var(--layer-color, #7a0000) 0%, transparent 70%),
    radial-gradient(ellipse at 70% 20%, rgba(0,0,0,0.6) 0%, transparent 50%),
    linear-gradient(180deg, rgba(0,0,0,0.5), rgba(0,0,0,0.8));
}
.scene-icon {
  font-size: 40px;
  position: relative;
  z-index: 1;
  filter: drop-shadow(0 0 10px rgba(0,0,0,0.7));
}
.scene-texture {
  position: absolute;
  inset: 0;
  pointer-events: none;
  background:
    repeating-linear-gradient(0deg, transparent, transparent 3px, rgba(255,255,255,0.015) 3px, rgba(255,255,255,0.015) 4px);
}

.layer-body {
  flex: 1;
  padding: 18px 22px;
}

.layer-hd {
  margin-bottom: 12px;
}
.layer-name {
  font-size: 18px;
  color: var(--gold);
  font-weight: 700;
  letter-spacing: 3px;
}

.layer-fields {
  display: flex;
  gap: 24px;
  margin-bottom: 12px;
}
.layer-field {
  flex: 1;
}
.field-tag {
  font-size: 10px;
  color: var(--blood-bright);
  letter-spacing: 2px;
  text-transform: uppercase;
  border: 1px solid rgba(122,0,0,0.25);
  padding: 1px 8px;
  border-radius: 2px;
  display: inline-block;
  margin-bottom: 6px;
}
.layer-field p {
  font-size: 13px;
  color: #908070;
  line-height: 1.6;
  margin: 0;
}

.layer-duration {
  font-size: 12px;
  color: var(--blood-bright);
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
}
.duration-mark {
  width: 6px; height: 6px;
  background: var(--blood-bright);
  border-radius: 50%;
  animation: candleFlicker 2s ease-in-out infinite;
}

/* 底部深渊 */
.hell-abyss {
  text-align: center;
  padding: 60px 0 40px;
  position: relative;
}
.hell-abyss::before {
  content: '';
  position: absolute;
  top: 0; left: 50%; transform: translateX(-50%);
  width: 120px; height: 1px;
  background: linear-gradient(90deg, transparent, rgba(122,0,0,0.4), transparent);
}
.abyss-text {
  font-size: 16px;
  color: #3a2020;
  letter-spacing: 8px;
  font-weight: 700;
  animation: candleFlicker 5s ease-in-out infinite;
}

/* 响应式 */
@media (max-width: 700px) {
  .layer-left, .layer-right { flex-direction: column; }
  .layer-num-col { display: none; }
  .hell-layer::after { display: none; }
  .layer-card { flex-direction: column; }
  .layer-scene { width: 100%; height: 100px; }
  .layer-fields { flex-direction: column; gap: 8px; }
}
</style>
