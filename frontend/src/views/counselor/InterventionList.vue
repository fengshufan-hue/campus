<template>
  <div><el-card><div slot="header"><span>干预记录</span><el-button type="primary" size="small" style="float:right" @click="handleAdd">添加干预</el-button></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="studentId" label="学生ID"/><el-table-column prop="warningId" label="预警ID"/>
      <el-table-column prop="interventionType" label="类型"/><el-table-column prop="interventionContent" label="内容" show-overflow-tooltip/>
      <el-table-column prop="interventionDate" label="干预时间" width="160">
        <template slot-scope="scope">{{ scope.row.interventionDate | formatDate }}</template>
      </el-table-column>
    </el-table>
  </el-card>
  <el-dialog title="添加干预" :visible.sync="dialogVisible" width="600px">
    <el-form :model="form" label-width="100px">
      <el-form-item label="预警"><el-select v-model="form.warningId" placeholder="选择预警" clearable><el-option v-for="w in warnings" :key="w.warningId" :label="'预警#' + w.warningId + ' - ' + w.warningReason + ' (' + w.warningLevel + '级)'" :value="w.warningId"/></el-select></el-form-item>
      <el-form-item label="学生"><el-select v-model="form.studentId" placeholder="选择学生" clearable><el-option v-for="s in students" :key="s.studentId" :label="s.studentName + ' (' + s.college + ')'" :value="s.studentId"/></el-select></el-form-item>
      <el-form-item label="类型"><el-select v-model="form.interventionType"><el-option label="谈话" value="谈话"/><el-option label="家访" value="家访"/><el-option label="转介" value="转介"/><el-option label="其他" value="其他"/></el-select></el-form-item>
      <el-form-item label="内容"><el-input type="textarea" :rows="4" v-model="form.interventionContent"/></el-form-item>
      <el-form-item label="结果"><el-input v-model="form.interventionResult"/></el-form-item>
    </el-form>
    <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></div>
  </el-dialog>
</div></template>
<script>
import * as api from '@/api/warning'
import * as userApi from '@/api/user'
export default {
  data() { return { tableData: [], warnings: [], students: [], dialogVisible: false, form: { counselorId: null } } },
  created() { this.form.counselorId = this.$store.state.user.userId; this.loadStudents(); this.loadWarnings(); this.loadFromQuery(); this.loadData() },
  methods: {
    loadStudents() { userApi.getStudentList({ counselorId: this.form.counselorId, pageNum: 1, pageSize: 100 }).then(r => this.students = (r.data && r.data.records) || []).catch(() => {}) },
    loadWarnings() { api.getWarningList({ counselorId: this.form.counselorId, pageNum: 1, pageSize: 100 }).then(r => this.warnings = (r.data && r.data.records) || []).catch(() => {}) },
    loadFromQuery() { if (this.$route.query.warningId) { this.form.warningId = parseInt(this.$route.query.warningId); this.form.studentId = parseInt(this.$route.query.studentId) } },
    loadData() { api.getInterventionList({ counselorId: this.form.counselorId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = (r.data && r.data.records) || []).catch(() => {}) },
    handleAdd() {
      const now = new Date()
      const formatted = now.getFullYear() + '-' +
        String(now.getMonth() + 1).padStart(2, '0') + '-' +
        String(now.getDate()).padStart(2, '0') + ' ' +
        String(now.getHours()).padStart(2, '0') + ':' +
        String(now.getMinutes()).padStart(2, '0') + ':' +
        String(now.getSeconds()).padStart(2, '0')
      this.form = { counselorId: this.$store.state.user.userId, interventionDate: formatted }
      this.loadFromQuery()
      this.dialogVisible = true
    },
    handleSave() { api.saveIntervention(this.form).then(() => { this.$message.success('添加成功'); this.dialogVisible = false; this.loadData() }).catch(() => this.$message.error('操作失败')) }
  }
}
</script>
