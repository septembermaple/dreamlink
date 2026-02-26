<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="logo">
        <h1 class="app-title">{{ t('app.title') }}</h1>
      </div>
      <h2 class="auth-title">{{ t('auth.register') }}</h2>
      
      <form @submit.prevent="handleRegister">
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
          <label for="phone">{{ t('auth.phone') }}</label>
          <div class="phone-input-group">
            <input 
              type="tel" 
              id="phone" 
              v-model="form.phone" 
              class="input phone-input" 
              :placeholder="t('auth.phone')"
              required
            />
            <button 
              type="button" 
              class="btn btn-secondary code-btn" 
              @click="sendCode" 
              :disabled="countdown > 0"
            >
              {{ countdown > 0 ? `${countdown}s后重发` : t('auth.getCode') }}
            </button>
          </div>
        </div>
        
        <div class="form-group">
          <label for="code">{{ t('auth.verificationCode') }}</label>
          <input 
            type="text" 
            id="code" 
            v-model="form.code" 
            class="input" 
            :placeholder="t('auth.verificationCode')"
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
          <label for="constellation">{{ t('profile.constellation') }}</label>
          <select 
            id="constellation" 
            v-model="form.constellation" 
            class="input" 
            required
          >
            <option value="">{{ t('profile.constellation') }}</option>
            <option value="白羊座">{{ t('profile.constellations.aries') }}</option>
            <option value="金牛座">{{ t('profile.constellations.taurus') }}</option>
            <option value="双子座">{{ t('profile.constellations.gemini') }}</option>
            <option value="巨蟹座">{{ t('profile.constellations.cancer') }}</option>
            <option value="狮子座">{{ t('profile.constellations.leo') }}</option>
            <option value="处女座">{{ t('profile.constellations.virgo') }}</option>
            <option value="天秤座">{{ t('profile.constellations.libra') }}</option>
            <option value="天蝎座">{{ t('profile.constellations.scorpio') }}</option>
            <option value="射手座">{{ t('profile.constellations.sagittarius') }}</option>
            <option value="摩羯座">{{ t('profile.constellations.capricorn') }}</option>
            <option value="水瓶座">{{ t('profile.constellations.aquarius') }}</option>
            <option value="双鱼座">{{ t('profile.constellations.pisces') }}</option>
          </select>
        </div>
        
        <div class="form-group">
          <button type="submit" class="btn btn-primary w-full" :disabled="loading">
            {{ loading ? t('auth.registerLoading') : t('auth.registerButton') }}
          </button>
        </div>
        
        <div class="form-group">
          <button type="button" class="btn w-full" @click="switchToLogin">
            {{ t('auth.switchToLogin') }}
          </button>
        </div>
      </form>
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
  phone: '',
  code: '',
  password: '',
  constellation: ''
})
const loading = ref(false)
const error = ref('')
const countdown = ref(0)

const handleRegister = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const response = await authAPI.register(form.value)
    if (response.success) {
      // 存储token和用户信息
      localStorage.setItem('token', response.data.token)
      localStorage.setItem('user', JSON.stringify(response.data.user))
      // 跳转到首页
      router.push('/')
    } else {
      error.value = response.message
    }
  } catch (err) {
    error.value = '注册失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

const switchToLogin = () => {
  router.push('/login')
}

const sendCode = () => {
  // 模拟发送验证码
  if (form.value.phone) {
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer)
      }
    }, 1000)
    alert('验证码已发送（模拟）')
  } else {
    alert('请输入手机号')
  }
}
</script>

<style scoped>
.auth-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  padding: 20px;
}

.auth-card {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 400px;
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
  margin-bottom: 30px;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #555;
}

.phone-input-group {
  display: flex;
  gap: 10px;
}

.phone-input {
  flex: 1;
}

.code-btn {
  width: 120px;
  white-space: nowrap;
}

.w-full {
  width: 100%;
}

.error-message {
  color: #f44336;
  margin-bottom: 15px;
  font-size: 14px;
  text-align: center;
}
</style>