import axios from 'axios'
import { Message } from 'element-ui'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// Request interceptor
request.interceptors.request.use(
  config => {
    const user = localStorage.getItem('user')
    if (user) {
      try {
        const parsed = JSON.parse(user)
        if (parsed.token) {
          config.headers['Authorization'] = 'Bearer ' + parsed.token
        }
      } catch (e) {
        localStorage.removeItem('user')
      }
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// Response interceptor
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === undefined || res.code !== 200) {
      Message.error(res.message || '操作失败')
      if (res.code === 401) {
        localStorage.removeItem('user')
        window.location.href = '/login'
      }
      return Promise.reject(new Error(res.message || '操作失败'))
    }
    return res
  },
  error => {
    const status = error.response ? error.response.status : null
    if (status === 401) {
      Message.error('登录已过期，请重新登录')
      localStorage.removeItem('user')
      window.location.href = '/login'
    } else if (status === 404) {
      Message.error('请求的资源不存在')
    } else if (status === 500) {
      Message.error('服务器内部错误')
    } else if (!error.response) {
      Message.error('网络请求失败，请检查网络连接')
    } else {
      Message.error(error.response.data && error.response.data.message ? error.response.data.message : '操作失败')
    }
    return Promise.reject(error)
  }
)

export default request
