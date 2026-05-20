<template>
  <div><el-card><div slot="header"><span>预警管理</span></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="studentId" label="学生ID"/><el-table-column prop="warningLevel" label="等级">
        <template slot-scope="scope"><el-tag :type="scope.row.warningLevel==='高'?'danger':scope.row.warningLevel==='中'?'warning':'info'">{{ scope.row.warningLevel }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="warningReason" label="原因" show-overflow-tooltip/>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">{{ ['待处理','处理中','已处理'][scope.row.status] || '未知' }}</template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160">
        <template slot-scope="scope">{{ scope.row.createTime | formatDate }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button v-if="!scope.row.counselorId" type="text" size="small" @click="handleAssign(scope.row)">接收</el-button>
          <el-button v-if="scope.row.counselorId && scope.row.status!==2" type="text" size="small" style="color:#67C23A" @click="handleResolve(scope.row)">标记已处理</el-button>
          <el-button type="text" size="small" @click="$router.push({path:'/counselor/intervention',query:{warningId:scope.row.warningId,studentId:scope.row.studentId}})">添加干预</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card></div>
</template>
<script>
import * as api from '@/api/warning'
export default {
  data() { return { tableData: [], counselorId: null } },
  created() { this.counselorId = this.$store.state.user.userId; this.loadData() },
  methods: {
    loadData() { api.getWarningList({ counselorId: this.counselorId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = (r.data && r.data.records) || []).catch(() => {}) },
    handleAssign(row) { api.assignWarning(row.warningId, this.counselorId).then(() => { this.$message.success('已接收'); this.loadData() }).catch(() => this.$message.error('操作失败')) },
    handleResolve(row) { api.updateWarning(row).then(() => { this.$message.success('已标记处理'); this.loadData() }).catch(() => this.$message.error('操作失败')) }
  }
}
</script>
