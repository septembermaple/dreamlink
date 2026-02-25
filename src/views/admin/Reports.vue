<template>
  <div class="admin-reports-container">
    <header class="admin-header">
      <div class="header-left">
        <button class="btn btn-back" @click="goBack">← 返回</button>
      </div>
      <h1 class="admin-title">举报管理</h1>
      <div class="header-right"></div>
    </header>
    
    <main class="admin-content">
      <div class="container">
        <div v-if="loading" class="loading-state">
          <p>加载中...</p>
        </div>
        <div v-else>
          <div class="filter-bar">
            <select v-model="statusFilter" class="form-select filter-select">
              <option value="">全部状态</option>
              <option value="pending">待处理</option>
              <option value="processed">已处理</option>
              <option value="dismissed">已驳回</option>
            </select>
          </div>
          
          <div class="reports-table-container">
            <table class="reports-table">
              <thead>
                <tr>
                  <th>举报ID</th>
                  <th>梦境ID</th>
                  <th>举报原因</th>
                  <th>举报人</th>
                  <th>状态</th>
                  <th>举报时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="report in filteredReports" :key="report.id">
                  <td>{{ report.id }}</td>
                  <td>
                    <router-link :to="`/dream/${report.dreamId}`" class="dream-link" target="_blank">
                      {{ report.dreamId }}
                    </router-link>
                  </td>
                  <td>{{ report.reason }}</td>
                  <td>{{ getReporterName(report.reporterId) }}</td>
                  <td>
                    <span class="status-badge" :class="`status-${report.status}`">
                      {{ getStatusText(report.status) }}
                    </span>
                  </td>
                  <td>{{ formatDate(report.createdAt) }}</td>
                  <td>
                    <div class="report-actions">
                      <button 
                        class="btn btn-sm btn-primary" 
                        @click="viewReportDetail(report)"
                      >
                        详情
                      </button>
                      <button 
                        v-if="report.status === 'pending'"
                        class="btn btn-sm btn-success" 
                        @click="processReport(report, 'processed')"
                      >
                        处理
                      </button>
                      <button 
                        v-if="report.status === 'pending'"
                        class="btn btn-sm btn-secondary" 
                        @click="processReport(report, 'dismissed')"
                      >
                        驳回
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <div v-if="filteredReports.length === 0" class="empty-state">
            <p>暂无举报记录</p>
          </div>
        </div>
      </div>
    </main>

    <!-- 举报详情弹窗 -->
    <div v-if="showDetailModal" class="modal-overlay" @click="closeDetailModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>举报详情</h3>
          <button class="modal-close" @click="closeDetailModal">×</button>
        </div>
        <div class="modal-body">
          <div class="detail-item">
            <label>举报ID:</label>
            <span>{{ selectedReport?.id }}</span>
          </div>
          <div class="detail-item">
            <label>梦境ID:</label>
            <span>
              <router-link :to="`/dream/${selectedReport?.dreamId}`" class="dream-link" target="_blank">
                {{ selectedReport?.dreamId }}
              </router-link>
            </span>
          </div>
          <div class="detail-item">
            <label>举报原因:</label>
            <span>{{ selectedReport?.reason }}</span>
          </div>
          <div class="detail-item">
            <label>详细描述:</label>
            <span>{{ selectedReport?.description || '无' }}</span>
          </div>
          <div class="detail-item">
            <label>举报人:</label>
            <span>{{ getReporterName(selectedReport?.reporterId) }}</span>
          </div>
          <div class="detail-item">
            <label>举报时间:</label>
            <span>{{ formatDate(selectedReport?.createdAt) }}</span>
          </div>
          <div class="detail-item" v-if="selectedReport?.processedAt">
            <label>处理时间:</label>
            <span>{{ formatDate(selectedReport?.processedAt) }}</span>
          </div>
          <div class="detail-item" v-if="selectedReport?.processedBy">
            <label>处理人:</label>
            <span>{{ getProcessorName(selectedReport?.processedBy) }}</span>
          </div>
        </div>
        <div class="modal-footer">
          <button 
            class="btn btn-secondary" 
            @click="closeDetailModal"
          >
            关闭
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { reportAPI, dreamAPI, authAPI } from '../../services/api'

const router = useRouter()
const user = ref(JSON.parse(localStorage.getItem('user')))
const reports = ref([])
const loading = ref(true)
const statusFilter = ref('')
const showDetailModal = ref(false)
const selectedReport = ref(null)
const processing = ref(false)

// 模拟用户数据
const users = [
  { id: 1, username: 'admin1', nickname: '管理员1', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Aries', constellation: '白羊座' },
  { id: 2, username: 'admin2', nickname: '管理员2', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Taurus', constellation: '金牛座' },
  { id: 3, username: 'admin3', nickname: '管理员3', avatar: 'https://api.dicebear.com/9.x/avataaars/svg?seed=Gemini', constellation: '双子座' }
]

onMounted(() => {
  if (!user.value) {
    router.push('/login')
    return
  }
  loadReports()
})

const loadReports = async () => {
  loading.value = true
  try {
    const response = await reportAPI.getAllReports()
    if (response.success) {
      reports.value = response.data
    }
  } catch (error) {
    console.error('加载举报记录失败:', error)
    alert('加载失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const filteredReports = computed(() => {
  if (!statusFilter.value) {
    return reports.value
  }
  return reports.value.filter(report => report.status === statusFilter.value)
})

const goBack = () => {
  router.push('/')
}

const viewReportDetail = (report) => {
  selectedReport.value = report
  showDetailModal.value = true
}

const closeDetailModal = () => {
  showDetailModal.value = false
  selectedReport.value = null
}

const processReport = async (report, status) => {
  if (!confirm(`确定要${status === 'processed' ? '处理' : '驳回'}该举报吗？`)) {
    return
  }
  
  processing.value = true
  try {
    const response = await reportAPI.updateReportStatus(report.id, {
      status,
      processedBy: user.value.id
    })
    if (response.success) {
      alert('操作成功')
      loadReports()
    } else {
      alert('操作失败，请稍后重试')
    }
  } catch (error) {
    console.error('操作失败:', error)
    alert('操作失败，请稍后重试')
  } finally {
    processing.value = false
  }
}

const getStatusText = (status) => {
  const statusMap = {
    pending: '待处理',
    processed: '已处理',
    dismissed: '已驳回'
  }
  return statusMap[status] || status
}

const getReporterName = (reporterId) => {
  const reporter = users.find(u => u.id === reporterId)
  return reporter?.nickname || `用户${reporterId}`
}

const getProcessorName = (processorId) => {
  const processor = users.find(u => u.id === processorId)
  return processor?.nickname || `用户${processorId}`
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}
</script>

<style scoped>
.admin-reports-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.admin-header {
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

.admin-title {
  flex: 2;
  text-align: center;
  font-size: 20px;
  color: #4CAF50;
  margin: 0;
}

.admin-content {
  flex: 1;
  padding: 20px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.filter-bar {
  margin-bottom: 20px;
  display: flex;
  justify-content: flex-end;
}

.filter-select {
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  background-color: white;
}

.reports-table-container {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.reports-table {
  width: 100%;
  border-collapse: collapse;
}

.reports-table th,
.reports-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.reports-table th {
  background-color: #f9f9f9;
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.reports-table td {
  font-size: 14px;
  color: #666;
}

.status-badge {
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-pending {
  background-color: #ffeb3b;
  color: #f57f17;
}

.status-processed {
  background-color: #4caf50;
  color: white;
}

.status-dismissed {
  background-color: #9e9e9e;
  color: white;
}

.report-actions {
  display: flex;
  gap: 8px;
}

.btn-sm {
  padding: 4px 12px;
  font-size: 12px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-sm.btn-primary {
  background-color: #2196f3;
  color: white;
  border: none;
}

.btn-sm.btn-primary:hover {
  background-color: #1976d2;
}

.btn-sm.btn-success {
  background-color: #4caf50;
  color: white;
  border: none;
}

.btn-sm.btn-success:hover {
  background-color: #388e3c;
}

.btn-sm.btn-secondary {
  background-color: #9e9e9e;
  color: white;
  border: none;
}

.btn-sm.btn-secondary:hover {
  background-color: #757575;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #666;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.dream-link {
  color: #2196f3;
  text-decoration: none;
  font-weight: 500;
}

.dream-link:hover {
  text-decoration: underline;
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
  max-width: 600px;
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

.detail-item {
  margin-bottom: 16px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-item label {
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.detail-item span {
  color: #666;
  font-size: 14px;
  line-height: 1.4;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .reports-table {
    font-size: 12px;
  }
  
  .reports-table th,
  .reports-table td {
    padding: 8px 12px;
  }
  
  .report-actions {
    flex-direction: column;
    gap: 4px;
  }
  
  .btn-sm {
    width: 100%;
  }
}
</style>