<template>
  <div><el-card><div slot="header"><span>咨询记录</span></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="consultTopic" label="咨询主题"/><el-table-column prop="consultDate" label="咨询时间" width="160">
        <template slot-scope="scope">{{ scope.row.consultDate | formatDate }}</template>
      </el-table-column>
      <el-table-column prop="consultSummary" label="总结" show-overflow-tooltip/>
    </el-table>
  </el-card></div>
</template>
<script>
import * as api from '@/api/feedback'
export default {
  data() { return { tableData: [] } },
  created() { api.getConsultationRecords({ studentId: this.$store.state.user.userId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = (r.data && r.data.records) || []).catch(() => {}) }
}
</script>
