<template>
  <div>
    <el-row :gutter="20" style="margin-bottom:20px">
      <el-col :span="8"><el-card><div slot="header">可参与测评</div><h1>{{ stats.assessments || 0 }}</h1></el-card></el-col>
      <el-col :span="8"><el-card><div slot="header">我的预约</div><h1>{{ stats.bookings || 0 }}</h1></el-card></el-col>
      <el-col :span="8"><el-card><div slot="header">已完成咨询</div><h1>{{ stats.consultations || 0 }}</h1></el-card></el-col>
    </el-row>
    <el-card><div slot="header">最新公告</div>
      <div v-for="a in announcements" :key="a.announcementId" style="padding:10px 0;border-bottom:1px solid #eee">
        <span style="font-weight:bold">{{ a.title }}</span>
        <span style="float:right;color:#999;font-size:12px">{{ a.publishTime | formatDate }}</span>
      </div>
      <div v-if="!announcements.length" style="color:#999;text-align:center;padding:20px">暂无公告</div>
    </el-card>
  </div>
</template>
<script>
import * as assessmentApi from '@/api/assessment'
import * as consultationApi from '@/api/consultation'
import * as announcementApi from '@/api/announcement'
export default {
  data() { return { stats: { assessments: 0, bookings: 0, consultations: 0 }, announcements: [] } },
  created() { this.loadStats() },
  methods: {
    loadStats() {
      const uid = this.$store.state.user.userId
      assessmentApi.getEnabledAssessments().then(r => this.stats.assessments = r.data ? r.data.total || (Array.isArray(r.data) ? r.data.length : 0) : 0).catch(() => {})
      consultationApi.getBookingList({ studentId: uid, pageNum: 1, pageSize: 100 }).then(r => { this.stats.bookings = (r.data && r.data.total) || 0; this.stats.consultations = (r.data && r.data.records) ? r.data.records.filter(b => b.status === 3).length : 0 }).catch(() => {})
      announcementApi.getPublishedAnnouncements({ pageNum: 1, pageSize: 5 }).then(r => this.announcements = (r.data && r.data.records) || []).catch(() => {})
    }
  }
}
</script>
