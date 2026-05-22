import request from './request'

export function getRebirths(params) {
  return request.get('/rebirths', { params })
}

export function createRebirth(data) {
  return request.post('/rebirths', data)
}

export async function exportRebirths() {
  const token = localStorage.getItem('token')
  const res = await fetch('/api/rebirths/export', {
    headers: { Authorization: 'Bearer ' + token }
  })
  if (!res.ok) throw new Error('导出失败')
  return res.blob()
}
