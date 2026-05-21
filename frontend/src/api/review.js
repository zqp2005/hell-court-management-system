import request from './request'

export function getReviews(params) {
  return request.get('/reviews', { params })
}

export function approveReview(data) {
  return request.post('/reviews', data)
}
