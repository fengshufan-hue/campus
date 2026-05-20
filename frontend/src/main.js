import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI)
Vue.config.productionTip = false

// Global date formatter: '2026-04-10T23:30:37' → '2026-04-10 23:30'
Vue.filter('formatDate', val => {
  if (!val) return ''
  const d = new Date(val)
  if (isNaN(d.getTime())) return val
  const pad = n => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
