<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">地府官制</h2>
      <p class="page-desc">阴司人事管理</p>
    </div>
    <div class="content-card">
      <el-button class="btn-red" style="margin-bottom: 16px" @click="showDialog(null)">新增官员</el-button>
      <el-table :data="tableData" border stripe v-loading="loading" class="dark-table"
        header-cell-class-name="table-header" cell-class-name="table-cell">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="username" label="官号" />
        <el-table-column prop="realName" label="官名" />
        <el-table-column label="职司" width="100">
          <template #default="{ row }"><el-tag effect="dark" :type="row.roleId === 4 ? 'danger' : row.roleId === 3 ? 'warning' : row.roleId === 2 ? 'primary' : 'info'">{{ row.roleName || roleMap[row.roleId] }}</el-tag></template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" effect="dark">{{ row.status === 1 ? '在任' : '卸任' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" class="btn-ghost" @click="showDialog(row)">编辑</el-button>
            <el-button size="small" class="btn-ghost-danger" @click="handleDelete(row.id)">罢免</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="page" v-model:page-size="size" :total="total"
        layout="total, prev, pager, next" background class="pagination" @change="fetchData" />
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogId ? '编辑官员' : '新增官员'" width="450px" class="dialog-dark">
      <el-form :model="form" label-width="80px">
        <el-form-item label="官号"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-form-item label="官名"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="职司">
          <el-select v-model="form.roleId" style="width: 100%">
            <el-option v-for="(v, k) in roleMap" :key="k" :label="v" :value="Number(k)" />
          </el-select>
        </el-form-item>
        <el-form-item label="在任" v-if="dialogId">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
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
import { getUsers, createUser, updateUser, deleteUser } from '@/api/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { roleMap } from '@/utils'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const dialogVisible = ref(false)
const dialogId = ref(null)
const form = reactive({ username: '', password: '', realName: '', roleId: 2, status: 1 })

async function fetchData() {
  loading.value = true
  const res = await getUsers({ page: page.value, size: size.value })
  tableData.value = res.data.records
  total.value = res.data.total
  loading.value = false
}

function showDialog(row) {
  dialogId.value = row?.id || null
  form.username = row?.username || ''
  form.password = ''
  form.realName = row?.realName || ''
  form.roleId = row?.roleId ?? 2
  form.status = row?.status ?? 1
  dialogVisible.value = true
}

async function handleSave() {
  if (dialogId.value) { await updateUser(dialogId.value, form); ElMessage.success('修改成功') }
  else { await createUser(form); ElMessage.success('新增成功') }
  dialogVisible.value = false; fetchData()
}

async function handleDelete(id) {
  await ElMessageBox.confirm('确定罢免该官员？', '提示', { confirmButtonClass: 'btn-red' })
  await deleteUser(id); ElMessage.success('已罢免'); fetchData()
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
.btn-red:hover { background: #a00000; }
.btn-gold { background: linear-gradient(135deg, #8b0000, #4a0000); border: 1px solid #ffd700; color: #ffd700; }
.btn-gold:hover { background: linear-gradient(135deg, #a00000, #6b0000); }
.btn-ghost { background: transparent; border: 1px solid #555; color: #ccc; }
.btn-ghost:hover { border-color: #ffd700; color: #ffd700; }
.btn-ghost-danger { background: transparent; border: 1px solid #e74c3c; color: #e74c3c; }
.btn-ghost-danger:hover { background: rgba(231,76,60,0.1); }
.dialog-dark :deep(.el-dialog) { background: #1a1a2e; border: 1px solid #2a2a3e; }
.dialog-dark :deep(.el-dialog__title) { color: #ffd700; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
.pagination :deep(.el-pager li) { background: #0a0a0f; color: #888; }
.pagination :deep(.el-pager li.active) { background: #8b0000; color: #ffd700; }
</style>
