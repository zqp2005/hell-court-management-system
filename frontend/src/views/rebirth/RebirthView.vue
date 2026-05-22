<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">六道轮回</h2>
      <p class="page-desc">因果不虚 · 轮回不息</p>
    </div>
    <div class="content-card">
      <el-button class="btn-red" style="margin-bottom: 16px" @click="showDialog" v-if="userStore.roleCode === ROLE.KING">分配轮回</el-button>
      <el-button class="btn-ghost-success" style="margin-bottom: 16px; margin-left: 8px" @click="handleExport" v-if="userStore.roleCode === ROLE.ADMIN" :loading="exporting">导出Excel</el-button>
      <el-table :data="tableData" border stripe v-loading="loading" class="dark-table"
        header-cell-class-name="table-header" cell-class-name="table-cell">
        <template #empty>
          <div class="empty-state"><span class="empty-icon">🔄</span><span class="empty-text">暂无轮回记录</span></div>
        </template>
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
        <el-button type="primary" class="btn-gold" @click="handleSave" :loading="saving">分配</el-button>
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
import { ROLE } from '@/utils/constants'

const userStore = useUserStore()

const pathColors = { 1: '#ffd700', 2: '#ff6b35', 3: '#2ecc71', 4: '#95a5a6', 5: '#e74c3c', 6: '#8b0000' }
const loading = ref(false)
const saving = ref(false)
const exporting = ref(false)
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
  const res = await getSouls({ page: 1, size: 200, soulStatus: 3 })
  soulOptions.value = (res.data.records || []).map(s => ({ id: s.id, name: s.name, genderText: s.gender === 0 ? '男' : '女' }))
  form.soulId = null; form.path = 3; form.rebirthDate = ''; form.destination = ''
  dialogVisible.value = true
}

async function handleSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    await createRebirth(form)
    ElMessage.success('分配成功')
    dialogVisible.value = false; fetchData()
  } finally { saving.value = false }
}

async function handleExport() {
  exporting.value = true
  try {
    const blob = await exportRebirths()
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url; a.download = '轮回记录.xlsx'
    document.body.appendChild(a); a.click()
    document.body.removeChild(a)
    URL.revokeObjectURL(url)
    ElMessage.success('下载成功')
  } catch {
    ElMessage.error('导出失败，请联系管理员')
  } finally { exporting.value = false }
}

onMounted(fetchData)
</script>

<style scoped>
</style>
