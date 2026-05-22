<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">业镜台</h2>
      <p class="page-desc">阴司行事 · 皆有录记</p>
    </div>
    <div class="content-card">
      <el-table :data="tableData" border stripe v-loading="loading" class="dark-table"
        header-cell-class-name="table-header" cell-class-name="table-cell">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="userId" label="操作人" width="80" />
        <el-table-column prop="action" label="操作" width="150" />
        <el-table-column prop="targetType" label="对象" width="100" />
        <el-table-column prop="targetId" label="对象ID" width="80" />
        <el-table-column prop="detail" label="详情" show-overflow-tooltip />
        <el-table-column prop="ipAddress" label="IP" width="130" />
        <el-table-column prop="createdAt" label="时间" width="160">
          <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="page" v-model:page-size="size" :total="total"
        layout="total, prev, pager, next" background class="pagination" @change="fetchData" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getLogs } from '@/api/log'
import { formatDateTime } from '@/utils'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(20)

async function fetchData() {
  loading.value = true
  const res = await getLogs({ page: page.value, size: size.value })
  tableData.value = res.data.records
  total.value = res.data.total
  loading.value = false
}

onMounted(fetchData)
</script>

<style scoped>
.page-header { margin-bottom: 20px; }
.page-title { font-size: 22px; color: #ffd700; letter-spacing: 4px; }
.page-desc { font-size: 12px; color: #666; letter-spacing: 2px; margin-top: 4px; }
.content-card { background: #1a1a2e; border: 1px solid #2a2a3e; border-radius: 12px; padding: 20px; }
.dark-table :deep(.table-header) { background: #0a0a0f !important; color: #ffd700; }
.dark-table :deep(.table-cell) { background: rgba(26,26,46,0.8); color: #ccc; }
.dark-table :deep(.el-table__row:hover td) { background: rgba(255,215,0,0.05) !important; }
.dark-table :deep(.el-table--border) { border-color: #2a2a3e; }
.dark-table :deep(.el-table__body) { background: transparent; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
.pagination :deep(.el-pager li) { background: #0a0a0f; color: #888; }
.pagination :deep(.el-pager li.active) { background: #8b0000; color: #ffd700; }
</style>
