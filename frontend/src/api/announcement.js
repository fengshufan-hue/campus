import request from '@/utils/request'

export function getAnnouncementList(params) {
  return request.get('/announcement/list', { params })
}
export function getPublishedAnnouncements(params) {
  return request.get('/announcement/published', { params })
}
export function getAnnouncement(id) {
  return request.get(`/announcement/${id}`)
}
export function saveAnnouncement(data) {
  return request.post('/announcement', data)
}
export function updateAnnouncement(data) {
  return request.put('/announcement', data)
}
export function publishAnnouncement(id) {
  return request.put(`/announcement/publish/${id}`)
}
export function deleteAnnouncement(id) {
  return request.delete(`/announcement/${id}`)
}
