<template>
  <div class="messages-container">
    <main class="main-content">
      <div class="container">
        <h1 class="app-title">{{ t('app.title') }}</h1>
        <h2 class="section-title">{{ t('app.messages') }}</h2>
        
        <div class="messages-tabs">
          <button 
            class="tab-btn" 
            :class="{ active: activeTab === 'chats' }" 
            @click="activeTab = 'chats'"
          >
            {{ t('messages.chat') }}
          </button>
          <button 
            class="tab-btn" 
            :class="{ active: activeTab === 'notifications' }" 
            @click="activeTab = 'notifications'"
          >
            {{ t('messages.notifications') }}
          </button>
        </div>
        
        <!-- 聊天列表 -->
        <div v-if="activeTab === 'chats'" class="chats-list">
          <div 
            v-for="chat in chatList" 
            :key="chat.id" 
            class="chat-item"
            @click="openChat(chat.id)"
          >
            <div class="chat-avatar">
              <img :src="chat.avatar" alt="头像" />
            </div>
            <div class="chat-info">
              <div class="chat-header">
                <h3 class="chat-name">{{ chat.name }}</h3>
                <span class="chat-time">{{ chat.time }}</span>
              </div>
              <div class="chat-message">
                <span>{{ chat.message }}</span>
                <span v-if="chat.unread > 0" class="unread-badge">{{ chat.unread }}</span>
              </div>
            </div>
          </div>
          
          <div v-if="chatList.length === 0" class="empty-state">
            <p>{{ t('messages.noChats') }}</p>
          </div>
        </div>
        
        <!-- 通知列表 -->
        <div v-else-if="activeTab === 'notifications'" class="notifications-list">
          <div 
            v-for="notification in notifications" 
            :key="notification.id" 
            class="notification-item"
            :class="{ unread: !notification.read }"
            @click="markAsRead(notification.id)"
          >
            <div class="notification-icon">
              <span v-if="notification.type === 'match'" class="icon match-icon">{{ t('messages.matchIcon') }}</span>
              <span v-else class="icon system-icon">{{ t('messages.systemIcon') }}</span>
            </div>
            <div class="notification-content">
              <p class="notification-text">{{ notification.content }}</p>
              <span class="notification-time">{{ formatDate(notification.createdAt) }}</span>
            </div>
          </div>
          
          <div v-if="notifications.length === 0" class="empty-state">
            <p>{{ t('messages.noNotifications') }}</p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { messageAPI } from '../services/api'
import { t } from '../locales/i18n'

const router = useRouter()
const user = ref(JSON.parse(localStorage.getItem('user')))
const activeTab = ref('chats')
const messages = ref([])

// 模拟聊天列表数据
const chatList = ref([
  {
    id: 2,
    name: '管理员2',
    avatar: 'https://via.placeholder.com/150',
    message: '你好，我们的梦境匹配了！',
    time: '10:30',
    unread: 2
  },
  {
    id: 3,
    name: '管理员3',
    avatar: 'https://via.placeholder.com/150',
    message: '你们都梦见了会说话的猫，不如从这里开始聊聊？',
    time: '昨天',
    unread: 0
  }
])

// 模拟通知数据
const notifications = ref([
  {
    id: 1,
    type: 'match',
    content: '您的梦境与用户 管理员2 的梦境匹配度超过70%！',
    createdAt: new Date().toISOString(),
    read: false
  },
  {
    id: 2,
    type: 'system',
    content: '欢迎使用梦境互联，开始记录你的梦境之旅吧！',
    createdAt: new Date(Date.now() - 86400000).toISOString(),
    read: true
  }
])

onMounted(() => {
  loadMessages()
})

const loadMessages = async () => {
  try {
    const response = await messageAPI.getUserMessages(user.value.id)
    if (response.success) {
      messages.value = response.data
      // 处理消息数据，构建聊天列表和通知列表
      processMessages()
    }
  } catch (error) {
    console.error('加载消息失败:', error)
  }
}

const processMessages = () => {
  // 这里可以根据实际消息数据构建聊天列表和通知列表
  // 目前使用模拟数据
}

const openChat = (userId) => {
  router.push(`/chat/${userId}`)
}

const markAsRead = (notificationId) => {
  const notification = notifications.value.find(n => n.id === notificationId)
  if (notification) {
    notification.read = true
  }
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
</script>

<style scoped>
.messages-container {
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

.messages-tabs {
  display: flex;
  margin-bottom: 30px;
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e0e0e0;
}

.tab-btn {
  flex: 1;
  padding: 15px;
  border: none;
  background: none;
  font-size: 16px;
  font-weight: 500;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-btn:hover {
  background-color: #f5f5f5;
}

.tab-btn.active {
  background-color: #4CAF50;
  color: white;
}

.chats-list,
.notifications-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.chat-item {
  background-color: white;
  border-radius: 8px;
  display: flex;
  align-items: center;
  padding: 15px;
  cursor: pointer;
  border: 1px solid #e0e0e0;
  transition: all 0.2s ease;
}

.chat-item:hover {
  background-color: #f9f9f9;
}

.chat-avatar {
  margin-right: 15px;
}

.chat-avatar img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.chat-info {
  flex: 1;
  min-width: 0;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.chat-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin: 0;
}

.chat-time {
  font-size: 12px;
  color: #666;
}

.chat-message {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chat-message span {
  font-size: 14px;
  color: #666;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.unread-badge {
  background-color: #f44336;
  color: white;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  margin-left: 10px;
  flex-shrink: 0;
  min-width: 24px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(244, 67, 54, 0.3);
  animation: pulse 2s infinite;
}

/* 脉冲动画效果 */
@keyframes pulse {
  0% {
    box-shadow: 0 2px 4px rgba(244, 67, 54, 0.3);
  }
  50% {
    box-shadow: 0 2px 12px rgba(244, 67, 54, 0.6);
  }
  100% {
    box-shadow: 0 2px 4px rgba(244, 67, 54, 0.3);
  }
}

/* 当未读消息数量较多时的样式 */
.unread-badge[style*="--count >= 10"] {
  padding: 4px 10px;
  min-width: 30px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .unread-badge {
    padding: 3px 6px;
    font-size: 11px;
    min-width: 20px;
  }
}

.notification-item {
  background-color: white;
  border-radius: 8px;
  display: flex;
  align-items: flex-start;
  padding: 15px;
  cursor: pointer;
  border: 1px solid #e0e0e0;
  transition: all 0.2s ease;
}

.notification-item:hover {
  background-color: #f9f9f9;
}

.notification-item.unread {
  background-color: #f9f9f9;
  border-left: 4px solid #4CAF50;
}

.notification-icon {
  margin-right: 15px;
  font-size: 24px;
}

.notification-content {
  flex: 1;
}

.notification-text {
  margin-bottom: 5px;
  line-height: 1.5;
  color: #333;
}

.notification-time {
  font-size: 12px;
  color: #666;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #666;
  background-color: white;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  margin: 20px 0;
}
</style>