<template>
  <div class="login-scene" @mousemove="onMouse" @mouseleave="onLeave">
    <!-- ====== 背景场景层 ====== -->
    <div class="scene-bg">
      <div class="blood-moon"></div>
      <div class="mountains">
        <span class="mt mt-1"></span><span class="mt mt-2"></span>
        <span class="mt mt-3"></span><span class="mt mt-4"></span>
      </div>
      <div class="ground-fog"></div>
      <div class="ground-fog fog-2"></div>
      <!-- 浮灰 -->
      <span v-for="i in 40" :key="'e'+i" class="ember"
        :style="{ left: ((i*179+31)%96)+'%', animationDelay: (i*0.35)+'s', animationDuration: (6+(i*0.4)%8)+'s', width: (1.5+(i%3)*1)+'px', height: (1.5+(i%3)*1)+'px', opacity: 0.15+(i%4)*0.1 }"></span>
      <!-- 远处鬼火 -->
      <span v-for="i in 6" :key="'w'+i" class="will-o-wisp"
        :style="{ left: (10+i*13)+'%', bottom: (8+i*3)+'%', animationDelay: (i*1.2)+'s', animationDuration: (3+i*0.8)+'s' }"></span>
    </div>

    <!-- ====== 鬼怪层（保留原有的四个 CSS 鬼差） ====== -->
    <div class="ghost-char char-hei" ref="charEls" data-idx="0">
      <div class="gc-body" ref="bodyEls">
        <div class="gc-head hei-head"><div class="hei-hat"></div><div class="gc-eyes"><span class="gc-pupil gold-pupil" ref="pupilEls"></span><span class="gc-pupil gold-pupil" ref="pupilEls"></span></div><div class="hei-mouth"></div></div>
        <div class="gc-robe robe-hei"><span class="robe-belt"></span></div><div class="hei-chain"></div>
      </div>
      <span class="gc-name">黑无常</span><span class="gc-fire"></span>
      <span v-for="m in 4" :key="'hm'+m" class="gc-mist" :style="{ left: ((m*33+10)%100)+'%', top: ((m*40+20)%100)+'%', animationDelay: (m*0.5)+'s', width: (20+m*8)+'px', height: (25+m*10)+'px' }"></span>
    </div>

    <div class="ghost-char char-bai" ref="charEls" data-idx="1">
      <div class="gc-body" ref="bodyEls">
        <div class="gc-head bai-head"><div class="bai-hat"></div><div class="gc-eyes"><span class="gc-pupil dark-pupil" ref="pupilEls"></span><span class="gc-pupil dark-pupil" ref="pupilEls"></span></div><div class="bai-mouth"></div></div>
        <div class="gc-robe robe-bai"></div><div class="bai-fan"></div>
      </div>
      <span class="gc-name">白无常</span><span class="gc-fire"></span>
      <span v-for="m in 4" :key="'bm'+m" class="gc-mist" :style="{ left: ((m*37+5)%100)+'%', top: ((m*35+15)%100)+'%', animationDelay: (m*0.6)+'s', width: (18+m*7)+'px', height: (22+m*9)+'px' }"></span>
    </div>

    <div class="ghost-char char-judge" ref="charEls" data-idx="2">
      <div class="gc-body" ref="bodyEls">
        <div class="gc-head judge-head"><div class="judge-hat"></div><div class="gc-eyes"><span class="gc-pupil gold-pupil" ref="pupilEls"></span><span class="gc-pupil gold-pupil" ref="pupilEls"></span></div><div class="judge-beard"></div></div>
        <div class="gc-robe robe-judge"></div><div class="judge-brush"></div>
      </div>
      <span class="gc-name">判 官</span><span class="gc-fire"></span>
      <span v-for="m in 4" :key="'jm'+m" class="gc-mist" :style="{ left: ((m*29+20)%100)+'%', top: ((m*45+10)%100)+'%', animationDelay: (m*0.55)+'s', width: (22+m*6)+'px', height: (28+m*8)+'px' }"></span>
    </div>

    <div class="ghost-char char-imp" ref="charEls" data-idx="3">
      <div class="gc-body" ref="bodyEls">
        <div class="gc-head imp-head"><div class="imp-horns"><span class="imp-horn imp-hl"></span><span class="imp-horn imp-hr"></span></div><div class="gc-eyes"><span class="gc-pupil imp-pupil" ref="pupilEls"></span><span class="gc-pupil imp-pupil" ref="pupilEls"></span></div><div class="imp-grin"></div></div>
        <div class="gc-robe robe-imp"></div>
      </div>
      <span class="gc-name">小 鬼</span><span class="gc-fire"></span>
      <span v-for="m in 4" :key="'im'+m" class="gc-mist" :style="{ left: ((m*31+15)%100)+'%', top: ((m*42+10)%100)+'%', animationDelay: (m*0.45)+'s', width: (16+m*6)+'px', height: (20+m*8)+'px' }"></span>
    </div>

    <!-- ====== 石碑登录卡片 ====== -->
    <div class="tablet-layer">
      <div class="tablet-shadow"></div>
      <div class="stone-tablet">
        <!-- 石碑顶部破损 -->
        <div class="tablet-break-top"></div>
        <!-- 石碑表面纹理层 -->
        <div class="tablet-surface">
          <!-- 碑额 -->
          <div class="tablet-header">
            <div class="tablet-seal"></div>
            <h1 class="tablet-title">
              <span class="title-char" v-for="(c, i) in '地府生死簿'" :key="i" :style="{ animationDelay: i*0.08+'s' }">{{ c }}</span>
            </h1>
            <div class="tablet-sub">—— 冥 书 石 契 ——</div>
          </div>

          <!-- 碑文表单 -->
          <el-form ref="formRef" :model="form" :rules="rules" class="tablet-form">
            <div class="tablet-field">
              <label class="field-rune">名</label>
              <div class="field-input-zone">
                <el-input v-model="form.username" placeholder="判官提笔 · 录入名讳"
                  @focus="focused='name'" @blur="focused=null" />
                <span class="carve-line" v-show="focused==='name'||form.username"></span>
              </div>
            </div>

            <div class="tablet-field">
              <label class="field-rune">印</label>
              <div class="field-input-zone">
                <el-input v-model="form.password" type="password" placeholder="阎王御印 · 密令通行"
                  show-password @focus="focused='pass'" @blur="focused=null" @keyup.enter="doLogin" />
                <span class="carve-line" v-show="focused==='pass'||form.password"></span>
              </div>
            </div>

            <button type="button" class="stone-btn" :class="{ pressed: clicked }" :disabled="loading" @click="doLogin">
              <span class="btn-carve">踏 入 地 府</span>
              <span class="btn-crack"></span>
            </button>
          </el-form>

          <p class="tablet-foot">十殿阎罗 · 森严戒备 · 生人勿入</p>
        </div>
        <!-- 石碑底部缺损 -->
        <div class="tablet-break-bottom"></div>
      </div>
    </div>

    <!-- 左下竖排铭文 -->
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

// 鼠标跟踪
let target = { x: -999, y: -999 }
let smooth = { x: -999, y: -999 }
let raf = null

function onMouse(e) { target.x = e.clientX; target.y = e.clientY }
function onLeave() { target.x = -999; target.y = -999 }

function track() {
  if (target.x < 0) {
    smooth.x += (-999 - smooth.x) * 0.04
    smooth.y += (-999 - smooth.y) * 0.04
  } else {
    smooth.x += (target.x - smooth.x) * 0.2
    smooth.y += (target.y - smooth.y) * 0.2
  }
  const isAway = target.x < 0
  const w = window.innerWidth, h = window.innerHeight
  const mx = isAway ? w * 0.45 : smooth.x
  const my = isAway ? h * 0.45 : smooth.y

  document.querySelectorAll('.ghost-char').forEach((charEl) => {
    const bodyEl = charEl.querySelector('.gc-body')
    if (!bodyEl) return
    const rect = charEl.getBoundingClientRect()
    const cx = rect.left + rect.width / 2
    const cy = rect.top + rect.height * 0.35
    const dx = mx - cx, dy = my - cy
    const dist = Math.sqrt(dx * dx + dy * dy)
    const angle = Math.atan2(dy, dx) * (180 / Math.PI)
    const influence = Math.min(dist / 250, 1)
    bodyEl.style.transform = `rotate(${Math.max(-50, Math.min(50, angle)) * influence}deg) translate(${(dx/(dist||1))*influence*4}px, ${(dy/(dist||1))*influence*2}px)`
    charEl.querySelectorAll('.gc-pupil').forEach(p => {
      p.style.transform = `translate(${(dx/(dist||1))*5*influence}px, ${(dy/(dist||1))*5*influence}px)`
    })
  })

  const vs = document.querySelector('.vert-seal')
  if (vs) {
    const dx2 = mx - w * 0.08, dy2 = my - h * 0.7
    vs.style.transform = `rotate(${Math.atan2(dy2,dx2)*0.03}deg) translate(${dx2*0.008}px, ${dy2*0.008}px)`
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
/* ============================== 场景 ============================== */
.login-scene {
  height: 100vh;
  background: radial-gradient(ellipse at 50% 60%, #0e0a14 0%, #06040a 50%, #020106 100%);
  font-family: 'Noto Serif SC', 'STSong', 'SimSun', serif;
  position: relative; overflow: hidden;
  display: flex; align-items: center; justify-content: center;
  cursor: default;
}

/* ===== 血月 ===== */
.blood-moon {
  position: absolute; top: 5%; left: 58%;
  width: 200px; height: 200px;
  background: radial-gradient(circle at 45% 40%, rgba(160,30,20,0.5) 0%, rgba(100,15,10,0.2) 35%, transparent 65%);
  border-radius: 50%; filter: blur(6px);
  animation: moonBreathe 6s ease-in-out infinite;
  pointer-events: none;
  box-shadow: 0 0 80px rgba(140,20,10,0.15), 0 0 200px rgba(100,10,5,0.08);
}
@keyframes moonBreathe {
  0%,100% { opacity: 0.6; transform: scale(1); }
  50% { opacity: 0.9; transform: scale(1.08); }
}

/* ===== 远山 ===== */
.mountains { position: absolute; inset: 0; pointer-events: none; }
.mt { position: absolute; bottom: 0; opacity: 0.7; width: 0; height: 0; border-bottom-style: solid; }
.mt-1 { left: -10%; bottom: 18%; border-left: 220px solid transparent; border-right: 320px solid transparent; border-bottom: 180px solid rgba(6,3,10,0.9); }
.mt-2 { left: 38%; bottom: 16%; border-left: 200px solid transparent; border-right: 120px solid transparent; border-bottom: 220px solid rgba(4,2,8,0.92); }
.mt-3 { left: -5%; bottom: 13%; border-left: 280px solid transparent; border-right: 380px solid transparent; border-bottom: 130px solid rgba(8,4,12,0.85); }
.mt-4 { left: 55%; bottom: 20%; border-left: 320px solid transparent; border-right: 180px solid transparent; border-bottom: 150px solid rgba(5,2,9,0.88); }

/* ===== 雾气 ===== */
.ground-fog {
  position: absolute; bottom: 0; left: 0; right: 0; height: 35%;
  background: linear-gradient(0deg, rgba(18,12,20,0.55) 0%, rgba(12,8,15,0.25) 45%, transparent 100%);
  pointer-events: none; animation: fogDrift 10s ease-in-out infinite;
}
.fog-2 { bottom: 5%; height: 22%; opacity: 0.4; animation: fogDrift 12s ease-in-out infinite reverse; }
@keyframes fogDrift {
  0%,100% { transform: translateX(0); }
  50% { transform: translateX(3%); }
}

/* ===== 浮灰 ===== */
.ember {
  position: absolute; bottom: -10px; border-radius: 50%;
  background: radial-gradient(circle, rgba(200,140,50,0.35), rgba(140,25,12,0.15));
  pointer-events: none; z-index: 1;
  animation: emberFloat linear infinite;
}
@keyframes emberFloat {
  0% { transform: translateY(0) translateX(0) scale(0); opacity: 0; }
  10% { opacity: 0.5; }
  50% { opacity: 0.3; transform: translateY(-50vh) translateX(15px) scale(1); }
  100% { transform: translateY(-100vh) translateX(-10px) scale(0); opacity: 0; }
}

/* ===== 远处鬼火 ===== */
.will-o-wisp {
  position: absolute; width: 8px; height: 8px; border-radius: 50%;
  background: radial-gradient(circle, rgba(100,180,120,0.6), rgba(60,140,80,0.2), transparent);
  box-shadow: 0 0 12px rgba(80,160,100,0.4), 0 0 30px rgba(60,120,70,0.2);
  pointer-events: none; z-index: 2;
  animation: wispFloat 4s ease-in-out infinite;
}
@keyframes wispFloat {
  0%,100% { transform: translateY(0) translateX(0); opacity: 0.3; }
  25% { transform: translateY(-20px) translateX(8px); opacity: 0.7; }
  50% { transform: translateY(-8px) translateX(-5px); opacity: 0.2; }
  75% { transform: translateY(-28px) translateX(-12px); opacity: 0.6; }
}

/* ============================== 鬼怪 ============================== */
.ghost-char { position: absolute; display: flex; flex-direction: column; align-items: center; z-index: 10; pointer-events: none; }
.char-hei  { left: 5%;  top: 8%; }
.char-bai  { right: 5%; top: 10%; }
.char-judge{ left: 7%; top: 55%; }
.char-imp  { right: 7%; bottom: 18%; }

.gc-body { display: flex; flex-direction: column; align-items: center; transform-origin: center 70%; transition: transform 0.06s ease-out; }
.gc-head { position: relative; width: 52px; height: 52px; border-radius: 50%; margin-bottom: 1px; z-index: 3; }
.gc-eyes { position: absolute; top: 53%; left: 50%; transform: translateX(-50%); display: flex; gap: 14px; }
.gc-pupil { display: block; width: 8px; height: 8px; border-radius: 50%; transition: transform 0.04s ease-out; }
.gold-pupil { background: #ffd700; box-shadow: 0 0 7px rgba(255,215,0,0.6), 0 0 14px rgba(255,180,20,0.3); }
.dark-pupil { background: #0a0a0f; box-shadow: 0 0 4px rgba(0,0,0,0.8); }
.imp-pupil { background: #ff4444; box-shadow: 0 0 7px rgba(255,68,68,0.6), 0 0 14px rgba(255,30,30,0.3); }
.gc-robe { position: relative; width: 42px; height: 52px; border-radius: 50% 50% 8px 8px; }
.robe-belt { position: absolute; top: 10px; left: -2px; right: -2px; height: 4px; background: #5a3a30; border-radius: 1px; }
.gc-fire { position: absolute; bottom: -6px; width: 36px; height: 10px; background: radial-gradient(ellipse at center, rgba(160,25,15,0.3) 0%, transparent 70%); border-radius: 50%; filter: blur(6px); animation: fireFlick 2.2s ease-in-out infinite; }
@keyframes fireFlick { 0%,100% { opacity: 0.35; transform: scaleX(1); } 40% { opacity: 0.75; transform: scaleX(1.3); } 70% { opacity: 0.2; transform: scaleX(0.75); } }
.gc-mist { position: absolute; border-radius: 50%; background: radial-gradient(ellipse, rgba(20,14,10,0.45) 0%, transparent 70%); filter: blur(12px); pointer-events: none; z-index: -1; animation: mistWisp 4s ease-in-out infinite; }
@keyframes mistWisp { 0%,100% { transform: translateY(0) scale(1); opacity: 0.3; } 50% { transform: translateY(-10px) scale(1.2); opacity: 0.5; } }
.gc-name { font-size: 11px; color: rgba(170,130,70,0.4); letter-spacing: 2px; margin-top: 8px; font-family: 'ZCOOL XiaoWei', 'STKaiti', serif; }

/* 黑无常 */
.hei-head { background: radial-gradient(circle at 45% 38%, #3a3035, #1a1015); border: 2px solid #2a2025; }
.hei-hat { position: absolute; top: -34px; left: 50%; transform: translateX(-50%); width: 0; height: 0; border-left: 17px solid transparent; border-right: 17px solid transparent; border-bottom: 36px solid #1a1018; filter: drop-shadow(0 2px 4px rgba(0,0,0,0.6)); }
.hei-hat::after { content: ''; position: absolute; top: -8px; left: -4px; width: 42px; height: 6px; background: #1a1018; border-radius: 2px; }
.hei-mouth { position: absolute; bottom: 12px; left: 50%; transform: translateX(-50%); width: 16px; height: 3px; background: #4a3040; border-radius: 1px; }
.robe-hei { background: linear-gradient(180deg, #1c1618, #0f0b10); border: 1px solid #2a2028; }
.hei-chain { position: absolute; right: -24px; top: 65%; width: 32px; height: 3px; background: repeating-linear-gradient(90deg, #6a5a4a 0px, #6a5a4a 6px, #1a1510 6px, #1a1510 10px); border-radius: 1px; transform: rotate(-18deg); }

/* 白无常 */
.bai-head { background: radial-gradient(circle at 45% 38%, #ede5da, #c8beb0); border: 2px solid #a09890; }
.bai-hat { position: absolute; top: -34px; left: 50%; transform: translateX(-50%); width: 0; height: 0; border-left: 17px solid transparent; border-right: 17px solid transparent; border-bottom: 36px solid #ede5da; filter: drop-shadow(0 2px 6px rgba(0,0,0,0.35)); }
.bai-hat::after { content: ''; position: absolute; top: -8px; left: -4px; width: 42px; height: 6px; background: #ede5da; border-radius: 2px; }
.bai-mouth { position: absolute; bottom: 10px; left: 50%; transform: translateX(-50%); width: 20px; height: 5px; background: #6a5a5a; border-radius: 2px; }
.robe-bai { background: linear-gradient(180deg, #ebe4dc, #ccc4b8); border: 1px solid #b0a8a0; }
.bai-fan { position: absolute; right: -20px; top: 58%; width: 24px; height: 30px; background: #f2ead8; border-radius: 50% 0 0 50%; border: 1px solid #c8b898; transform: rotate(12deg); }

/* 判官 */
.judge-head { background: radial-gradient(circle at 45% 35%, #c48060, #8a4030); border: 2px solid #6a3028; }
.judge-hat { position: absolute; top: -26px; left: 50%; transform: translateX(-50%); width: 46px; height: 20px; background: linear-gradient(180deg, #1a1018, #2a1820); border-radius: 3px 3px 0 0; }
.judge-hat::after { content: ''; position: absolute; left: -12px; right: -12px; top: 5px; height: 2px; background: #3a2a30; }
.judge-beard { position: absolute; bottom: 2px; left: 50%; transform: translateX(-50%); width: 0; height: 0; border-left: 8px solid transparent; border-right: 8px solid transparent; border-top: 14px solid #2a1a18; }
.robe-judge { background: linear-gradient(180deg, #6e1c1a, #3e0e0c); border: 1px solid #501212; }
.judge-brush { position: absolute; right: -16px; top: 52%; width: 3px; height: 34px; background: linear-gradient(180deg, #3a2010, #1a0a08, #d4b878); border-radius: 1px; }

/* 小鬼 */
.imp-head { background: radial-gradient(circle at 45% 35%, #5a7a5a, #2a4a2a); border: 2px solid #1a3a1a; }
.imp-horns { position: absolute; top: -16px; left: 50%; transform: translateX(-50%); display: flex; gap: 12px; }
.imp-horn { width: 0; height: 0; border-left: 5px solid transparent; border-right: 5px solid transparent; border-bottom: 15px solid #3a3a2a; }
.imp-hl { transform: rotate(-12deg); }
.imp-hr { transform: rotate(12deg); }
.imp-grin { position: absolute; bottom: 8px; left: 50%; transform: translateX(-50%); width: 14px; height: 8px; background: #3a5a3a; border-radius: 0 0 50% 50%; }
.imp-grin::after { content: ''; position: absolute; top: -3px; right: 0; width: 0; height: 0; border-left: 3px solid transparent; border-right: 3px solid transparent; border-bottom: 5px solid #4a3030; }
.robe-imp { background: linear-gradient(180deg, #3c4c3c, #1c2c1c); border: 1px solid #2a3a2a; }

/* ============================== 石碑卡片 ============================== */
.tablet-layer {
  position: relative; z-index: 20;
  /* 整体倾斜 */
  transform: rotate(-2.5deg);
  transition: transform 0.3s ease;
}
.tablet-layer:hover {
  transform: rotate(-2deg);
}

/* 石碑阴影 */
.tablet-shadow {
  position: absolute;
  inset: 12px -8px -12px -8px;
  background: radial-gradient(ellipse at 50% 50%, rgba(0,0,0,0.5) 0%, transparent 70%);
  filter: blur(20px);
  pointer-events: none;
  z-index: -1;
}

/* 石碑主体 */
.stone-tablet {
  position: relative;
  width: 420px;
  /* 石碑上宽下窄的梯形 */
  clip-path: polygon(
    3% 0%, 97% 0%,
    100% 3%, 100% 97%,
    95% 100%, 5% 100%,
    0% 97%, 0% 3%
  );
  /* 多层石材纹理 */
  background:
    /* 苔藓斑 */
    radial-gradient(ellipse at 15% 25%, rgba(30,50,25,0.3) 0%, transparent 30%),
    radial-gradient(ellipse at 85% 70%, rgba(25,40,20,0.25) 0%, transparent 35%),
    radial-gradient(ellipse at 60% 15%, rgba(35,45,30,0.2) 0%, transparent 25%),
    /* 石纹 */
    repeating-linear-gradient(2deg, transparent, transparent 3px, rgba(0,0,0,0.03) 3px, rgba(0,0,0,0.03) 4px),
    repeating-linear-gradient(89deg, transparent, transparent 6px, rgba(0,0,0,0.02) 6px, rgba(0,0,0,0.02) 7px),
    /* 石材基色 */
    linear-gradient(175deg, #3a3530 0%, #2e2a25 15%, #35312c 30%, #2a2622 50%, #332f2a 70%, #282420 85%, #302c28 100%);
  box-shadow:
    0 0 0 2px rgba(20,18,15,0.8),
    0 0 0 5px rgba(40,36,30,0.4),
    0 0 0 7px rgba(25,22,18,0.6),
    0 8px 40px rgba(0,0,0,0.5),
    inset 0 0 60px rgba(0,0,0,0.15),
    inset 0 2px 4px rgba(255,255,255,0.03);
}

/* 石碑顶部破损 */
.tablet-break-top {
  position: absolute; top: -4px; left: 8%; right: 8%; height: 8px;
  background: linear-gradient(180deg, rgba(30,26,22,0.6), transparent);
  z-index: 5;
  border-radius: 2px;
}
.tablet-break-top::after {
  content: ''; position: absolute; top: -2px; left: 15%;
  width: 70%; height: 4px;
  background: rgba(40,35,30,0.4);
  clip-path: polygon(0% 100%, 8% 0%, 20% 60%, 35% 0%, 50% 40%, 65% 0%, 78% 50%, 92% 0%, 100% 100%);
}

/* 石碑底部缺损 */
.tablet-break-bottom {
  position: absolute; bottom: -2px; left: 10%; right: 10%; height: 6px;
  background: linear-gradient(0deg, rgba(20,18,14,0.5), transparent);
  z-index: 5;
}

/* 可交互的表面涂层 */
.tablet-surface {
  position: relative; z-index: 1;
  padding: 40px 48px 34px;
  /* 石材内部微妙的渐变 */
  background: radial-gradient(ellipse at 50% 30%, rgba(60,55,48,0.2) 0%, transparent 60%);
}

/* ===== 碑额 ===== */
.tablet-header { text-align: center; margin-bottom: 28px; }

.tablet-seal {
  width: 56px; height: 56px; margin: 0 auto 16px;
  position: relative;
  background: radial-gradient(circle at 40% 35%, #8a1a18, #3a0808);
  transform: rotate(3deg);
  clip-path: polygon(50% 0%, 80% 10%, 100% 35%, 100% 70%, 80% 100%, 50% 95%, 20% 100%, 0% 70%, 0% 35%, 20% 10%);
  box-shadow: 0 0 20px rgba(120,10,6,0.35), inset 0 0 15px rgba(0,0,0,0.5);
  animation: sealGlow 3s ease-in-out infinite;
}
.tablet-seal::after {
  content: '印';
  position: absolute; inset: 0;
  display: flex; align-items: center; justify-content: center;
  font-family: 'ZCOOL XiaoWei','STKaiti',serif;
  font-size: 20px; color: rgba(215,165,45,0.7);
  text-shadow: 0 0 6px rgba(200,140,25,0.3);
  transform: rotate(-3deg);
}
@keyframes sealGlow {
  0%,100% { box-shadow: 0 0 20px rgba(120,10,6,0.35), inset 0 0 15px rgba(0,0,0,0.5); }
  50% { box-shadow: 0 0 30px rgba(160,14,10,0.5), inset 0 0 20px rgba(0,0,0,0.6); }
}

/* 标题 — 凿刻字体 */
.tablet-title {
  display: flex; justify-content: center; gap: 4px;
  margin: 0 0 6px;
}
.title-char {
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  font-size: 34px; font-weight: 400; letter-spacing: 3px;
  /* 凿刻进石头的凹陷文字效果 */
  color: #c9a84c;
  text-shadow:
    0 1px 0 rgba(0,0,0,0.8),
    0 2px 0 rgba(0,0,0,0.6),
    0 -1px 0 rgba(255,255,255,0.08),
    1px 0 0 rgba(0,0,0,0.4),
    -1px 0 0 rgba(0,0,0,0.4),
    0 0 12px rgba(201,168,76,0.25);
  /* 斑驳效果 — 不均匀的透明度 */
  opacity: 0;
  animation: charEmerge 0.6s ease both;
  filter: brightness(0.9) contrast(1.1);
}
@keyframes charEmerge {
  from { opacity: 0; transform: translateY(-6px); filter: blur(2px) brightness(0.9) contrast(1.1); }
  to { opacity: 1; transform: translateY(0); filter: blur(0) brightness(0.9) contrast(1.1); }
}

.tablet-sub {
  font-size: 12px; color: rgba(180,145,90,0.35);
  letter-spacing: 6px; font-family: 'ZCOOL XiaoWei','STKaiti',serif;
}

/* ===== 表单字段 ===== */
.tablet-form { display: flex; flex-direction: column; gap: 16px; }

.tablet-field {
  display: flex; align-items: stretch; gap: 0;
}

/* 铭文标签 */
.field-rune {
  width: 42px; flex-shrink: 0;
  display: flex; align-items: center; justify-content: center;
  font-family: 'ZCOOL XiaoWei','STKaiti',serif;
  font-size: 16px; color: rgba(180,145,90,0.35);
  /* 像刻在石头上的篆体 */
  background:
    linear-gradient(180deg, rgba(20,18,14,0.5), rgba(30,26,20,0.5));
  border: 1px solid rgba(80,65,40,0.2);
  border-right: none;
  text-shadow: 0 1px 0 rgba(0,0,0,0.5);
}

.field-input-zone { flex: 1; position: relative; }

.field-input-zone :deep(.el-input__wrapper) {
  background: rgba(18,15,10,0.6);
  border: 1px solid rgba(80,65,40,0.2);
  border-radius: 0;
  box-shadow: none; padding: 11px 14px;
  transition: all 0.35s;
}
.field-input-zone :deep(.el-input__inner) {
  color: #c0a878;
  font-family: 'Noto Serif SC','STSong',serif;
  font-size: 14px; letter-spacing: 2px;
}
.field-input-zone :deep(.el-input__inner::placeholder) {
  color: rgba(140,115,75,0.22); letter-spacing: 1px;
}
.field-input-zone :deep(.el-input__wrapper:hover) {
  border-color: rgba(140,100,50,0.35);
}
.field-input-zone :deep(.el-input__wrapper.is-focus) {
  border-color: rgba(195,150,60,0.45);
  box-shadow: 0 0 12px rgba(180,120,40,0.1), inset 0 0 8px rgba(0,0,0,0.2);
}
.field-input-zone :deep(.el-input__suffix) { color: rgba(140,115,75,0.2); }

/* 石刻划线 */
.carve-line {
  position: absolute; bottom: 0; left: 4px; right: 4px; height: 1px;
  background: linear-gradient(90deg, transparent, rgba(195,150,60,0.3), transparent);
  pointer-events: none;
  animation: carvePulse 1.8s ease-in-out infinite;
}
@keyframes carvePulse {
  0%,100% { opacity: 0.3; }
  50% { opacity: 0.7; }
}

/* ===== 按钮 — 石雕 ===== */
.stone-btn {
  width: 100%; height: 52px; margin-top: 4px;
  position: relative;
  border: none; border-radius: 2px;
  /* 深色石材按钮 */
  background:
    linear-gradient(180deg, #3d1816 0%, #24100e 40%, #2a1412 60%, #1a0c0a 100%);
  color: #d4b868;
  cursor: pointer;
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  font-size: 18px; letter-spacing: 8px;
  overflow: hidden;
  transition: all 0.3s;
  /* 玉石质感凹陷 */
  box-shadow:
    0 2px 0 rgba(0,0,0,0.4),
    0 4px 12px rgba(0,0,0,0.3),
    inset 0 1px 0 rgba(255,255,255,0.04),
    inset 0 -1px 0 rgba(0,0,0,0.3);
}
.stone-btn:hover {
  background: linear-gradient(180deg, #4d201e, #301412, #361614, #200e0c);
  box-shadow:
    0 2px 0 rgba(0,0,0,0.4),
    0 6px 20px rgba(180,30,20,0.2),
    0 0 40px rgba(140,20,10,0.1),
    inset 0 1px 0 rgba(255,255,255,0.05),
    inset 0 -1px 0 rgba(0,0,0,0.3);
  transform: translateY(-1px);
}
.stone-btn:active { transform: scale(0.97); }
.stone-btn:disabled { opacity: 0.4; cursor: not-allowed; transform: none; }

.btn-carve {
  position: relative; z-index: 1;
  /* 凿刻文字 */
  text-shadow:
    0 1px 0 rgba(0,0,0,0.7),
    0 2px 0 rgba(0,0,0,0.5),
    0 0 10px rgba(200,140,50,0.25);
}

/* 按钮裂缝纹理 */
.btn-crack {
  position: absolute; inset: 0; pointer-events: none; z-index: 0;
  opacity: 0.25;
  background:
    repeating-linear-gradient(88deg, transparent, transparent 4px, rgba(0,0,0,0.12) 4px, rgba(0,0,0,0.12) 5px),
    repeating-linear-gradient(2deg, transparent, transparent 3px, rgba(255,255,255,0.015) 3px, rgba(255,255,255,0.015) 4px);
}

/* 点击涟漪 */
.pressed::after {
  content: '';
  position: absolute;
  inset: -8px;
  border-radius: 4px;
  border: 2px solid rgba(180,25,15,0.25);
  animation: stoneRipple 0.5s ease both;
  pointer-events: none;
}
@keyframes stoneRipple {
  0% { transform: scale(0.92); opacity: 1; }
  100% { transform: scale(1.15); opacity: 0; }
}

/* ===== 底部 ===== */
.tablet-foot {
  text-align: center; margin-top: 16px;
  color: rgba(120,90,55,0.2);
  font-size: 10px; letter-spacing: 4px;
  animation: footBreathe 4s ease-in-out infinite;
}
@keyframes footBreathe {
  0%,100% { opacity: 0.25; }
  50% { opacity: 0.5; }
}

/* ============================== 竖排铭文 ============================== */
.vert-seal {
  position: absolute; bottom: 12%; left: 3%; z-index: 15;
  display: flex; flex-direction: column; align-items: center; gap: 1px;
  transition: transform 0.08s ease-out; pointer-events: none;
}
.vs-ch {
  font-size: 15px; color: rgba(175,130,50,0.3);
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  text-shadow: 0 0 3px rgba(175,130,35,0.1);
}
.vs-dot { color: rgba(90,10,6,0.25); font-size: 9px; }

/* ============================== 响应式 ============================== */
@media (max-width: 768px) {
  .char-hei { left: 1%; top: 4%; }
  .char-bai { right: 1%; top: 6%; }
  .char-judge { left: 2%; top: 52%; }
  .char-imp { right: 2%; bottom: 12%; }
  .stone-tablet { width: 340px; }
  .tablet-surface { padding: 30px 34px 26px; }
  .tablet-layer { transform: rotate(-2deg); }
  .vert-seal { display: none; }
}
</style>
