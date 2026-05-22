<template>
  <router-view v-slot="{ Component }">
    <transition name="ghost" mode="out-in">
      <component :is="Component" />
    </transition>
  </router-view>
</template>

<script setup>
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
html, body, #app {
  height: 100%;
  font-family: 'Microsoft YaHei', sans-serif;
}

/* 页面过渡 — 鬼影浮现 */
.ghost-enter-active {
  transition: all 0.35s cubic-bezier(0.22, 0.61, 0.36, 1);
}
.ghost-leave-active {
  transition: all 0.2s cubic-bezier(0.55, 0.06, 0.68, 0.19);
}
.ghost-enter-from {
  opacity: 0;
  transform: translateY(18px);
  filter: blur(4px);
}
.ghost-leave-to {
  opacity: 0;
  transform: translateY(-8px);
  filter: blur(2px);
}
</style>
