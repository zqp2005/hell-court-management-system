<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">业镜台</h2>
      <p class="page-desc">阴司行事 · 皆有录记</p>
    </div>
    <div class="content-card">
      <el-table :data="tableData" border stripe v-loading="loading" class="dark-table"
        header-cell-class-name="table-header" cell-class-name="table-cell">
        <template #empty>
          <div class="empty-state"><span class="empty-icon">📜</span><span class="empty-text">暂无操作记录</span></div>
        </template>
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
</style>
