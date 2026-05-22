<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">森罗殿</h2>
      <p class="page-desc">十殿阎王 · 复核审批</p>
    </div>

    <div class="content-card" style="margin-bottom: 20px">
      <span class="section-title" style="display:block;margin-bottom:16px">审核记录</span>
      <el-table :data="tableData" border stripe v-loading="loading" class="dark-table"
        header-cell-class-name="table-header" cell-class-name="table-cell">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="judgmentId" label="审判ID" width="80" />
        <el-table-column prop="reviewOpinion" label="复核意见" show-overflow-tooltip />
        <el-table-column label="结果" width="80">
          <template #default="{ row }">
            <el-tag :type="row.result === 1 ? 'success' : 'danger'" effect="dark">{{ row.result === 1 ? '通过' : '驳回' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="复核时间" width="160">
          <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
        </el-table-column>
      </el-table>
    </div>

    <div class="content-card">
      <span class="section-title" style="display:block;margin-bottom:16px">待审批列表</span>
      <el-table :data="pendingList" border stripe class="dark-table"
        header-cell-class-name="table-header" cell-class-name="table-cell">
        <el-table-column prop="id" label="审判ID" width="80" />
        <el-table-column prop="soulId" label="魂魄ID" width="80" />
        <el-table-column prop="judgmentDetail" label="审判内容" show-overflow-tooltip />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" class="btn-ghost-success" @click="handleApprove(row.id, 1)" v-if="userStore.roleCode === 'KING'">通过</el-button>
            <el-button size="small" class="btn-ghost-danger" @click="handleApprove(row.id, 2)" v-if="userStore.roleCode === 'KING'">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-pagination
      v-model:current-page="page" v-model:page-size="size" :total="total"
      layout="total, prev, pager, next" background class="pagination" @change="fetchData" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getReviews, approveReview } from '@/api/review'
import { getJudgments } from '@/api/judgment'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
import { formatDateTime } from '@/utils'

const userStore = useUserStore()

const loading = ref(false)
const tableData = ref([])
const pendingList = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(20)

async function fetchData() {
  loading.value = true
  const [res1, res2] = await Promise.all([
    getReviews({ page: page.value, size: size.value }),
    getJudgments({ page: 1, size: 100, status: 0 })
  ])
  tableData.value = res1.data.records
  total.value = res1.data.total
  pendingList.value = res2.data.records
  loading.value = false
}

async function handleApprove(judgmentId, result) {
  await approveReview({ judgmentId, result, reviewOpinion: result === 1 ? '准行' : '驳回再审' })
  ElMessage.success(result === 1 ? '已通过' : '已驳回')
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.page-header { margin-bottom: 20px; }
.page-title { font-size: 22px; color: #ffd700; letter-spacing: 4px; }
.page-desc { font-size: 12px; color: #666; letter-spacing: 2px; margin-top: 4px; }
.section-title { color: #ffd700; font-size: 15px; letter-spacing: 2px; }
.content-card { background: #1a1a2e; border: 1px solid #2a2a3e; border-radius: 12px; padding: 20px; }
.dark-table :deep(.table-header) { background: #0a0a0f !important; color: #ffd700; }
.dark-table :deep(.table-cell) { background: rgba(26,26,46,0.8); color: #ccc; }
.dark-table :deep(.el-table__row:hover td) { background: rgba(255,215,0,0.05) !important; }
.dark-table :deep(.el-table--border) { border-color: #2a2a3e; }
.dark-table :deep(.el-table__body) { background: transparent; }
.btn-ghost-success { background: transparent; border: 1px solid #2ecc71; color: #2ecc71; }
.btn-ghost-success:hover { background: rgba(46,204,113,0.1); }
.btn-ghost-danger { background: transparent; border: 1px solid #e74c3c; color: #e74c3c; }
.btn-ghost-danger:hover { background: rgba(231,76,60,0.1); }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
.pagination :deep(.el-pager li) { background: #0a0a0f; color: #888; }
.pagination :deep(.el-pager li.active) { background: #8b0000; color: #ffd700; }
</style>
