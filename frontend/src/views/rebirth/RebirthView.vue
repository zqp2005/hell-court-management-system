<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">六道轮回</h2>
      <p class="page-desc">因果不虚 · 轮回不息</p>
    </div>
    <div class="content-card">
      <el-button class="btn-red" style="margin-bottom: 16px" @click="showDialog" v-if="userStore.roleCode === 'KING'">分配轮回</el-button>
      <el-button class="btn-export" style="margin-bottom: 16px; margin-left: 8px" @click="handleExport" v-if="userStore.roleCode === 'ADMIN'">导出Excel</el-button>
      <el-table :data="tableData" border stripe v-loading="loading" class="dark-table"
        header-cell-class-name="table-header" cell-class-name="table-cell">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="soulId" label="魂魄ID" width="80" />
        <el-table-column label="六道" width="100">
          <template #default="{ row }">
            <el-tag :color="pathColors[row.path] || '#666'" effect="dark" style="border: none">
              {{ rebirthPathMap[row.path] || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="rebirthDate" label="投胎日期" width="160">
          <template #default="{ row }">{{ formatDateTime(row.rebirthDate) }}</template>
        </el-table-column>
        <el-table-column prop="destination" label="去向" />
        <el-table-column prop="createdAt" label="记录时间" width="160">
          <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" title="分配轮回" width="500px" class="dialog-dark">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选择魂魄" prop="soulId">
          <el-select v-model="form.soulId" style="width: 100%" placeholder="选择已判决的魂魄">
            <el-option v-for="s in soulOptions" :key="s.id" :label="'#' + s.id + ' ' + s.name + ' (' + s.genderText + ')'" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="轮回道" prop="path">
          <el-select v-model="form.path" style="width: 100%">
            <el-option v-for="(v, k) in rebirthPathMap" :key="k" :label="v" :value="Number(k)" />
          </el-select>
        </el-form-item>
        <el-form-item label="投胎日期" prop="rebirthDate">
          <el-date-picker v-model="form.rebirthDate" type="datetime" style="width: 100%" />
        </el-form-item>
        <el-form-item label="去向" prop="destination">
          <el-input v-model="form.destination" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" class="btn-gold" @click="handleSave">分配</el-button>
      </template>
    </el-dialog>

    <el-pagination
      v-model:current-page="page" v-model:page-size="size" :total="total"
      layout="total, prev, pager, next" background class="pagination" @change="fetchData" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getRebirths, createRebirth, exportRebirths } from '@/api/rebirth'
import { getSouls } from '@/api/soul'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
import { formatDateTime, rebirthPathMap } from '@/utils'

const userStore = useUserStore()

const pathColors = { 1: '#ffd700', 2: '#ff6b35', 3: '#2ecc71', 4: '#95a5a6', 5: '#e74c3c', 6: '#8b0000' }
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const dialogVisible = ref(false)
const formRef = ref(null)
const soulOptions = ref([])
const form = reactive({ soulId: null, path: 3, rebirthDate: '', destination: '' })
const rules = {
  soulId: [{ required: true, message: '请选择魂魄', trigger: 'change' }],
  path: [{ required: true, message: '请选择轮回道', trigger: 'change' }],
  rebirthDate: [{ required: true, message: '请选择投胎日期', trigger: 'change' }]
}

async function fetchData() {
  loading.value = true
  const res = await getRebirths({ page: page.value, size: size.value })
  tableData.value = res.data.records
  total.value = res.data.total
  loading.value = false
}

async function showDialog() {
  // 加载已判决的魂魄供选择
  const res = await getSouls({ page: 1, size: 200, soulStatus: 3 })
  soulOptions.value = (res.data.records || []).map(s => ({ id: s.id, name: s.name, genderText: s.gender === 0 ? '男' : '女' }))
  form.soulId = null; form.path = 3; form.rebirthDate = ''; form.destination = ''
  dialogVisible.value = true
}

async function handleSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  await createRebirth(form)
  ElMessage.success('分配成功')
  dialogVisible.value = false
  fetchData()
}

function handleExport() {
  const token = localStorage.getItem('token')
  const a = document.createElement('a')
  a.href = '/api/rebirths/export?token=' + encodeURIComponent(token)
  a.download = '轮回记录.xlsx'
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  ElMessage.success('正在下载...')
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
.btn-red { background: #8b0000; border-color: #8b0000; color: white; }
.btn-red:hover { background: #a00000; }
.btn-export { background: transparent; border: 1px solid #2ecc71; color: #2ecc71; }
.btn-export:hover { background: rgba(46,204,113,0.1); color: #2ecc71; }
.btn-gold { background: linear-gradient(135deg, #8b0000, #4a0000); border: 1px solid #ffd700; color: #ffd700; }
.btn-gold:hover { background: linear-gradient(135deg, #a00000, #6b0000); }
.dialog-dark :deep(.el-dialog) { background: #1a1a2e; border: 1px solid #2a2a3e; }
.dialog-dark :deep(.el-dialog__title) { color: #ffd700; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
.pagination :deep(.el-pager li) { background: #0a0a0f; color: #888; }
.pagination :deep(.el-pager li.active) { background: #8b0000; color: #ffd700; }
</style>
