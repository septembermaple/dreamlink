<template>
  <div id="app">
    <div v-if="isLoggedIn" class="main-app">
      <header class="app-header">
        <div class="logo">
          <h1 class="app-title">{{ t('app.title') }}</h1>
        </div>
      </header>
      <main class="content">
        <router-view />
      </main>
      <nav class="bottom-nav">
        <router-link to="/home" class="nav-item">
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
    <div v-else class="auth-container">
      <router-view />
    </div>
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
/* 简洁风格的颜色方案 */
:root {
  --primary-color: #4a6fa5;
  --secondary-color: #6c757d;
  --accent-color: #f8f9fa;
  --background-color: #ffffff;
  --card-background: #ffffff;
  --text-primary: #333333;
  --text-secondary: #666666;
  --border-color: #eaeaea;
  --shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 12px rgba(0, 0, 0, 0.08);
  --border-radius-sm: 8px;
  --border-radius-md: 12px;
  --border-radius-lg: 16px;
  --border-radius-full: 50px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 24px;
  --font-sans: 'Inter', 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  --primary-gradient: linear-gradient(135deg, #4a6fa5 0%, #6c757d 100%);
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
  overflow-x: hidden;
}

/* 简洁背景 */
#app {
  min-height: 100vh;
  position: relative;
  overflow-x: hidden;
  background-color: var(--background-color);
}

/* 简洁风格头部 */
.app-header {
  background: var(--card-background);
  border-bottom: 1px solid var(--border-color);
  padding: var(--spacing-md);
  position: sticky;
  top: 0;
  z-index: 99;
  box-shadow: var(--shadow-sm);
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
}

.app-title {
  font-size: 32px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  font-family: var(--font-sans);
  letter-spacing: 1px;
  text-align: center;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .app-title {
    font-size: 24px;
  }
}

#app::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(74, 111, 165, 0.05) 0%, rgba(248, 249, 250, 0.1) 100%);
  z-index: -1;
}

/* 认证页面容器 */
.auth-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.auth-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: var(--background-color);
  z-index: -1;
}

.main-app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.content {
  flex: 1;
  padding: var(--spacing-md);
  padding-top: calc(var(--spacing-md) + 80px);
  padding-bottom: 90px;
  animation: fadeIn 0.8s ease-out;
}

.container {
  max-width: 600px;
  margin: 0 auto;
}

/* 简洁风格底部导航 */
.bottom-nav {
  position: fixed;
  bottom: 16px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--card-background);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-full);
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 12px 16px;
  z-index: 100;
  box-shadow: var(--shadow-sm);
  max-width: 80%;
  transition: all 0.3s ease;
}

.bottom-nav:hover {
  box-shadow: var(--shadow-md);
  transform: translateX(-50%) translateY(-2px);
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
  border-radius: var(--border-radius-sm);
  position: relative;
}

.nav-item.router-link-active {
  color: var(--primary-color);
  font-weight: 600;
}

.nav-icon {
  font-size: 24px;
  margin-bottom: 4px;
  transition: transform 0.3s ease;
  display: block;
}

.nav-item:hover .nav-icon {
  transform: scale(1.1);
}

.nav-text {
  font-size: 10px;
  font-weight: 500;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

/* 简洁风格按钮 */
.btn {
  padding: 14px 28px;
  border: none;
  border-radius: var(--border-radius-md);
  cursor: pointer;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  text-decoration: none;
  font-family: var(--font-sans);
  box-shadow: var(--shadow-sm);
  letter-spacing: 0.5px;
}

.btn-primary {
  background: var(--primary-color);
  color: white;
}

.btn-secondary {
  background: var(--secondary-color);
  color: white;
}

.btn-accent {
  background: var(--accent-color);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
}

.btn-sm {
  padding: 10px 20px;
  font-size: 14px;
  border-radius: var(--border-radius-sm);
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.btn:active {
  transform: translateY(0);
  box-shadow: var(--shadow-sm);
}

/* 输入框样式 - 艺术创意风格 - 新设计 */
.input {
  padding: 20px 28px;
  border: 2px solid rgba(0, 0, 0, 0.05);
  border-radius: var(--border-radius-artistic);
  font-size: 16px;
  width: 100%;
  transition: all 0.3s ease;
  font-family: var(--font-artistic);
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  position: relative;
  overflow: hidden;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
}

.input::placeholder {
  color: var(--text-secondary);
  font-style: italic;
  transition: all 0.3s ease;
}

.input:focus {
  outline: none;
  border-color: transparent;
  background: white;
  box-shadow: 0 0 0 3px rgba(181, 101, 167, 0.3), 0 6px 24px rgba(181, 101, 167, 0.1);
  transform: translateY(-3px);
}

.input:focus::placeholder {
  color: transparent;
}

.input:hover {
  border-color: rgba(181, 101, 167, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(181, 101, 167, 0.1);
}

/* 特殊输入框样式 */
.input-artistic {
  border: 2px solid transparent;
  background: linear-gradient(white, white) padding-box, var(--primary-gradient) border-box;
  border-radius: var(--border-radius-artistic);
  transition: all 0.3s ease;
}

.input-artistic:focus {
  box-shadow: 0 0 0 3px rgba(181, 101, 167, 0.3), 0 6px 24px rgba(181, 101, 167, 0.1);
}

/* 卡片样式 - 艺术创意风格 - 新设计 */
.card {
  background: var(--card-background);
  backdrop-filter: blur(20px);
  border-radius: var(--border-radius-artistic);
  box-shadow: var(--shadow-md);
  padding: var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(255, 255, 255, 0.8) 100%);
}

.card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 8px;
  background: var(--primary-gradient);
  border-radius: var(--border-radius-artistic) var(--border-radius-artistic) 0 0;
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.card:hover::before {
  transform: scaleX(1);
}

.card:hover {
  transform: translateY(-10px) rotate(1deg);
  box-shadow: 0 20px 50px rgba(109, 89, 122, 0.2);
  border-color: rgba(181, 101, 167, 0.3);
}

.card::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(109, 89, 122, 0.05) 0%, rgba(181, 101, 167, 0.05) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
}

.card:hover::after {
  opacity: 1;
}

/* 特殊卡片样式 */
.card-artistic {
  border: 1px solid transparent;
  background: linear-gradient(rgba(255, 255, 255, 0.95), rgba(255, 255, 255, 0.8)) padding-box, var(--primary-gradient) border-box;
  border-radius: var(--border-radius-artistic);
  transition: all 0.3s ease;
}

.card-artistic:hover {
  box-shadow: 0 20px 50px rgba(109, 89, 122, 0.2);
}

/* 标题样式 - 艺术创意风格 - 新设计 */
.app-title {
  font-family: var(--font-artistic);
  font-size: 48px;
  font-weight: 800;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0 0 32px 0;
  text-align: center;
  position: relative;
  padding: 28px 0;
  letter-spacing: 2.5px;
  text-transform: uppercase;
  animation: titleShine 3s ease-in-out infinite alternate;
  text-shadow: 0 2px 8px rgba(181, 101, 167, 0.4);
}

.app-title::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 120%;
  height: 100%;
  background: linear-gradient(135deg, rgba(109, 89, 122, 0.1) 0%, rgba(181, 101, 167, 0.1) 100%);
  border-radius: 50%;
  z-index: -1;
  animation: titleFloat 4s ease-in-out infinite;
}

.section-title {
  font-family: var(--font-artistic);
  font-size: 32px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: var(--spacing-lg);
  text-align: center;
  padding: 24px 0;
  position: relative;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  transition: all 0.3s ease;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100px;
  height: 4px;
  background: var(--primary-gradient);
  border-radius: 4px;
  transform: scaleX(0) translateX(-50%);
  transition: transform 0.3s ease;
}

.section-title:hover::after {
  transform: scaleX(1) translateX(-50%);
}

.section-title:hover {
  color: #b565a7;
  text-shadow: 0 2px 8px rgba(181, 101, 167, 0.4);
}

/* 标题动画 */
@keyframes titleShine {
  0% {
    filter: brightness(1) drop-shadow(0 2px 4px rgba(181, 101, 167, 0.4));
  }
  100% {
    filter: brightness(1.1) drop-shadow(0 4px 8px rgba(181, 101, 167, 0.6));
  }
}

@keyframes titleFloat {
  0%, 100% {
    transform: translate(-50%, -50%) scale(1);
  }
  50% {
    transform: translate(-50%, -52%) scale(1.05);
  }
}

/* 加载状态 */
.loading-state {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 60px;
  background: var(--card-background);
  backdrop-filter: blur(20px);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 60px;
  color: var(--text-secondary);
  background: var(--card-background);
  backdrop-filter: blur(20px);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
  border: 2px dashed rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
}

.empty-state:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
  border-color: rgba(102, 126, 234, 0.6);
}

/* 头像样式 - 创意风格 */
.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

.avatar-large {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid white;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  position: relative;
}

.avatar-large::after {
  content: '';
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  background: var(--primary-gradient);
  border-radius: 50%;
  z-index: -1;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.avatar-large:hover::after {
  opacity: 1;
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.05);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.fade-in {
  animation: fadeIn 0.8s ease-out;
}

.pulse {
  animation: pulse 2s infinite;
}

.float {
  animation: float 3s ease-in-out infinite;
}

/* 语言切换器 - 创意风格 */
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
  gap: 8px;
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: var(--border-radius-full);
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: var(--shadow-sm);
  white-space: nowrap;
  position: relative;
  overflow: hidden;
}

.lang-dropdown-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: var(--primary-gradient);
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
}

.lang-dropdown-btn:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.lang-dropdown-btn:hover::before {
  opacity: 1;
}

.lang-dropdown-btn:hover {
  color: white;
}

.lang-dropdown-arrow {
  font-size: 12px;
  transition: transform 0.3s ease;
  font-weight: bold;
}

.lang-dropdown-arrow.rotated {
  transform: rotate(180deg);
}

.lang-dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 12px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-md);
  min-width: 140px;
  z-index: 102;
  animation: dropdownFadeIn 0.4s ease;
  overflow: hidden;
}

.lang-dropdown-item {
  display: block;
  width: 100%;
  padding: 12px 20px;
  text-align: left;
  border: none;
  background: none;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-primary);
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.lang-dropdown-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: var(--primary-gradient);
  transform: scaleY(0);
  transition: transform 0.3s ease;
}

.lang-dropdown-item:hover {
  background: rgba(102, 126, 234, 0.1);
  padding-left: 24px;
}

.lang-dropdown-item:hover::before {
  transform: scaleY(1);
}

.lang-dropdown-item.active {
  background: var(--primary-gradient);
  color: white;
  padding-left: 24px;
}

.lang-dropdown-item.active::before {
  transform: scaleY(1);
}

/* 动画效果 */
@keyframes dropdownFadeIn {
  from {
    opacity: 0;
    transform: translateY(-15px) scale(0.9);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
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
    top: 15px;
    right: 15px;
  }
  
  .lang-dropdown-btn {
    padding: 8px 16px;
    font-size: 12px;
  }
  
  .lang-dropdown-item {
    padding: 10px 16px;
    font-size: 12px;
  }
  
  .bottom-nav {
    bottom: 15px;
    padding: 10px 20px;
  }
  
  .nav-icon {
    font-size: 24px;
  }
  
  .nav-text {
    font-size: 10px;
  }
  
  .app-title {
    font-size: 28px;
  }
  
  .section-title {
    font-size: 20px;
  }
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  :root {
    --background-color: #1a1a2e;
    --card-background: rgba(255, 255, 255, 0.1);
    --text-primary: #f0f0f0;
    --text-secondary: #b0b0b0;
    --border-color: rgba(255, 255, 255, 0.1);
  }
  
  #app::before {
    background: 
      radial-gradient(circle at 10% 20%, rgba(100, 116, 252, 0.1) 0%, transparent 20%),
      radial-gradient(circle at 90% 30%, rgba(255, 107, 107, 0.1) 0%, transparent 20%),
      radial-gradient(circle at 50% 80%, rgba(107, 255, 200, 0.1) 0%, transparent 20%);
  }
  
  .bottom-nav {
    background: rgba(26, 26, 46, 0.95);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .card {
    background: rgba(255, 255, 255, 0.05);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .input {
    background: rgba(255, 255, 255, 0.1);
    border-color: rgba(255, 255, 255, 0.1);
    color: var(--text-primary);
  }
  
  .input:focus {
    background: rgba(255, 255, 255, 0.15);
  }
  
  .lang-dropdown-btn {
    background: rgba(26, 26, 46, 0.95);
    border: 1px solid rgba(255, 255, 255, 0.1);
    color: var(--text-primary);
  }
  
  .lang-dropdown-menu {
    background: rgba(26, 26, 46, 0.95);
    border: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .lang-dropdown-item {
    color: var(--text-primary);
  }
  
  .lang-dropdown-item:hover {
    background: rgba(102, 126, 234, 0.2);
  }
  
  .empty-state {
    background: rgba(255, 255, 255, 0.05);
    border-color: rgba(102, 126, 234, 0.4);
  }
}
</style>