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
        <template #empty>
          <div class="empty-state"><span class="empty-icon">👤</span><span class="empty-text">暂无官员记录</span></div>
        </template>
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
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="官号" prop="username"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="密码" prop="password"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-form-item label="官名" prop="realName"><el-input v-model="form.realName" /></el-form-item>
        <el-form-item label="职司" prop="roleId">
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
        <el-button type="primary" class="btn-gold" @click="handleSave" :loading="saving">保存</el-button>
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
const saving = ref(false)
const tableData = ref([])
const total = ref(0)
const page = ref(1)
const size = ref(20)
const dialogVisible = ref(false)
const dialogId = ref(null)
const formRef = ref(null)
const form = reactive({ username: '', password: '', realName: '', roleId: 2, status: 1 })
const formRules = {
  username: [{ required: true, message: '请输入官号', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入官名', trigger: 'blur' }],
  roleId: [{ required: true, message: '请选择职司', trigger: 'change' }]
}

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
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  saving.value = true
  try {
    if (dialogId.value) { await updateUser(dialogId.value, form); ElMessage.success('修改成功') }
    else { await createUser(form); ElMessage.success('新增成功') }
    dialogVisible.value = false; fetchData()
  } finally { saving.value = false }
}

async function handleDelete(id) {
  await ElMessageBox.confirm('确定罢免该官员？', '提示', { confirmButtonClass: 'btn-red' })
  await deleteUser(id); ElMessage.success('已罢免'); fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
</style>
