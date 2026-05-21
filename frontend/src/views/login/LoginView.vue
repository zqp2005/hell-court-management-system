<template>
  <div class="login-scene" @mousemove="onMouse" @mouseleave="onLeave">
    <!-- ====== 背景场景层 ====== -->
    <div class="scene-bg">
      <!-- 血月 -->
      <div class="blood-moon"></div>
      <!-- 远山剪影 -->
      <div class="mountains">
        <span class="mt mt-1"></span>
        <span class="mt mt-2"></span>
        <span class="mt mt-3"></span>
        <span class="mt mt-4"></span>
      </div>
      <!-- 地面雾气 -->
      <div class="ground-fog"></div>
      <div class="ground-fog fog-2"></div>
      <!-- 浮灰粒子 -->
      <span v-for="i in 40" :key="'e'+i" class="ember"
        :style="{
          left: ((i*179+31)%96) + '%',
          animationDelay: (i*0.35) + 's',
          animationDuration: (6+(i*0.4)%8) + 's',
          width: (1.5+(i%3)*1) + 'px',
          height: (1.5+(i%3)*1) + 'px',
          opacity: 0.15 + (i%4)*0.1
        }"
      ></span>
    </div>

    <!-- ====== 鬼怪层 ====== -->
    <!-- 黑无常 — 左上 -->
    <div class="ghost-char char-hei" ref="charEls" data-idx="0">
      <div class="gc-body" ref="bodyEls">
        <div class="gc-head hei-head">
          <div class="hei-hat"></div>
          <div class="gc-eyes">
            <span class="gc-pupil gold-pupil" ref="pupilEls"></span>
            <span class="gc-pupil gold-pupil" ref="pupilEls"></span>
          </div>
          <div class="hei-mouth"></div>
        </div>
        <div class="gc-robe robe-hei">
          <span class="robe-belt"></span>
        </div>
        <div class="hei-chain"></div>
      </div>
      <span class="gc-name">黑无常</span>
      <span class="gc-fire"></span>
      <span class="gc-mist" v-for="m in 4" :key="'hm'+m"
        :style="{
          left: ((m*33+10)%100)+'%',
          top: ((m*40+20)%100)+'%',
          animationDelay: (m*0.5)+'s',
          width: (20+m*8)+'px', height: (25+m*10)+'px'
        }"></span>
    </div>

    <!-- 白无常 — 右上 -->
    <div class="ghost-char char-bai" ref="charEls" data-idx="1">
      <div class="gc-body" ref="bodyEls">
        <div class="gc-head bai-head">
          <div class="bai-hat"></div>
          <div class="gc-eyes">
            <span class="gc-pupil dark-pupil" ref="pupilEls"></span>
            <span class="gc-pupil dark-pupil" ref="pupilEls"></span>
          </div>
          <div class="bai-mouth"></div>
        </div>
        <div class="gc-robe robe-bai"></div>
        <div class="bai-fan"></div>
      </div>
      <span class="gc-name">白无常</span>
      <span class="gc-fire"></span>
      <span class="gc-mist" v-for="m in 4" :key="'bm'+m"
        :style="{
          left: ((m*37+5)%100)+'%',
          top: ((m*35+15)%100)+'%',
          animationDelay: (m*0.6)+'s',
          width: (18+m*7)+'px', height: (22+m*9)+'px'
        }"></span>
    </div>

    <!-- 判官 — 左下 -->
    <div class="ghost-char char-judge" ref="charEls" data-idx="2">
      <div class="gc-body" ref="bodyEls">
        <div class="gc-head judge-head">
          <div class="judge-hat"></div>
          <div class="gc-eyes">
            <span class="gc-pupil gold-pupil" ref="pupilEls"></span>
            <span class="gc-pupil gold-pupil" ref="pupilEls"></span>
          </div>
          <div class="judge-beard"></div>
        </div>
        <div class="gc-robe robe-judge"></div>
        <div class="judge-brush"></div>
      </div>
      <span class="gc-name">判 官</span>
      <span class="gc-fire"></span>
      <span class="gc-mist" v-for="m in 4" :key="'jm'+m"
        :style="{
          left: ((m*29+20)%100)+'%',
          top: ((m*45+10)%100)+'%',
          animationDelay: (m*0.55)+'s',
          width: (22+m*6)+'px', height: (28+m*8)+'px'
        }"></span>
    </div>

    <!-- 小鬼 — 右下 -->
    <div class="ghost-char char-imp" ref="charEls" data-idx="3">
      <div class="gc-body" ref="bodyEls">
        <div class="gc-head imp-head">
          <div class="imp-horns">
            <span class="imp-horn imp-hl"></span>
            <span class="imp-horn imp-hr"></span>
          </div>
          <div class="gc-eyes">
            <span class="gc-pupil imp-pupil" ref="pupilEls"></span>
            <span class="gc-pupil imp-pupil" ref="pupilEls"></span>
          </div>
          <div class="imp-grin"></div>
        </div>
        <div class="gc-robe robe-imp"></div>
      </div>
      <span class="gc-name">小 鬼</span>
      <span class="gc-fire"></span>
      <span class="gc-mist" v-for="m in 4" :key="'im'+m"
        :style="{
          left: ((m*31+15)%100)+'%',
          top: ((m*42+10)%100)+'%',
          animationDelay: (m*0.45)+'s',
          width: (16+m*6)+'px', height: (20+m*8)+'px'
        }"></span>
    </div>

    <!-- ====== 登录卡片 ====== -->
    <div class="card-layer">
      <div class="card-aura"></div>
      <div class="login-card">
        <span class="c-corner cc-tl"></span>
        <span class="c-corner cc-tr"></span>
        <span class="c-corner cc-bl"></span>
        <span class="c-corner cc-br"></span>

        <div class="card-inner">
          <!-- 官印 -->
          <div class="jade-seal">
            <div class="seal-sq">
              <span class="seal-char">地</span>
              <span class="seal-char">府</span>
              <span class="seal-char">司</span>
              <span class="seal-char">印</span>
              <div class="seal-sweep"></div>
            </div>
          </div>

          <h1 class="main-title"><span class="gold-txt">地 府 生 死 簿</span></h1>
          <div class="subtitle">
            <span class="sub-l"></span>
            <span class="sub-t">阴 司 重 地</span>
            <span class="sub-l"></span>
          </div>

          <el-form ref="formRef" :model="form" :rules="rules" class="login-form">
            <div class="fld-wrap">
              <span class="fld-icon fld-icon-name"></span>
              <div class="fld-input-zone">
                <el-input v-model="form.username" placeholder="判官提笔 · 录入名讳" class="ancient-inp"
                  @focus="focused='name'" @blur="focused=null" />
                <span class="fld-glow" v-show="focused==='name'||form.username"></span>
                <span class="fld-rune" :class="{ 'rune-on': focused === 'name' }"></span>
              </div>
            </div>

            <div class="fld-wrap">
              <span class="fld-icon fld-icon-lock"></span>
              <div class="fld-input-zone">
                <el-input v-model="form.password" type="password" placeholder="阎王御印 · 密令通行" class="ancient-inp"
                  show-password @focus="focused='pass'" @blur="focused=null" @keyup.enter="doLogin" />
                <span class="fld-glow" v-show="focused==='pass'||form.password"></span>
                <span class="fld-rune" :class="{ 'rune-on': focused === 'pass' }"></span>
              </div>
            </div>

            <button type="button" class="enter-btn" :class="{ ripple: clicked }" :disabled="loading" @click="doLogin">
              <span class="btn-fl"></span>
              <span class="btn-fr"></span>
              <span class="btn-txt">踏 入 地 府</span>
            </button>
          </el-form>

          <p class="card-foot">十殿阎罗 · 森严戒备 · 生人勿入</p>
        </div>
      </div>
    </div>

    <!-- 左下竖排篆文 -->
    <div class="vert-seal">
      <span v-for="c in '陰司監錄'" :key="c" class="vs-ch">{{ c }}</span>
      <span class="vs-dot">·</span>
      <span v-for="c in '生死有命'" :key="c" class="vs-ch">{{ c }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)
const focused = ref(null)
const clicked = ref(false)

const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '留下你的名号', trigger: 'blur' }],
  password: [{ required: true, message: '需验证御印方可通行', trigger: 'blur' }]
}

// —————— 鼠标跟踪 ——————
let target = { x: -999, y: -999 }
let smooth = { x: -999, y: -999 }
let raf = null

function onMouse(e) {
  target.x = e.clientX
  target.y = e.clientY
}
function onLeave() {
  target.x = -999
  target.y = -999
}

function track() {
  // 更灵敏的平滑插值
  if (target.x < 0) {
    smooth.x += (-999 - smooth.x) * 0.04
    smooth.y += (-999 - smooth.y) * 0.04
  } else {
    smooth.x += (target.x - smooth.x) * 0.2
    smooth.y += (target.y - smooth.y) * 0.2
  }

  const isAway = target.x < 0
  const w = window.innerWidth
  const h = window.innerHeight
  const centerX = w * 0.45
  const centerY = h * 0.45
  const mx = isAway ? centerX : smooth.x
  const my = isAway ? centerY : smooth.y

  // 更新每个鬼怪
  document.querySelectorAll('.ghost-char').forEach((charEl, i) => {
    const bodyEl = charEl.querySelector('.gc-body')
    if (!bodyEl) return
    const rect = charEl.getBoundingClientRect()
    const cx = rect.left + rect.width / 2
    const cy = rect.top + rect.height * 0.35

    const dx = mx - cx
    const dy = my - cy
    const dist = Math.sqrt(dx * dx + dy * dy)
    const angle = Math.atan2(dy, dx) * (180 / Math.PI)

    // 增大转动幅度
    const maxAngle = 50
    const clamped = Math.max(-maxAngle, Math.min(maxAngle, angle))
    const influence = Math.min(dist / 250, 1)

    bodyEl.style.transform = `rotate(${clamped * influence}deg)`
    // 身体轻微位移，增强灵敏感
    const shiftX = (dx / (dist || 1)) * influence * 4
    const shiftY = (dy / (dist || 1)) * influence * 2
    bodyEl.style.transform += ` translate(${shiftX}px, ${shiftY}px)`

    // 瞳孔偏移 — 更大范围
    const pupils = charEl.querySelectorAll('.gc-pupil')
    const px = (dx / (dist || 1)) * 5 * influence
    const py = (dy / (dist || 1)) * 5 * influence
    pupils.forEach(p => {
      p.style.transform = `translate(${px}px, ${py}px)`
    })
  })

  // 更新竖排文字
  const vs = document.querySelector('.vert-seal')
  if (vs) {
    const dx2 = mx - w * 0.08
    const dy2 = my - h * 0.7
    const angle2 = Math.atan2(dy2, dx2) * (180 / Math.PI)
    vs.style.transform = `rotate(${angle2 * 0.03}deg) translate(${dx2 * 0.008}px, ${dy2 * 0.008}px)`
  }

  raf = requestAnimationFrame(track)
}

async function doLogin() {
  clicked.value = true
  setTimeout(() => { clicked.value = false }, 600)

  const ok = await formRef.value.validate().catch(() => false)
  if (!ok) { clicked.value = false; return }
  loading.value = true
  try {
    await userStore.login(form)
    ElMessage.success('魂归来兮 · 登录成功')
    router.push('/dashboard')
  } catch { loading.value = false; clicked.value = false }
}

onMounted(() => { raf = requestAnimationFrame(track) })
onUnmounted(() => { if (raf) cancelAnimationFrame(raf) })
</script>

<style scoped>
/* ============================== 场景基底 ============================== */
.login-scene {
  height: 100vh;
  background: linear-gradient(180deg, #06040a 0%, #0a070f 30%, #0d0a12 60%, #0a0710 100%);
  font-family: 'Noto Serif SC', 'STSong', 'SimSun', serif;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: default;
}

/* ============================== 血月 ============================== */
.blood-moon {
  position: absolute;
  top: 6%; left: 60%;
  width: 180px; height: 180px;
  background: radial-gradient(circle at 45% 40%,
    rgba(180,40,30,0.45) 0%,
    rgba(120,20,15,0.25) 30%,
    rgba(80,10,5,0.1) 55%,
    transparent 70%);
  border-radius: 50%;
  filter: blur(4px);
  animation: moonBreathe 5s ease-in-out infinite;
  pointer-events: none;
}
/* 月晕 */
.blood-moon::after {
  content: '';
  position: absolute;
  inset: -40px;
  background: radial-gradient(circle at 45% 40%, rgba(180,40,30,0.15) 0%, transparent 60%);
  border-radius: 50%;
}
@keyframes moonBreathe {
  0%,100% { opacity: 0.7; transform: scale(1); }
  50% { opacity: 1; transform: scale(1.06); }
}

/* ============================== 远山剪影 ============================== */
.mountains { position: absolute; inset: 0; pointer-events: none; }
.mt {
  position: absolute; bottom: 0;
  width: 100%; height: 0;
  border-bottom-style: solid;
  opacity: 0.6;
}
.mt-1 {
  left: -10%; bottom: 20%;
  border-left: 200px solid transparent;
  border-right: 300px solid transparent;
  border-bottom: 160px solid rgba(8,5,12,0.85);
  width: 0; height: 0;
}
.mt-2 {
  left: 40%; bottom: 18%;
  border-left: 180px solid transparent;
  border-right: 100px solid transparent;
  border-bottom: 200px solid rgba(6,4,10,0.88);
  width: 0; height: 0;
}
.mt-3 {
  left: -5%; bottom: 15%;
  border-left: 250px solid transparent;
  border-right: 350px solid transparent;
  border-bottom: 120px solid rgba(10,6,14,0.8);
  width: 0; height: 0;
}
.mt-4 {
  left: 60%; bottom: 22%;
  border-left: 300px solid transparent;
  border-right: 150px solid transparent;
  border-bottom: 140px solid rgba(7,4,11,0.83);
  width: 0; height: 0;
}

/* ============================== 地面雾气 ============================== */
.ground-fog {
  position: absolute;
  bottom: 0; left: 0; right: 0;
  height: 30%;
  background: linear-gradient(0deg,
    rgba(20,14,22,0.5) 0%,
    rgba(15,10,18,0.25) 40%,
    transparent 100%);
  pointer-events: none;
  animation: fogDrift 8s ease-in-out infinite;
}
.fog-2 {
  bottom: 5%;
  height: 20%;
  opacity: 0.5;
  animation: fogDrift 10s ease-in-out infinite reverse;
}
@keyframes fogDrift {
  0%,100% { transform: translateX(0); }
  50% { transform: translateX(2%); }
}

/* ============================== 浮灰粒子 ============================== */
.ember {
  position: absolute;
  bottom: -10px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(200,150,60,0.4), rgba(160,30,15,0.2));
  pointer-events: none;
  z-index: 1;
  animation: emberFloat linear infinite;
}
@keyframes emberFloat {
  0% { transform: translateY(0) translateX(0) scale(0); opacity: 0; }
  10% { opacity: 0.5; }
  50% { opacity: 0.3; transform: translateY(-50vh) translateX(15px) scale(1); }
  100% { transform: translateY(-100vh) translateX(-10px) scale(0); opacity: 0; }
}

/* ============================== 鬼怪基础 ============================== */
.ghost-char {
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  z-index: 10;
  pointer-events: none;
}
/* 四个鬼怪的位置 — 环绕中心卡片 */
.char-hei  { left: 6%;  top: 10%; }
.char-bai  { right: 6%; top: 12%; }
.char-judge{ left: 8%; top: 58%; }
.char-imp  { right: 8%; bottom: 20%; }

.gc-body {
  display: flex;
  flex-direction: column;
  align-items: center;
  transform-origin: center 70%;
  transition: transform 0.06s ease-out;
}

/* 头部 */
.gc-head {
  position: relative;
  width: 52px; height: 52px;
  border-radius: 50%;
  margin-bottom: 1px;
  z-index: 3;
}
.gc-eyes {
  position: absolute;
  top: 53%; left: 50%;
  transform: translateX(-50%);
  display: flex; gap: 14px;
}
.gc-pupil {
  display: block;
  width: 8px; height: 8px;
  border-radius: 50%;
  transition: transform 0.04s ease-out;
}
.gold-pupil {
  background: #ffd700;
  box-shadow: 0 0 7px rgba(255,215,0,0.6), 0 0 14px rgba(255,180,20,0.3);
}
.dark-pupil {
  background: #0a0a0f;
  box-shadow: 0 0 4px rgba(0,0,0,0.8);
}
.imp-pupil {
  background: #ff4444;
  box-shadow: 0 0 7px rgba(255,68,68,0.6), 0 0 14px rgba(255,30,30,0.3);
}

/* 身体 */
.gc-robe {
  position: relative;
  width: 42px; height: 52px;
  border-radius: 50% 50% 8px 8px;
}
.robe-belt {
  position: absolute;
  top: 10px; left: -2px; right: -2px;
  height: 4px; background: #5a3a30;
  border-radius: 1px;
}

/* 鬼火 */
.gc-fire {
  position: absolute;
  bottom: -6px;
  width: 36px; height: 10px;
  background: radial-gradient(ellipse at center, rgba(160,25,15,0.3) 0%, transparent 70%);
  border-radius: 50%;
  filter: blur(6px);
  animation: fireFlick 2.2s ease-in-out infinite;
}
@keyframes fireFlick {
  0%,100% { opacity: 0.35; transform: scaleX(1); }
  40% { opacity: 0.75; transform: scaleX(1.3); }
  70% { opacity: 0.2; transform: scaleX(0.75); }
}

/* 黑雾 */
.gc-mist {
  position: absolute;
  border-radius: 50%;
  background: radial-gradient(ellipse, rgba(20,14,10,0.45) 0%, transparent 70%);
  filter: blur(12px);
  pointer-events: none;
  z-index: -1;
  animation: mistWisp 4s ease-in-out infinite;
}
@keyframes mistWisp {
  0%,100% { transform: translateY(0) scale(1); opacity: 0.3; }
  50% { transform: translateY(-10px) scale(1.2); opacity: 0.5; }
}

/* 名签 */
.gc-name {
  font-size: 11px; color: rgba(170,130,70,0.45);
  letter-spacing: 2px; margin-top: 8px;
  font-family: 'ZCOOL XiaoWei', 'STKaiti', serif;
}

/* ===== 黑无常 ===== */
.hei-head {
  background: radial-gradient(circle at 45% 38%, #3a3035, #1a1015);
  border: 2px solid #2a2025;
}
.hei-hat {
  position: absolute;
  top: -34px; left: 50%; transform: translateX(-50%);
  width: 0; height: 0;
  border-left: 17px solid transparent;
  border-right: 17px solid transparent;
  border-bottom: 36px solid #1a1018;
  filter: drop-shadow(0 2px 4px rgba(0,0,0,0.6));
}
.hei-hat::after {
  content: '';
  position: absolute; top: -8px; left: -4px;
  width: 42px; height: 6px;
  background: #1a1018; border-radius: 2px;
}
.hei-mouth {
  position: absolute;
  bottom: 12px; left: 50%; transform: translateX(-50%);
  width: 16px; height: 3px;
  background: #4a3040; border-radius: 1px;
}
.robe-hei {
  background: linear-gradient(180deg, #1c1618, #0f0b10);
  border: 1px solid #2a2028;
}
.hei-chain {
  position: absolute;
  right: -24px; top: 65%;
  width: 32px; height: 3px;
  background: repeating-linear-gradient(90deg, #6a5a4a 0px, #6a5a4a 6px, #1a1510 6px, #1a1510 10px);
  border-radius: 1px;
  transform: rotate(-18deg);
}

/* ===== 白无常 ===== */
.bai-head {
  background: radial-gradient(circle at 45% 38%, #ede5da, #c8beb0);
  border: 2px solid #a09890;
}
.bai-hat {
  position: absolute;
  top: -34px; left: 50%; transform: translateX(-50%);
  width: 0; height: 0;
  border-left: 17px solid transparent;
  border-right: 17px solid transparent;
  border-bottom: 36px solid #ede5da;
  filter: drop-shadow(0 2px 6px rgba(0,0,0,0.35));
}
.bai-hat::after {
  content: '';
  position: absolute; top: -8px; left: -4px;
  width: 42px; height: 6px;
  background: #ede5da; border-radius: 2px;
}
.bai-mouth {
  position: absolute;
  bottom: 10px; left: 50%; transform: translateX(-50%);
  width: 20px; height: 5px;
  background: #6a5a5a; border-radius: 2px;
}
.robe-bai {
  background: linear-gradient(180deg, #ebe4dc, #ccc4b8);
  border: 1px solid #b0a8a0;
}
.bai-fan {
  position: absolute;
  right: -20px; top: 58%;
  width: 24px; height: 30px;
  background: #f2ead8;
  border-radius: 50% 0 0 50%;
  border: 1px solid #c8b898;
  transform: rotate(12deg);
}

/* ===== 判官 ===== */
.judge-head {
  background: radial-gradient(circle at 45% 35%, #c48060, #8a4030);
  border: 2px solid #6a3028;
}
.judge-hat {
  position: absolute;
  top: -26px; left: 50%; transform: translateX(-50%);
  width: 46px; height: 20px;
  background: linear-gradient(180deg, #1a1018, #2a1820);
  border-radius: 3px 3px 0 0;
}
.judge-hat::after {
  content: '';
  position: absolute;
  left: -12px; right: -12px; top: 5px;
  height: 2px; background: #3a2a30;
}
.judge-beard {
  position: absolute;
  bottom: 2px; left: 50%; transform: translateX(-50%);
  width: 0; height: 0;
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
  border-top: 14px solid #2a1a18;
}
.robe-judge {
  background: linear-gradient(180deg, #6e1c1a, #3e0e0c);
  border: 1px solid #501212;
}
.judge-brush {
  position: absolute;
  right: -16px; top: 52%;
  width: 3px; height: 34px;
  background: linear-gradient(180deg, #3a2010, #1a0a08, #d4b878);
  border-radius: 1px;
}

/* ===== 小鬼 ===== */
.imp-head {
  background: radial-gradient(circle at 45% 35%, #5a7a5a, #2a4a2a);
  border: 2px solid #1a3a1a;
}
.imp-horns {
  position: absolute;
  top: -16px; left: 50%; transform: translateX(-50%);
  display: flex; gap: 12px;
}
.imp-horn {
  width: 0; height: 0;
  border-left: 5px solid transparent;
  border-right: 5px solid transparent;
  border-bottom: 15px solid #3a3a2a;
}
.imp-hl { transform: rotate(-12deg); }
.imp-hr { transform: rotate(12deg); }
.imp-grin {
  position: absolute;
  bottom: 8px; left: 50%; transform: translateX(-50%);
  width: 14px; height: 8px;
  background: #3a5a3a; border-radius: 0 0 50% 50%;
}
.imp-grin::after {
  content: '';
  position: absolute; top: -3px; right: 0;
  width: 0; height: 0;
  border-left: 3px solid transparent;
  border-right: 3px solid transparent;
  border-bottom: 5px solid #4a3030;
}
.robe-imp {
  background: linear-gradient(180deg, #3c4c3c, #1c2c1c);
  border: 1px solid #2a3a2a;
}

/* ============================== 卡片层 ============================== */
.card-layer {
  position: relative; z-index: 20;
}
.card-aura {
  position: absolute;
  inset: -5px; border-radius: 8px;
  background: linear-gradient(135deg,
    rgba(120,10,6,0.5), rgba(200,30,15,0.08),
    rgba(70,6,3,0.4), rgba(170,24,10,0.12), rgba(120,10,6,0.5));
  background-size: 300% 300%;
  animation: auraBr 3.2s ease-in-out infinite;
  filter: blur(4px);
}
@keyframes auraBr {
  0%,100% { background-position: 0% 50%; opacity: 0.6; }
  50% { background-position: 100% 50%; opacity: 1; }
}

.login-card {
  position: relative;
  width: 400px;
  background: linear-gradient(180deg, rgba(16,12,8,0.94), rgba(8,5,3,0.97));
  border: 1px solid rgba(80,8,4,0.35);
  border-radius: 6px;
  backdrop-filter: blur(8px);
}

/* 四角 */
.c-corner { position: absolute; width: 18px; height: 18px; z-index: 2; }
.c-corner::before, .c-corner::after {
  content: ''; position: absolute;
  background: rgba(150,25,12,0.4);
}
.c-corner::before { width: 100%; height: 1px; }
.c-corner::after  { width: 1px; height: 100%; }
.cc-tl { top: 8px; left: 12px; } .cc-tr { top: 8px; right: 12px; }
.cc-bl { bottom: 8px; left: 12px; } .cc-br { bottom: 8px; right: 12px; }
.cc-tl::before,.cc-tl::after { top: 0; left: 0; }
.cc-tr::before,.cc-tr::after { top: 0; right: 0; }
.cc-bl::before,.cc-bl::after { bottom: 0; left: 0; }
.cc-br::before,.cc-br::after { bottom: 0; right: 0; }

.card-inner { padding: 36px 44px; }

/* 官印 */
.jade-seal { display: flex; justify-content: center; margin-bottom: 16px; }
.seal-sq {
  width: 62px; height: 62px;
  position: relative;
  background: linear-gradient(135deg, #780c0c, #540808, #841010, #680a0a);
  transform: rotate(4deg);
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  place-items: center;
  padding: 5px;
  box-shadow: 0 0 12px rgba(150,18,8,0.3), inset 0 0 8px rgba(0,0,0,0.4);
  animation: sealHit 0.45s ease 0.3s both, sealP 2.8s ease-in-out 1s infinite;
}
.seal-char {
  font-size: 15px; color: rgba(215,165,45,0.8);
  font-family: 'ZCOOL XiaoWei','STKaiti',serif;
  transform: rotate(-4deg);
  text-shadow: 0 0 4px rgba(200,140,25,0.35);
}
.seal-sweep {
  position: absolute; inset: 0;
  background: linear-gradient(180deg, rgba(255,215,100,0.2), transparent 45%);
  animation: sweep 2.2s ease-in-out infinite;
  pointer-events: none;
}
@keyframes sealHit {
  from { transform: rotate(4deg) scale(1.2); opacity: 0; }
  to { transform: rotate(4deg) scale(1); opacity: 1; }
}
@keyframes sealP {
  0%,100% { box-shadow: 0 0 12px rgba(150,18,8,0.3), inset 0 0 8px rgba(0,0,0,0.4); }
  50% { box-shadow: 0 0 20px rgba(180,22,12,0.45), inset 0 0 12px rgba(0,0,0,0.5); }
}
@keyframes sweep {
  0%,100% { transform: translateY(-30%); opacity: 0; }
  50% { transform: translateY(35%); opacity: 0.5; }
}

/* 标题 */
.main-title {
  text-align: center; margin: 0 0 4px;
  font-size: 26px; font-weight: 400;
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  letter-spacing: 6px;
}
.gold-txt {
  background: linear-gradient(100deg,
    #8a6830 0%, #c9a84c 20%, #f0d880 38%,
    #c9a84c 55%, #8a6830 74%, #d4b860 86%, #8a6830 100%);
  background-size: 200% 100%;
  -webkit-background-clip: text; background-clip: text;
  -webkit-text-fill-color: transparent; color: transparent;
  animation: goldF 3s ease-in-out infinite;
}
@keyframes goldF {
  0%,100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}
.subtitle {
  display: flex; align-items: center; justify-content: center;
  gap: 10px; margin-bottom: 26px;
}
.sub-l {
  width: 36px; height: 1px;
  background: linear-gradient(90deg, transparent, rgba(90,8,5,0.35), transparent);
}
.sub-t {
  font-size: 11px; color: rgba(155,95,55,0.55);
  letter-spacing: 8px; font-weight: 600;
}

/* 表单 */
.login-form { display: flex; flex-direction: column; gap: 14px; }

.fld-wrap { display: flex; align-items: stretch; }
.fld-icon {
  width: 38px; flex-shrink: 0;
  border: 1px solid rgba(70,8,5,0.2);
  border-right: none; border-radius: 2px 0 0 2px;
  background: rgba(14,9,6,0.5);
  display: flex; align-items: center; justify-content: center;
}
.fld-icon::after {
  font-size: 11px; color: rgba(150,110,65,0.35);
  font-family: 'ZCOOL XiaoWei','STKaiti',serif;
}
.fld-icon-name::after { content: '名'; }
.fld-icon-lock::after { content: '印'; }

.fld-input-zone { flex: 1; position: relative; overflow: hidden; }

.ancient-inp :deep(.el-input__wrapper) {
  background: rgba(14,9,6,0.5);
  border: 1px solid rgba(70,8,5,0.2);
  border-radius: 0 2px 2px 0;
  box-shadow: none; padding: 10px 12px;
  transition: all 0.3s;
}
.ancient-inp :deep(.el-input__inner) {
  color: #c0a888; font-family: 'Noto Serif SC',serif;
  font-size: 14px; letter-spacing: 1px;
}
.ancient-inp :deep(.el-input__inner::placeholder) {
  color: rgba(125,95,65,0.28); letter-spacing: 1px;
}
.ancient-inp :deep(.el-input__wrapper:hover) {
  border-color: rgba(120,14,6,0.4);
}
.ancient-inp :deep(.el-input__wrapper.is-focus) {
  border-color: rgba(195,135,45,0.4);
  box-shadow: 0 0 10px rgba(90,6,4,0.15), 0 0 2px rgba(195,130,25,0.08);
}
.ancient-inp :deep(.el-input__suffix) { color: rgba(125,95,65,0.25); }

/* 符文旋转 */
.fld-rune {
  position: absolute; inset: 2px;
  pointer-events: none; opacity: 0;
  border-radius: 0 2px 2px 0;
  background:
    radial-gradient(circle at 20% 50%, rgba(110,12,6,0.07) 0%, transparent 35%),
    radial-gradient(circle at 80% 50%, rgba(110,12,6,0.07) 0%, transparent 35%),
    repeating-linear-gradient(0deg, transparent, transparent 7px, rgba(90,8,4,0.025) 7px, rgba(90,8,4,0.025) 8px);
  transition: opacity 0.35s;
}
.rune-on { opacity: 1; animation: runeSpin 3.5s linear infinite; }
@keyframes runeSpin {
  to { transform: rotate(360deg); }
}

/* 底部光条 */
.fld-glow {
  position: absolute;
  bottom: 0; left: 3px; right: 3px;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(195,130,35,0.35), transparent);
  pointer-events: none;
}

/* 按钮 */
.enter-btn {
  width: 100%; height: 50px; margin-top: 6px;
  position: relative;
  border: none; border-radius: 2px;
  background: linear-gradient(180deg, #541012, #7e1516, #621012, #340808);
  color: #d0b068;
  cursor: pointer;
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  font-size: 18px; letter-spacing: 10px;
  overflow: hidden;
  transition: all 0.3s;
  box-shadow: 0 0 8px rgba(150,16,10,0.2), 0 0 20px rgba(70,6,3,0.06);
}
.enter-btn:hover {
  background: linear-gradient(180deg, #701216, #9c1c1c, #801416, #440a0a);
  box-shadow: 0 0 14px rgba(190,24,16,0.35), 0 0 32px rgba(90,6,4,0.15), 0 4px 14px rgba(0,0,0,0.5);
  transform: translateY(-1px);
}
.enter-btn:active { transform: scale(0.97); }
.enter-btn:disabled { opacity: 0.5; cursor: not-allowed; transform: none; }

.btn-fl, .btn-fr {
  position: absolute; bottom: 2px;
  width: 42%; height: 1px;
  pointer-events: none;
  animation: fireLn 1.8s ease-in-out infinite;
}
.btn-fl {
  left: 6%;
  background: linear-gradient(90deg, transparent, rgba(255,100,25,0.55), rgba(255,170,45,0.2));
  animation-delay: 0s;
}
.btn-fr {
  right: 6%;
  background: linear-gradient(270deg, transparent, rgba(255,100,25,0.55), rgba(255,170,45,0.2));
  animation-delay: 0.65s;
}
@keyframes fireLn {
  0%,100% { opacity: 0.2; }
  45% { opacity: 0.7; }
  70% { opacity: 0.12; }
}

.btn-txt {
  position: relative; z-index: 1;
  text-shadow: 0 0 5px rgba(195,135,40,0.3), 0 2px 3px rgba(0,0,0,0.6);
}

/* 点击波纹 */
.ripple .btn-txt {
  position: relative;
}
.ripple::after {
  content: '';
  position: absolute;
  inset: -6px;
  border-radius: 50%;
  border: 1px solid rgba(190,22,12,0.3);
  animation: ripOut 0.5s ease both;
  pointer-events: none;
}
@keyframes ripOut {
  0% { transform: scale(0); opacity: 1; }
  100% { transform: scale(1.7); opacity: 0; }
}

/* 底部文字 */
.card-foot {
  text-align: center; margin-top: 18px;
  color: rgba(95,25,18,0.28);
  font-size: 11px; letter-spacing: 3px;
  animation: footBreathe 3.5s ease-in-out infinite;
}
@keyframes footBreathe {
  0%,100% { opacity: 0.3; }
  50% { opacity: 0.55; }
}

/* ============================== 竖排篆文 ============================== */
.vert-seal {
  position: absolute;
  bottom: 12%; left: 3%;
  z-index: 15;
  display: flex;
  flex-direction: column;
  align-items: center; gap: 1px;
  transition: transform 0.08s ease-out;
  pointer-events: none;
}
.vs-ch {
  font-size: 15px;
  color: rgba(175,130,50,0.4);
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  text-shadow: 0 0 3px rgba(175,130,35,0.15);
}
.vs-dot { color: rgba(110,12,8,0.3); font-size: 9px; }

/* ============================== 响应式 ============================== */
@media (max-width: 768px) {
  .char-hei { left: 2%; top: 5%; }
  .char-bai { right: 2%; top: 8%; }
  .char-judge { left: 3%; top: 55%; }
  .char-imp { right: 3%; bottom: 15%; }
  .login-card { width: 340px; }
  .card-inner { padding: 28px 32px; }
  .vert-seal { display: none; }
}
</style>
