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
    const response = await dreamAPI.getUserDreams(user.value.id)
    if (response.success) {
      recentDreams.value = response.data.slice(0, 5) // 只显示最近5个
    }
  } catch (error) {
    console.error('加载梦境失败:', error)
  }
}

const handleSubmit = async () => {
  if (dreamContent.value.length < 50) {
    alert('梦境内容至少需要50字')
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
  dreamContent.value = dream.content
  isPublic.value = dream.isPublic
  editingDreamId.value = dream.id
  // 滚动到表单顶部
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const deleteDream = async (dreamId) => {
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

.app-title {
  font-size: 36px;
  color: #4CAF50;
  margin: 0 0 30px 0;
  text-align: center;
  font-weight: bold;
}

.dream-form-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
  border: 1px solid #e0e0e0;
}

.form-title {
  margin-bottom: 20px;
  color: #333;
  font-size: 18px;
}

.dream-textarea {
  width: 100%;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  resize: vertical;
  font-family: inherit;
  margin-bottom: 10px;
  background-color: #f9f9f9;
}

.word-count {
  text-align: right;
  font-size: 14px;
  color: #666;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 14px;
}

.form-actions {
  margin-top: 20px;
  text-align: center;
}

.section-title {
  margin: 30px 0 20px 0;
  color: #333;
  font-size: 18px;
  text-align: center;
}

.recent-dreams {
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
  padding: 5px 10px;
  font-size: 14px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #666;
  background-color: white;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  margin-top: 20px;
}
</style>