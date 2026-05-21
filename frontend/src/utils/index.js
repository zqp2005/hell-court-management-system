export function formatDate(date) {
  if (!date) return '-'
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

export function formatDateTime(date) {
  if (!date) return '-'
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hour = String(d.getHours()).padStart(2, '0')
  const min = String(d.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${min}`
}

export const soulStatusMap = {
  0: { label: '待勾魂', type: 'info' },
  1: { label: '已到殿', type: '' },
  2: { label: '审判中', type: 'warning' },
  3: { label: '已判决', type: 'success' },
  4: { label: '已轮回', type: 'primary' }
}

export const roleMap = {
  1: '无常',
  2: '判官',
  3: '阎王',
  4: '管理员'
}

export const rebirthPathMap = {
  1: '天道',
  2: '阿修罗道',
  3: '人道',
  4: '畜生道',
  5: '饿鬼道',
  6: '地狱道'
}
