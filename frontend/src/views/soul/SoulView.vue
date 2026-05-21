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
            <el-button type="success" class="btn-red" @click="showDialog(null)" v-if="userStore.roleCode === 'WUCHANG'">新增魂魄</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" class="soul-table"
        header-cell-class-name="table-header" cell-class-name="table-cell">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column label="性别" width="60">
          <template #default="{ row }">{{ row.gender === 0 ? '男' : '女' }}</template>
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
              <span class="tl-dot" :class="{ active: row.soulStatus >= 0 }">勾</span>
              <span class="tl-line" :class="{ active: row.soulStatus >= 1 }"></span>
              <span class="tl-dot" :class="{ active: row.soulStatus >= 1 }">到</span>
              <span class="tl-line" :class="{ active: row.soulStatus >= 2 }"></span>
              <span class="tl-dot" :class="{ active: row.soulStatus >= 2 }">审</span>
              <span class="tl-line" :class="{ active: row.soulStatus >= 3 }"></span>
              <span class="tl-dot" :class="{ active: row.soulStatus >= 3 }">判</span>
              <span class="tl-line" :class="{ active: row.soulStatus >= 4 }"></span>
              <span class="tl-dot" :class="{ active: row.soulStatus >= 4 }">轮</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button size="small" class="btn-ghost" @click="showDialog(row)" v-if="userStore.roleCode === 'WUCHANG'">编辑</el-button>
            <el-button size="small" class="btn-ghost-warning" @click="handleStatus(row)" v-if="row.soulStatus === 0 && userStore.roleCode === 'WUCHANG'">勾魂</el-button>
            <el-button size="small" class="btn-ghost-danger" @click="handleDelete(row.id)" v-if="userStore.roleCode === 'ADMIN'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="search.page"
        v-model:page-size="search.size"
        :total="total"
        layout="total, prev, pager, next"
        background
        class="pagination"
        @change="fetchData"
      />
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogId ? '编辑魂魄' : '新增魂魄'" width="500px"
      class="dialog-dark">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :value="0">男</el-radio>
            <el-radio :value="1">女</el-radio>
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
        <el-button type="primary" class="btn-gold" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getSouls, createSoul, updateSoul, deleteSoul, updateSoulStatus } from '@/api/soul'
import { useUserStore } from '@/store/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDate, soulStatusMap } from '@/utils'

const userStore = useUserStore()

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogId = ref(null)
const formRef = ref(null)

const search = reactive({ page: 1, size: 10, name: '', soulStatus: null })
const statusOptions = { 0: '待勾魂', 1: '已到殿', 2: '审判中', 3: '已判决', 4: '已轮回' }
const form = reactive({ name: '', gender: 0, birthDate: '', deathDate: '', description: '' })
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
  form.gender = row?.gender ?? 0
  form.birthDate = row?.birthDate || ''
  form.deathDate = row?.deathDate || ''
  form.description = row?.description || ''
  dialogVisible.value = true
}

async function handleSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (dialogId.value) { await updateSoul(dialogId.value, form); ElMessage.success('修改成功') }
  else { await createSoul(form); ElMessage.success('新增成功') }
  dialogVisible.value = false; fetchData()
}

async function handleStatus(row) {
  await updateSoulStatus(row.id, 1)
  ElMessage.success('已勾魂')
  fetchData()
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
.page-header { margin-bottom: 20px; }
.page-title { font-size: 22px; color: #ffd700; letter-spacing: 4px; }
.page-desc { font-size: 12px; color: #666; letter-spacing: 2px; margin-top: 4px; }

.content-card { background: #1a1a2e; border: 1px solid #2a2a3e; border-radius: 12px; padding: 20px; }

.toolbar { margin-bottom: 16px; }

.search-input { width: 160px; }
.search-input :deep(.el-input__wrapper) { background: rgba(255,255,255,0.05); border: 1px solid #333; box-shadow: none; }
.search-input :deep(.el-input__inner) { color: #e0d5c0; }

.soul-table { width: 100%; }
.soul-table :deep(.table-header) { background: #0a0a0f !important; color: #ffd700; }
.soul-table :deep(.table-cell) { background: rgba(26,26,46,0.8); color: #ccc; }
.soul-table :deep(.el-table__row:hover td) { background: rgba(255,215,0,0.05) !important; }

:deep(.el-table--border) { border-color: #2a2a3e; }
:deep(.el-table__body) { background: transparent; }

.btn-gold { background: linear-gradient(135deg, #8b0000, #4a0000); border: 1px solid #ffd700; color: #ffd700; }
.btn-gold:hover { background: linear-gradient(135deg, #a00000, #6b0000); color: #ffd700; }
.btn-red { background: #8b0000; border-color: #8b0000; color: white; }
.btn-red:hover { background: #a00000; border-color: #a00000; }
.btn-ghost { background: transparent; border: 1px solid #555; color: #ccc; }
.btn-ghost:hover { border-color: #ffd700; color: #ffd700; }
.btn-ghost-warning { background: transparent; border: 1px solid #e67e22; color: #e67e22; }
.btn-ghost-warning:hover { background: #e67e2215; }
.btn-ghost-danger { background: transparent; border: 1px solid #e74c3c; color: #e74c3c; }
.btn-ghost-danger:hover { background: #e74c3c15; }

.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
.pagination :deep(.el-pager li) { background: #0a0a0f; color: #888; }
.pagination :deep(.el-pager li.active) { background: #8b0000; color: #ffd700; border-color: #ffd700; }

.dialog-dark :deep(.el-dialog) { background: #1a1a2e; border: 1px solid #2a2a3e; }
.dialog-dark :deep(.el-dialog__title) { color: #ffd700; }
.dialog-dark :deep(.el-form-item__label) { color: #ccc; }
.dialog-dark :deep(.el-input__wrapper) { background: rgba(255,255,255,0.05); border: 1px solid #333; box-shadow: none; }
.dialog-dark :deep(.el-input__inner) { color: #e0d5c0; }
.dialog-dark :deep(.el-textarea__inner) { background: rgba(255,255,255,0.05); border: 1px solid #333; color: #e0d5c0; }

.timeline { display: flex; align-items: center; gap: 0; }
.tl-dot {
  width: 20px; height: 20px; border-radius: 50%;
  background: #222; color: #555; font-size: 10px;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
}
.tl-dot.active { background: #8b0000; color: #ffd700; box-shadow: 0 0 6px rgba(139,0,0,0.6); }
.tl-line { width: 12px; height: 2px; background: #222; flex-shrink: 0; }
.tl-line.active { background: #8b0000; }

</style>
