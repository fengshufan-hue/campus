<template>
  <div>
    <el-row :gutter="20" style="margin-bottom:20px">
      <el-col :span="8"><el-card><div slot="header">待处理预警</div><h1>{{ stats.pendingWarnings || 0 }}</h1></el-card></el-col>
      <el-col :span="8"><el-card><div slot="header">干预记录</div><h1>{{ stats.interventions || 0 }}</h1></el-card></el-col>
      <el-col :span="8"><el-card><div slot="header">负责学生</div><h1>{{ stats.students || 0 }}</h1></el-card></el-col>
    </el-row>
    <el-card><div slot="header">最新预警</div>
      <el-table :data="warningData" border stripe>
        <el-table-column prop="studentId" label="学生ID"/><el-table-column prop="warningLevel" label="等级">
          <template slot-scope="scope"><el-tag :type="scope.row.warningLevel==='高'?'danger':scope.row.warningLevel==='中'?'warning':'info'">{{ scope.row.warningLevel }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="warningReason" label="原因" show-overflow-tooltip/>
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">{{ ['待处理','处理中','已处理'][scope.row.status] || '未知' }}</template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script>
import * as warningApi from '@/api/warning'
import * as userApi from '@/api/user'
export default {
  data() { return { stats: { pendingWarnings: 0, interventions: 0, students: 0 }, warningData: [] } },
  created() { this.loadStats() },
  methods: {
    loadStats() {
      const cid = this.$store.state.user.userId
      warningApi.getWarningList({ counselorId: cid, status: 0, pageNum: 1, pageSize: 100 }).then(r => this.stats.pendingWarnings = (r.data && r.data.total) || 0).catch(() => {})
      warningApi.getInterventionList({ counselorId: cid, pageNum: 1, pageSize: 100 }).then(r => this.stats.interventions = (r.data && r.data.total) || 0).catch(() => {})
      userApi.getStudentList({ counselorId: cid, pageNum: 1, pageSize: 1 }).then(r => this.stats.students = (r.data && r.data.total) || 0).catch(() => {})
      warningApi.getWarningList({ counselorId: cid, pageNum: 1, pageSize: 5 }).then(r => this.warningData = (r.data && r.data.records) || []).catch(() => {})
    }
  }
}
</script>
