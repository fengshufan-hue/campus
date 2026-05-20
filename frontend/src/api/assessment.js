import request from '@/utils/request'

export function getAssessmentList(params) {
  return request.get('/assessment/list', { params })
}
export function getEnabledAssessments() {
  return request.get('/assessment/enabled')
}
export function getAssessment(id) {
  return request.get(`/assessment/${id}`)
}
export function saveAssessment(data) {
  return request.post('/assessment', data)
}
export function updateAssessment(data) {
  return request.put('/assessment', data)
}
export function deleteAssessment(id) {
  return request.delete(`/assessment/${id}`)
}
export function getQuestions(assessmentId) {
  return request.get(`/assessment/question/list/${assessmentId}`)
}
export function saveQuestions(data) {
  return request.post('/assessment/question/batch', data)
}
export function updateQuestion(data) {
  return request.put('/assessment/question', data)
}
export function deleteQuestion(id) {
  return request.delete(`/assessment/question/${id}`)
}
export function submitAssessment(data) {
  return request.post('/assessment/record/submit', data)
}
export function getAssessmentRecords(params) {
  return request.get('/assessment/record/list', { params })
}
