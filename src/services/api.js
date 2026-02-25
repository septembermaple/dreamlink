// API服务配置
const API_BASE_URL = '/api'

// 获取存储的token
const getToken = () => {
  return localStorage.getItem('token')
}

// 通用请求函数
const request = async (url, options = {}) => {
  const token = getToken()
  
  const defaultOptions = {
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { 'Authorization': `Bearer ${token}` } : {})
    }
  }
  
  const mergedOptions = {
    ...defaultOptions,
    ...options,
    headers: {
      ...defaultOptions.headers,
      ...options.headers
    }
  }
  
  try {
    const response = await fetch(`${API_BASE_URL}${url}`, mergedOptions)
    
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      throw new Error(errorData.message || `请求失败: ${response.status}`)
    }
    
    return await response.json()
  } catch (error) {
    console.error('API请求错误:', error)
    throw error
  }
}

// 认证相关API
export const authAPI = {
  // 登录
  async login(data) {
    return await request('/auth/login', {
      method: 'POST',
      body: JSON.stringify(data)
    })
  },
  
  // 注册
  async register(data) {
    return await request('/auth/register', {
      method: 'POST',
      body: JSON.stringify(data)
    })
  },
  
  // 获取用户信息
  async getUserInfo(userId) {
    return await request(`/auth/user/${userId}`)
  },
  
  // 更新用户信息
  async updateUserInfo(userId, data) {
    return await request(`/auth/user/${userId}`, {
      method: 'PUT',
      body: JSON.stringify(data)
    })
  }
}

// 梦境相关API
export const dreamAPI = {
  // 创建梦境
  async createDream(data) {
    return await request('/dream', {
      method: 'POST',
      body: JSON.stringify(data)
    })
  },
  
  // 获取用户的梦境
  async getUserDreams(userId) {
    return await request(`/dream/user/${userId}`)
  },
  
  // 获取公开梦境（世界/广场）
  async getPublicDreams() {
    return await request('/dream/public')
  },
  
  // 获取梦境详情
  async getDreamDetail(dreamId) {
    return await request(`/dream/${dreamId}`)
  },
  
  // 更新梦境
  async updateDream(dreamId, data) {
    return await request(`/dream/${dreamId}`, {
      method: 'PUT',
      body: JSON.stringify(data)
    })
  },
  
  // 删除梦境
  async deleteDream(dreamId) {
    return await request(`/dream/${dreamId}`, {
      method: 'DELETE'
    })
  }
}

// 消息相关API
export const messageAPI = {
  // 获取用户的消息
  async getUserMessages(userId) {
    return await request(`/message/user/${userId}`)
  },
  
  // 获取聊天记录
  async getChatHistory(userId1, userId2) {
    return await request(`/message/chat/${userId1}/${userId2}`)
  },
  
  // 发送消息
  async sendMessage(data) {
    return await request('/message', {
      method: 'POST',
      body: JSON.stringify(data)
    })
  },
  
  // 标记消息为已读
  async markAsRead(messageId) {
    return await request(`/message/read/${messageId}`, {
      method: 'PUT'
    })
  }
}

// 评论相关API
export const commentAPI = {
  // 创建评论
  async createComment(data) {
    return await request('/comment', {
      method: 'POST',
      body: JSON.stringify(data)
    })
  },
  
  // 获取梦境的评论
  async getDreamComments(dreamId) {
    return await request(`/comment/dream/${dreamId}`)
  }
}

// 举报相关API
export const reportAPI = {
  // 创建举报
  async createReport(data) {
    return await request('/report', {
      method: 'POST',
      body: JSON.stringify(data)
    })
  },
  
  // 获取所有举报
  async getAllReports() {
    return await request('/report')
  },
  
  // 获取举报详情
  async getReportDetail(reportId) {
    return await request(`/report/${reportId}`)
  },
  
  // 更新举报状态
  async updateReportStatus(reportId, data) {
    return await request(`/report/${reportId}`, {
      method: 'PUT',
      body: JSON.stringify(data)
    })
  }
}

export default {
  auth: authAPI,
  dream: dreamAPI,
  message: messageAPI,
  comment: commentAPI,
  report: reportAPI
}