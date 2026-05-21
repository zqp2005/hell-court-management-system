import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    meta: { requiresAuth: true },
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/DashboardView.vue'),
        meta: { title: '工作台' }
      },
      {
        path: 'soul',
        name: 'Soul',
        component: () => import('@/views/soul/SoulView.vue'),
        meta: { title: '魂魄管理' }
      },
      {
        path: 'judgment',
        name: 'Judgment',
        component: () => import('@/views/judgment/JudgmentView.vue'),
        meta: { title: '审判管理' }
      },
      {
        path: 'review',
        name: 'Review',
        component: () => import('@/views/review/ReviewView.vue'),
        meta: { title: '复核管理' }
      },
      {
        path: 'rebirth',
        name: 'Rebirth',
        component: () => import('@/views/rebirth/RebirthView.vue'),
        meta: { title: '轮回管理' }
      },
      {
        path: 'hell',
        name: 'Hell',
        component: () => import('@/views/hell/HellLayerView.vue'),
        meta: { title: '十八层地狱' }
      },
      {
        path: 'demon',
        name: 'Demon',
        component: () => import('@/views/demon/DemonView.vue'),
        meta: { title: '百鬼录' }
      },
      {
        path: 'system/user',
        name: 'User',
        component: () => import('@/views/system/UserView.vue'),
        meta: { title: '用户管理', roleCode: 'ADMIN' }
      },
      {
        path: 'system/log',
        name: 'Log',
        component: () => import('@/views/system/LogView.vue'),
        meta: { title: '操作日志', roleCode: 'ADMIN' }
      }
    ]
  },
  {
    path: '/screen',
    name: 'DataScreen',
    component: () => import('@/views/dashboard/DataScreenView.vue'),
    meta: { requiresAuth: true }
  },
  { path: '/:pathMatch(.*)*', component: () => import('@/views/error/404.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else if (to.meta.roleCode) {
    const userStr = localStorage.getItem('userInfo')
    if (userStr) {
      const userInfo = JSON.parse(userStr)
      if (userInfo.roleCode !== to.meta.roleCode) {
        next('/dashboard')
        return
      }
    }
    next()
  } else {
    next()
  }
})

export default router
