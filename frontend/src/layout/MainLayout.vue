<template>
  <!-- 鬼影漂浮背景 -->
  <div class="ghost-faces-layer" aria-hidden="true">
    <span class="ghost-face"></span>
    <span class="ghost-face"></span>
    <span class="ghost-face"></span>
    <span class="ghost-face"></span>
    <span class="ghost-face"></span>
    <span class="ghost-face"></span>
  </div>
  <!-- 血滴效果 -->
  <div class="blood-drops" aria-hidden="true">
    <span class="blood-drop"></span>
    <span class="blood-drop"></span>
    <span class="blood-drop"></span>
    <span class="blood-drop"></span>
    <span class="blood-drop"></span>
  </div>

  <el-container style="height: 100vh; position: relative; z-index: 1;">
    <el-aside width="220px" style="background: linear-gradient(180deg, #0d0a08 0%, #110d08 100%); border-right: 1px solid rgba(122,0,0,0.2);">
      <div class="logo">
        <span class="logo-icon"></span>
        <span class="logo-text">地 府 司</span>
      </div>
      <el-menu
        :default-active="route.path"
        router
        background-color="#0d0a08"
        text-color="#8a7a68"
        active-text-color="#c9a84c"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>工作台</span>
        </el-menu-item>
        <el-menu-item index="/screen">
          <el-icon><Monitor /></el-icon>
          <span>数据大屏</span>
        </el-menu-item>
        <el-menu-item index="/soul">
          <el-icon><User /></el-icon>
          <span>魂魄管理</span>
        </el-menu-item>
        <el-menu-item index="/judgment" v-if="userStore.roleCode === 'JUDGE'">
          <el-icon><Document /></el-icon>
          <span>审判管理</span>
        </el-menu-item>
        <el-menu-item index="/review" v-if="userStore.roleCode === 'KING'">
          <el-icon><Checked /></el-icon>
          <span>复核管理</span>
        </el-menu-item>
        <el-menu-item index="/rebirth" v-if="userStore.roleCode === 'KING' || userStore.roleCode === 'ADMIN'">
          <el-icon><Refresh /></el-icon>
          <span>轮回管理</span>
        </el-menu-item>
        <el-menu-item index="/hell">
          <el-icon><Warning /></el-icon>
          <span>十八层地狱</span>
        </el-menu-item>
        <el-menu-item index="/demon">
          <el-icon><MagicStick /></el-icon>
          <span>百鬼录</span>
        </el-menu-item>
        <el-sub-menu index="system" v-if="userStore.roleCode === 'ADMIN'">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user">用户管理</el-menu-item>
          <el-menu-item index="/system/log">操作日志</el-menu-item>
          <el-menu-item index="" @click="openSwagger">API文档</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background: #0d0a08; border-bottom: 1px solid rgba(122,0,0,0.15); display: flex; align-items: center; justify-content: flex-end; gap: 16px;">
        <el-button :icon="audioPlaying ? 'VideoPause' : 'Headset'" size="small" @click="toggleAudio"
          :type="audioPlaying ? 'warning' : 'default'"
          style="color: #ffd700; border-color: #ffd700; font-size: 13px; letter-spacing: 1px;">
          {{ audioPlaying ? '静音' : '阴乐' }}
        </el-button>
        <el-dropdown @command="handleCommand">
          <span style="cursor: pointer; display: flex; align-items: center; gap: 8px; color: #8a7a68;">
            <el-avatar size="small" icon="UserFilled" />
            {{ userStore.userInfo?.realName || '用户' }}
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main style="background: var(--ink);">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ref, onUnmounted } from 'vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const audioPlaying = ref(false)
let audioEl = null

function startAmbientAudio() {
  if (audioPlaying.value) return
  try {
    if (!audioEl) {
      audioEl = new Audio('/bg-music.mp3')
      audioEl.loop = true
      audioEl.volume = 0.25
    }
    audioEl.play().catch(e => console.log('播放被浏览器拦截，请点击阴乐按钮'))
    audioPlaying.value = true
  } catch (e) {
    console.error('音频启动失败:', e)
  }
}

function stopAmbientAudio() {
  if (audioEl) {
    audioEl.pause()
    audioEl.currentTime = 0
  }
  audioPlaying.value = false
}

function toggleAudio() {
  if (audioPlaying.value) stopAmbientAudio()
  else startAmbientAudio()
}


function openSwagger() {
  window.open('http://localhost:8080/doc.html', '_blank')
}

function handleCommand(command) {
  if (command === 'logout') {
    stopAmbientAudio()
    if (audioEl) { audioEl = null }
    userStore.logout()
    router.push('/login')
  }
}

onUnmounted(() => {
  stopAmbientAudio()
  if (audioEl) { audioEl = null }
})
</script>

<style scoped>
/* 鬼影 — 纯 CSS 雾状漂浮体 */
.ghost-faces-layer {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;
}
.ghost-face {
  position: absolute;
  width: 80px;
  height: 100px;
  border-radius: 50% 50% 10% 10%;
  background: radial-gradient(ellipse at 50% 30%, rgba(255,255,255,0.04) 0%, transparent 70%);
  filter: blur(12px);
  animation: wispFloat 14s ease-in-out infinite;
}
.ghost-face:nth-child(1) { top: 12%; left: 5%;  animation-delay: 0s; width: 100px; height: 120px; }
.ghost-face:nth-child(2) { top: 55%; left: 88%; animation-delay: 3s; width: 65px; height: 85px; }
.ghost-face:nth-child(3) { top: 28%; left: 48%; animation-delay: 6s; width: 120px; height: 140px; }
.ghost-face:nth-child(4) { top: 72%; left: 18%; animation-delay: 9s; width: 75px; height: 90px; }
.ghost-face:nth-child(5) { top: 8%;  left: 72%; animation-delay: 2s; width: 90px; height: 105px; }
.ghost-face:nth-child(6) { top: 42%; left: 32%; animation-delay: 5s; width: 55px; height: 70px; }

@keyframes wispFloat {
  0%, 100% { transform: translateY(0) translateX(0) scale(1); opacity: 0.25; }
  25% { transform: translateY(-35px) translateX(15px) scale(1.15); opacity: 0.4; }
  50% { transform: translateY(-15px) translateX(-10px) scale(0.9); opacity: 0.2; }
  75% { transform: translateY(-50px) translateX(-8px) scale(1.1); opacity: 0.35; }
}

/* 血滴 — 更有机 */
.blood-drops {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;
}
.blood-drop {
  position: absolute;
  width: 2px;
  background: linear-gradient(180deg, transparent, rgba(122,0,0,0.35), rgba(90,0,0,0.1));
  border-radius: 0 0 50% 50%;
  animation: bloodDrip linear infinite;
  opacity: 0.5;
}
.blood-drop:nth-child(1) { left: 12%; height: 35px; animation-duration: 9s; animation-delay: 0s; }
.blood-drop:nth-child(2) { left: 48%; height: 25px; animation-duration: 13s; animation-delay: 2s; }
.blood-drop:nth-child(3) { left: 75%; height: 45px; animation-duration: 11s; animation-delay: 5s; }
.blood-drop:nth-child(4) { left: 30%; height: 20px; animation-duration: 15s; animation-delay: 7s; }
.blood-drop:nth-child(5) { left: 62%; height: 30px; animation-duration: 10s; animation-delay: 4s; }

/* Logo — CSS 骷髅 */
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  color: var(--gold, #c9a84c);
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 400;
  border-bottom: 1px solid rgba(122,0,0,0.3);
  background: linear-gradient(180deg, rgba(122,0,0,0.08), transparent);
  animation: candleFlicker 4s ease-in-out infinite;
}
.logo-icon {
  display: block;
  width: 24px;
  height: 28px;
  position: relative;
  flex-shrink: 0;
}
/* CSS 骷髅头 */
.logo-icon::before {
  content: '';
  position: absolute;
  top: 0; left: 4px;
  width: 16px; height: 16px;
  background: var(--gold, #c9a84c);
  border-radius: 50% 50% 30% 30%;
  box-shadow: 0 0 10px rgba(201,168,76,0.5);
}
.logo-icon::after {
  content: '';
  position: absolute;
  bottom: 2px; left: 2px;
  width: 20px; height: 10px;
  border: 1.5px solid var(--gold, #c9a84c);
  border-top: none;
  border-radius: 0 0 30% 30%;
  box-shadow: 0 0 6px rgba(201,168,76,0.3);
}
</style>
