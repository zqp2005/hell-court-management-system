import request from './request'

export function getRebirths(params) {
  return request.get('/rebirths', { params })
}

export function createRebirth(data) {
  return request.post('/rebirths', data)
}

export function exportRebirths() {
  return request.get('/rebirths/export', { responseType: 'blob' })
}
