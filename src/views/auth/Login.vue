<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="logo">
        <h1 class="app-title">{{ t('app.title') }}</h1>
      </div>
      <h2 class="auth-title">{{ t('auth.login') }}</h2>
      
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">{{ t('auth.username') }}</label>
          <input 
            type="text" 
            id="username" 
            v-model="form.username" 
            class="input" 
            :placeholder="t('auth.username')"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="password">{{ t('auth.password') }}</label>
          <input 
            type="password" 
            id="password" 
            v-model="form.password" 
            class="input" 
            :placeholder="t('auth.password')"
            required
          />
        </div>
        
        <div class="form-group">
          <button type="submit" class="btn btn-primary w-full" :disabled="loading">
            {{ loading ? t('auth.loginLoading') : t('auth.loginButton') }}
          </button>
        </div>
        
        <div class="form-group">
          <button type="button" class="btn w-full" @click="switchToRegister">
            {{ t('auth.switchToRegister') }}
          </button>
        </div>
      </form>
      
      <div class="test-accounts">
        <h3>{{ t('auth.testAccounts') }}</h3>
        <div class="test-account-item">
          <p>{{ t('auth.username') }}: admin1</p>
          <p>{{ t('auth.password') }}: admin1</p>
        </div>
        <div class="test-account-item">
          <p>{{ t('auth.username') }}: admin2</p>
          <p>{{ t('auth.password') }}: admin2</p>
        </div>
        <div class="test-account-item">
          <p>{{ t('auth.username') }}: admin3</p>
          <p>{{ t('auth.password') }}: admin3</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { authAPI } from '../../services/api'
import { t } from '../../locales/i18n'

const router = useRouter()
const form = ref({
  username: '',
  password: ''
})
const loading = ref(false)
const error = ref('')

const handleLogin = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const response = await authAPI.login(form.value)
    if (response.success) {
      // 存储token和用户信息
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('user', JSON.stringify(response.data.user))
      // 跳转到首页
      router.push('/home')
    } else {
      error.value = response.message
    }
  } catch (err) {
    error.value = '登录失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

const switchToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.auth-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: 
    radial-gradient(circle at 10% 20%, rgba(100, 116, 252, 0.15) 0%, transparent 20%),
    radial-gradient(circle at 90% 30%, rgba(255, 107, 107, 0.15) 0%, transparent 20%),
    radial-gradient(circle at 50% 80%, rgba(107, 255, 200, 0.15) 0%, transparent 20%),
    linear-gradient(135deg, rgba(168, 237, 234, 0.1) 0%, rgba(254, 214, 227, 0.1) 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
  animation: backgroundShift 20s ease-in-out infinite;
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

.auth-container::after {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI1IiBoZWlnaHQ9IjUiPgo8cmVjdCB3aWR0aD0iNSIgaGVpZ2h0PSI1IiBmaWxsPSIjZmZmIiBmaWxsLW9wYWNpdHk9IjAuMDUiPjwvcmVjdD4KPHBhdGggZD0iTTAgNUw1IDBaTTYgNEw0IDZaTS0xIDFMMSAtMVoiIHN0cm9rZT0iIzY2N2VlYSIgc3Ryb2tlLXdpZHRoPSIxIiBmaWxsPSJub25lIiBzdHJva2Utb3BhY2l0eT0iMC4xIj48L3BhdGg+Cjwvc3ZnPg==');
  z-index: -1;
}

.auth-card {
  background: var(--card-background);
  backdrop-filter: blur(20px);
  border-radius: var(--border-radius-artistic);
  box-shadow: var(--shadow-artistic);
  padding: var(--spacing-lg);
  width: 100%;
  max-width: 450px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  position: relative;
  overflow: hidden;
  animation: fadeIn 0.8s ease-out, float 6s ease-in-out infinite;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(255, 255, 255, 0.8) 100%);
  border: 1px solid rgba(102, 126, 234, 0.2);
}

.auth-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 6px;
  background: var(--artistic-gradient-1);
  border-radius: var(--border-radius-artistic) var(--border-radius-artistic) 0 0;
  transform: scaleX(0);
  transition: transform 0.4s ease;
}

.auth-card:hover::before {
  transform: scaleX(1);
}

.auth-card::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(168, 237, 234, 0.05) 0%, rgba(254, 214, 227, 0.05) 100%);
  opacity: 0;
  transition: opacity 0.4s ease;
  z-index: -1;
}

.auth-card:hover::after {
  opacity: 1;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: var(--spacing-lg);
}

.app-title {
  font-size: 32px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0;
  font-family: var(--font-sans);
  letter-spacing: 1px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.auth-title {
  font-size: 20px;
  text-align: center;
  margin-bottom: var(--spacing-lg);
  color: var(--text-primary);
  position: relative;
  padding: 20px 0;
  font-weight: 600;
  transition: all 0.4s ease;
}

.auth-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 4px;
  background: var(--artistic-gradient-1);
  border-radius: 4px;
  transform: scaleX(0) translateX(-50%);
  transition: transform 0.4s ease;
}

.auth-title:hover::after {
  transform: scaleX(1) translateX(-50%);
}

.auth-title:hover {
  color: #667eea;
  text-shadow: 0 2px 8px rgba(102, 126, 234, 0.3);
}

.form-group {
  margin-bottom: var(--spacing-lg);
  position: relative;
}

.form-group label {
  display: block;
  margin-bottom: 12px;
  font-weight: 600;
  color: var(--text-primary);
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  font-family: var(--font-artistic);
  transition: all 0.4s ease;
  position: relative;
  padding-left: 12px;
}

.form-group label::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 20px;
  background: var(--artistic-gradient-1);
  border-radius: 4px;
  opacity: 0;
  transition: opacity 0.4s ease;
}

.form-group:hover label::before {
  opacity: 1;
}

.form-group:hover label {
  color: #667eea;
  transform: translateX(4px);
}

.w-full {
  width: 100%;
  transition: all 0.4s ease;
}

.w-full:hover {
  transform: translateY(-2px);
}

.test-accounts {
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  animation: fadeIn 1.2s ease-out;
  position: relative;
}

.test-accounts::before {
  content: '';
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 2px;
  background: var(--artistic-gradient-1);
  border-radius: 2px;
}

.test-accounts h3 {
  font-size: 18px;
  margin-bottom: var(--spacing-lg);
  color: var(--text-secondary);
  font-weight: 600;
  text-align: center;
  font-family: var(--font-artistic);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  transition: all 0.4s ease;
}

.test-accounts h3:hover {
  color: #667eea;
  text-shadow: 0 2px 4px rgba(102, 126, 234, 0.3);
}

.test-account-item {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1) 0%, rgba(118, 75, 162, 0.1) 100%);
  padding: var(--spacing-md);
  border-radius: var(--border-radius-artistic);
  margin-bottom: var(--spacing-md);
  font-size: 14px;
  border-left: 4px solid transparent;
  border-image: var(--artistic-gradient-1);
  border-image-slice: 1;
  transition: all 0.4s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
  position: relative;
  overflow: hidden;
}

.test-account-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
  opacity: 0;
  transition: opacity 0.4s ease;
  z-index: -1;
}

.test-account-item:hover {
  transform: translateX(8px) translateY(-2px);
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.15) 0%, rgba(118, 75, 162, 0.15) 100%);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.15);
}

.test-account-item:hover::before {
  opacity: 1;
}

.test-account-item p {
  margin: 8px 0;
  color: var(--text-primary);
  font-weight: 500;
  font-family: var(--font-artistic);
  transition: all 0.4s ease;
}

.test-account-item:hover p {
  color: #667eea;
  transform: translateX(4px);
}

.error-message {
  color: #f5576c;
  margin-bottom: var(--spacing-lg);
  font-size: 14px;
  text-align: center;
  padding: 16px;
  background: linear-gradient(135deg, rgba(245, 87, 108, 0.1) 0%, rgba(240, 147, 251, 0.1) 100%);
  border-radius: var(--border-radius-artistic);
  border-left: 4px solid transparent;
  border-image: var(--secondary-gradient);
  border-image-slice: 1;
  box-shadow: 0 4px 15px rgba(245, 87, 108, 0.1);
  font-family: var(--font-artistic);
  transition: all 0.4s ease;
  position: relative;
  overflow: hidden;
  animation: fadeIn 0.8s ease-out;
}

.error-message::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(245, 87, 108, 0.05) 0%, rgba(240, 147, 251, 0.05) 100%);
  opacity: 0;
  transition: opacity 0.4s ease;
  z-index: -1;
}

.error-message:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(245, 87, 108, 0.15);
}

.error-message:hover::before {
  opacity: 1;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .auth-card {
    padding: var(--spacing-md);
    max-width: 95%;
  }
  
  .auth-title {
    font-size: 24px;
    padding: 16px 0;
  }
  
  .test-accounts {
    margin-top: var(--spacing-md);
    padding-top: var(--spacing-md);
  }
  
  .test-accounts h3 {
    font-size: 16px;
    margin-bottom: var(--spacing-md);
  }
  
  .test-account-item {
    padding: var(--spacing-sm);
    margin-bottom: var(--spacing-sm);
  }
  
  .form-group {
    margin-bottom: var(--spacing-md);
  }
  
  .form-group label {
    margin-bottom: 8px;
    font-size: 13px;
  }
  
  .error-message {
    padding: 12px;
    margin-bottom: var(--spacing-md);
  }
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .test-accounts {
    border-top: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .test-account-item {
    background: rgba(102, 126, 234, 0.2);
    border-left: 3px solid #764ba2;
  }
  
  .test-account-item:hover {
    background: rgba(102, 126, 234, 0.3);
  }
  
  .error-message {
    background: rgba(245, 87, 108, 0.2);
  }
}
</style>