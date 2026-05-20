import request from '@/utils/request'

export function getScheduleList(params) {
  return request.get('/schedule/list', { params })
}
export function getOpenSchedules() {
  return request.get('/schedule/open')
}
export function getSchedule(id) {
  return request.get(`/schedule/${id}`)
}
export function saveSchedule(data) {
  return request.post('/schedule', data)
}
export function updateSchedule(data) {
  return request.put('/schedule', data)
}
export function deleteSchedule(id) {
  return request.delete(`/schedule/${id}`)
}
