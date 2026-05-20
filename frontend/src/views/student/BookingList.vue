<template>
  <div><el-card><div slot="header"><span>我的预约</span></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="scheduleId" label="排班ID"/><el-table-column prop="reason" label="预约原因" show-overflow-tooltip/>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">{{ ['待确认','已确认','已取消','已完成'][scope.row.status] || '未知' }}</template>
      </el-table-column>
      <el-table-column prop="teacherReply" label="教师回复" show-overflow-tooltip/>
      <el-table-column prop="createTime" label="预约时间" width="160">
        <template slot-scope="scope">{{ scope.row.createTime | formatDate }}</template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status===0" size="mini" type="danger" @click="handleCancel(scope.row)">取消</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card></div>
</template>
<script>
import * as api from '@/api/consultation'
export default {
  data() { return { tableData: [], studentId: null } },
  created() { this.studentId = this.$store.state.user.userId; api.getBookingList({ studentId: this.studentId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = r.data.records || []).catch(() => {}) },
  methods: {
    loadData() { api.getBookingList({ studentId: this.studentId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = r.data.records || []).catch(() => {}) },
    handleCancel(row) {
      const originalStatus = row.status
      row.status = 2
      api.confirmBooking(row).then(() => {
        this.$message.success('已取消')
        this.loadData()
      }).catch(() => {
        row.status = originalStatus
      })
    }
  }
}
</script>
