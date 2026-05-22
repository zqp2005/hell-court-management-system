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
        <template #empty>
          <div class="empty-state"><span class="empty-icon">📋</span><span class="empty-text">暂无审核记录</span></div>
        </template>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="judgmentId" label="审判ID" width="80" />
        <el-table-column prop="reviewOpinion" label="复核意见" show-overflow-tooltip />
        <el-table-column label="结果" width="80">
          <template #default="{ row }">
            <el-tag :type="row.result === REVIEW_RESULT.PASS ? 'success' : 'danger'" effect="dark">{{ row.result === REVIEW_RESULT.PASS ? '通过' : '驳回' }}</el-tag>
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
        <template #empty>
          <div class="empty-state"><span class="empty-icon">✅</span><span class="empty-text">没有待审批的审判</span></div>
        </template>
        <el-table-column prop="id" label="审判ID" width="80" />
        <el-table-column prop="soulId" label="魂魄ID" width="80" />
        <el-table-column prop="judgmentDetail" label="审判内容" show-overflow-tooltip />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" class="btn-ghost-success" @click="handleApprove(row.id, REVIEW_RESULT.PASS)" v-if="userStore.roleCode === ROLE.KING" :loading="approving === row.id + '-pass'">通过</el-button>
            <el-button size="small" class="btn-ghost-danger" @click="handleApprove(row.id, REVIEW_RESULT.REJECT)" v-if="userStore.roleCode === ROLE.KING" :loading="approving === row.id + '-reject'">驳回</el-button>
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
import { ROLE, REVIEW_RESULT } from '@/utils/constants'

const userStore = useUserStore()

const loading = ref(false)
const approving = ref(null)
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
  const key = judgmentId + '-' + (result === REVIEW_RESULT.PASS ? 'pass' : 'reject')
  approving.value = key
  try {
    await approveReview({ judgmentId, result, reviewOpinion: result === REVIEW_RESULT.PASS ? '准行' : '驳回再审' })
    ElMessage.success(result === REVIEW_RESULT.PASS ? '已通过' : '已驳回')
    fetchData()
  } finally { approving.value = null }
}

onMounted(fetchData)
</script>

<style scoped>
.section-title { color: #ffd700; font-size: 15px; letter-spacing: 2px; }
</style>
