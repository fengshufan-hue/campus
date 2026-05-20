<template>
  <div><el-card><div slot="header"><span>咨询记录</span><el-button type="primary" size="small" style="float:right" @click="handleAdd">添加记录</el-button></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="studentName" label="学生"/><el-table-column prop="consultTopic" label="主题"/>
      <el-table-column prop="consultDate" label="咨询时间" width="160">
        <template slot-scope="scope">{{ scope.row.consultDate | formatDate }}</template>
      </el-table-column>
      <el-table-column prop="consultSummary" label="总结" show-overflow-tooltip/>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope"><el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button><el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button></template>
      </el-table-column>
    </el-table>
  </el-card>
  <el-dialog :title="editTitle" :visible.sync="dialogVisible" width="600px">
    <el-form :model="form" label-width="100px">
      <el-form-item label="学生"><el-select v-model="form.studentId" placeholder="选择学生" style="width:100%"><el-option v-for="s in students" :key="s.studentId" :label="s.studentName + ' (' + s.college + ')'" :value="s.studentId"/></el-select></el-form-item>
      <el-form-item label="预约"><el-select v-model="form.bookingId" placeholder="选择预约" style="width:100%" :disabled="!form.studentId"><el-option v-for="b in bookings" :key="b.bookingId" :label="'#' + b.bookingId + ' - ' + (b.reason || '无') + ' (' + b.statusText + ')'" :value="b.bookingId"/></el-select></el-form-item>
      <el-form-item label="咨询时间"><el-date-picker v-model="form.consultDate" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%"/></el-form-item>
      <el-form-item label="主题"><el-input v-model="form.consultTopic"/></el-form-item>
      <el-form-item label="内容"><el-input type="textarea" :rows="4" v-model="form.consultContent"/></el-form-item>
      <el-form-item label="总结"><el-input type="textarea" :rows="3" v-model="form.consultSummary"/></el-form-item>
      <el-form-item label="后续建议"><el-input type="textarea" :rows="2" v-model="form.followUp"/></el-form-item>
    </el-form>
    <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></div>
  </el-dialog>
</div></template>
<script>
import * as api from '@/api/feedback'
import * as userApi from '@/api/user'
import * as bookingApi from '@/api/consultation'
export default {
  data() { return { tableData: [], students: [], bookings: [], dialogVisible: false, form: { teacherId: null }, editTitle: '', isEdit: false } },
  created() { this.form.teacherId = this.$store.state.user.userId; this.loadStudents(); this.loadFromQuery(); this.loadData() },
  watch: {
    'form.studentId'() { this.loadBookings() }
  },
  methods: {
    loadStudents() { userApi.getStudentList({ pageNum: 1, pageSize: 100 }).then(r => this.students = (r.data && r.data.records) || []).catch(() => {}) },
    loadBookings() { if (!this.form.studentId) { this.bookings = []; return } bookingApi.getBookingList({ studentId: this.form.studentId, pageNum: 1, pageSize: 100 }).then(r => this.bookings = (r.data && r.data.records || []).map(b => ({ ...b, statusText: ['待确认','已确认','已完成','已取消'][b.status] || '未知' }))).catch(() => {}) },
    loadFromQuery() { if (this.$route.query.bookingId) { this.form.bookingId = parseInt(this.$route.query.bookingId); this.form.studentId = parseInt(this.$route.query.studentId) } },
    loadData() { api.getConsultationRecords({ teacherId: this.form.teacherId, pageNum: 1, pageSize: 100 }).then(r => {
      const records = (r.data && r.data.records) || []
      this.tableData = records.map(row => {
        const s = this.students.find(s => s.studentId === row.studentId)
        return { ...row, studentName: s ? s.studentName : '未知' }
      })
    }).catch(() => {}) },
    handleAdd() { this.form = { teacherId: this.$store.state.user.userId }; this.bookings = []; this.loadFromQuery(); this.editTitle = '添加记录'; this.isEdit = false; this.dialogVisible = true },
    handleEdit(row) { this.form = { ...row }; this.editTitle = '编辑记录'; this.isEdit = true; this.dialogVisible = true },
    handleSave() {
      const req = this.isEdit ? api.updateConsultationRecord(this.form) : api.saveConsultationRecord(this.form)
      req.then(() => {
        this.dialogVisible = false
        this.loadData()
      }).catch(() => this.$message.error('操作失败'))
    },
    handleDelete(row) { this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => { api.deleteConsultationRecord(row.recordId).then(() => this.loadData()).catch(() => this.$message.error('操作失败')) }).catch(() => {}) }
  }
}
</script>
