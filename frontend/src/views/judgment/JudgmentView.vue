<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">审判司</h2>
      <p class="page-desc">判官笔 · 善恶定</p>
    </div>
    <div class="content-card">
      <el-button class="btn-red" style="margin-bottom: 16px" @click="showDialog(null)" v-if="userStore.roleCode === 'JUDGE'">录入审判</el-button>
      <el-table :data="tableData" border stripe v-loading="loading" class="dark-table"
        header-cell-class-name="table-header" cell-class-name="table-cell">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="soulId" label="魂魄ID" width="80" />
        <el-table-column prop="judgmentDetail" label="审判内容" show-overflow-tooltip />
        <el-table-column prop="goodReward" label="善报" show-overflow-tooltip />
        <el-table-column prop="evilPunishment" label="恶报" show-overflow-tooltip />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 0 ? 'warning' : row.status === 1 ? 'success' : 'danger'" effect="dark">
              {{ row.status === 0 ? '待复核' : row.status === 1 ? '已通过' : '已驳回' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" class="btn-ghost" @click="showDialog(row)" v-if="userStore.roleCode === 'JUDGE'">编辑</el-button>
            <el-button size="small" class="btn-ghost-gold" @click="handleSubmit(row.id)" v-if="row.status === 0 && userStore.roleCode === 'JUDGE'">提交复核</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="page" v-model:page-size="size" :total="total"
        layout="total, prev, pager, next" background class="pagination" @change="fetchData" />
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogId ? '编辑审判' : '录入审判'" width="600px" class="dialog-dark">
      <el-form :model="form" label-width="80px">
        <el-form-item label="选择魂魄">
          <el-select v-model="form.soulId" style="width: 100%" placeholder="选择已到殿的魂魄" :disabled="!!dialogId">
            <el-option v-for="s in soulOptions" :key="s.id" :label="'#' + s.id + ' ' + s.name + ' (' + s.genderText + ')'" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="审判详情">
          <el-input v-model="form.judgmentDetail" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="善报">
          <el-input v-model="form.goodReward" placeholder="善有善报——减罪、免刑、升入善道等" />
        </el-form-item>
        <el-form-item label="恶报">
          <el-input v-model="form.evilPunishment" placeholder="恶有恶报——下地狱、入饿鬼道、来世受苦等" />
        </el-form-item>
        <el-form-item label="对应地狱" v-if="form.evilPunishment && !dialogId">
          <el-select v-model="form.hellLayer" style="width: 100%" placeholder="选填：罪涉哪层地狱（仅作参考）" clearable>
            <el-option v-for="h in hellLayers" :key="h.id" :label="'第' + h.layerNum + '层 · ' + h.name" :value="h.name" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" class="btn-gold" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getJudgments, createJudgment, updateJudgment, submitReview } from '@/api/judgment'
import { getSouls } from '@/api/soul'
import { getHellLayers } from '@/api/hell'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const dialogVisible = ref(false)
const dialogId = ref(null)
const soulOptions = ref([])
const hellLayers = ref([])
const form = reactive({ soulId: null, hellLayer: null, judgmentDetail: '', goodReward: '', evilPunishment: '' })

async function fetchData() {
  loading.value = true
  const res = await getJudgments({ page: page.value, size: size.value })
  tableData.value = res.data.records
  total.value = res.data.total
  loading.value = false
}

async function showDialog(row) {
  dialogId.value = row?.id || null
  if (!row) {
    const [soulRes, hellRes] = await Promise.all([
      getSouls({ page: 1, size: 200, soulStatus: 1 }),
      getHellLayers()
    ])
    soulOptions.value = (soulRes.data.records || []).map(s => ({ id: s.id, name: s.name, genderText: s.gender === 0 ? '男' : '女' }))
    hellLayers.value = hellRes.data || []
    form.soulId = null
    form.hellLayer = null
  } else {
    form.soulId = row.soulId
    form.hellLayer = null
  }
  form.judgmentDetail = row?.judgmentDetail || ''
  form.goodReward = row?.goodReward || ''
  form.evilPunishment = row?.evilPunishment || ''
  dialogVisible.value = true
}

async function handleSave() {
  const data = { ...form }
  if (form.hellLayer) {
    data.judgmentDetail = `[${form.hellLayer}] ` + (form.judgmentDetail || '')
  }
  if (dialogId.value) { await updateJudgment(dialogId.value, data) }
  else { await createJudgment(data) }
  ElMessage.success('保存成功')
  dialogVisible.value = false
  fetchData()
}

async function handleSubmit(id) {
  await submitReview(id)
  ElMessage.success('已提交复核')
  fetchData()
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
.btn-red { background: #8b0000; border-color: #8b0000; color: white; }
.btn-red:hover { background: #a00000; border-color: #a00000; }
.btn-gold { background: linear-gradient(135deg, #8b0000, #4a0000); border: 1px solid #ffd700; color: #ffd700; }
.btn-gold:hover { background: linear-gradient(135deg, #a00000, #6b0000); color: #ffd700; }
.btn-ghost { background: transparent; border: 1px solid #555; color: #ccc; }
.btn-ghost:hover { border-color: #ffd700; color: #ffd700; }
.btn-ghost-gold { background: transparent; border: 1px solid #ffd700; color: #ffd700; }
.btn-ghost-gold:hover { background: rgba(255,215,0,0.1); }
.dialog-dark :deep(.el-dialog) { background: #1a1a2e; border: 1px solid #2a2a3e; }
.dialog-dark :deep(.el-dialog__title) { color: #ffd700; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
.pagination :deep(.el-pager li) { background: #0a0a0f; color: #888; }
.pagination :deep(.el-pager li.active) { background: #8b0000; color: #ffd700; }
</style>
