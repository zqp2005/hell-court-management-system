<template>
  <div class="login-scene" @mousemove="onMouse" @mouseleave="onLeave">
    <!-- ====== 背景场景 ====== -->
    <div class="scene-bg">
      <div class="blood-moon"></div>
      <div class="mountains">
        <span class="mt mt-1"></span><span class="mt mt-2"></span>
        <span class="mt mt-3"></span><span class="mt mt-4"></span>
      </div>
      <div class="ground-fog"></div><div class="ground-fog fog-2"></div>
      <span v-for="i in 40" :key="'e'+i" class="ember"
        :style="{ left: ((i*179+31)%96)+'%', animationDelay: (i*0.35)+'s', animationDuration: (6+(i*0.4)%8)+'s', width: (1.5+(i%3)*1)+'px', height: (1.5+(i%3)*1)+'px', opacity: 0.12+(i%4)*0.08 }"></span>
      <span v-for="i in 8" :key="'w'+i" class="wisp"
        :style="{ left: (5+i*11)+'%', bottom: (5+i*4)+'%', animationDelay: (i*1.5)+'s', animationDuration: (3+i*0.7)+'s' }"></span>
    </div>

    <!-- ====== 鬼怪 ====== -->
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

    <!-- ====== 石书登录卡片 ====== -->
    <div class="book-layer">
      <div class="book-shadow"></div>
      <!-- 石书主体 -->
      <div class="stone-book">
        <!-- 书脊 — 金属装订 -->
        <div class="book-spine">
          <span class="spine-rivet" v-for="i in 3" :key="'rv'+i"></span>
        </div>

        <!-- 左侧石页 — 装饰页 -->
        <div class="stone-page page-left">
          <div class="page-blood-stain bs-1"></div>
          <div class="page-blood-stain bs-2"></div>
          <div class="page-crack cr-1"></div>
          <div class="page-crack cr-2"></div>
          <div class="page-texture"></div>
          <!-- 左页铭文 -->
          <div class="left-inscription">
            <span class="insc-char" v-for="(c,i) in '陰陽生死'" :key="i" :style="{ transform: 'rotate('+[-6,8,-10,5][i]+'deg)', animationDelay: i*0.15+'s' }">{{ c }}</span>
          </div>
          <div class="left-inscription insc-2">
            <span class="insc-char insc-big" :style="{ transform: 'rotate(-4deg)', animationDelay: '0.8s' }">簿</span>
          </div>
          <!-- 血手印 -->
          <div class="blood-handprint"></div>
        </div>

        <!-- 右侧石页 — 登录表单 -->
        <div class="stone-page page-right">
          <div class="page-blood-stain bs-3"></div>
          <div class="page-crack cr-3"></div>
          <div class="page-texture"></div>

          <div class="page-content">
            <!-- 页眉 -->
            <div class="page-header-area">
              <div class="seal-mark"></div>
              <h1 class="book-title">
                <span class="bt-char" v-for="(c,i) in '地府生死簿'" :key="i" :style="{ animationDelay: i*0.1+'s' }">{{ c }}</span>
              </h1>
              <div class="book-sub">—— 冥 契 石 书 ——</div>
            </div>

            <!-- 表单 -->
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

let target = { x: -999, y: -999 }
let smooth = { x: -999, y: -999 }
let raf = null

function onMouse(e) { target.x = e.clientX; target.y = e.clientY }
function onLeave() { target.x = -999; target.y = -999 }

function track() {
  if (target.x < 0) {
    smooth.x += (-999 - smooth.x) * 0.04; smooth.y += (-999 - smooth.y) * 0.04
  } else {
    smooth.x += (target.x - smooth.x) * 0.2; smooth.y += (target.y - smooth.y) * 0.2
  }
  const w = window.innerWidth, h = window.innerHeight
  const mx = target.x < 0 ? w * 0.45 : smooth.x
  const my = target.x < 0 ? h * 0.45 : smooth.y

  document.querySelectorAll('.ghost-char').forEach((charEl) => {
    const bodyEl = charEl.querySelector('.gc-body')
    if (!bodyEl) return
    const rect = charEl.getBoundingClientRect()
    const cx = rect.left + rect.width / 2, cy = rect.top + rect.height * 0.35
    const dx = mx - cx, dy = my - cy
    const dist = Math.sqrt(dx * dx + dy * dy)
    const angle = Math.atan2(dy, dx) * (180 / Math.PI)
    const inf = Math.min(dist / 250, 1)
    bodyEl.style.transform = `rotate(${Math.max(-50,Math.min(50,angle))*inf}deg) translate(${(dx/(dist||1))*inf*4}px,${(dy/(dist||1))*inf*2}px)`
    charEl.querySelectorAll('.gc-pupil').forEach(p => {
      p.style.transform = `translate(${(dx/(dist||1))*5*inf}px,${(dy/(dist||1))*5*inf}px)`
    })
  })
  raf = requestAnimationFrame(track)
}

async function doLogin() {
  clicked.value = true; setTimeout(() => { clicked.value = false }, 600)
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
  background: radial-gradient(ellipse at 50% 65%, #0e0a14 0%, #050308 45%, #020104 100%);
  font-family: 'Noto Serif SC','STSong','SimSun',serif;
  position: relative; overflow: hidden;
  display: flex; align-items: center; justify-content: center;
  cursor: default;
}

/* ===== 血月 ===== */
.blood-moon {
  position: absolute; top: 4%; left: 56%;
  width: 220px; height: 220px;
  background: radial-gradient(circle at 45% 40%, rgba(180,30,18,0.55) 0%, rgba(110,12,8,0.2) 38%, transparent 68%);
  border-radius: 50%; filter: blur(8px);
  animation: moonPulse 5s ease-in-out infinite;
  pointer-events: none;
  box-shadow: 0 0 100px rgba(150,20,10,0.12), 0 0 250px rgba(80,8,4,0.06);
}
@keyframes moonPulse {
  0%,100% { opacity: 0.55; transform: scale(1); }
  50% { opacity: 0.85; transform: scale(1.1); }
}

/* ===== 远山 ===== */
.mountains { position: absolute; inset: 0; pointer-events: none; }
.mt { position: absolute; bottom: 0; width: 0; height: 0; border-bottom-style: solid; opacity: 0.75; }
.mt-1 { left: -10%; bottom: 16%; border-left: 240px solid transparent; border-right: 340px solid transparent; border-bottom: 190px solid rgba(4,2,8,0.92); }
.mt-2 { left: 35%; bottom: 14%; border-left: 220px solid transparent; border-right: 140px solid transparent; border-bottom: 240px solid rgba(3,1,6,0.94); }
.mt-3 { left: -5%; bottom: 12%; border-left: 300px solid transparent; border-right: 400px solid transparent; border-bottom: 140px solid rgba(5,3,9,0.88); }
.mt-4 { left: 52%; bottom: 18%; border-left: 340px solid transparent; border-right: 200px solid transparent; border-bottom: 160px solid rgba(4,2,7,0.9); }

/* ===== 雾气 ===== */
.ground-fog { position: absolute; bottom: 0; left: 0; right: 0; height: 38%; background: linear-gradient(0deg, rgba(16,10,18,0.6) 0%, rgba(10,6,13,0.25) 50%, transparent 100%); pointer-events: none; animation: fogDrift 10s ease-in-out infinite; }
.fog-2 { bottom: 5%; height: 25%; opacity: 0.35; animation: fogDrift 13s ease-in-out infinite reverse; }
@keyframes fogDrift { 0%,100% { transform: translateX(0); } 50% { transform: translateX(4%); } }

.ember { position: absolute; bottom: -10px; border-radius: 50%; background: radial-gradient(circle, rgba(190,130,45,0.3), rgba(130,20,10,0.12)); pointer-events: none; z-index: 1; animation: emberFloat linear infinite; }
@keyframes emberFloat {
  0% { transform: translateY(0) translateX(0) scale(0); opacity: 0; }
  10% { opacity: 0.45; } 50% { opacity: 0.25; transform: translateY(-50vh) translateX(15px) scale(1); }
  100% { transform: translateY(-100vh) translateX(-10px) scale(0); opacity: 0; }
}

.wisp { position: absolute; width: 10px; height: 10px; border-radius: 50%; background: radial-gradient(circle, rgba(70,160,110,0.5), rgba(40,120,70,0.15), transparent); box-shadow: 0 0 14px rgba(60,140,90,0.35), 0 0 35px rgba(40,100,60,0.15); pointer-events: none; z-index: 2; animation: wispDrift 4s ease-in-out infinite; }
@keyframes wispDrift { 0%,100% { transform: translateY(0) translateX(0); opacity: 0.25; } 25% { transform: translateY(-22px) translateX(10px); opacity: 0.65; } 50% { transform: translateY(-10px) translateX(-6px); opacity: 0.18; } 75% { transform: translateY(-32px) translateX(-14px); opacity: 0.55; } }

/* ============================== 鬼怪 ============================== */
.ghost-char { position: absolute; display: flex; flex-direction: column; align-items: center; z-index: 10; pointer-events: none; }
.char-hei  { left: 4%; top: 6%; } .char-bai  { right: 4%; top: 8%; }
.char-judge{ left: 6%; top: 52%; } .char-imp  { right: 6%; bottom: 15%; }
.gc-body { display: flex; flex-direction: column; align-items: center; transform-origin: center 70%; transition: transform 0.06s ease-out; }
.gc-head { position: relative; width: 52px; height: 52px; border-radius: 50%; margin-bottom: 1px; z-index: 3; }
.gc-eyes { position: absolute; top: 53%; left: 50%; transform: translateX(-50%); display: flex; gap: 14px; }
.gc-pupil { display: block; width: 8px; height: 8px; border-radius: 50%; transition: transform 0.04s ease-out; }
.gold-pupil { background: #ffd700; box-shadow: 0 0 7px rgba(255,215,0,0.6), 0 0 14px rgba(255,180,20,0.3); }
.dark-pupil { background: #0a0a0f; box-shadow: 0 0 4px rgba(0,0,0,0.8); }
.imp-pupil { background: #f44; box-shadow: 0 0 7px rgba(255,68,68,0.6), 0 0 14px rgba(255,30,30,0.3); }
.gc-robe { position: relative; width: 42px; height: 52px; border-radius: 50% 50% 8px 8px; }
.robe-belt { position: absolute; top: 10px; left: -2px; right: -2px; height: 4px; background: #5a3a30; border-radius: 1px; }
.gc-fire { position: absolute; bottom: -6px; width: 36px; height: 10px; background: radial-gradient(ellipse at center, rgba(160,25,15,0.3) 0%, transparent 70%); border-radius: 50%; filter: blur(6px); animation: fireFlick 2.2s ease-in-out infinite; }
@keyframes fireFlick { 0%,100% { opacity: 0.35; transform: scaleX(1); } 40% { opacity: 0.75; transform: scaleX(1.3); } 70% { opacity: 0.2; transform: scaleX(0.75); } }
.gc-mist { position: absolute; border-radius: 50%; background: radial-gradient(ellipse, rgba(20,14,10,0.45) 0%, transparent 70%); filter: blur(12px); pointer-events: none; z-index: -1; animation: mistWisp 4s ease-in-out infinite; }
@keyframes mistWisp { 0%,100% { transform: translateY(0) scale(1); opacity: 0.3; } 50% { transform: translateY(-10px) scale(1.2); opacity: 0.5; } }
.gc-name { font-size: 11px; color: rgba(170,130,70,0.38); letter-spacing: 2px; margin-top: 8px; font-family: 'ZCOOL XiaoWei','STKaiti',serif; }

.hei-head { background: radial-gradient(circle at 45% 38%, #3a3035, #1a1015); border: 2px solid #2a2025; }
.hei-hat { position: absolute; top: -34px; left: 50%; transform: translateX(-50%); width: 0; height: 0; border-left: 17px solid transparent; border-right: 17px solid transparent; border-bottom: 36px solid #1a1018; filter: drop-shadow(0 2px 4px rgba(0,0,0,0.6)); }
.hei-hat::after { content: ''; position: absolute; top: -8px; left: -4px; width: 42px; height: 6px; background: #1a1018; border-radius: 2px; }
.hei-mouth { position: absolute; bottom: 12px; left: 50%; transform: translateX(-50%); width: 16px; height: 3px; background: #4a3040; border-radius: 1px; }
.robe-hei { background: linear-gradient(180deg, #1c1618, #0f0b10); border: 1px solid #2a2028; }
.hei-chain { position: absolute; right: -24px; top: 65%; width: 32px; height: 3px; background: repeating-linear-gradient(90deg, #6a5a4a 0px, #6a5a4a 6px, #1a1510 6px, #1a1510 10px); border-radius: 1px; transform: rotate(-18deg); }

.bai-head { background: radial-gradient(circle at 45% 38%, #ede5da, #c8beb0); border: 2px solid #a09890; }
.bai-hat { position: absolute; top: -34px; left: 50%; transform: translateX(-50%); width: 0; height: 0; border-left: 17px solid transparent; border-right: 17px solid transparent; border-bottom: 36px solid #ede5da; filter: drop-shadow(0 2px 6px rgba(0,0,0,0.35)); }
.bai-hat::after { content: ''; position: absolute; top: -8px; left: -4px; width: 42px; height: 6px; background: #ede5da; border-radius: 2px; }
.bai-mouth { position: absolute; bottom: 10px; left: 50%; transform: translateX(-50%); width: 20px; height: 5px; background: #6a5a5a; border-radius: 2px; }
.robe-bai { background: linear-gradient(180deg, #ebe4dc, #ccc4b8); border: 1px solid #b0a8a0; }
.bai-fan { position: absolute; right: -20px; top: 58%; width: 24px; height: 30px; background: #f2ead8; border-radius: 50% 0 0 50%; border: 1px solid #c8b898; transform: rotate(12deg); }

.judge-head { background: radial-gradient(circle at 45% 35%, #c48060, #8a4030); border: 2px solid #6a3028; }
.judge-hat { position: absolute; top: -26px; left: 50%; transform: translateX(-50%); width: 46px; height: 20px; background: linear-gradient(180deg, #1a1018, #2a1820); border-radius: 3px 3px 0 0; }
.judge-hat::after { content: ''; position: absolute; left: -12px; right: -12px; top: 5px; height: 2px; background: #3a2a30; }
.judge-beard { position: absolute; bottom: 2px; left: 50%; transform: translateX(-50%); width: 0; height: 0; border-left: 8px solid transparent; border-right: 8px solid transparent; border-top: 14px solid #2a1a18; }
.robe-judge { background: linear-gradient(180deg, #6e1c1a, #3e0e0c); border: 1px solid #501212; }
.judge-brush { position: absolute; right: -16px; top: 52%; width: 3px; height: 34px; background: linear-gradient(180deg, #3a2010, #1a0a08, #d4b878); border-radius: 1px; }

.imp-head { background: radial-gradient(circle at 45% 35%, #5a7a5a, #2a4a2a); border: 2px solid #1a3a1a; }
.imp-horns { position: absolute; top: -16px; left: 50%; transform: translateX(-50%); display: flex; gap: 12px; }
.imp-horn { width: 0; height: 0; border-left: 5px solid transparent; border-right: 5px solid transparent; border-bottom: 15px solid #3a3a2a; }
.imp-hl { transform: rotate(-12deg); } .imp-hr { transform: rotate(12deg); }
.imp-grin { position: absolute; bottom: 8px; left: 50%; transform: translateX(-50%); width: 14px; height: 8px; background: #3a5a3a; border-radius: 0 0 50% 50%; }
.imp-grin::after { content: ''; position: absolute; top: -3px; right: 0; width: 0; height: 0; border-left: 3px solid transparent; border-right: 3px solid transparent; border-bottom: 5px solid #4a3030; }
.robe-imp { background: linear-gradient(180deg, #3c4c3c, #1c2c1c); border: 1px solid #2a3a2a; }

/* ============================== 石书主容器 ============================== */
.book-layer { position: relative; z-index: 20; perspective: 1000px; }
.book-shadow {
  position: absolute; inset: 20px -20px -20px -20px;
  background: radial-gradient(ellipse at 50% 50%, rgba(0,0,0,0.55) 0%, transparent 70%);
  filter: blur(30px); pointer-events: none; z-index: -1;
}

.stone-book {
  display: flex;
  position: relative;
  transform: rotate(-2deg) rotateY(3deg);
  transition: transform 0.4s ease;
  filter: drop-shadow(0 12px 50px rgba(0,0,0,0.6));
}
.stone-book:hover { transform: rotate(-1.5deg) rotateY(2deg); }

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

/* ===== 石页通用 ===== */
.stone-page {
  width: 230px; min-height: 440px;
  position: relative;
  overflow: hidden;
  /* 石材基色 + 纸纤维纹理 */
  background:
    radial-gradient(ellipse at 20% 30%, rgba(50,40,30,0.4) 0%, transparent 50%),
    radial-gradient(ellipse at 70% 60%, rgba(35,28,20,0.3) 0%, transparent 45%),
    repeating-linear-gradient(0deg, transparent, transparent 2px, rgba(0,0,0,0.015) 2px, rgba(0,0,0,0.015) 3px),
    repeating-linear-gradient(90deg, transparent, transparent 5px, rgba(0,0,0,0.01) 5px, rgba(0,0,0,0.01) 6px),
    linear-gradient(175deg, #3e3830 0%, #322d26 20%, #3a342e 45%, #2c2722 65%, #36312b 85%, #2a2520 100%);
  box-shadow: inset 0 0 80px rgba(0,0,0,0.2);
}

/* 石质纹理叠加 */
.page-texture {
  position: absolute; inset: 0; pointer-events: none; z-index: 1;
  background:
    radial-gradient(circle at 15% 25%, rgba(255,255,255,0.02) 0%, transparent 30%),
    radial-gradient(circle at 80% 70%, rgba(0,0,0,0.08) 0%, transparent 40%),
    repeating-linear-gradient(1deg, transparent, transparent 4px, rgba(0,0,0,0.02) 4px, rgba(0,0,0,0.02) 5px);
}

/* ===== 血迹污渍 ===== */
.page-blood-stain {
  position: absolute; pointer-events: none; z-index: 2; border-radius: 50%;
  background: radial-gradient(ellipse, rgba(110,12,8,0.18) 0%, rgba(80,6,4,0.08) 50%, transparent 70%);
  filter: blur(2px);
}
.bs-1 { width: 90px; height: 70px; top: 8%; left: 10%; animation: stainPulse 5s ease-in-out infinite; }
.bs-2 { width: 60px; height: 45px; bottom: 18%; right: 15%; animation: stainPulse 4.5s ease-in-out 1s infinite; }
.bs-3 { width: 80px; height: 55px; top: 65%; right: 12%; animation: stainPulse 5.5s ease-in-out 2s infinite; }
@keyframes stainPulse {
  0%,100% { opacity: 0.5; }
  50% { opacity: 0.8; }
}

/* ===== 裂缝 ===== */
.page-crack {
  position: absolute; pointer-events: none; z-index: 2;
  background: rgba(20,12,10,0.3);
}
.cr-1 { top: 12%; left: 25%; width: 1px; height: 35%; transform: rotate(-15deg); box-shadow: 0 0 2px rgba(0,0,0,0.2); }
.cr-2 { top: 55%; left: 60%; width: 1px; height: 25%; transform: rotate(8deg); }
.cr-3 { bottom: 20%; left: 30%; width: 1px; height: 30%; transform: rotate(-5deg); }

/* ===== 左侧页 — 装饰 ===== */
.page-left {
  border-right: 1px solid rgba(0,0,0,0.2);
  display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 30px;
  padding: 40px 20px;
  /* 微微的纸页弯曲 */
  clip-path: polygon(0% 0%, 100% 2%, 100% 98%, 0% 100%);
}

/* 左页血色铭文 — 扭曲狂草 */
.left-inscription {
  position: relative; z-index: 3;
  display: flex; flex-direction: column; align-items: center; gap: 14px;
}
.insc-char {
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  font-size: 44px; font-weight: 400;
  color: #8a1015;
  writing-mode: vertical-rl;
  /* 血色多层光晕 */
  text-shadow:
    0 0 10px rgba(140,15,20,0.6),
    0 0 25px rgba(120,8,12,0.35),
    0 0 40px rgba(100,5,8,0.2),
    0 2px 3px rgba(0,0,0,0.5),
    0 -1px 0 rgba(255,80,60,0.15);
  opacity: 0;
  animation: bloodCharEmerge 0.6s ease both;
  filter: contrast(1.2) brightness(0.85) saturate(1.5);
  position: relative;
}
/* 每个字底部渗血 */
.insc-char::after {
  content: '';
  position: absolute; bottom: -4px; left: 20%; right: 20%; height: 3px;
  background: linear-gradient(90deg, transparent, rgba(160,20,15,0.5), rgba(180,25,15,0.3), transparent);
  border-radius: 50%;
  filter: blur(2px);
  animation: bloodDripChar 2.5s ease-in-out infinite;
}
.insc-big {
  font-size: 62px;
  text-shadow:
    0 0 15px rgba(160,18,22,0.7),
    0 0 35px rgba(130,10,14,0.4),
    0 0 55px rgba(100,5,8,0.25),
    0 3px 4px rgba(0,0,0,0.5),
    0 -2px 0 rgba(255,80,50,0.18);
}
.insc-big::after {
  bottom: -6px; height: 5px;
  background: linear-gradient(90deg, transparent, rgba(180,20,15,0.6), rgba(200,30,18,0.4), transparent);
}

@keyframes bloodCharEmerge {
  from { opacity: 0; transform: scale(0.85); filter: blur(4px) contrast(1.2) brightness(0.85) saturate(1.5); }
  to { opacity: 1; transform: scale(1); filter: blur(0) contrast(1.2) brightness(0.85) saturate(1.5); }
}
@keyframes bloodDripChar {
  0%,100% { opacity: 0.3; transform: translateY(0); }
  50% { opacity: 0.8; transform: translateY(3px); }
}
.insc-2 { margin-top: 12px; }
.insc-2 .insc-char { font-size: 62px; color: #a01218; }

/* 血手印 */
.blood-handprint {
  position: relative; z-index: 3;
  width: 70px; height: 80px;
  background: radial-gradient(ellipse at 45% 25%, rgba(100,15,10,0.3), rgba(60,6,4,0.12) 40%, transparent 65%);
  border-radius: 50% 50% 45% 45%;
  filter: blur(1px);
  transform: rotate(-10deg);
  animation: handPulse 4s ease-in-out infinite;
}
@keyframes handPulse {
  0%,100% { opacity: 0.4; }
  50% { opacity: 0.7; }
}

/* ===== 右侧页 — 表单 ===== */
.page-right {
  border-left: 1px solid rgba(255,255,255,0.03);
  clip-path: polygon(0% 2%, 100% 0%, 100% 100%, 0% 98%);
}
.page-content {
  position: relative; z-index: 3;
  padding: 36px 32px;
  display: flex; flex-direction: column;
}

/* 页眉 */
.page-header-area { text-align: center; margin-bottom: 24px; }

.seal-mark {
  width: 48px; height: 48px; margin: 0 auto 12px;
  background: radial-gradient(circle at 40% 35%, #6e1010, #2e0404);
  clip-path: polygon(50% 0%, 75% 15%, 100% 35%, 100% 70%, 80% 100%, 50% 90%, 20% 100%, 0% 70%, 0% 35%, 25% 15%);
  box-shadow: 0 0 18px rgba(100,8,5,0.4), inset 0 0 12px rgba(0,0,0,0.5);
  animation: sealPulse 3s ease-in-out infinite;
  position: relative;
}
.seal-mark::after {
  content: '契';
  position: absolute; inset: 0;
  display: flex; align-items: center; justify-content: center;
  font-family: 'ZCOOL XiaoWei','STKaiti',serif;
  font-size: 18px; color: rgba(200,145,55,0.65);
  text-shadow: 0 0 5px rgba(180,120,30,0.25);
}
@keyframes sealPulse {
  0%,100% { box-shadow: 0 0 18px rgba(100,8,5,0.4), inset 0 0 12px rgba(0,0,0,0.5); }
  50% { box-shadow: 0 0 28px rgba(140,12,8,0.55), inset 0 0 18px rgba(0,0,0,0.6); }
}

/* 标题 — 石刻篆体 */
.book-title {
  display: flex; justify-content: center; gap: 6px; margin: 0 0 6px;
}
.bt-char {
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  font-size: 30px; font-weight: 400; letter-spacing: 2px;
  /* 深凿刻入石 + 血色渗出 */
  color: #b8945c;
  text-shadow:
    /* 凹陷阴影 */
    0 2px 0 rgba(0,0,0,0.8),
    0 3px 1px rgba(0,0,0,0.6),
    0 1px 0 rgba(0,0,0,0.7),
    /* 受光边缘 */
    0 -1px 0 rgba(255,255,255,0.06),
    /* 血色泛光 */
    0 0 6px rgba(120,15,8,0.2),
    0 0 15px rgba(90,8,4,0.1);
  opacity: 0;
  animation: charEmerge 0.5s ease both;
  filter: contrast(0.9) brightness(0.85) saturate(0.8);
  position: relative;
}
/* 斑驳效果 — 随机覆盖半透明噪点 */
.bt-char::after {
  content: '';
  position: absolute; inset: -2px;
  background: radial-gradient(circle at 30% 40%, transparent 60%, rgba(0,0,0,0.3) 100%);
  pointer-events: none;
  mix-blend-mode: multiply;
}

.book-sub {
  font-size: 11px; color: rgba(150,120,70,0.3);
  letter-spacing: 5px; font-family: 'ZCOOL XiaoWei','STKaiti',serif;
}

/* ===== 表单 ===== */
.book-form { display: flex; flex-direction: column; gap: 14px; }

.stone-field { display: flex; align-items: stretch; }

.sf-rune {
  width: 38px; flex-shrink: 0;
  display: flex; align-items: center; justify-content: center;
  font-family: 'ZCOOL XiaoWei','STKaiti',serif;
  font-size: 15px;
  color: rgba(120,90,50,0.3);
  background: linear-gradient(180deg, rgba(15,12,8,0.5), rgba(25,20,14,0.5));
  border: 1px solid rgba(70,50,30,0.15);
  border-right: none;
  text-shadow: 0 1px 0 rgba(0,0,0,0.5);
}

.sf-input { flex: 1; position: relative; }

.sf-input :deep(.el-input__wrapper) {
  background: rgba(14,11,7,0.55);
  border: 1px solid rgba(70,50,30,0.15);
  border-radius: 0; box-shadow: none;
  padding: 10px 12px; transition: all 0.35s;
}
.sf-input :deep(.el-input__inner) {
  color: #b09870;
  font-family: 'Noto Serif SC','STSong',serif;
  font-size: 13px; letter-spacing: 2px;
}
.sf-input :deep(.el-input__inner::placeholder) {
  color: rgba(120,90,60,0.2); letter-spacing: 1px;
}
.sf-input :deep(.el-input__wrapper:hover) {
  border-color: rgba(120,80,40,0.3);
}
.sf-input :deep(.el-input__wrapper.is-focus) {
  border-color: rgba(180,120,50,0.4);
  box-shadow: 0 0 10px rgba(150,90,30,0.08), inset 0 0 6px rgba(0,0,0,0.15);
}
.sf-input :deep(.el-input__suffix) { color: rgba(120,90,60,0.18); }

.sf-carve {
  position: absolute; bottom: 0; left: 4px; right: 4px; height: 1px;
  background: linear-gradient(90deg, transparent, rgba(170,120,50,0.25), transparent);
  pointer-events: none; animation: carvePulse 2s ease-in-out infinite;
}
@keyframes carvePulse { 0%,100% { opacity: 0.25; } 50% { opacity: 0.65; } }

/* ===== 血色按钮 ===== */
.blood-seal-btn {
  width: 100%; height: 50px; margin-top: 4px;
  position: relative; border: none; border-radius: 1px; overflow: hidden;
  cursor: pointer;
  /* 干涸的血色 */
  background: linear-gradient(180deg, #4a1214 0%, #2e0a0c 35%, #3a1012 55%, #1e0608 100%);
  color: #c09058;
  font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif;
  font-size: 17px; letter-spacing: 6px;
  transition: all 0.3s;
  box-shadow: 0 2px 0 rgba(0,0,0,0.4), 0 4px 15px rgba(0,0,0,0.3), inset 0 1px 0 rgba(255,255,255,0.03);
}
.blood-seal-btn:hover {
  background: linear-gradient(180deg, #5a1618, #3a0e10, #461416, #2a0a0c);
  box-shadow: 0 2px 0 rgba(0,0,0,0.4), 0 6px 25px rgba(160,25,18,0.2), 0 0 50px rgba(120,15,8,0.08), inset 0 1px 0 rgba(255,255,255,0.04);
  transform: translateY(-1px);
}
.blood-seal-btn:active { transform: scale(0.96); }
.blood-seal-btn:disabled { opacity: 0.4; cursor: not-allowed; transform: none; }

.bsb-text {
  position: relative; z-index: 1;
  text-shadow:
    0 1px 0 rgba(0,0,0,0.7),
    0 2px 0 rgba(0,0,0,0.5),
    0 0 8px rgba(180,100,30,0.2);
}

/* 新鲜血液渗出 */
.bsb-blood {
  position: absolute; bottom: 0; left: 0; right: 0; height: 2px;
  background: linear-gradient(90deg, transparent, rgba(200,20,12,0.5), rgba(180,15,10,0.4), transparent);
  pointer-events: none; z-index: 0;
  animation: bloodSeep 2s ease-in-out infinite;
}
@keyframes bloodSeep {
  0%,100% { opacity: 0.3; }
  50% { opacity: 0.8; }
}

/* 点击血溅 */
.pressed::after {
  content: '';
  position: absolute; inset: -10px; border-radius: 3px;
  border: 2px solid rgba(180,20,10,0.2);
  animation: bloodRipple 0.45s ease both;
  pointer-events: none;
}
@keyframes bloodRipple {
  0% { transform: scale(0.9); opacity: 0.8; }
  100% { transform: scale(1.2); opacity: 0; }
}

/* ===== 页脚 ===== */
.page-foot {
  text-align: center; margin-top: 14px;
  color: rgba(100,70,40,0.18);
  font-size: 10px; letter-spacing: 3px;
  animation: footBreathe 4s ease-in-out infinite;
}
@keyframes footBreathe { 0%,100% { opacity: 0.2; } 50% { opacity: 0.45; } }

/* ============================== 竖排铭文 ============================== */
.vert-seal {
  position: absolute; bottom: 12%; left: 3%; z-index: 15;
  display: flex; flex-direction: column; align-items: center; gap: 1px;
  pointer-events: none;
}
.vs-ch { font-size: 15px; color: rgba(150,110,40,0.25); font-family: 'ZCOOL XiaoWei','STKaiti','KaiTi',serif; text-shadow: 0 0 3px rgba(150,110,30,0.08); }
.vs-dot { color: rgba(80,10,5,0.2); font-size: 9px; }

/* ============================== 响应式 ============================== */
@media (max-width: 768px) {
  .char-hei { left: 1%; top: 3%; } .char-bai { right: 1%; top: 5%; }
  .char-judge { left: 2%; top: 50%; } .char-imp { right: 2%; bottom: 10%; }
  .stone-page { width: 160px; min-height: 380px; }
  .page-content { padding: 24px 20px; }
  .bt-char { font-size: 22px; }
  .vert-seal { display: none; }
}
</style>
