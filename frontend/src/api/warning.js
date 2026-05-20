import request from '@/utils/request'

export function getWarningList(params) {
  return request.get('/warning/list', { params })
}
export function saveWarning(data) {
  return request.post('/warning', data)
}
export function updateWarning(data) {
  return request.put('/warning', data)
}
export function assignWarning(warningId, counselorId) {
  return request.put('/warning/assign', null, { params: { warningId, counselorId } })
}
export function getWarning(id) {
  return request.get(`/warning/${id}`)
}
export function deleteWarning(id) {
  return request.delete(`/warning/${id}`)
}
export function getInterventionList(params) {
  return request.get('/intervention/list', { params })
}
export function saveIntervention(data) {
  return request.post('/intervention', data)
}
export function updateIntervention(data) {
  return request.put('/intervention', data)
}
export function deleteIntervention(id) {
  return request.delete(`/intervention/${id}`)
}
