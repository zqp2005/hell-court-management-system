<template>
  <div class="login-scene" @mousemove="onMouse" @mouseleave="onLeave">
    <!-- 后处理层 -->
    <div class="post-vignette"></div>
    <div class="post-grain"></div>
    <div class="post-scanlines"></div>
    <div class="post-flicker"></div>

    <!-- ====== 背景场景 ====== -->
    <div class="scene-bg">
      <div class="blood-moon">
        <div class="moon-corona"></div>
        <span v-for="i in 12" :key="'ray'+i" class="moon-ray"
          :style="{ transform: 'rotate('+(i*30)+'deg)', animationDelay: (i*0.3)+'s' }"></span>
      </div>
      <div class="mountains">
        <span class="mt mt-1"></span><span class="mt mt-2"></span>
        <span class="mt mt-3"></span><span class="mt mt-4"></span>
      </div>
      <div class="ground-fog"></div><div class="ground-fog fog-2"></div><div class="ground-fog fog-3"></div>
      <span v-for="i in 50" :key="'e'+i" class="ember"
        :style="{ left: ((i*179+31)%96)+'%', animationDelay: (i*0.35)+'s', animationDuration: (5+(i*0.4)%7)+'s', width: (1.5+(i%3)*1.2)+'px', height: (1.5+(i%3)*1.2)+'px', opacity: 0.15+(i%4)*0.1 }"></span>
      <span v-for="i in 12" :key="'w'+i" class="wisp"
        :style="{ left: (3+i*8)+'%', bottom: (3+i*5)+'%', animationDelay: (i*1.2)+'s', animationDuration: (2.5+i*0.6)+'s' }"></span>
      <!-- 幽魂粒子 -->
      <span v-for="i in 20" :key="'soul'+i" class="soul-particle"
        :style="{ left: ((i*137+23)%90+5)+'%', animationDelay: (i*0.8)+'s', animationDuration: (8+(i*1.3)%6)+'s' }"></span>
    </div>

    <!-- ====== 横飞鬼怪 ====== -->
    <div class="flying-ghosts">
      <div v-for="i in 15" :key="'fg'+i" class="fly-ghost"
        :class="'fg-'+((i%5)+1)"
        :style="{
          animationDuration: (6 + (i*1.7)%8) + 's',
          animationDelay: (i * 0.9) + 's',
          top: ((i*67+13)%80) + '%',
          opacity: 0.15 + (i%4)*0.1,
          transform: 'scale(' + (0.4 + (i%5)*0.2) + ')'
        }">
        <div class="fg-body"></div>
        <div class="fg-eyes">
          <span class="fg-eye"></span><span class="fg-eye"></span>
        </div>
      </div>
    </div>

    <!-- ====== 环境恐怖 ====== -->
    <div class="horror-hands">
      <div class="creep-hand hand-left"></div>
      <div class="creep-hand hand-right"></div>
      <div class="creep-hand hand-bottom"></div>
    </div>
    <div class="horror-face"></div>

    <!-- 点击印记容器 -->
    <div class="click-stamps" ref="stampsRef"></div>

    <!-- ====== 石书登录卡片 ====== -->
    <div class="book-layer">
      <div class="book-shadow"></div>
      <div class="stone-book">
        <div class="book-spine">
          <span class="spine-rivet" v-for="i in 3" :key="'rv'+i"></span>
        </div>

        <!-- 左侧石页 -->
        <div class="stone-page page-left">
          <div class="page-blood-stain bs-1"></div>
          <div class="page-blood-stain bs-2"></div>
          <div class="page-crack cr-1"></div>
          <div class="page-crack cr-2"></div>
          <div class="page-texture"></div>
          <div class="blood-drip-container">
            <span v-for="i in 3" :key="'drip'+i" class="blood-drip" :style="{ left: (20+i*25)+'%', animationDelay: (i*1.8)+'s', animationDuration: (2.5+i*0.5)+'s' }"></span>
          </div>
          <div class="left-inscription">
            <span class="insc-char" v-for="(c,i) in '陰陽生死'" :key="i" :style="{ transform: 'rotate('+[-6,8,-10,5][i]+'deg)', animationDelay: i*0.15+'s' }">{{ c }}</span>
          </div>
          <div class="left-inscription insc-2">
            <span class="insc-char insc-big" :style="{ transform: 'rotate(-4deg)', animationDelay: '0.8s' }">簿</span>
          </div>
          <div class="blood-handprint"></div>
        </div>

        <!-- 右侧石页 — 登录表单 -->
        <div class="stone-page page-right">
          <div class="page-blood-stain bs-3"></div>
          <div class="page-crack cr-3"></div>
          <div class="page-texture"></div>

          <div class="page-content">
            <div class="page-header-area">
              <div class="seal-mark"></div>
              <h1 class="book-title">
                <span class="bt-char" v-for="(c,i) in '地府生死簿'" :key="i" :style="{ animationDelay: i*0.1+'s' }">{{ c }}</span>
              </h1>
              <div class="book-sub">—— 冥 契 石 书 ——</div>
            </div>

            <el-form ref="formRef" :model="form" :rules="rules" class="book-form">
              <div class="stone-field">
                <span class="sf-rune">諱</span>
                <div class="sf-input">
                  <el-input v-model="form.username" placeholder="录入名讳 · 判官提笔"
                    @focus="focused='name'" @blur="focused=null" />
                  <span class="sf-carve" v-show="focused==='name'||form.username"></span>
                </div>
              </div>
              <div class="stone-field">
                <span class="sf-rune">印</span>
                <div class="sf-input">
                  <el-input v-model="form.password" type="password" placeholder="密令通行 · 阎王御印"
                    show-password @focus="focused='pass'" @blur="focused=null" @keyup.enter="doLogin" />
                  <span class="sf-carve" v-show="focused==='pass'||form.password"></span>
                </div>
              </div>
              <button type="button" class="blood-seal-btn" :class="{ pressed: clicked }" :disabled="loading" @click="doLogin">
                <span class="bsb-text">踏 入 地 府</span>
                <span class="bsb-blood"></span>
                <span class="bsb-pulse"></span>
              </button>
            </el-form>
            <p class="page-foot">十 殿 阎 罗 · 森 严 戒 备 · 生 人 勿 入</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 左下竖排 -->
    <div class="vert-seal">
      <span v-for="c in '陰司監錄'" :key="c" class="vs-ch">{{ c }}</span>
      <span class="vs-dot">·</span>
      <span v-for="c in '生死有命'" :key="c" class="vs-ch">{{ c }}</span>
    </div>

    <!-- 鬼笔光标 -->
    <div class="ghost-cursor" ref="ghostCursorRef" :class="{ clicking: isClicking }">
      <div class="gc-ghost-body">
        <div class="gc-ghost-head">
          <span class="gc-ghost-eye"></span>
          <span class="gc-ghost-eye"></span>
        </div>
        <div class="gc-ghost-tail">
          <span></span><span></span><span></span>
        </div>
      </div>
      <div class="gc-brush">
        <div class="gc-brush-handle"></div>
        <div class="gc-brush-tip"></div>
      </div>
    </div>
    <canvas ref="inkCanvasRef" class="ink-trail-canvas"></canvas>
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
const ghostCursorRef = ref(null)
const inkCanvasRef = ref(null)
const stampsRef = ref(null)
const isClicking = ref(false)

const form = reactive({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '留下你的名号', trigger: 'blur' }],
  password: [{ required: true, message: '需验证御印方可通行', trigger: 'blur' }]
}

let target = { x: -999, y: -999 }
let smooth = { x: -999, y: -999 }
let cursorPos = { x: -999, y: -999 }
let raf = null
let flickerTimer = null
let faceTimer = null
let inkCtx = null
let inkTrail = []
const INK_MAX = 30

function onMouse(e) { target.x = e.clientX; target.y = e.clientY }
function onLeave() { target.x = -999; target.y = -999 }

function onSceneClick(e) {
  isClicking.value = true
  setTimeout(() => { isClicking.value = false }, 300)
  // 生成点击印记
  if (stampsRef.value) {
    const stamp = document.createElement('div')
    stamp.className = 'click-stamp'
    const chars = '封印鬼魂魄煞冥判'
    stamp.textContent = chars[Math.floor(Math.random() * chars.length)]
    stamp.style.left = e.clientX + 'px'
    stamp.style.top = e.clientY + 'px'
    stampsRef.value.appendChild(stamp)
    setTimeout(() => stamp.remove(), 1200)
  }
}

function triggerFlicker() {
  const el = document.querySelector('.post-flicker')
  if (el) {
    el.classList.add('flash')
    setTimeout(() => el.classList.remove('flash'), 150)
  }
  flickerTimer = setTimeout(triggerFlicker, 4000 + Math.random() * 8000)
}

function triggerFaceFlash() {
  const face = document.querySelector('.horror-face')
  if (face) {
    face.classList.add('visible')
    setTimeout(() => face.classList.remove('visible'), 200 + Math.random() * 300)
  }
  faceTimer = setTimeout(triggerFaceFlash, 8000 + Math.random() * 15000)
}
/* SCRIPT_CONTINUE */

function drawInk() {
  if (!inkCtx) return
  inkCtx.clearRect(0, 0, inkCtx.canvas.width, inkCtx.canvas.height)
  for (let i = 0; i < inkTrail.length; i++) {
    const p = inkTrail[i]
    const alpha = (1 - i / inkTrail.length) * 0.4 * p.life
    inkCtx.beginPath()
    inkCtx.arc(p.x, p.y, 1.5 * p.life, 0, Math.PI * 2)
    inkCtx.fillStyle = `rgba(80, 20, 15, ${alpha})`
    inkCtx.fill()
    p.life -= 0.02
  }
  inkTrail = inkTrail.filter(p => p.life > 0)
}

function track() {
  if (target.x < 0) {
    smooth.x += (-999 - smooth.x) * 0.04
    smooth.y += (-999 - smooth.y) * 0.04
  } else {
    smooth.x += (target.x - smooth.x) * 0.15
    smooth.y += (target.y - smooth.y) * 0.15
  }
  const w = window.innerWidth, h = window.innerHeight
  const mx = target.x < 0 ? w * 0.45 : smooth.x
  const my = target.x < 0 ? h * 0.45 : smooth.y

  // 鬼笔光标跟随（比鼠标慢一点，飘忽感）
  cursorPos.x = target.x
  cursorPos.y = target.y
  if (ghostCursorRef.value && target.x > 0) {
    ghostCursorRef.value.style.transform = `translate(${cursorPos.x - 20}px, ${cursorPos.y - 45}px)`
    ghostCursorRef.value.style.opacity = '1'
    // 墨迹拖尾
    const dx = target.x - cursorPos.x
    const dy = target.y - cursorPos.y
    const speed = Math.sqrt(dx * dx + dy * dy)
    if (speed > 2 && inkTrail.length < INK_MAX) {
      inkTrail.push({ x: cursorPos.x, y: cursorPos.y + 35, life: 1 })
    }
  } else if (ghostCursorRef.value) {
    ghostCursorRef.value.style.opacity = '0'
  }
  drawInk()
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

onMounted(() => {
  raf = requestAnimationFrame(track)
  flickerTimer = setTimeout(triggerFlicker, 3000 + Math.random() * 5000)
  faceTimer = setTimeout(triggerFaceFlash, 6000 + Math.random() * 10000)
  // 初始化墨迹 canvas
  const canvas = inkCanvasRef.value
  if (canvas) {
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight
    inkCtx = canvas.getContext('2d')
  }
  // 点击监听
  document.querySelector('.login-scene')?.addEventListener('mousedown', onSceneClick)
})
onUnmounted(() => {
  if (raf) cancelAnimationFrame(raf)
  if (flickerTimer) clearTimeout(flickerTimer)
  if (faceTimer) clearTimeout(faceTimer)
  document.querySelector('.login-scene')?.removeEventListener('mousedown', onSceneClick)
})
</script>

<style scoped>
/* ============================== 后处理效果 ============================== */
.post-vignette {
  position: fixed; inset: 0; z-index: 100; pointer-events: none;
  background: radial-gradient(ellipse at 50% 50%, transparent 40%, rgba(0,0,0,0.4) 70%, rgba(0,0,0,0.85) 100%);
  animation: vignettePulse 4s ease-in-out infinite;
}
@keyframes vignettePulse {
  0%,100% { opacity: 1; }
  50% { opacity: 0.75; }
}
.post-grain {
  position: fixed; inset: 0; z-index: 99; pointer-events: none; opacity: 0.04;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.85' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)'/%3E%3C/svg%3E");
  animation: grainShift 0.3s steps(3) infinite;
}
@keyframes grainShift {
  0% { transform: translate(0,0); } 33% { transform: translate(-2px,1px); }
  66% { transform: translate(1px,-1px); } 100% { transform: translate(0,0); }
}
.post-scanlines {
  position: fixed; inset: 0; z-index: 98; pointer-events: none; opacity: 0.03;
  background: repeating-linear-gradient(0deg, transparent, transparent 2px, rgba(0,0,0,0.3) 2px, rgba(0,0,0,0.3) 4px);
}
.post-flicker {
  position: fixed; inset: 0; z-index: 101; pointer-events: none;
  background: rgba(180,20,10,0.0); transition: background 0.05s;
}
.post-flicker.flash {
  background: rgba(180,20,10,0.04);
}

/* ============================== 场景 ============================== */
.login-scene {
  height: 100vh;
  background: url('../../assets/login-bg.jpg') center center / cover no-repeat;
  font-family: 'Noto Serif SC','STSong','SimSun',serif;
  position: relative; overflow: hidden;
  display: flex; align-items: center; justify-content: center;
  cursor: default;
  animation: sceneBreath 6s ease-in-out infinite;
}
@keyframes sceneBreath {
  0%,100% { filter: brightness(1); }
  50% { filter: brightness(0.88); }
}
/* STYLE_CONTINUE_1 */

/* ===== 血月 — 隐藏（背景图自带） ===== */
.blood-moon { display: none; }
.moon-corona { display: none; }
.moon-ray { display: none; }

/* ===== 远山 — 隐藏（背景图自带） ===== */
.mountains { display: none; }

/* ===== 雾气 — 叠加在背景图上 ===== */
.ground-fog { position: absolute; bottom: 0; left: 0; right: 0; height: 45%; background: linear-gradient(0deg, rgba(5,2,8,0.75) 0%, rgba(10,6,13,0.35) 50%, transparent 100%); pointer-events: none; animation: fogDrift 8s ease-in-out infinite; }
.fog-2 { bottom: 5%; height: 30%; opacity: 0.5; animation: fogDrift 11s ease-in-out infinite reverse; }
.fog-3 { bottom: 0; height: 18%; opacity: 0.7; animation: fogDrift 6s ease-in-out infinite; background: linear-gradient(0deg, rgba(8,3,5,0.8) 0%, transparent 100%); }
@keyframes fogDrift { 0%,100% { transform: translateX(0); } 50% { transform: translateX(3%); } }

.ember { position: absolute; bottom: -10px; border-radius: 50%; background: radial-gradient(circle, rgba(200,140,45,0.35), rgba(150,25,10,0.15)); pointer-events: none; z-index: 1; animation: emberFloat linear infinite; }
@keyframes emberFloat {
  0% { transform: translateY(0) translateX(0) scale(0); opacity: 0; }
  10% { opacity: 0.5; } 50% { opacity: 0.3; transform: translateY(-50vh) translateX(15px) scale(1); }
  100% { transform: translateY(-100vh) translateX(-10px) scale(0); opacity: 0; }
}
/* STYLE_CONTINUE_2 */

.wisp { position: absolute; width: 12px; height: 12px; border-radius: 50%; background: radial-gradient(circle, rgba(70,180,110,0.55), rgba(40,130,70,0.2), transparent); box-shadow: 0 0 18px rgba(60,160,90,0.4), 0 0 40px rgba(40,110,60,0.2); pointer-events: none; z-index: 2; animation: wispDrift 3.5s ease-in-out infinite; }
@keyframes wispDrift { 0%,100% { transform: translateY(0) translateX(0); opacity: 0.2; } 25% { transform: translateY(-28px) translateX(12px); opacity: 0.7; } 50% { transform: translateY(-12px) translateX(-8px); opacity: 0.15; } 75% { transform: translateY(-38px) translateX(-16px); opacity: 0.6; } }

/* 幽魂粒子 */
.soul-particle {
  position: absolute; bottom: 10%; width: 6px; height: 18px;
  background: linear-gradient(180deg, rgba(180,200,220,0.15), rgba(120,140,160,0.05), transparent);
  border-radius: 50% 50% 0 0; pointer-events: none; z-index: 2;
  filter: blur(1px); animation: soulRise linear infinite;
}
@keyframes soulRise {
  0% { transform: translateY(0) translateX(0) scaleX(1); opacity: 0; }
  10% { opacity: 0.4; }
  40% { transform: translateY(-35vh) translateX(20px) scaleX(0.7); opacity: 0.25; }
  70% { transform: translateY(-60vh) translateX(-15px) scaleX(1.2); opacity: 0.15; }
  100% { transform: translateY(-90vh) translateX(5px) scaleX(0.5); opacity: 0; }
}

/* ============================== 横飞鬼怪 ============================== */
.flying-ghosts {
  position: fixed; inset: 0; pointer-events: none; z-index: 8; overflow: hidden;
}
.fly-ghost {
  position: absolute; left: -80px;
  width: 60px; height: 70px;
  animation: flyAcross linear infinite;
}
.fg-1 { animation-name: flyAcross1; }
.fg-2 { animation-name: flyAcross2; }
.fg-3 { animation-name: flyAcross3; }
.fg-4 { animation-name: flyAcross4; }
.fg-5 { animation-name: flyAcross5; }

@keyframes flyAcross1 {
  0% { left: -80px; transform: scale(0.5) translateY(0); opacity: 0; }
  5% { opacity: 0.3; }
  50% { transform: scale(0.6) translateY(-30px); opacity: 0.25; }
  95% { opacity: 0.3; }
  100% { left: 110%; transform: scale(0.5) translateY(10px); opacity: 0; }
}
@keyframes flyAcross2 {
  0% { left: 110%; transform: scale(0.7) translateY(0); opacity: 0; }
  5% { opacity: 0.25; }
  40% { transform: scale(0.8) translateY(40px); opacity: 0.3; }
  95% { opacity: 0.2; }
  100% { left: -80px; transform: scale(0.7) translateY(-20px); opacity: 0; }
}
@keyframes flyAcross3 {
  0% { left: -80px; transform: scale(0.4) translateY(20px) rotate(-5deg); opacity: 0; }
  10% { opacity: 0.35; }
  60% { transform: scale(0.5) translateY(-50px) rotate(5deg); opacity: 0.2; }
  100% { left: 110%; transform: scale(0.4) translateY(30px) rotate(-3deg); opacity: 0; }
}
@keyframes flyAcross4 {
  0% { left: 110%; transform: scale(0.9) translateY(-10px); opacity: 0; }
  8% { opacity: 0.2; }
  50% { transform: scale(1) translateY(20px); opacity: 0.15; }
  100% { left: -80px; transform: scale(0.9) translateY(-30px); opacity: 0; }
}
@keyframes flyAcross5 {
  0% { left: -80px; transform: scale(0.6) translateY(0) rotate(3deg); opacity: 0; }
  5% { opacity: 0.3; }
  30% { transform: scale(0.65) translateY(-60px) rotate(-2deg); opacity: 0.25; }
  70% { transform: scale(0.55) translateY(20px) rotate(4deg); opacity: 0.2; }
  100% { left: 110%; transform: scale(0.6) translateY(-10px) rotate(0deg); opacity: 0; }
}

.fg-body {
  width: 100%; height: 100%;
  background: radial-gradient(ellipse at 50% 35%, rgba(15,8,20,0.9) 0%, rgba(8,4,12,0.5) 45%, transparent 75%);
  border-radius: 50% 50% 30% 70% / 60% 60% 40% 40%;
  filter: blur(4px);
  animation: fgWarp 2s ease-in-out infinite;
}
@keyframes fgWarp {
  0%,100% { border-radius: 50% 50% 30% 70% / 60% 60% 40% 40%; }
  50% { border-radius: 45% 55% 40% 60% / 55% 50% 50% 45%; }
}
.fg-eyes {
  position: absolute; top: 30%; left: 50%; transform: translateX(-50%);
  display: flex; gap: 10px;
}
.fg-eye {
  width: 6px; height: 8px; border-radius: 50%;
  background: radial-gradient(circle, rgba(255,60,30,0.9), rgba(180,20,10,0.5));
  box-shadow: 0 0 6px rgba(255,50,20,0.7), 0 0 12px rgba(200,30,10,0.4);
  animation: fgEyeBlink 3s ease-in-out infinite;
}
@keyframes fgEyeBlink {
  0%,90%,94%,100% { transform: scaleY(1); }
  92% { transform: scaleY(0.1); }
}

/* ====== 点击印记 ====== */
.click-stamps {
  position: fixed; inset: 0; pointer-events: none; z-index: 95;
}
.click-stamp {
  position: absolute; transform: translate(-50%, -50%);
  width: 44px; height: 44px;
  display: flex; align-items: center; justify-content: center;
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  font-size: 22px; color: rgba(180,25,15,0.85);
  border: 2px solid rgba(160,20,12,0.6);
  border-radius: 4px;
  text-shadow: 0 0 6px rgba(200,30,15,0.5);
  box-shadow: 0 0 15px rgba(180,20,10,0.4), inset 0 0 8px rgba(100,10,5,0.3);
  animation: stampAppear 1.2s ease both;
}
@keyframes stampAppear {
  0% { transform: translate(-50%,-50%) scale(2) rotate(-10deg); opacity: 0; }
  15% { transform: translate(-50%,-50%) scale(1) rotate(0deg); opacity: 0.9; }
  40% { transform: translate(-50%,-50%) scale(1) rotate(0deg); opacity: 0.8; }
  100% { transform: translate(-50%,-50%) scale(0.8) rotate(5deg); opacity: 0; }
}

/* ====== 环境恐怖 ====== */
.horror-hands { position: fixed; inset: 0; pointer-events: none; z-index: 30; }
.creep-hand {
  position: absolute;
  filter: blur(2px);
}
.hand-left {
  left: -60px; top: 30%; width: 120px; height: 200px;
  background: radial-gradient(ellipse at 70% 50%, rgba(10,5,15,0.95) 0%, rgba(8,3,12,0.7) 40%, transparent 75%);
  border-radius: 0 70% 50% 0 / 0 40% 60% 0;
  animation: handCreepLeft 10s ease-in-out infinite;
}
.hand-right {
  right: -60px; top: 50%; width: 110px; height: 180px;
  background: radial-gradient(ellipse at 30% 50%, rgba(10,5,15,0.95) 0%, rgba(8,3,12,0.7) 40%, transparent 75%);
  border-radius: 70% 0 0 50% / 40% 0 0 60%;
  animation: handCreepRight 13s ease-in-out infinite 4s;
}
.hand-bottom {
  bottom: -40px; left: 40%; width: 160px; height: 100px;
  background: radial-gradient(ellipse at 50% 30%, rgba(10,5,15,0.9) 0%, rgba(8,3,12,0.6) 45%, transparent 75%);
  border-radius: 50% 50% 0 0;
  animation: handCreepBottom 14s ease-in-out infinite 7s;
}
@keyframes handCreepLeft {
  0%,55%,100% { transform: translateX(0); opacity: 0; }
  60% { opacity: 0.3; }
  65% { transform: translateX(70px); opacity: 0.85; }
  75% { transform: translateX(60px); opacity: 0.7; }
  85% { transform: translateX(50px); opacity: 0.5; }
  92% { transform: translateX(0); opacity: 0; }
}
@keyframes handCreepRight {
  0%,50%,100% { transform: translateX(0); opacity: 0; }
  55% { opacity: 0.3; }
  60% { transform: translateX(-65px); opacity: 0.8; }
  72% { transform: translateX(-55px); opacity: 0.65; }
  82% { transform: translateX(-40px); opacity: 0.4; }
  90% { transform: translateX(0); opacity: 0; }
}
@keyframes handCreepBottom {
  0%,45%,100% { transform: translateY(0); opacity: 0; }
  50% { opacity: 0.3; }
  55% { transform: translateY(-60px); opacity: 0.8; }
  68% { transform: translateY(-50px); opacity: 0.6; }
  78% { transform: translateY(-30px); opacity: 0.35; }
  88% { transform: translateY(0); opacity: 0; }
}

.horror-face {
  position: fixed; top: 50%; left: 50%; z-index: 90;
  width: 200px; height: 260px; margin: -130px 0 0 -100px;
  pointer-events: none; opacity: 0;
  transition: opacity 0.05s;
  background:
    radial-gradient(ellipse at 35% 35%, rgba(200,180,160,0.04) 8%, transparent 12%),
    radial-gradient(ellipse at 65% 35%, rgba(200,180,160,0.04) 8%, transparent 12%),
    radial-gradient(ellipse at 50% 60%, rgba(0,0,0,0.06) 5%, transparent 15%),
    radial-gradient(ellipse at 50% 50%, rgba(20,10,15,0.15) 30%, transparent 60%);
  filter: blur(2px) contrast(1.5);
}
.horror-face.visible { opacity: 1; }

/* ============================== 石书主容器 ============================== */
.book-layer { position: relative; z-index: 20; perspective: 1000px; }
.book-shadow {
  position: absolute; inset: 20px -20px -20px -20px;
  background: radial-gradient(ellipse at 50% 50%, rgba(0,0,0,0.6) 0%, transparent 70%);
  filter: blur(35px); pointer-events: none; z-index: -1;
}

.stone-book {
  display: flex; position: relative;
  transform: rotate(-2deg) rotateY(3deg);
  transition: transform 0.4s ease;
  filter: drop-shadow(0 12px 50px rgba(0,0,0,0.65));
  animation: bookHover 5s ease-in-out infinite;
}
@keyframes bookHover {
  0%,100% { transform: rotate(-2deg) rotateY(3deg) translateY(0); }
  50% { transform: rotate(-1.8deg) rotateY(2.5deg) translateY(-4px); }
}
.stone-book:hover { transform: rotate(-1.5deg) rotateY(2deg); animation-play-state: paused; }

/* ===== 书脊 ===== */
.book-spine {
  width: 18px; flex-shrink: 0;
  background: linear-gradient(90deg, #1a1410 0%, #2a2218 30%, #3a3020 50%, #2a2218 70%, #1a1410 100%);
  border-left: 2px solid rgba(60,45,25,0.5);
  border-right: 2px solid rgba(30,20,12,0.6);
  position: relative; z-index: 2;
  display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 50px;
}
.spine-rivet {
  width: 10px; height: 10px; border-radius: 50%;
  background: radial-gradient(circle at 35% 35%, #8a7a5a, #3a2a15);
  box-shadow: 0 0 4px rgba(0,0,0,0.5), inset 0 1px 2px rgba(0,0,0,0.4);
}
/* STYLE_CONTINUE_5 */

/* ===== 石页通用 ===== */
.stone-page {
  width: 230px; min-height: 440px;
  position: relative; overflow: hidden;
  background:
    radial-gradient(ellipse at 20% 30%, rgba(50,40,30,0.4) 0%, transparent 50%),
    radial-gradient(ellipse at 70% 60%, rgba(35,28,20,0.3) 0%, transparent 45%),
    repeating-linear-gradient(0deg, transparent, transparent 2px, rgba(0,0,0,0.015) 2px, rgba(0,0,0,0.015) 3px),
    repeating-linear-gradient(90deg, transparent, transparent 5px, rgba(0,0,0,0.01) 5px, rgba(0,0,0,0.01) 6px),
    linear-gradient(175deg, #3e3830 0%, #322d26 20%, #3a342e 45%, #2c2722 65%, #36312b 85%, #2a2520 100%);
  box-shadow: inset 0 0 80px rgba(0,0,0,0.25);
}
.page-texture {
  position: absolute; inset: 0; pointer-events: none; z-index: 1;
  background:
    radial-gradient(circle at 15% 25%, rgba(255,255,255,0.02) 0%, transparent 30%),
    radial-gradient(circle at 80% 70%, rgba(0,0,0,0.08) 0%, transparent 40%),
    repeating-linear-gradient(1deg, transparent, transparent 4px, rgba(0,0,0,0.02) 4px, rgba(0,0,0,0.02) 5px);
}

/* ===== 血迹 ===== */
.page-blood-stain {
  position: absolute; pointer-events: none; z-index: 2; border-radius: 50%;
  background: radial-gradient(ellipse, rgba(120,14,8,0.22) 0%, rgba(80,6,4,0.1) 50%, transparent 70%);
  filter: blur(2px);
}
.bs-1 { width: 100px; height: 80px; top: 8%; left: 10%; animation: stainPulse 4s ease-in-out infinite; }
.bs-2 { width: 70px; height: 50px; bottom: 18%; right: 15%; animation: stainPulse 3.5s ease-in-out 1s infinite; }
.bs-3 { width: 90px; height: 60px; top: 65%; right: 12%; animation: stainPulse 5s ease-in-out 2s infinite; }
@keyframes stainPulse { 0%,100% { opacity: 0.5; } 50% { opacity: 0.9; } }

/* ===== 裂缝 ===== */
.page-crack { position: absolute; pointer-events: none; z-index: 2; background: rgba(20,12,10,0.35); }
.cr-1 { top: 12%; left: 25%; width: 1px; height: 35%; transform: rotate(-15deg); box-shadow: 0 0 3px rgba(0,0,0,0.3); }
.cr-2 { top: 55%; left: 60%; width: 1px; height: 25%; transform: rotate(8deg); }
.cr-3 { bottom: 20%; left: 30%; width: 1px; height: 30%; transform: rotate(-5deg); }

/* ===== 血液滴落 ===== */
.blood-drip-container { position: absolute; top: 0; left: 0; right: 0; height: 100%; pointer-events: none; z-index: 4; }
.blood-drip {
  position: absolute; top: -5px; width: 2px; height: 0;
  background: linear-gradient(180deg, rgba(160,20,10,0.6), rgba(120,10,5,0.3), transparent);
  border-radius: 0 0 50% 50%;
  animation: dripFall ease-in infinite;
}
@keyframes dripFall {
  0% { height: 0; opacity: 0.8; top: -5px; }
  70% { height: 60px; opacity: 0.5; top: 30%; }
  100% { height: 30px; opacity: 0; top: 85%; }
}
/* STYLE_CONTINUE_6 */

/* ===== 左侧页 ===== */
.page-left {
  border-right: 1px solid rgba(0,0,0,0.2);
  display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 30px;
  padding: 40px 20px;
  clip-path: polygon(0% 0%, 100% 2%, 100% 98%, 0% 100%);
}
.left-inscription {
  position: relative; z-index: 3;
  display: flex; flex-direction: column; align-items: center; gap: 14px;
}
.insc-char {
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  font-size: 44px; font-weight: 400; color: #8a1015;
  writing-mode: vertical-rl;
  text-shadow: 0 0 12px rgba(140,15,20,0.7), 0 0 30px rgba(120,8,12,0.4), 0 0 50px rgba(100,5,8,0.25), 0 2px 3px rgba(0,0,0,0.6), 0 -1px 0 rgba(255,80,60,0.15);
  opacity: 0; animation: bloodCharEmerge 0.6s ease both;
  filter: contrast(1.2) brightness(0.85) saturate(1.5);
  position: relative;
}
.insc-char::after {
  content: ''; position: absolute; bottom: -4px; left: 20%; right: 20%; height: 3px;
  background: linear-gradient(90deg, transparent, rgba(160,20,15,0.5), rgba(180,25,15,0.3), transparent);
  border-radius: 50%; filter: blur(2px);
  animation: bloodDripChar 2.5s ease-in-out infinite;
}
.insc-big {
  font-size: 62px;
  text-shadow: 0 0 18px rgba(160,18,22,0.8), 0 0 40px rgba(130,10,14,0.5), 0 0 60px rgba(100,5,8,0.3), 0 3px 4px rgba(0,0,0,0.6), 0 -2px 0 rgba(255,80,50,0.2);
}
.insc-big::after { bottom: -6px; height: 5px; background: linear-gradient(90deg, transparent, rgba(180,20,15,0.6), rgba(200,30,18,0.4), transparent); }
@keyframes bloodCharEmerge {
  from { opacity: 0; transform: scale(0.85); filter: blur(4px) contrast(1.2) brightness(0.85) saturate(1.5); }
  to { opacity: 1; transform: scale(1); filter: blur(0) contrast(1.2) brightness(0.85) saturate(1.5); }
}
@keyframes bloodDripChar { 0%,100% { opacity: 0.3; transform: translateY(0); } 50% { opacity: 0.8; transform: translateY(3px); } }
.insc-2 { margin-top: 12px; }
.insc-2 .insc-char { font-size: 62px; color: #a01218; }

/* 血手印 */
.blood-handprint {
  position: relative; z-index: 3; width: 75px; height: 85px;
  background: radial-gradient(ellipse at 45% 25%, rgba(110,18,12,0.35), rgba(70,8,5,0.15) 40%, transparent 65%);
  border-radius: 50% 50% 45% 45%; filter: blur(1px);
  transform: rotate(-10deg); animation: handPulse 3.5s ease-in-out infinite;
}
@keyframes handPulse { 0%,100% { opacity: 0.4; } 50% { opacity: 0.8; } }
/* STYLE_CONTINUE_7 */

/* ===== 右侧页 — 表单 ===== */
.page-right {
  border-left: 1px solid rgba(255,255,255,0.03);
  clip-path: polygon(0% 2%, 100% 0%, 100% 100%, 0% 98%);
}
.page-content {
  position: relative; z-index: 3;
  padding: 36px 32px; display: flex; flex-direction: column;
}
.page-header-area { text-align: center; margin-bottom: 24px; }
.seal-mark {
  width: 48px; height: 48px; margin: 0 auto 12px;
  background: radial-gradient(circle at 40% 35%, #6e1010, #2e0404);
  clip-path: polygon(50% 0%, 75% 15%, 100% 35%, 100% 70%, 80% 100%, 50% 90%, 20% 100%, 0% 70%, 0% 35%, 25% 15%);
  box-shadow: 0 0 20px rgba(100,8,5,0.5), inset 0 0 12px rgba(0,0,0,0.5);
  animation: sealPulse 2.5s ease-in-out infinite;
  position: relative;
}
.seal-mark::after {
  content: '契'; position: absolute; inset: 0;
  display: flex; align-items: center; justify-content: center;
  font-family: 'ZCOOL XiaoWei','STKaiti',serif;
  font-size: 18px; color: rgba(200,145,55,0.7);
  text-shadow: 0 0 6px rgba(180,120,30,0.3);
}
@keyframes sealPulse {
  0%,100% { box-shadow: 0 0 20px rgba(100,8,5,0.5), inset 0 0 12px rgba(0,0,0,0.5); }
  50% { box-shadow: 0 0 35px rgba(160,15,10,0.6), inset 0 0 20px rgba(0,0,0,0.6); }
}

/* 标题 */
.book-title { display: flex; justify-content: center; gap: 6px; margin: 0 0 6px; }
.bt-char {
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  font-size: 30px; font-weight: 400; letter-spacing: 2px;
  color: #b8945c;
  text-shadow: 0 2px 0 rgba(0,0,0,0.8), 0 3px 1px rgba(0,0,0,0.6), 0 1px 0 rgba(0,0,0,0.7), 0 -1px 0 rgba(255,255,255,0.06), 0 0 8px rgba(120,15,8,0.25), 0 0 18px rgba(90,8,4,0.12);
  opacity: 0; animation: charEmerge 0.5s ease both;
  filter: contrast(0.9) brightness(0.85) saturate(0.8);
  position: relative;
}
.bt-char::after {
  content: ''; position: absolute; inset: -2px;
  background: radial-gradient(circle at 30% 40%, transparent 60%, rgba(0,0,0,0.3) 100%);
  pointer-events: none; mix-blend-mode: multiply;
}
@keyframes charEmerge {
  from { opacity: 0; transform: translateY(6px); filter: blur(2px); }
  to { opacity: 1; transform: translateY(0); filter: blur(0); }
}
.book-sub {
  font-size: 11px; color: rgba(150,120,70,0.3);
  letter-spacing: 5px; font-family: 'ZCOOL XiaoWei','STKaiti',serif;
}
/* STYLE_CONTINUE_8 */

/* ===== 表单 ===== */
.book-form { display: flex; flex-direction: column; gap: 14px; }
.stone-field { display: flex; align-items: stretch; }
.sf-rune {
  width: 38px; flex-shrink: 0;
  display: flex; align-items: center; justify-content: center;
  font-family: 'ZCOOL XiaoWei','STKaiti',serif; font-size: 15px;
  color: rgba(120,90,50,0.35);
  background: linear-gradient(180deg, rgba(15,12,8,0.5), rgba(25,20,14,0.5));
  border: 1px solid rgba(70,50,30,0.15); border-right: none;
  text-shadow: 0 1px 0 rgba(0,0,0,0.5);
  animation: runeGlow 3s ease-in-out infinite;
}
@keyframes runeGlow { 0%,100% { color: rgba(120,90,50,0.35); } 50% { color: rgba(160,120,60,0.5); } }
.sf-input { flex: 1; position: relative; }
.sf-input :deep(.el-input__wrapper) {
  background: rgba(14,11,7,0.6); border: 1px solid rgba(70,50,30,0.15);
  border-radius: 0; box-shadow: none; padding: 10px 12px; transition: all 0.35s;
}
.sf-input :deep(.el-input__inner) {
  color: #b09870; font-family: 'Noto Serif SC','STSong',serif;
  font-size: 13px; letter-spacing: 2px;
}
.sf-input :deep(.el-input__inner::placeholder) { color: rgba(120,90,60,0.22); letter-spacing: 1px; }
.sf-input :deep(.el-input__wrapper:hover) { border-color: rgba(120,80,40,0.35); }
.sf-input :deep(.el-input__wrapper.is-focus) {
  border-color: rgba(180,120,50,0.45);
  box-shadow: 0 0 12px rgba(150,90,30,0.1), 0 0 30px rgba(120,20,10,0.05), inset 0 0 6px rgba(0,0,0,0.15);
}
.sf-input :deep(.el-input__suffix) { color: rgba(120,90,60,0.2); }
.sf-carve {
  position: absolute; bottom: 0; left: 4px; right: 4px; height: 1px;
  background: linear-gradient(90deg, transparent, rgba(170,120,50,0.3), transparent);
  pointer-events: none; animation: carvePulse 1.8s ease-in-out infinite;
}
@keyframes carvePulse { 0%,100% { opacity: 0.25; } 50% { opacity: 0.7; } }

/* ===== 血色按钮 ===== */
.blood-seal-btn {
  width: 100%; height: 50px; margin-top: 4px;
  position: relative; border: none; border-radius: 1px; overflow: hidden; cursor: pointer;
  background: linear-gradient(180deg, #4a1214 0%, #2e0a0c 35%, #3a1012 55%, #1e0608 100%);
  color: #c09058;
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  font-size: 17px; letter-spacing: 6px; transition: all 0.3s;
  box-shadow: 0 2px 0 rgba(0,0,0,0.4), 0 4px 15px rgba(0,0,0,0.3), inset 0 1px 0 rgba(255,255,255,0.03);
}
.blood-seal-btn:hover {
  background: linear-gradient(180deg, #5a1618, #3a0e10, #461416, #2a0a0c);
  box-shadow: 0 2px 0 rgba(0,0,0,0.4), 0 6px 30px rgba(160,25,18,0.25), 0 0 60px rgba(120,15,8,0.1), inset 0 1px 0 rgba(255,255,255,0.04);
  transform: translateY(-1px);
}
.blood-seal-btn:active { transform: scale(0.96); }
.blood-seal-btn:disabled { opacity: 0.4; cursor: not-allowed; transform: none; }
.bsb-text {
  position: relative; z-index: 1;
  text-shadow: 0 1px 0 rgba(0,0,0,0.7), 0 2px 0 rgba(0,0,0,0.5), 0 0 10px rgba(180,100,30,0.25);
}
/* STYLE_CONTINUE_9 */

/* 血液渗出 + 脉动 */
.bsb-blood {
  position: absolute; bottom: 0; left: 0; right: 0; height: 3px;
  background: linear-gradient(90deg, transparent, rgba(200,20,12,0.6), rgba(180,15,10,0.5), transparent);
  pointer-events: none; z-index: 0; animation: bloodSeep 1.8s ease-in-out infinite;
}
.bsb-pulse {
  position: absolute; inset: 0; pointer-events: none;
  background: radial-gradient(ellipse at 50% 50%, rgba(180,20,10,0.08), transparent 70%);
  animation: btnPulse 2.5s ease-in-out infinite;
}
@keyframes btnPulse { 0%,100% { opacity: 0; } 50% { opacity: 1; } }
@keyframes bloodSeep { 0%,100% { opacity: 0.3; } 50% { opacity: 0.9; } }

.pressed::after {
  content: ''; position: absolute; inset: -10px; border-radius: 3px;
  border: 2px solid rgba(180,20,10,0.25);
  animation: bloodRipple 0.45s ease both; pointer-events: none;
}
@keyframes bloodRipple { 0% { transform: scale(0.9); opacity: 0.8; } 100% { transform: scale(1.2); opacity: 0; } }

/* ===== 页脚 ===== */
.page-foot {
  text-align: center; margin-top: 14px;
  color: rgba(100,70,40,0.2); font-size: 10px; letter-spacing: 3px;
  animation: footBreathe 3.5s ease-in-out infinite;
}
@keyframes footBreathe { 0%,100% { opacity: 0.2; } 50% { opacity: 0.5; } }

/* ============================== 竖排铭文 ============================== */
.vert-seal {
  position: absolute; bottom: 12%; left: 3%; z-index: 15;
  display: flex; flex-direction: column; align-items: center; gap: 1px;
  pointer-events: none;
}
.vs-ch { font-size: 15px; color: rgba(150,110,40,0.28); font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif; text-shadow: 0 0 4px rgba(150,110,30,0.1); animation: vertGlow 4s ease-in-out infinite; }
.vs-dot { color: rgba(80,10,5,0.22); font-size: 9px; }
@keyframes vertGlow { 0%,100% { opacity: 0.6; } 50% { opacity: 1; } }

/* ============================== 鬼笔光标 ============================== */
.login-scene { cursor: none; }
.login-scene * { cursor: none; }

.ghost-cursor {
  position: fixed; top: 0; left: 0; z-index: 9999;
  pointer-events: none; opacity: 0;
  transition: opacity 0.3s;
  animation: ghostCursorFloat 2s ease-in-out infinite;
}
@keyframes ghostCursorFloat {
  0%,100% { margin-top: 0; }
  50% { margin-top: -3px; }
}
.gc-ghost-body {
  position: relative; width: 28px; height: 32px;
}
.gc-ghost-head {
  width: 28px; height: 22px;
  background: radial-gradient(ellipse at 50% 45%, rgba(180,200,210,0.25), rgba(100,120,140,0.15), rgba(50,60,70,0.08));
  border-radius: 50% 50% 40% 40%;
  position: relative;
  box-shadow: 0 0 10px rgba(150,180,200,0.2), 0 0 20px rgba(100,130,160,0.1);
  display: flex; align-items: center; justify-content: center; gap: 6px;
  padding-top: 6px;
}
.gc-ghost-eye {
  width: 4px; height: 5px; border-radius: 50%;
  background: rgba(255,200,50,0.8);
  box-shadow: 0 0 4px rgba(255,200,50,0.6), 0 0 8px rgba(255,180,0,0.3);
}
.gc-ghost-tail {
  display: flex; justify-content: center; margin-top: -2px;
}
.gc-ghost-tail span {
  width: 9px; height: 12px;
  background: radial-gradient(ellipse at 50% 0%, rgba(150,170,190,0.2), transparent);
  border-radius: 0 0 50% 50%;
  animation: tailWave 1.5s ease-in-out infinite;
}
.gc-ghost-tail span:nth-child(2) { animation-delay: 0.2s; }
.gc-ghost-tail span:nth-child(3) { animation-delay: 0.4s; }
@keyframes tailWave {
  0%,100% { transform: translateY(0) scaleY(1); }
  50% { transform: translateY(2px) scaleY(1.2); }
}

.gc-brush {
  position: absolute; bottom: -8px; right: -2px;
  transform: rotate(25deg); transform-origin: top center;
}
.gc-brush-handle {
  width: 2.5px; height: 22px;
  background: linear-gradient(180deg, #4a3520, #2a1a0a, #3a2815);
  border-radius: 1px;
  margin: 0 auto;
}
.gc-brush-tip {
  width: 4px; height: 8px;
  background: linear-gradient(180deg, #1a0a05, rgba(80,20,10,0.8));
  border-radius: 1px 1px 50% 50%;
  margin: 0 auto;
  box-shadow: 0 2px 4px rgba(100,20,10,0.4);
}

/* 点击动画 */
.ghost-cursor.clicking .gc-brush {
  animation: brushStamp 0.3s ease both;
}
@keyframes brushStamp {
  0% { transform: rotate(25deg); }
  40% { transform: rotate(5deg) translateY(4px); }
  100% { transform: rotate(25deg); }
}
.ghost-cursor.clicking .gc-ghost-body {
  animation: ghostBounce 0.3s ease;
}
@keyframes ghostBounce {
  0%,100% { transform: scale(1); }
  50% { transform: scale(0.9) translateY(2px); }
}

/* 墨迹 canvas */
.ink-trail-canvas {
  position: fixed; inset: 0; z-index: 9998;
  pointer-events: none;
}

/* ============================== 响应式 ============================== */
@media (max-width: 768px) {
  .shadow-entity { transform: scale(0.7); }
  .stone-page { width: 160px; min-height: 380px; }
  .page-content { padding: 24px 20px; }
  .bt-char { font-size: 22px; }
  .vert-seal { display: none; }
  .post-scanlines { display: none; }
  .ghost-cursor { display: none; }
  .login-scene { cursor: auto; }
  .login-scene * { cursor: auto; }
}
</style>
