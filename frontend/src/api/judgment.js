import request from './request'

export function getJudgments(params) {
  return request.get('/judgments', { params })
}

export function createJudgment(data) {
  return request.post('/judgments', data)
}

export function updateJudgment(id, data) {
  return request.put(`/judgments/${id}`, data)
}

export function submitReview(id) {
  return request.post(`/judgments/${id}/submit`)
}
