import request from '@/utils/request'

export function getBookingList(params) {
  return request.get('/booking/list', { params })
}
export function bookConsultation(data) {
  return request.post('/booking', data)
}
export function confirmBooking(data) {
  return request.put('/booking/confirm', data)
}
export function getBooking(id) {
  return request.get(`/booking/${id}`)
}
export function deleteBooking(id) {
  return request.delete(`/booking/${id}`)
}
