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

.section-subtitle {
  margin-bottom: var(--spacing-lg);
  color: var(--text-secondary);
  font-size: 16px;
  text-align: center;
  font-weight: 500;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
  line-height: 1.6;
}

.dreams-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: var(--spacing-lg);
  margin-top: var(--spacing-lg);
}

.dream-card {
  background: var(--card-background);
  backdrop-filter: blur(20px);
  border-radius: var(--border-radius-artistic);
  box-shadow: var(--shadow-md);
  padding: var(--spacing-lg);
  cursor: pointer;
  border: 1px solid rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  animation: fadeIn 0.6s ease-out;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(255, 255, 255, 0.8) 100%);
}

.dream-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 6px;
  background: var(--artistic-gradient-3);
  border-radius: var(--border-radius-artistic) var(--border-radius-artistic) 0 0;
  transform: scaleX(0);
  transition: transform 0.4s ease;
}

.dream-card:hover::before {
  transform: scaleX(1);
}

.dream-card:hover {
  transform: translateY(-8px) rotate(1deg);
  box-shadow: 0 15px 45px rgba(79, 172, 254, 0.2);
  border-color: rgba(79, 172, 254, 0.3);
}

.dream-card::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(79, 172, 254, 0.05) 0%, rgba(0, 242, 254, 0.05) 100%);
  opacity: 0;
  transition: opacity 0.4s ease;
  z-index: -1;
}

.dream-card:hover::after {
  opacity: 1;
}

.dream-author {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: var(--spacing-md);
  padding-bottom: var(--spacing-sm);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.author-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.author-avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

.author-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
}

.author-name {
  font-weight: 600;
  color: var(--text-primary);
  font-size: 15px;
  transition: all 0.3s ease;
}

.author-avatar:hover + .author-info .author-name {
  color: #667eea;
}

.author-constellation {
  font-size: 12px;
  color: white;
  background: var(--primary-gradient);
  padding: 4px 12px;
  border-radius: var(--border-radius-full);
  align-self: flex-start;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.dream-preview {
  margin-bottom: var(--spacing-md);
  line-height: 1.7;
  color: var(--text-primary);
  font-size: 15px;
  height: 120px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  position: relative;
}

.dream-preview::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 30px;
  background: linear-gradient(to bottom, transparent, var(--card-background));
  pointer-events: none;
}

.dream-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: var(--text-secondary);
  font-weight: 500;
}

.dream-date {
  transition: all 0.3s ease;
}

.dream-comments {
  background: var(--secondary-gradient);
  color: white;
  padding: 4px 12px;
  border-radius: var(--border-radius-full);
  font-size: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 4px;
  transition: all 0.3s ease;
}

.dream-comments::before {
  content: '💬';
  font-size: 12px;
}

.dream-card:hover .dream-comments {
  transform: scale(1.05);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .dreams-grid {
    grid-template-columns: 1fr;
    gap: var(--spacing-md);
  }
  
  .dream-card {
    padding: var(--spacing-md);
  }
  
  .author-avatar {
    width: 40px;
    height: 40px;
  }
  
  .author-name {
    font-size: 14px;
  }
  
  .author-constellation {
    font-size: 11px;
    padding: 3px 10px;
  }
  
  .dream-preview {
    font-size: 14px;
    height: 100px;
  }
}

/* 暗色模式支持 */
@media (prefers-color-scheme: dark) {
  .dream-author {
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  }
  
  .dream-preview::after {
    background: linear-gradient(to bottom, transparent, var(--card-background));
  }
}
</style>