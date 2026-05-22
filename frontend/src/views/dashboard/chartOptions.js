import echarts from '@/utils/echarts'

export function initChart(el, option) {
  if (!el) return null
  const instance = echarts.init(el)
  instance.setOption(option)
  return instance
}

export function disposeChart(instance) {
  instance?.dispose()
}

export function makeLineOption(data) {
  return {
    backgroundColor: 'transparent',
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(10,10,15,0.9)', borderColor: '#8b0000', textStyle: { color: '#ccc', fontSize: 13 } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: data.map(m => m.month), axisLine: { lineStyle: { color: '#444' } } },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#222', type: 'dashed' } } },
    series: [{
      data: data.map(m => m.count),
      type: 'line', smooth: true,
      lineStyle: { color: '#ffd700', width: 3 },
      areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(255,215,0,0.3)' }, { offset: 1, color: 'rgba(255,215,0,0)' }]) },
      symbol: 'circle', symbolSize: 8, itemStyle: { color: '#ffd700' }
    }]
  }
}

export function makePieOption(data) {
  const pathNames = ['天道', '阿修罗道', '人道', '畜生道', '饿鬼道', '地狱道']
  const pathColors = ['#ffd700', '#ff6b35', '#2ecc71', '#95a5a6', '#e74c3c', '#8b0000']
  return {
    backgroundColor: 'transparent',
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    series: [{
      type: 'pie', radius: ['40%', '70%'], center: ['50%', '50%'], roseType: 'area',
      data: data.map((d, i) => ({ name: pathNames[d.path - 1] || '未知', value: d.count, itemStyle: { color: pathColors[d.path - 1] || '#666' } })),
      label: { color: '#ccc', fontSize: 12 },
      labelLine: { lineStyle: { color: '#555' } },
      itemStyle: { borderRadius: 4, borderColor: '#1a1a2e', borderWidth: 2 }
    }]
  }
}

export { echarts }
