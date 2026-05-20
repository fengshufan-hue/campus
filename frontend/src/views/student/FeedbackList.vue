<template>
  <div><el-card><div slot="header"><span>反馈评价</span><el-button type="primary" size="small" style="float:right" @click="dialogVisible=true">新增评价</el-button></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="bookingId" label="预约ID"/><el-table-column prop="teacherId" label="教师ID"/>
      <el-table-column prop="rating" label="评分">
        <template slot-scope="scope"><span style="color:#f7ba2a">{{ '★'.repeat(scope.row.rating || 0) }}{{ '☆'.repeat(5-(scope.row.rating || 0)) }}</span></template>
      </el-table-column>
      <el-table-column prop="content" label="评价内容" show-overflow-tooltip/>
      <el-table-column prop="createTime" label="评价时间" width="160">
        <template slot-scope="scope">{{ scope.row.createTime | formatDate }}</template>
      </el-table-column>
    </el-table>
  </el-card>
  <el-dialog title="新增评价" :visible.sync="dialogVisible" width="500px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="预约"><el-select v-model="form.bookingId" placeholder="选择预约" clearable @change="onBookingChange"><el-option v-for="b in bookings" :key="b.bookingId" :label="'#' + b.bookingId + ' - ' + (b.reason || '无')" :value="b.bookingId"/></el-select></el-form-item>
      <el-form-item label="教师"><el-select v-model="form.teacherId" placeholder="选择教师" clearable><el-option v-for="t in teachers" :key="t.teacherId" :label="t.teacherName + ' (' + t.title + ')'" :value="t.teacherId"/></el-select></el-form-item>
      <el-form-item label="评分"><el-rate v-model="form.rating"/></el-form-item>
      <el-form-item label="内容"><el-input type="textarea" :rows="3" v-model="form.content"/></el-form-item>
      <el-form-item label="匿名"><el-switch v-model="form.isAnonymous" :active-value="1" :inactive-value="0"/></el-form-item>
    </el-form>
    <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSave">提交</el-button></div>
  </el-dialog>
</div></template>
<script>
import * as api from '@/api/feedback'
import * as bookingApi from '@/api/consultation'
import * as userApi from '@/api/user'
export default {
  data() { return { tableData: [], bookings: [], teachers: [], dialogVisible: false, form: { studentId: null, rating: 5, isAnonymous: 0 } } },
  created() { this.form.studentId = this.$store.state.user.userId; this.loadBookings(); this.loadTeachers(); api.getMyFeedback({ studentId: this.form.studentId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = r.data.records || []).catch(() => {}) },
  methods: {
    loadBookings() { bookingApi.getBookingList({ studentId: this.form.studentId, pageNum: 1, pageSize: 100 }).then(r => this.bookings = r.data.records || []).catch(() => {}) },
    loadTeachers() { userApi.getTeacherList({ pageNum: 1, pageSize: 100 }).then(r => this.teachers = r.data.records || []).catch(() => {}) },
    onBookingChange(val) {
      if (val) {
        const b = this.bookings.find(x => x.bookingId === val)
        if (b && b.teacherId) {
          this.form.teacherId = b.teacherId
        }
      }
    },
    handleSave() { api.saveFeedback(this.form).then(() => { this.$message.success('评价成功'); this.dialogVisible = false; api.getMyFeedback({ studentId: this.form.studentId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = r.data.records || []).catch(() => {}) }).catch(() => this.$message.error('操作失败')) } }
}
</script>
