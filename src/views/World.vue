<template>
  <div class="world-container">
    <main class="main-content">
      <div class="container">
        <h1 class="app-title">{{ t('app.title') }}</h1>
        <h2 class="section-title">{{ t('world.dreamPlaza') }}</h2>
        <p class="section-subtitle">{{ t('world.discoverDreams') }}</p>
        
        <div v-if="loading" class="loading-state">
          <p>{{ t('world.loading') }}</p>
        </div>
        
        <div v-else-if="dreams.length > 0" class="dreams-grid">
          <div 
            v-for="dream in dreams" 
            :key="dream.id" 
            class="dream-card"
            @click="viewDream(dream.id)"
          >
            <div class="dream-author">
              <img :src="getUserById(dream.userId)?.avatar" alt="作者头像" class="author-avatar" />
              <div class="author-info">
                <span class="author-name">{{ getUserById(dream.userId)?.nickname }}</span>
                <span class="author-constellation">{{ getUserById(dream.userId)?.constellation }}</span>
              </div>
            </div>
            
            <div class="dream-preview">
              {{ truncateText(dream.content, 150) }}
            </div>
            
            <div class="dream-meta">
              <span class="dream-date">{{ formatDate(dream.createdAt) }}</span>
              <span class="dream-comments">{{ getCommentCount(dream.id) }} {{ t('world.comments') }}</span>
            </div>
          </div>
        </div>
        
        <div v-else class="empty-state">
          <p>{{ t('world.noPublicDreams') }}</p>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { dreamAPI, commentAPI } from '../services/api'
import { t } from '../locales/i18n'

const router = useRouter()
const user = ref(JSON.parse(localStorage.getItem('user')))
const dreams = ref([])
const loading = ref(true)
const comments = ref([])

// 模拟用户数据
const users = [
  { id: 1, username: 'admin1', nickname: '管理员1', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Aries', constellation: '白羊座' },
  { id: 2, username: 'admin2', nickname: '管理员2', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Taurus', constellation: '金牛座' },
  { id: 3, username: 'admin3', nickname: '管理员3', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Gemini', constellation: '双子座' }
]

onMounted(() => {
  loadPublicDreams()
  loadAllComments()
})

const loadPublicDreams = async () => {
  try {
    const response = await dreamAPI.getPublicDreams()
    if (response.success) {
      dreams.value = response.data
    }
  } catch (error) {
    console.error('加载梦境失败:', error)
    alert('加载失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const loadAllComments = async () => {
  try {
    // 这里简化处理，实际应该按需加载
    // 模拟获取所有评论
    const response = await commentAPI.getDreamComments(0) // 模拟获取所有评论
    if (response.success) {
      comments.value = response.data
    }
  } catch (error) {
    console.error('加载评论失败:', error)
  }
}

const viewDream = (dreamId) => {
  router.push(`/dream/${dreamId}`)
}

const getUserById = (userId) => {
  return users.find(u => u.id === userId)
}

const getCommentCount = (dreamId) => {
  return comments.value.filter(c => c.dreamId === dreamId).length
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
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
.world-container {
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
  margin-bottom: 5px;
  color: #333;
  text-align: center;
  font-size: 18px;
}

.section-subtitle {
  margin-bottom: 30px;
  color: #666;
  font-size: 14px;
  text-align: center;
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

.dreams-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.dream-card {
  background-color: white;
  border-radius: 8px;
  padding: 15px;
  cursor: pointer;
  border: 1px solid #e0e0e0;
  transition: all 0.2s ease;
}

.dream-card:hover {
  background-color: #f9f9f9;
}

.dream-author {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.author-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.author-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.author-name {
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.author-constellation {
  font-size: 12px;
  color: #666;
  background-color: #f5f5f5;
  padding: 1px 6px;
  border-radius: 10px;
  align-self: flex-start;
}

.dream-preview {
  margin-bottom: 15px;
  line-height: 1.6;
  color: #333;
  font-size: 14px;
  height: 100px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
}

.dream-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #666;
}

.dream-comments {
  background-color: #f5f5f5;
  padding: 2px 8px;
  border-radius: 12px;
}

@media (max-width: 768px) {
  .dreams-grid {
    grid-template-columns: 1fr;
  }
}
</style>