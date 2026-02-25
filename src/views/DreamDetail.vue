<template>
  <div class="dream-detail-container">
    <header class="header">
      <div class="header-left">
        <button class="btn btn-back" @click="goBack">← 返回</button>
      </div>
      <h1 class="app-title">梦境详情</h1>
      <div class="header-right">
        <router-link to="/profile" class="avatar-link">
          <img :src="user?.avatar" alt="头像" class="avatar" />
        </router-link>
      </div>
    </header>
    
    <main class="main-content">
      <div class="container">
        <div v-if="loading" class="loading-state">
          <p>加载中...</p>
        </div>
        <div v-else-if="dream" class="dream-detail-card card">
          <div class="dream-meta">
            <div class="dream-author">
              <img :src="dreamAuthor?.avatar" alt="作者头像" class="author-avatar" />
              <span class="author-name">{{ dreamAuthor?.nickname }}</span>
              <span class="author-constellation">{{ dreamAuthor?.constellation }}</span>
            </div>
            <div class="dream-date">{{ formatDate(dream.createdAt) }}</div>
          </div>
          
          <div class="dream-content">
            {{ dream.content }}
          </div>
          
          <div class="dream-tags">
            <span v-if="dream.isPublic" class="tag tag-public">公开</span>
          </div>
          
          <div class="dream-actions">
            <div v-if="isOwnDream" class="owner-actions">
              <button class="btn btn-primary" @click="editDream">编辑</button>
              <button class="btn btn-secondary" @click="deleteDream">删除</button>
            </div>
            <button v-else class="btn btn-report" @click="openReportModal">举报</button>
          </div>
        </div>
        <div v-else class="error-state">
          <p>梦境不存在</p>
        </div>
        
        <!-- 评论部分 -->
        <div class="comments-section">
          <h3 class="section-title">评论</h3>
          <div class="comment-form">
            <textarea 
              v-model="commentContent" 
              class="comment-textarea" 
              placeholder="写下你的评论..."
              rows="3"
              maxlength="500"
            ></textarea>
            <div class="comment-actions">
              <button class="btn btn-primary" @click="submitComment" :disabled="!commentContent.trim()">
                发表评论
              </button>
            </div>
          </div>
          
          <div class="comments-list">
            <div 
              v-for="comment in comments" 
              :key="comment.id" 
              class="comment-item"
            >
              <div class="comment-header">
                <div class="comment-author">
                  <img :src="getUserById(comment.userId)?.avatar" alt="评论者头像" class="comment-avatar" />
                  <span class="comment-author-name">{{ getUserById(comment.userId)?.nickname }}</span>
                </div>
                <div class="comment-time">{{ formatDate(comment.createdAt) }}</div>
              </div>
              <div class="comment-content">{{ comment.content }}</div>
            </div>
            <div v-if="comments.length === 0" class="empty-comments">
              <p>暂无评论，快来发表第一条评论吧！</p>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 举报弹窗 -->
    <div v-if="showReportModal" class="modal-overlay" @click="closeReportModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>举报梦境</h3>
          <button class="modal-close" @click="closeReportModal">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="report-reason">举报原因</label>
            <select 
              id="report-reason" 
              v-model="reportReason"
              class="form-select"
              :disabled="reporting"
            >
              <option value="">请选择原因</option>
              <option value="内容不当">内容不当</option>
              <option value="抄袭侵权">抄袭侵权</option>
              <option value="垃圾信息">垃圾信息</option>
              <option value="其他">其他</option>
            </select>
          </div>
          <div class="form-group">
            <label for="report-description">详细描述（可选）</label>
            <textarea 
              id="report-description"
              v-model="reportDescription"
              class="form-textarea"
              placeholder="请详细描述您的举报原因..."
              rows="4"
              maxlength="500"
              :disabled="reporting"
            ></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button 
            class="btn btn-secondary" 
            @click="closeReportModal"
            :disabled="reporting"
          >
            取消
          </button>
          <button 
            class="btn btn-primary" 
            @click="submitReport"
            :disabled="reporting || !reportReason"
          >
            {{ reporting ? '提交中...' : '提交举报' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { dreamAPI, commentAPI, reportAPI } from '../services/api'

const router = useRouter()
const route = useRoute()
const dreamId = computed(() => parseInt(route.params.id))

const user = ref(JSON.parse(localStorage.getItem('user')))
const dream = ref(null)
const dreamAuthor = ref(null)
const comments = ref([])
const loading = ref(true)
const commentContent = ref('')

// 举报相关状态
const showReportModal = ref(false)
const reportReason = ref('')
const reportDescription = ref('')
const reporting = ref(false)

// 模拟用户数据
const users = [
  { id: 1, username: 'admin1', nickname: '管理员1', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Aries', constellation: '白羊座' },
  { id: 2, username: 'admin2', nickname: '管理员2', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Taurus', constellation: '金牛座' },
  { id: 3, username: 'admin3', nickname: '管理员3', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Gemini', constellation: '双子座' }
]

const isOwnDream = computed(() => dream.value && user.value && dream.value.userId === user.value.id)

onMounted(() => {
  loadDreamDetail()
  loadComments()
})

const loadDreamDetail = async () => {
  try {
    const response = await dreamAPI.getDreamDetail(dreamId.value)
    if (response.success) {
      dream.value = response.data
      // 获取作者信息
      dreamAuthor.value = users.find(u => u.id === dream.value.userId)
    } else {
      alert('加载梦境失败')
      goBack()
    }
  } catch (error) {
    console.error('加载梦境失败:', error)
    alert('加载失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const loadComments = async () => {
  try {
    const response = await commentAPI.getDreamComments(dreamId.value)
    if (response.success) {
      comments.value = response.data
    }
  } catch (error) {
    console.error('加载评论失败:', error)
  }
}

const submitComment = async () => {
  if (!commentContent.value.trim()) return
  
  try {
    const response = await commentAPI.createComment({
      dreamId: dreamId.value,
      userId: user.value.id,
      content: commentContent.value
    })
    if (response.success) {
      comments.value.unshift(response.data)
      commentContent.value = ''
      alert('评论成功')
    } else {
      alert('评论失败')
    }
  } catch (error) {
    console.error('评论失败:', error)
    alert('评论失败，请稍后重试')
  }
}

const editDream = () => {
  // 跳转到首页并传递编辑参数
  router.push({
    path: '/',
    query: { edit: dreamId.value }
  })
}

const deleteDream = async () => {
  if (confirm('确定要删除这个梦境吗？')) {
    try {
      const response = await dreamAPI.deleteDream(dreamId.value)
      if (response.success) {
        alert('删除成功')
        goBack()
      } else {
        alert('删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      alert('删除失败，请稍后重试')
    }
  }
}

const goBack = () => {
  router.back()
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

const getUserById = (userId) => {
  return users.find(u => u.id === userId)
}

// 举报相关方法
const openReportModal = () => {
  if (!user.value) {
    alert('请先登录')
    router.push('/login')
    return
  }
  showReportModal.value = true
}

const closeReportModal = () => {
  showReportModal.value = false
  reportReason.value = ''
  reportDescription.value = ''
}

const submitReport = async () => {
  if (!reportReason.value) {
    alert('请选择举报原因')
    return
  }
  
  reporting.value = true
  try {
    const response = await reportAPI.createReport({
      dreamId: dreamId.value,
      reporterId: user.value.id,
      reason: reportReason.value,
      description: reportDescription.value
    })
    if (response.success) {
      alert('举报成功，我们会尽快处理')
      closeReportModal()
    } else {
      alert('举报失败，请稍后重试')
    }
  } catch (error) {
    console.error('举报失败:', error)
    alert('举报失败，请稍后重试')
  } finally {
    reporting.value = false
  }
}
</script>

<style scoped>
.dream-detail-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  padding: 20px 0;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left,
.header-right {
  flex: 1;
}

.header-left {
  text-align: left;
}

.header-right {
  text-align: right;
}

.btn-back {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.btn-back:hover {
  background-color: #f5f5f5;
}

.app-title {
  flex: 2;
  text-align: center;
  font-size: 20px;
  color: #4CAF50;
  margin: 0;
}

.loading-state,
.error-state {
  text-align: center;
  padding: 40px 0;
  color: #666;
}

.dream-detail-card {
  margin-bottom: 30px;
}

.dream-meta {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.dream-author {
  display: flex;
  align-items: center;
  gap: 10px;
}

.author-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.author-name {
  font-weight: 500;
  color: #333;
}

.author-constellation {
  font-size: 12px;
  color: #666;
  background-color: #f5f5f5;
  padding: 2px 8px;
  border-radius: 12px;
}

.dream-date {
  font-size: 14px;
  color: #666;
}

.dream-content {
  line-height: 1.8;
  color: #333;
  margin-bottom: 20px;
  white-space: pre-wrap;
}

.dream-tags {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.tag {
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 14px;
}

.tag-public {
  background-color: #4CAF50;
  color: white;
}

.dream-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  align-items: center;
}

.owner-actions {
  display: flex;
  gap: 10px;
}

.btn-report {
  background-color: #ff4444;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-report:hover {
  background-color: #cc0000;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  animation: modalFadeIn 0.3s ease;
}

@keyframes modalFadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  color: #333;
}

.modal-close {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.modal-close:hover {
  background-color: #f5f5f5;
  color: #333;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
}

.form-select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  background-color: #fff;
  transition: border-color 0.3s ease;
}

.form-select:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
}

.form-textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  resize: vertical;
  font-family: inherit;
  transition: border-color 0.3s ease;
}

.form-textarea:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
}

.modal-footer {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  padding: 20px;
  border-top: 1px solid #eee;
  background-color: #f9f9f9;
  border-radius: 0 0 8px 8px;
}

.comments-section {
  margin-top: 30px;
}

.section-title {
  margin-bottom: 20px;
  color: #333;
}

.comment-form {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.comment-textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  resize: vertical;
  font-family: inherit;
  margin-bottom: 10px;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.comment-item {
  background-color: white;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.comment-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.comment-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  object-fit: cover;
}

.comment-author-name {
  font-weight: 500;
  font-size: 14px;
  color: #333;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-content {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
}

.empty-comments {
  text-align: center;
  padding: 40px 0;
  color: #666;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>