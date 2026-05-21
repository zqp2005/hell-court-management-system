import request from './request'

export function getStatistics() {
  return request.get('/dashboard/statistics')
}
