<template>
  <div class="chat-detail-container">
    <header class="chat-header">
      <div class="header-left">
        <button class="btn btn-back" @click="goBack">← 返回</button>
        <div class="chat-partner-info">
          <img :src="chatPartner?.avatar" alt="头像" class="partner-avatar" />
          <div class="partner-details">
            <h3 class="partner-name">{{ chatPartner?.nickname }}</h3>
            <span class="partner-constellation">{{ chatPartner?.constellation }}</span>
          </div>
        </div>
      </div>
    </header>
    
    <main class="chat-content">
      <div class="messages-container">
        <!-- 系统消息：匹配提示 -->
        <div v-if="showMatchPrompt" class="system-message match-prompt">
          <p>{{ matchPrompt }}</p>
        </div>
        
        <!-- 聊天消息 -->
        <div 
          v-for="message in messages" 
          :key="message.id" 
          class="message-item"
          :class="{ 'my-message': message.userId === user?.id, 'their-message': message.userId !== user?.id }"
        >
          <div v-if="message.userId !== user?.id" class="message-avatar">
            <img :src="chatPartner?.avatar" alt="头像" />
          </div>
          <div class="message-content">
            <div class="message-text">{{ message.content }}</div>
            <div class="message-time">{{ formatDate(message.createdAt) }}</div>
          </div>
          <div v-if="message.userId === user?.id" class="message-avatar">
            <img :src="user?.avatar" alt="头像" />
          </div>
        </div>
      </div>
      
      <!-- 聊天输入框 -->
      <div class="chat-input-container">
        <textarea 
          v-model="messageInput" 
          class="message-input" 
          placeholder="输入消息..."
          rows="1"
          maxlength="500"
          @keydown.enter.exact="sendMessage"
          @keydown.enter.shift="" 
        ></textarea>
        <button class="btn btn-primary send-btn" @click="sendMessage" :disabled="!messageInput.trim()">
          发送
        </button>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { messageAPI } from '../services/api'

const router = useRouter()
const route = useRoute()
const userId = computed(() => parseInt(route.params.userId))

const user = ref(JSON.parse(localStorage.getItem('user')))
const chatPartner = ref(null)
const messages = ref([])
const messageInput = ref('')
const showMatchPrompt = ref(true)

// 模拟用户数据
const users = [
  { id: 1, username: 'admin1', nickname: '管理员1', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Aries', constellation: '白羊座' },
  { id: 2, username: 'admin2', nickname: '管理员2', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Taurus', constellation: '金牛座' },
  { id: 3, username: 'admin3', nickname: '管理员3', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Gemini', constellation: '双子座' }
]

// 模拟匹配提示信息
const matchPrompt = ref('你们都梦见了会说话的猫，不如从这里开始聊聊？')

onMounted(() => {
  loadChatPartner()
  loadChatHistory()
})

const loadChatPartner = () => {
  chatPartner.value = users.find(u => u.id === userId.value)
}

const loadChatHistory = async () => {
  try {
    const response = await messageAPI.getChatHistory(user.value.id, userId.value)
    if (response.success) {
      messages.value = response.data
    }
  } catch (error) {
    console.error('加载聊天记录失败:', error)
  }
}

const sendMessage = async () => {
  if (!messageInput.value.trim()) return
  
  try {
    const response = await messageAPI.sendMessage({
      userId: user.value.id,
      otherUserId: userId.value,
      content: messageInput.value
    })
    if (response.success) {
      messages.value.push(response.data)
      messageInput.value = ''
      // 滚动到底部
      scrollToBottom()
    } else {
      alert('发送失败')
    }
  } catch (error) {
    console.error('发送失败:', error)
    alert('发送失败，请稍后重试')
  }
}

const goBack = () => {
  router.back()
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

const scrollToBottom = () => {
  setTimeout(() => {
    const messagesContainer = document.querySelector('.messages-container')
    if (messagesContainer) {
      messagesContainer.scrollTop = messagesContainer.scrollHeight
    }
  }, 100)
}

// 监听消息变化，自动滚动到底部
watch(messages, () => {
  scrollToBottom()
}, { deep: true })
</script>

<style scoped>
.chat-detail-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.chat-header {
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

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
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

.chat-partner-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.partner-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.partner-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.partner-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin: 0;
}

.partner-constellation {
  font-size: 12px;
  color: #666;
  background-color: #f5f5f5;
  padding: 1px 6px;
  border-radius: 10px;
  align-self: flex-start;
}

.chat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.messages-container {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.system-message {
  background-color: #e3f2fd;
  color: #1976d2;
  padding: 10px 15px;
  border-radius: 16px;
  text-align: center;
  font-size: 14px;
  align-self: center;
  max-width: 80%;
  margin: 0 auto;
}

.match-prompt {
  background-color: #f3e5f5;
  color: #7b1fa2;
  border: 1px solid #ce93d8;
}

.message-item {
  display: flex;
  align-items: flex-end;
  gap: 10px;
  max-width: 70%;
}

.my-message {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.their-message {
  align-self: flex-start;
}

.message-avatar img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  object-fit: cover;
}

.message-content {
  flex: 1;
}

.message-text {
  padding: 10px 15px;
  border-radius: 18px;
  font-size: 14px;
  line-height: 1.4;
  word-wrap: break-word;
}

.my-message .message-text {
  background-color: #4CAF50;
  color: white;
  border-bottom-right-radius: 4px;
}

.their-message .message-text {
  background-color: white;
  color: #333;
  border-bottom-left-radius: 4px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.message-time {
  font-size: 10px;
  color: #666;
  margin-top: 4px;
  text-align: right;
}

.their-message .message-time {
  text-align: left;
}

.chat-input-container {
  padding: 15px 20px;
  background-color: white;
  box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  gap: 10px;
  align-items: flex-end;
}

.message-input {
  flex: 1;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 20px;
  font-size: 14px;
  resize: none;
  min-height: 40px;
  max-height: 120px;
  font-family: inherit;
}

.send-btn {
  padding: 10px 20px;
  border-radius: 20px;
  font-size: 14px;
  white-space: nowrap;
}

@media (max-width: 768px) {
  .message-item {
    max-width: 85%;
  }
}
</style>