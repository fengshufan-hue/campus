import request from '@/utils/request'

export function getFeedbackList(params) {
  return request.get('/feedback/list', { params })
}
export function getMyFeedback(params) {
  return request.get('/feedback/my', { params })
}
export function deleteConsultationRecord(id) {
  return request.delete(`/consultation/record/${id}`)
}
export function saveFeedback(data) {
  return request.post('/feedback', data)
}
export function getFeedback(id) {
  return request.get(`/feedback/${id}`)
}
export function deleteFeedback(id) {
  return request.delete(`/feedback/${id}`)
}
export function getConsultationRecords(params) {
  return request.get('/consultation/record/list', { params })
}
export function saveConsultationRecord(data) {
  return request.post('/consultation/record', data)
}
export function updateConsultationRecord(data) {
  return request.put('/consultation/record', data)
}
