<template>
  <div id="app">
    <div v-if="isLoggedIn" class="main-app">
      <main class="content">
        <router-view />
      </main>
      <nav class="bottom-nav">
        <router-link to="/" class="nav-item">
          <span class="nav-icon">🏠</span>
          <span class="nav-text">{{ t('app.record') }}</span>
        </router-link>
        <router-link to="/world" class="nav-item">
          <span class="nav-icon">🌍</span>
          <span class="nav-text">{{ t('app.world') }}</span>
        </router-link>
        <router-link to="/messages" class="nav-item">
          <span class="nav-icon">💬</span>
          <span class="nav-text">{{ t('app.messages') }}</span>
        </router-link>
        <router-link to="/profile" class="nav-item">
          <span class="nav-icon">👤</span>
          <span class="nav-text">{{ t('app.profile') }}</span>
        </router-link>
        <router-link v-if="user && user.isAdmin" to="/admin/reports" class="nav-item">
          <span class="nav-icon">⚙️</span>
          <span class="nav-text">管理</span>
        </router-link>
      </nav>
      <div class="language-switcher">
        <div class="lang-dropdown">
          <button class="lang-dropdown-btn" @click="toggleLangDropdown">
            <span class="current-lang">{{ currentLocale === 'zh' ? '中文' : 'English' }}</span>
            <span class="lang-dropdown-arrow" :class="{ rotated: showLangDropdown }">{{ showLangDropdown ? '▲' : '▼' }}</span>
          </button>
          <div class="lang-dropdown-menu" v-if="showLangDropdown">
            <button 
              class="lang-dropdown-item" 
              :class="{ active: currentLocale === 'zh' }"
              @click="selectLanguage('zh')"
            >
              中文
            </button>
            <button 
              class="lang-dropdown-item" 
              :class="{ active: currentLocale === 'en' }"
              @click="selectLanguage('en')"
            >
              English
            </button>
          </div>
        </div>
      </div>
    </div>
    <router-view v-else name="auth" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { currentLocale, t, setLocale } from './locales/i18n'

const route = useRoute()
const isLoggedIn = ref(false)
const showLangDropdown = ref(false)
const user = ref(null)

// 检查登录状态的函数
const checkLoginStatus = () => {
  const token = localStorage.getItem('token')
  const userInfo = localStorage.getItem('user')
  isLoggedIn.value = !!token
  if (userInfo) {
    user.value = JSON.parse(userInfo)
  } else {
    user.value = null
  }
}

onMounted(() => {
  // 初始检查
  checkLoginStatus()
  
  // 监听本地存储变化
  window.addEventListener('storage', (event) => {
    if (event.key === 'token') {
      checkLoginStatus()
    }
  })
  
  // 点击外部关闭下拉菜单
  window.addEventListener('click', (event) => {
    const langDropdown = document.querySelector('.lang-dropdown')
    if (langDropdown && !langDropdown.contains(event.target)) {
      showLangDropdown.value = false
    }
  })
})

// 监听路由变化，更新登录状态
watch(
  () => route.path,
  () => {
    checkLoginStatus()
  }
)

// 切换语言下拉菜单
const toggleLangDropdown = () => {
  showLangDropdown.value = !showLangDropdown.value
}

// 选择语言并关闭下拉菜单
const selectLanguage = (locale) => {
  setLocale(locale)
  showLangDropdown.value = false
}

// 提供登录状态给子组件
defineExpose({
  isLoggedIn
})
</script>

<style>
:root {
  --primary-color: #4CAF50;
  --primary-hover: #45a049;
  --secondary-color: #f44336;
  --secondary-hover: #d32f2f;
  --background-color: #f9f9f9;
  --card-background: #ffffff;
  --text-primary: #333333;
  --text-secondary: #666666;
  --border-color: #e0e0e0;
  --shadow-sm: 0 1px 2px rgba(0, 0, 0, 0.1);
  --border-radius-md: 4px;
  --border-radius-lg: 8px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 24px;
  --font-sans: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: var(--font-sans);
  background-color: var(--background-color);
  color: var(--text-primary);
  line-height: 1.6;
}

#app {
  min-height: 100vh;
}

.main-app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.content {
  flex: 1;
  padding: var(--spacing-md);
  padding-bottom: 70px;
}

.container {
  max-width: 600px;
  margin: 0 auto;
}

/* 底部导航 */
.bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: var(--card-background);
  border-top: 1px solid var(--border-color);
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 8px 0;
  z-index: 100;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-decoration: none;
  color: var(--text-secondary);
  padding: 8px 16px;
  transition: all 0.3s ease;
  flex: 1;
}

.nav-item.router-link-active {
  color: var(--primary-color);
}

.nav-icon {
  font-size: 24px;
  margin-bottom: 4px;
}

.nav-text {
  font-size: 12px;
  font-weight: 500;
}

/* 按钮样式 */
.btn {
  padding: 12px 24px;
  border: none;
  border-radius: var(--border-radius-md);
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  text-decoration: none;
}

.btn-primary {
  background-color: var(--primary-color);
  color: white;
}

.btn-primary:hover {
  background-color: var(--primary-hover);
}

.btn-secondary {
  background-color: var(--secondary-color);
  color: white;
}

.btn-secondary:hover {
  background-color: var(--secondary-hover);
}

.btn-sm {
  padding: 6px 12px;
  font-size: 14px;
}

/* 输入框样式 */
.input {
  padding: 12px 16px;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  font-size: 16px;
  width: 100%;
  transition: all 0.3s ease;
  font-family: inherit;
  background-color: #f9f9f9;
}

.input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
}

/* 卡片样式 */
.card {
  background-color: var(--card-background);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
  border: 1px solid var(--border-color);
}

/* 标题样式 */
.section-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: var(--spacing-md);
  text-align: center;
  padding: 16px 0;
}

/* 加载状态 */
.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
  background-color: var(--card-background);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-sm);
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 40px;
  color: var(--text-secondary);
  background-color: var(--card-background);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-sm);
  border: 2px dashed var(--border-color);
}

/* 头像样式 */
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--border-color);
}

.avatar-large {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid var(--border-color);
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.fade-in {
  animation: fadeIn 0.5s ease-out;
}

/* 语言切换器 */
.language-switcher {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 101;
}

.lang-dropdown {
  position: relative;
  display: inline-block;
}

.lang-dropdown-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background-color: white;
  border: 1px solid var(--border-color);
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  white-space: nowrap;
}

.lang-dropdown-btn:hover {
  background-color: #f9f9f9;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.lang-dropdown-arrow {
  font-size: 10px;
  transition: transform 0.3s ease;
}

.lang-dropdown-arrow.rotated {
  transform: rotate(180deg);
}

.lang-dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 8px;
  background-color: white;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  min-width: 120px;
  z-index: 102;
  animation: dropdownFadeIn 0.3s ease;
}

.lang-dropdown-item {
  display: block;
  width: 100%;
  padding: 10px 16px;
  text-align: left;
  border: none;
  background: none;
  font-size: 14px;
  color: var(--text-primary);
  cursor: pointer;
  transition: all 0.2s ease;
  border-radius: 4px;
  margin: 2px 0;
}

.lang-dropdown-item:hover {
  background-color: #f5f5f5;
}

.lang-dropdown-item.active {
  background-color: var(--primary-color);
  color: white;
}

/* 动画效果 */
@keyframes dropdownFadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .content {
    padding: var(--spacing-md);
  }
  
  .card {
    padding: var(--spacing-md);
  }
  
  .language-switcher {
    top: 10px;
    right: 10px;
  }
  
  .lang-dropdown-btn {
    padding: 6px 12px;
    font-size: 12px;
  }
  
  .lang-dropdown-item {
    padding: 8px 12px;
    font-size: 12px;
  }
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .lang-dropdown-btn {
    background-color: #333;
    border-color: #555;
    color: #f0f0f0;
  }
  
  .lang-dropdown-btn:hover {
    background-color: #444;
  }
  
  .lang-dropdown-menu {
    background-color: #333;
    border-color: #555;
  }
  
  .lang-dropdown-item {
    color: #f0f0f0;
  }
  
  .lang-dropdown-item:hover {
    background-color: #444;
  }
}
</style>