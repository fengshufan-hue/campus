<template>
  <div><el-card><div slot="header"><span>预约管理</span></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="bookingId" label="ID" width="60"/><el-table-column prop="studentId" label="学生ID"/>
      <el-table-column prop="reason" label="原因" show-overflow-tooltip/>
      <el-table-column prop="status" label="状态"><template slot-scope="scope">{{ ['待确认','已确认','已取消','已完成'][scope.row.status] || '未知' }}</template></el-table-column>
      <el-table-column prop="createTime" label="预约时间" width="160">
        <template slot-scope="scope">{{ scope.row.createTime | formatDate }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status===0" type="text" size="small" style="color:#67C23A" @click="handleConfirm(scope.row,1)">确认</el-button>
          <el-button v-if="scope.row.status===0" type="text" size="small" style="color:#F56C6C" @click="handleConfirm(scope.row,2)">拒绝</el-button>
          <el-button v-if="scope.row.status===1" type="text" size="small" style="color:#409EFF" @click="$router.push({path:'/teacher/consultation/record',query:{bookingId:scope.row.bookingId,studentId:scope.row.studentId}})">填写记录</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card></div>
</template>
<script>
import * as api from '@/api/consultation'
export default {
  data() { return { tableData: [], teacherId: null } },
  created() { this.teacherId = this.$store.state.user.userId; api.getBookingList({ teacherId: this.teacherId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = r.data.records || []).catch(() => {}) },
  methods: {
    loadData() { api.getBookingList({ teacherId: this.teacherId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = r.data.records || []).catch(() => {}) },
    handleConfirm(row, status) {
      const originalStatus = row.status
      row.status = status
      api.confirmBooking(row).then(() => this.loadData()).catch(() => { row.status = originalStatus })
    }
  }
}
</script>
