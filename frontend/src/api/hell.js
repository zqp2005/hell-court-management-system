import request from './request'

export function getHellLayers() {
  return request.get('/hell-layers')
}
