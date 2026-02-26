<template>
  <div class="home-container">
    <main class="main-content">
      <div class="container">
        <h1 class="app-title">{{ t('app.title') }}</h1>
        
        <div class="dream-form-card">
          <h2 class="form-title">{{ t('home.recordDream') }}</h2>
          
          <form @submit.prevent="handleSubmit">
            <div class="form-group">
              <textarea 
                v-model="dreamContent" 
                class="dream-textarea" 
                :placeholder="t('home.dreamPlaceholder')"
                rows="10"
                maxlength="2000"
                required
              ></textarea>
              <div class="word-count">{{ dreamContent.length }}/2000</div>
            </div>
            
            <div class="form-group">
              <label class="checkbox-label">
                <input type="checkbox" v-model="isPublic" />
                <span>{{ t('home.publicDream') }}</span>
              </label>
            </div>
            
            <div class="form-actions">
              <button type="submit" class="btn btn-primary" :disabled="loading || dreamContent.length < 50">
                {{ loading ? t('home.submitLoading') : t('home.submitDream') }}
              </button>
            </div>
          </form>
        </div>
        
        <div class="recent-dreams">
          <h2 class="section-title">{{ t('home.recentDreams') }}</h2>
          <div v-if="recentDreams.length > 0" class="dreams-list">
            <div 
              v-for="dream in recentDreams" 
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
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { dreamAPI } from '../services/api'
import { t } from '../locales/i18n'

const router = useRouter()
const user = ref(JSON.parse(localStorage.getItem('user')))
const dreamContent = ref('')
const isPublic = ref(true)
const loading = ref(false)
const recentDreams = ref([])
const editingDreamId = ref(null)

const wordCount = computed(() => dreamContent.value.length)

onMounted(() => {
  loadRecentDreams()
})

const loadRecentDreams = async () => {
  try {
    // 检查用户是否已登录
    if (!user.value) {
      console.error('用户未登录')
      return
    }
    const response = await dreamAPI.getUserDreams(user.value.id)
    if (response.success) {
      recentDreams.value = response.data.slice(0, 5) // 只显示最近5个
    }
  } catch (error) {
    console.error('加载梦境失败:', error)
    // 不在这里处理登录重定向，让路由守卫处理
  }
}

const handleSubmit = async () => {
  if (dreamContent.value.length < 50) {
    alert('梦境内容至少需要50字')
    return
  }
  
  // 检查用户是否已登录
  if (!user.value) {
    alert('请先登录')
    return
  }
  
  loading.value = true
  try {
    const data = {
      userId: user.value.id,
      content: dreamContent.value,
      isPublic: isPublic.value
    }
    
    let response
    if (editingDreamId.value) {
      response = await dreamAPI.updateDream(editingDreamId.value, data)
    } else {
      response = await dreamAPI.createDream(data)
    }
    
    if (response.success) {
      // 重置表单
      dreamContent.value = ''
      isPublic.value = true
      editingDreamId.value = null
      // 重新加载梦境列表
      await loadRecentDreams()
      alert(editingDreamId.value ? t('home.updateSuccess') : t('home.submitDream'))
    } else {
      alert(response.message || '操作失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    alert('操作失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const viewDream = (dreamId) => {
  router.push(`/dream/${dreamId}`)
}

const editDream = (dream) => {
  // 检查用户是否已登录
  if (!user.value) {
    alert('请先登录')
    return
  }
  
  dreamContent.value = dream.content
  isPublic.value = dream.isPublic
  editingDreamId.value = dream.id
  // 滚动到表单顶部
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const deleteDream = async (dreamId) => {
  // 检查用户是否已登录
  if (!user.value) {
    alert('请先登录')
    return
  }
  
  if (confirm(t('home.confirmDelete'))) {
    try {
      const response = await dreamAPI.deleteDream(dreamId)
      if (response.success) {
        await loadRecentDreams()
        alert(t('home.deleteSuccess'))
      } else {
        alert(response.message || t('home.deleteFailed'))
      }
    } catch (error) {
      console.error('删除失败:', error)
      alert(t('home.deleteFailed'))
    }
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
.home-container {
  min-height: 100vh;
}

.main-content {
  padding: 20px 0;
}

.dream-form-card {
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
  animation: fadeIn 0.6s ease-out;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(255, 255, 255, 0.8) 100%);
}

.dream-form-card::before {
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

.dream-form-card:hover::before {
  transform: scaleX(1);
}

.dream-form-card:hover {
  transform: translateY(-10px) rotate(1deg);
  box-shadow: 0 20px 50px rgba(109, 89, 122, 0.2);
  border-color: rgba(181, 101, 167, 0.3);
}

.dream-form-card::after {
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

.dream-form-card:hover::after {
  opacity: 1;
}

.form-title {
  font-family: var(--font-artistic);
  font-size: 28px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: var(--spacing-lg);
  text-align: center;
  padding: 20px 0;
  position: relative;
  text-transform: uppercase;
  letter-spacing: 1.5px;
  transition: all 0.3s ease;
}

.form-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 80px;
  height: 4px;
  background: var(--primary-gradient);
  border-radius: 4px;
  transform: scaleX(0) translateX(-50%);
  transition: transform 0.3s ease;
}

.form-title:hover::after {
  transform: scaleX(1) translateX(-50%);
}

.form-title:hover {
  color: #b565a7;
  text-shadow: 0 2px 8px rgba(181, 101, 167, 0.4);
}

.dream-textarea {
  width: 100%;
  padding: 20px 24px;
  border: 2px solid rgba(0, 0, 0, 0.1);
  border-radius: var(--border-radius-artistic);
  font-size: 16px;
  resize: vertical;
  font-family: var(--font-artistic);
  margin-bottom: 12px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(10px);
  transition: all 0.4s ease;
  min-height: 220px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
  position: relative;
  overflow: hidden;
}

.dream-textarea::placeholder {
  color: var(--text-secondary);
  font-style: italic;
  transition: all 0.4s ease;
}

.dream-textarea:focus {
  outline: none;
  border-color: transparent;
  background: white;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.3), 0 4px 20px rgba(102, 126, 234, 0.1);
  transform: translateY(-2px);
}

.dream-textarea:focus::placeholder {
  color: transparent;
}

.dream-textarea:hover {
  border-color: rgba(102, 126, 234, 0.3);
  transform: translateY(-1px);
  box-shadow: 0 2px 10px rgba(102, 126, 234, 0.1);
}

.word-count {
  text-align: right;
  font-size: 14px;
  color: var(--text-secondary);
  font-weight: 500;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 14px;
  color: var(--text-primary);
  transition: all 0.3s ease;
  padding: 8px 12px;
  border-radius: var(--border-radius-md);
}

.checkbox-label:hover {
  background: rgba(102, 126, 234, 0.1);
}

.checkbox-label input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
  accent-color: #667eea;
}

.form-actions {
  margin-top: 24px;
  text-align: center;
}

.recent-dreams {
  margin-top: 40px;
}

.dreams-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.dream-item {
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
  cursor: pointer;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(255, 255, 255, 0.8) 100%);
  animation: fadeIn 0.6s ease-out;
}

.dream-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 8px;
  background: var(--secondary-gradient);
  border-radius: var(--border-radius-artistic) var(--border-radius-artistic) 0 0;
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.dream-item:hover::before {
  transform: scaleX(1);
}

.dream-item:hover {
  transform: translateY(-10px) rotate(1deg);
  box-shadow: 0 20px 50px rgba(255, 181, 181, 0.2);
  border-color: rgba(134, 117, 169, 0.3);
}

.dream-item::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(255, 181, 181, 0.05) 0%, rgba(134, 117, 169, 0.05) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
}

.dream-item:hover::after {
  opacity: 1;
}

.dream-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
  color: var(--text-secondary);
}

.dream-date {
  font-weight: 500;
}

.dream-public {
  background: var(--accent-gradient);
  color: white;
  padding: 4px 12px;
  border-radius: var(--border-radius-full);
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.dream-preview {
  margin-bottom: 16px;
  line-height: 1.6;
  color: var(--text-primary);
  font-size: 15px;
  line-height: 1.7;
}

.dream-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  padding-top: 12px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .dream-form-card {
    padding: var(--spacing-md);
  }
  
  .dream-textarea {
    padding: 14px 18px;
    font-size: 15px;
  }
  
  .form-actions {
    margin-top: 20px;
  }
  
  .dream-item {
    padding: var(--spacing-md);
  }
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .dream-textarea {
    background: rgba(255, 255, 255, 0.1);
    border-color: rgba(255, 255, 255, 0.1);
    color: var(--text-primary);
  }
  
  .dream-textarea:focus {
    background: rgba(255, 255, 255, 0.15);
  }
  
  .checkbox-label:hover {
    background: rgba(102, 126, 234, 0.2);
  }
  
  .dream-actions {
    border-top: 1px solid rgba(255, 255, 255, 0.1);
  }
}
</style>