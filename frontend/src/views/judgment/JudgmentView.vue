<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">审判司</h2>
      <p class="page-desc">判官笔 · 善恶定</p>
    </div>
    <div class="content-card">
      <el-button class="btn-red" style="margin-bottom: 16px" @click="showDialog(null)" v-if="userStore.roleCode === ROLE.JUDGE">录入审判</el-button>
      <el-table :data="tableData" border stripe v-loading="loading" class="dark-table"
        header-cell-class-name="table-header" cell-class-name="table-cell">
        <template #empty>
          <div class="empty-state"><span class="empty-icon">⚖</span><span class="empty-text">暂无审判记录</span></div>
        </template>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="soulId" label="魂魄ID" width="80" />
        <el-table-column prop="judgmentDetail" label="审判内容" show-overflow-tooltip />
        <el-table-column prop="goodReward" label="善报" show-overflow-tooltip />
        <el-table-column prop="evilPunishment" label="恶报" show-overflow-tooltip />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === JUDGMENT_STATUS.PENDING ? 'warning' : row.status === JUDGMENT_STATUS.APPROVED ? 'success' : 'danger'" effect="dark">
              {{ row.status === JUDGMENT_STATUS.PENDING ? '待复核' : row.status === JUDGMENT_STATUS.APPROVED ? '已通过' : '已驳回' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" class="btn-ghost" @click="showDialog(row)" v-if="userStore.roleCode === ROLE.JUDGE">编辑</el-button>
            <el-button size="small" class="btn-ghost-gold" @click="handleSubmit(row.id)" v-if="row.status === JUDGMENT_STATUS.PENDING && userStore.roleCode === ROLE.JUDGE">提交复核</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="page" v-model:page-size="size" :total="total"
        layout="total, prev, pager, next" background class="pagination" @change="fetchData" />
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogId ? '编辑审判' : '录入审判'" width="600px" class="dialog-dark">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="选择魂魄" prop="soulId">
          <el-select v-model="form.soulId" style="width: 100%" placeholder="选择已到殿的魂魄" :disabled="!!dialogId">
            <el-option v-for="s in soulOptions" :key="s.id" :label="'#' + s.id + ' ' + s.name + ' (' + s.genderText + ')'" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="审判详情" prop="judgmentDetail">
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
        <el-button type="primary" class="btn-gold" @click="handleSave" :loading="saving">保存</el-button>
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
import { ROLE, JUDGMENT_STATUS } from '@/utils/constants'

const userStore = useUserStore()

const loading = ref(false)
const saving = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const dialogVisible = ref(false)
const dialogId = ref(null)
const formRef = ref(null)
const soulOptions = ref([])
const hellLayers = ref([])
const form = reactive({ soulId: null, hellLayer: null, judgmentDetail: '', goodReward: '', evilPunishment: '' })
const formRules = {
  soulId: [{ required: true, message: '请选择魂魄', trigger: 'change' }],
  judgmentDetail: [{ required: true, message: '请输入审判详情', trigger: 'blur' }]
}

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
    form.soulId = null; form.hellLayer = null
  } else {
    form.soulId = row.soulId; form.hellLayer = null
  }
  form.judgmentDetail = row?.judgmentDetail || ''
  form.goodReward = row?.goodReward || ''
  form.evilPunishment = row?.evilPunishment || ''
  dialogVisible.value = true
}

async function handleSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    const data = { ...form }
    if (form.hellLayer) data.judgmentDetail = `[${form.hellLayer}] ` + (form.judgmentDetail || '')
    if (dialogId.value) { await updateJudgment(dialogId.value, data) }
    else { await createJudgment(data) }
    ElMessage.success('保存成功')
    dialogVisible.value = false; fetchData()
  } finally { saving.value = false }
}

async function handleSubmit(id) {
  await submitReview(id)
  ElMessage.success('已提交复核')
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
</style>
