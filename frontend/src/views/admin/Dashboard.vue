<template>
  <div>
    <el-row :gutter="20" style="margin-bottom:20px">
      <el-col :span="8"><el-card><div slot="header">学生总数</div><h1>{{ stats.studentCount || 0 }}</h1></el-card></el-col>
      <el-col :span="8"><el-card><div slot="header">辅导员总数</div><h1>{{ stats.counselorCount || 0 }}</h1></el-card></el-col>
      <el-col :span="8"><el-card><div slot="header">心理教师总数</div><h1>{{ stats.teacherCount || 0 }}</h1></el-card></el-col>
    </el-row>
    <el-card><div slot="header">欢迎使用校园心理健康服务平台管理系统</div>
      <p>您可以在左侧菜单管理学生、辅导员和心理教师信息，发布系统公告。</p>
    </el-card>
  </div>
</template>

<script>
import * as userApi from '@/api/user'
export default {
  data() { return { stats: { studentCount: 0, counselorCount: 0, teacherCount: 0 } } },
  created() { this.loadStats() },
  methods: {
    loadStats() {
      userApi.getStudentList({ pageNum: 1, pageSize: 1 }).then(r => this.stats.studentCount = (r.data && r.data.total) || 0).catch(() => {})
      userApi.getCounselorList({ pageNum: 1, pageSize: 1 }).then(r => this.stats.counselorCount = (r.data && r.data.total) || 0).catch(() => {})
      userApi.getTeacherList({ pageNum: 1, pageSize: 1 }).then(r => this.stats.teacherCount = (r.data && r.data.total) || 0).catch(() => {})
    }
  }
}
</script>
