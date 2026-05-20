<template>
  <div><el-card><div slot="header"><span>反馈评价</span></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="studentId" label="学生ID"/><el-table-column prop="bookingId" label="预约ID"/>
      <el-table-column prop="rating" label="评分">
        <template slot-scope="scope"><span style="color:#f7ba2a">{{ '★'.repeat(scope.row.rating || 0) }}{{ '☆'.repeat(5-(scope.row.rating || 0)) }}</span></template>
      </el-table-column>
      <el-table-column prop="content" label="评价内容" show-overflow-tooltip/>
      <el-table-column prop="isAnonymous" label="匿名"><template slot-scope="scope">{{ scope.row.isAnonymous === 1 ? '是' : '否' }}</template></el-table-column>
      <el-table-column prop="createTime" label="时间" width="160">
        <template slot-scope="scope">{{ scope.row.createTime | formatDate }}</template>
      </el-table-column>
    </el-table>
  </el-card></div>
</template>
<script>
import * as api from '@/api/feedback'
export default {
  data() { return { tableData: [] } },
  created() { api.getFeedbackList({ teacherId: this.$store.state.user.userId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = (r.data && r.data.records) || []).catch(() => {}) }
}
</script>
