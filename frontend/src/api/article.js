import request from '@/utils/request'

export function getArticleList(params) {
  return request.get('/article/list', { params })
}
export function getPublishedArticles(params) {
  return request.get('/article/published', { params })
}
export function getArticle(id) {
  return request.get(`/article/${id}`)
}
export function saveArticle(data) {
  return request.post('/article', data)
}
export function updateArticle(data) {
  return request.put('/article', data)
}
export function publishArticle(id) {
  return request.put(`/article/publish/${id}`)
}
export function incrementView(id) {
  return request.put(`/article/view/${id}`)
}
export function deleteArticle(id) {
  return request.delete(`/article/${id}`)
}
