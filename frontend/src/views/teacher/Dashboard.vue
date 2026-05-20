<template>
  <div>
    <el-row :gutter="20" style="margin-bottom:20px">
      <el-col :span="6"><el-card><div slot="header">测评数量</div><h1>{{ stats.assessments || 0 }}</h1></el-card></el-col>
      <el-col :span="6"><el-card><div slot="header">预约数量</div><h1>{{ stats.bookings || 0 }}</h1></el-card></el-col>
      <el-col :span="6"><el-card><div slot="header">文章数量</div><h1>{{ stats.articles || 0 }}</h1></el-card></el-col>
      <el-col :span="6"><el-card><div slot="header">反馈数量</div><h1>{{ stats.feedback || 0 }}</h1></el-card></el-col>
    </el-row>
    <el-card><div slot="header">欢迎使用心理健康服务平台教师端</div><p>您可以在左侧菜单管理测评、排班、预约、咨询记录、科普文章等。</p></el-card>
  </div>
</template>
<script>
import * as assessmentApi from '@/api/assessment'
import * as consultationApi from '@/api/consultation'
import * as articleApi from '@/api/article'
import * as feedbackApi from '@/api/feedback'
export default {
  data() { return { stats: { assessments: 0, bookings: 0, articles: 0, feedback: 0 } } },
  created() {
    const user = JSON.parse(localStorage.getItem('user')) || {}
    const tid = user.userId || this.$store.state.user.userId
    assessmentApi.getAssessmentList({ pageNum: 1, pageSize: 1 }).then(r => { this.stats.assessments = r.data.total || 0 }).catch(() => { this.stats.assessments = 0 })
    consultationApi.getBookingList({ teacherId: tid, pageNum: 1, pageSize: 1 }).then(r => { this.stats.bookings = r.data.total || 0 }).catch(() => { this.stats.bookings = 0 })
    articleApi.getArticleList({ pageNum: 1, pageSize: 1 }).then(r => { this.stats.articles = r.data.total || 0 }).catch(() => { this.stats.articles = 0 })
    feedbackApi.getFeedbackList({ teacherId: tid, pageNum: 1, pageSize: 1 }).then(r => { this.stats.feedback = r.data.total || 0 }).catch(() => { this.stats.feedback = 0 })
  }
}
</script>
