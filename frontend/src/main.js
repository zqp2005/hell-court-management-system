import { createApp } from 'vue'
import './assets/global.css'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'

import {
  Odometer, Monitor, User, Document, Checked, Refresh,
  Warning, MagicStick, Setting, ArrowDown, FullScreen,
  VideoPause, Headset, UserFilled, Edit
} from '@element-plus/icons-vue'

const app = createApp(App)
app.use(router)
app.use(createPinia())

const icons = { Odometer, Monitor, User, Document, Checked, Refresh, Warning, MagicStick, Setting, ArrowDown, FullScreen, VideoPause, Headset, UserFilled, Edit }
for (const [key, component] of Object.entries(icons)) {
  app.component(key, component)
}

app.mount('#app')
