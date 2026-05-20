import request from '@/utils/request'

export function getStudentList(params) {
  return request.get('/student/list', { params })
}
export function getStudent(id) {
  return request.get(`/student/${id}`)
}
export function saveStudent(data) {
  return request.post('/student', data)
}
export function updateStudent(data) {
  return request.put('/student', data)
}
export function deleteStudent(id) {
  return request.delete(`/student/${id}`)
}
export function resetStudentPwd(id) {
  return request.put(`/student/resetPwd/${id}`)
}
export function resetCounselorPwd(id) {
  return request.put(`/counselor/resetPwd/${id}`)
}
export function resetTeacherPwd(id) {
  return request.put(`/teacher/resetPwd/${id}`)
}
export function resetAdminPwd(id) {
  return request.put(`/admin/resetPwd/${id}`)
}
export function getCounselorList(params) {
  return request.get('/counselor/list', { params })
}
export function getCounselor(id) {
  return request.get(`/counselor/${id}`)
}
export function saveCounselor(data) {
  return request.post('/counselor', data)
}
export function updateCounselor(data) {
  return request.put('/counselor', data)
}
export function deleteCounselor(id) {
  return request.delete(`/counselor/${id}`)
}
export function getTeacherList(params) {
  return request.get('/teacher/list', { params })
}
export function getTeacher(id) {
  return request.get(`/teacher/${id}`)
}
export function saveTeacher(data) {
  return request.post('/teacher', data)
}
export function updateTeacher(data) {
  return request.put('/teacher', data)
}
export function deleteTeacher(id) {
  return request.delete(`/teacher/${id}`)
}
export function changePassword(role, data) {
  return request.put(`/${role}/changePwd`, data)
}
