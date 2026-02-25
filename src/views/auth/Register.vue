<template>
  <div class="auth-container">
    <div class="auth-card">
      <h1 class="app-title">匹配一个梦</h1>
      <h2 class="auth-title">注册</h2>
      
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="username">用户名</label>
          <input 
            type="text" 
            id="username" 
            v-model="form.username" 
            class="input" 
            placeholder="请输入用户名"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="phone">手机号</label>
          <div class="phone-input-group">
            <input 
              type="tel" 
              id="phone" 
              v-model="form.phone" 
              class="input phone-input" 
              placeholder="请输入手机号"
              required
            />
            <button 
              type="button" 
              class="btn btn-secondary code-btn" 
              @click="sendCode" 
              :disabled="countdown > 0"
            >
              {{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
            </button>
          </div>
        </div>
        
        <div class="form-group">
          <label for="code">验证码</label>
          <input 
            type="text" 
            id="code" 
            v-model="form.code" 
            class="input" 
            placeholder="请输入验证码"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="password">密码</label>
          <input 
            type="password" 
            id="password" 
            v-model="form.password" 
            class="input" 
            placeholder="请输入密码"
            required
          />
        </div>
        
        <div class="form-group">
          <label for="constellation">星座</label>
          <select 
            id="constellation" 
            v-model="form.constellation" 
            class="input" 
            required
          >
            <option value="">请选择星座</option>
            <option value="白羊座">白羊座</option>
            <option value="金牛座">金牛座</option>
            <option value="双子座">双子座</option>
            <option value="巨蟹座">巨蟹座</option>
            <option value="狮子座">狮子座</option>
            <option value="处女座">处女座</option>
            <option value="天秤座">天秤座</option>
            <option value="天蝎座">天蝎座</option>
            <option value="射手座">射手座</option>
            <option value="摩羯座">摩羯座</option>
            <option value="水瓶座">水瓶座</option>
            <option value="双鱼座">双鱼座</option>
          </select>
        </div>
        
        <div class="form-group">
          <button type="submit" class="btn btn-primary w-full" :disabled="loading">
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </div>
        
        <div class="form-group">
          <button type="button" class="btn w-full" @click="switchToLogin">
            已有账号？去登录
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

.app-title {
  color: #4CAF50;
  font-size: 24px;
  text-align: center;
  margin-bottom: 10px;
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