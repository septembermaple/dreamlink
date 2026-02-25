import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  // 认证相关路由
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/auth/Login.vue'),
    meta: { requiresAuth: false },
    components: {
      auth: () => import('../views/auth/Login.vue')
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/auth/Register.vue'),
    meta: { requiresAuth: false },
    components: {
      auth: () => import('../views/auth/Register.vue')
    }
  },
  
  // 主应用路由
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/world',
    name: 'World',
    component: () => import('../views/World.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/messages',
    name: 'Messages',
    component: () => import('../views/Messages.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/Profile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/dream/:id',
    name: 'DreamDetail',
    component: () => import('../views/DreamDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/chat/:userId',
    name: 'Chat',
    component: () => import('../views/Chat.vue'),
    meta: { requiresAuth: true }
  },
  
  // 后台管理路由
  {
    path: '/admin/reports',
    name: 'AdminReports',
    component: () => import('../views/admin/Reports.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const requiresAuth = to.meta.requiresAuth !== false
  const token = localStorage.getItem('token')
  
  if (requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router