import request from './request'

export function getDemons() {
  return request.get('/demons')
}
