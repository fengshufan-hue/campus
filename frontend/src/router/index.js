import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/common/Login.vue')
  },
  {
    path: '/',
    redirect: '/login'
  },
  // Admin routes
  {
    path: '/admin',
    component: () => import('../components/Layout.vue'),
    meta: { role: 'admin' },
    children: [
      { path: '', redirect: '/admin/dashboard' },
      { path: 'dashboard', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'student', component: () => import('../views/admin/StudentManage.vue') },
      { path: 'counselor', component: () => import('../views/admin/CounselorManage.vue') },
      { path: 'teacher', component: () => import('../views/admin/TeacherManage.vue') },
      { path: 'announcement', component: () => import('../views/admin/AnnouncementManage.vue') },
      { path: 'profile', component: () => import('../views/common/Profile.vue') }
    ]
  },
  // Student routes
  {
    path: '/student',
    component: () => import('../components/Layout.vue'),
    meta: { role: 'student' },
    children: [
      { path: '', redirect: '/student/dashboard' },
      { path: 'dashboard', component: () => import('../views/student/Dashboard.vue') },
      { path: 'assessment', component: () => import('../views/student/AssessmentList.vue') },
      { path: 'assessment/take/:id', component: () => import('../views/student/AssessmentTake.vue') },
      { path: 'assessment/record', component: () => import('../views/student/AssessmentRecord.vue') },
      { path: 'booking', component: () => import('../views/student/BookingList.vue') },
      { path: 'booking/new', component: () => import('../views/student/BookingNew.vue') },
      { path: 'consultation/record', component: () => import('../views/student/ConsultationRecord.vue') },
      { path: 'article', component: () => import('../views/student/ArticleList.vue') },
      { path: 'article/:id', component: () => import('../views/student/ArticleDetail.vue') },
      { path: 'feedback', component: () => import('../views/student/FeedbackList.vue') },
      { path: 'announcement', component: () => import('../views/student/AnnouncementList.vue') },
      { path: 'profile', component: () => import('../views/common/Profile.vue') }
    ]
  },
  // Counselor routes
  {
    path: '/counselor',
    component: () => import('../components/Layout.vue'),
    meta: { role: 'counselor' },
    children: [
      { path: '', redirect: '/counselor/dashboard' },
      { path: 'dashboard', component: () => import('../views/counselor/Dashboard.vue') },
      { path: 'warning', component: () => import('../views/counselor/WarningList.vue') },
      { path: 'intervention', component: () => import('../views/counselor/InterventionList.vue') },
      { path: 'student', component: () => import('../views/counselor/StudentList.vue') },
      { path: 'assessment/record', component: () => import('../views/counselor/AssessmentRecord.vue') },
      { path: 'announcement', component: () => import('../views/counselor/AnnouncementList.vue') },
      { path: 'profile', component: () => import('../views/common/Profile.vue') }
    ]
  },
  // Teacher routes
  {
    path: '/teacher',
    component: () => import('../components/Layout.vue'),
    meta: { role: 'teacher' },
    children: [
      { path: '', redirect: '/teacher/dashboard' },
      { path: 'dashboard', component: () => import('../views/teacher/Dashboard.vue') },
      { path: 'assessment', component: () => import('../views/teacher/AssessmentManage.vue') },
      { path: 'assessment/edit/:id', component: () => import('../views/teacher/AssessmentEdit.vue') },
      { path: 'schedule', component: () => import('../views/teacher/ScheduleManage.vue') },
      { path: 'booking', component: () => import('../views/teacher/BookingManage.vue') },
      { path: 'consultation/record', component: () => import('../views/teacher/ConsultationRecord.vue') },
      { path: 'article', component: () => import('../views/teacher/ArticleManage.vue') },
      { path: 'article/edit', component: () => import('../views/teacher/ArticleEdit.vue') },
      { path: 'feedback', component: () => import('../views/teacher/FeedbackList.vue') },
      { path: 'announcement', component: () => import('../views/teacher/AnnouncementList.vue') },
      { path: 'profile', component: () => import('../views/common/Profile.vue') }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

// Navigation guard
router.beforeEach((to, from, next) => {
  let user = null
  try {
    user = JSON.parse(localStorage.getItem('user'))
  } catch (e) {
    localStorage.removeItem('user')
  }

  if (to.path !== '/login' && !user) {
    next('/login')
  } else if (to.meta.role && user && user.role && to.meta.role !== user.role) {
    next('/' + user.role)
  } else {
    next()
  }
})

export default router
