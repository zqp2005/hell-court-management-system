<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">魂魄名录</h2>
      <p class="page-desc">众生生死簿 · 天地人三界</p>
    </div>

    <div class="content-card">
      <div class="toolbar">
        <el-form :inline="true" :model="search" class="search-form">
          <el-form-item label="姓名">
            <el-input v-model="search.name" placeholder="搜索姓名..." clearable class="search-input" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="search.soulStatus" placeholder="全部" clearable class="search-input">
              <el-option v-for="(v, k) in statusOptions" :key="k" :label="v" :value="Number(k)" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" class="btn-gold" @click="fetchData">查 询</el-button>
            <el-button type="success" class="btn-red" @click="showDialog(null)" v-if="userStore.roleCode === ROLE.WUCHANG">新增魂魄</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" class="dark-table"
        header-cell-class-name="table-header" cell-class-name="table-cell">
        <template #empty>
          <div class="empty-state"><span class="empty-icon">👻</span><span class="empty-text">暂无魂魄记录</span></div>
        </template>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column label="性别" width="60">
          <template #default="{ row }">{{ row.gender === GENDER.MALE ? '男' : '女' }}</template>
        </el-table-column>
        <el-table-column prop="birthDate" label="出生" width="110">
          <template #default="{ row }">{{ formatDate(row.birthDate) }}</template>
        </el-table-column>
        <el-table-column prop="deathDate" label="死亡" width="110">
          <template #default="{ row }">{{ formatDate(row.deathDate) }}</template>
        </el-table-column>
        <el-table-column prop="lifespan" label="阳寿" width="60" />
        <el-table-column label="进度" width="180">
          <template #default="{ row }">
            <div class="timeline">
              <span class="tl-dot" :class="{ active: row.soulStatus >= SOUL_STATUS.PENDING }">勾</span>
              <span class="tl-line" :class="{ active: row.soulStatus >= SOUL_STATUS.ARRIVED }"></span>
              <span class="tl-dot" :class="{ active: row.soulStatus >= SOUL_STATUS.ARRIVED }">到</span>
              <span class="tl-line" :class="{ active: row.soulStatus >= SOUL_STATUS.JUDGING }"></span>
              <span class="tl-dot" :class="{ active: row.soulStatus >= SOUL_STATUS.JUDGING }">审</span>
              <span class="tl-line" :class="{ active: row.soulStatus >= SOUL_STATUS.JUDGED }"></span>
              <span class="tl-dot" :class="{ active: row.soulStatus >= SOUL_STATUS.JUDGED }">判</span>
              <span class="tl-line" :class="{ active: row.soulStatus >= SOUL_STATUS.REBIRTH }"></span>
              <span class="tl-dot" :class="{ active: row.soulStatus >= SOUL_STATUS.REBIRTH }">轮</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button size="small" class="btn-ghost" @click="showDialog(row)" v-if="userStore.roleCode === ROLE.WUCHANG">编辑</el-button>
            <el-button size="small" class="btn-ghost-warning" @click="handleStatus(row)" v-if="row.soulStatus === SOUL_STATUS.PENDING && userStore.roleCode === ROLE.WUCHANG" :loading="statusLoading === row.id">勾魂</el-button>
            <el-button size="small" class="btn-ghost-danger" @click="handleDelete(row.id)" v-if="userStore.roleCode === ROLE.ADMIN">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="search.page" v-model:page-size="search.size"
        :total="total" layout="total, prev, pager, next" background
        class="pagination" @change="fetchData" />
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogId ? '编辑魂魄' : '新增魂魄'" width="500px" class="dialog-dark">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="姓名" prop="name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :value="GENDER.MALE">男</el-radio>
            <el-radio :value="GENDER.FEMALE">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出生" prop="birthDate">
          <el-date-picker v-model="form.birthDate" type="datetime" style="width: 100%" />
        </el-form-item>
        <el-form-item label="死亡">
          <el-date-picker v-model="form.deathDate" type="datetime" style="width: 100%" />
        </el-form-item>
        <el-form-item label="生平">
          <el-input v-model="form.description" type="textarea" :rows="3" />
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
import { getSouls, createSoul, updateSoul, deleteSoul, updateSoulStatus } from '@/api/soul'
import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDate } from '@/utils'
import { ROLE, SOUL_STATUS, GENDER } from '@/utils/constants'

const userStore = useUserStore()

const loading = ref(false)
const saving = ref(false)
const statusLoading = ref(null)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogId = ref(null)
const formRef = ref(null)

const search = reactive({ page: 1, size: 10, name: '', soulStatus: null })
const statusOptions = { 0: '待勾魂', 1: '已到殿', 2: '审判中', 3: '已判决', 4: '已轮回' }
const form = reactive({ name: '', gender: GENDER.MALE, birthDate: '', deathDate: '', description: '' })
const formRules = { name: [{ required: true, message: '请输入姓名' }], birthDate: [{ required: true, message: '请选择出生日期' }] }

async function fetchData() {
  loading.value = true
  const res = await getSouls(search)
  tableData.value = res.data.records
  total.value = res.data.total
  loading.value = false
}

function showDialog(row) {
  dialogId.value = row?.id || null
  form.name = row?.name || ''
  form.gender = row?.gender ?? GENDER.MALE
  form.birthDate = row?.birthDate || ''
  form.deathDate = row?.deathDate || ''
  form.description = row?.description || ''
  dialogVisible.value = true
}

async function handleSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (dialogId.value) { await updateSoul(dialogId.value, form); ElMessage.success('修改成功') }
    else { await createSoul(form); ElMessage.success('新增成功') }
    dialogVisible.value = false; fetchData()
  } finally { saving.value = false }
}

async function handleStatus(row) {
  statusLoading.value = row.id
  try {
    await updateSoulStatus(row.id, SOUL_STATUS.ARRIVED)
    ElMessage.success('已勾魂')
    fetchData()
  } finally { statusLoading.value = null }
}

async function handleDelete(id) {
  await ElMessageBox.confirm('确定移除该魂魄？', '提示', { confirmButtonClass: 'btn-red' })
  await deleteSoul(id)
  ElMessage.success('已移除')
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.toolbar { margin-bottom: 16px; }
.search-input { width: 160px; }

.timeline { display: flex; align-items: center; gap: 0; }
.tl-dot {
  width: 20px; height: 20px; border-radius: 50%;
  background: #222; color: #555; font-size: 10px;
  display: flex; align-items: center; justify-content: center; flex-shrink: 0;
  transition: all 0.3s;
}
.tl-dot.active { background: #8b0000; color: #ffd700; box-shadow: 0 0 6px rgba(139,0,0,0.6); }
.tl-line { width: 12px; height: 2px; background: #222; flex-shrink: 0; transition: background 0.3s; }
.tl-line.active { background: #8b0000; }
</style>
