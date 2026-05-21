import request from './request'

export function getSouls(params) {
  return request.get('/souls', { params })
}

export function getSoulById(id) {
  return request.get(`/souls/${id}`)
}

export function createSoul(data) {
  return request.post('/souls', data)
}

export function updateSoul(id, data) {
  return request.put(`/souls/${id}`, data)
}

export function deleteSoul(id) {
  return request.delete(`/souls/${id}`)
}

export function updateSoulStatus(id, status) {
  return request.put(`/souls/${id}/status`, null, { params: { status } })
}
