<template>
  <div class="auth-container">
    <div class="auth-card">
      <h1 class="app-title">匹配一个梦</h1>
      <h2 class="auth-title">登录</h2>
      
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">用户名/手机号</label>
          <input 
            type="text" 
            id="username" 
            v-model="form.username" 
            class="input" 
            placeholder="请输入用户名或手机号"
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
          <button type="submit" class="btn btn-primary w-full" :disabled="loading">
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </div>
        
        <div class="form-group">
          <button type="button" class="btn w-full" @click="switchToRegister">
            还没有账号？去注册
          </button>
        </div>
      </form>
      
      <div class="test-accounts">
        <h3>测试账号</h3>
        <div class="test-account-item">
          <p>账号: admin1</p>
          <p>密码: admin1</p>
        </div>
        <div class="test-account-item">
          <p>账号: admin2</p>
          <p>密码: admin2</p>
        </div>
        <div class="test-account-item">
          <p>账号: admin3</p>
          <p>密码: admin3</p>
        </div>
      </div>
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
      router.push('/')
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

.w-full {
  width: 100%;
}

.test-accounts {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.test-accounts h3 {
  font-size: 16px;
  margin-bottom: 15px;
  color: #666;
}

.test-account-item {
  background-color: #f9f9f9;
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 10px;
  font-size: 14px;
}

.test-account-item p {
  margin: 5px 0;
  color: #666;
}

.error-message {
  color: #f44336;
  margin-bottom: 15px;
  font-size: 14px;
  text-align: center;
}
</style>