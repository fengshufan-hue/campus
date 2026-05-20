<template>
  <div><el-card><div slot="header"><span>测评结果</span></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="studentName" label="学生"/><el-table-column prop="assessmentName" label="测评"/>
      <el-table-column prop="totalScore" label="得分"/><el-table-column prop="resultLevel" label="结果">
        <template slot-scope="scope"><el-tag :type="scope.row.resultLevel==='正常'?'success':'danger'">{{ scope.row.resultLevel }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="submitTime" label="提交时间" width="160">
        <template slot-scope="scope">{{ scope.row.submitTime | formatDate }}</template>
      </el-table-column>
    </el-table>
  </el-card></div>
</template>
<script>
import * as api from '@/api/assessment'
export default {
  data() { return { tableData: [] } },
  created() { api.getAssessmentRecords({ counselorId: this.$store.state.user.userId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = (r.data && r.data.records) || []).catch(() => {}) }
}
</script>
