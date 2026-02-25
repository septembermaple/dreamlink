<template>
  <div class="profile-container">
    <main class="main-content">
      <div class="container">
        <h1 class="app-title">{{ t('app.title') }}</h1>
        
        <!-- 个人资料部分 -->
        <div class="profile-card">
          <div class="profile-header">
            <h2 class="section-title">{{ t('profile.personalInfo') }}</h2>
            <button class="btn btn-primary" @click="toggleEditMode">
              {{ isEditMode ? t('profile.saveProfile') : t('profile.editProfile') }}
            </button>
          </div>
          
          <div class="profile-content">
            <div class="profile-avatar">
              <img :src="userProfile.avatar" alt="头像" class="avatar-large" />
              <div v-if="isEditMode" class="avatar-upload">
                <input type="file" class="avatar-input" accept="image/*" @change="handleAvatarUpload" />
                <label class="avatar-label">{{ t('profile.changeAvatar') }}</label>
              </div>
            </div>
            
            <div class="profile-info">
              <div class="info-item">
                <label>{{ t('profile.nickname') }}</label>
                <div v-if="isEditMode">
                  <input 
                    type="text" 
                    v-model="userProfile.nickname" 
                    class="input" 
                    placeholder="请输入昵称"
                    maxlength="20"
                  />
                </div>
                <div v-else class="info-value">{{ userProfile.nickname }}</div>
              </div>
              
              <div class="info-item">
                <label>{{ t('profile.constellation') }}</label>
                <div v-if="isEditMode">
                  <select v-model="userProfile.constellation" class="input">
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
                <div v-else class="info-value">{{ userProfile.constellation }}</div>
              </div>
              
              <div class="info-item">
                <label>{{ t('profile.username') }}</label>
                <div class="info-value">{{ userProfile.username }}</div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 历史梦境记录 -->
        <div class="dreams-history">
          <h2 class="section-title">{{ t('profile.dreamHistory') }}</h2>
          
          <div v-if="loading" class="loading-state">
            <p>{{ t('world.loading') }}</p>
          </div>
          
          <div v-else-if="userDreams.length > 0" class="dreams-list">
            <div 
              v-for="dream in userDreams" 
              :key="dream.id" 
              class="dream-item"
              @click="viewDream(dream.id)"
            >
              <div class="dream-meta">
                <span class="dream-date">{{ formatDate(dream.createdAt) }}</span>
                <span v-if="dream.isPublic" class="dream-public">{{ t('common.public') }}</span>
              </div>
              <div class="dream-preview">{{ truncateText(dream.content, 100) }}</div>
              <div class="dream-actions">
                <button class="btn btn-sm" @click.stop="editDream(dream)">{{ t('home.edit') }}</button>
                <button class="btn btn-sm btn-secondary" @click.stop="deleteDream(dream.id)">{{ t('home.delete') }}</button>
              </div>
            </div>
          </div>
          
          <div v-else class="empty-state">
            <p>{{ t('home.noDreams') }}</p>
          </div>
        </div>
        
        <!-- 退出登录按钮 -->
        <div class="logout-section">
          <button class="btn btn-secondary" @click="logout">{{ t('profile.logout') }}</button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { authAPI, dreamAPI } from '../services/api'
import { t } from '../locales/i18n'

const router = useRouter()
const user = ref(JSON.parse(localStorage.getItem('user')))
const userProfile = ref({ ...user.value })
const isEditMode = ref(false)
const loading = ref(false)
const userDreams = ref([])

onMounted(() => {
  loadUserDreams()
})

const loadUserDreams = async () => {
  loading.value = true
  try {
    const response = await dreamAPI.getUserDreams(user.value.id)
    if (response.success) {
      userDreams.value = response.data
    }
  } catch (error) {
    console.error('加载梦境失败:', error)
    alert('加载失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const toggleEditMode = async () => {
  if (isEditMode.value) {
    // 保存编辑
    try {
      const response = await authAPI.updateUserInfo(user.value.id, {
        nickname: userProfile.value.nickname,
        constellation: userProfile.value.constellation
      })
      if (response.success) {
        // 更新本地存储中的用户信息
        user.value = { ...user.value, ...userProfile.value }
        localStorage.setItem('user', JSON.stringify(user.value))
        isEditMode.value = false
        alert(t('profile.saveSuccess'))
      } else {
        alert(t('profile.saveFailed'))
      }
    } catch (error) {
      console.error('保存失败:', error)
      alert(t('profile.saveFailed'))
    }
  } else {
    // 进入编辑模式
    isEditMode.value = true
  }
}

const handleAvatarUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    // 这里简化处理，实际应该上传文件到服务器
    // 模拟上传成功
    alert(t('profile.avatarUpload'))
    // 生成一个随机的头像URL
    const randomAvatar = `https://via.placeholder.com/150?text=${Math.random().toString(36).substr(2, 5)}`
    userProfile.value.avatar = randomAvatar
  }
}

const viewDream = (dreamId) => {
  router.push(`/dream/${dreamId}`)
}

const editDream = (dream) => {
  // 跳转到首页并传递编辑参数
  router.push({
    path: '/',
    query: { edit: dream.id }
  })
}

const deleteDream = async (dreamId) => {
  if (confirm(t('home.confirmDelete'))) {
    try {
      const response = await dreamAPI.deleteDream(dreamId)
      if (response.success) {
        userDreams.value = userDreams.value.filter(d => d.id !== dreamId)
        alert(t('home.deleteSuccess'))
      } else {
        alert(t('home.deleteFailed'))
      }
    } catch (error) {
      console.error('删除失败:', error)
      alert(t('home.deleteFailed'))
    }
  }
}

const logout = () => {
  if (confirm(t('profile.confirmLogout'))) {
    // 清除本地存储
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    // 跳转到登录页
    router.push('/login')
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const truncateText = (text, maxLength) => {
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
}

.main-content {
  padding: 20px 0;
}

.app-title {
  font-size: 36px;
  color: #4CAF50;
  margin: 0 0 20px 0;
  text-align: center;
  font-weight: bold;
}

.section-title {
  margin-bottom: 20px;
  color: #333;
  text-align: center;
  font-size: 18px;
}

.profile-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
  border: 1px solid #e0e0e0;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.profile-content {
  display: flex;
  gap: 30px;
  align-items: flex-start;
}

.profile-avatar {
  position: relative;
}

.avatar-large {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #f5f5f5;
}

.avatar-upload {
  margin-top: 10px;
  text-align: center;
}

.avatar-input {
  display: none;
}

.avatar-label {
  display: inline-block;
  padding: 5px 15px;
  background-color: #f5f5f5;
  border-radius: 16px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.avatar-label:hover {
  background-color: #e0e0e0;
}

.profile-info {
  flex: 1;
  min-width: 0;
}

.info-item {
  margin-bottom: 20px;
}

.info-item label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #555;
}

.info-value {
  font-size: 16px;
  color: #333;
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.dreams-history {
  margin-top: 40px;
}

.dreams-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.dream-item {
  background-color: white;
  border-radius: 8px;
  padding: 15px;
  cursor: pointer;
  border: 1px solid #e0e0e0;
  transition: all 0.2s ease;
}

.dream-item:hover {
  background-color: #f9f9f9;
}

.dream-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-size: 14px;
  color: #666;
}

.dream-public {
  background-color: #4CAF50;
  color: white;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
}

.dream-preview {
  margin-bottom: 15px;
  line-height: 1.6;
  color: #333;
}

.dream-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}

.btn-sm {
  padding: 6px 12px;
  font-size: 14px;
}

.loading-state,
.empty-state {
  text-align: center;
  padding: 40px;
  color: #666;
  background-color: white;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  margin: 20px 0;
}

.logout-section {
  margin-top: 40px;
  text-align: center;
}

@media (max-width: 768px) {
  .profile-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .info-item {
    text-align: left;
  }
}
</style>