<template>
  <div><el-card><div slot="header"><span>排班管理</span><el-button type="primary" size="small" style="float:right" @click="handleAdd">添加排班</el-button></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="scheduleDate" label="日期">
        <template slot-scope="scope">{{ scope.row.scheduleDate | formatDate }}</template>
      </el-table-column><el-table-column prop="startTime" label="开始时间"/>
      <el-table-column prop="endTime" label="结束时间"/><el-table-column prop="location" label="地点"/>
      <el-table-column prop="bookedCount" label="已约"/><el-table-column prop="maxCount" label="上限"/>
      <el-table-column prop="status" label="状态"><template slot-scope="scope">{{ ['停用','开放','已满'][scope.row.status] || '未知' }}</template></el-table-column>
      <el-table-column label="操作" width="140" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <el-dialog :title="editTitle" :visible.sync="dialogVisible" width="500px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="日期"><el-date-picker v-model="form.scheduleDate" type="date" value-format="yyyy-MM-dd" style="width:100%"/></el-form-item>
      <el-form-item label="开始"><el-time-picker v-model="form.startTime" value-format="HH:mm:ss" style="width:100%"/></el-form-item>
      <el-form-item label="结束"><el-time-picker v-model="form.endTime" value-format="HH:mm:ss" style="width:100%"/></el-form-item>
      <el-form-item label="地点"><el-input v-model="form.location"/></el-form-item>
      <el-form-item label="上限"><el-input-number v-model="form.maxCount" :min="1"/></el-form-item>
      <el-form-item label="状态"><el-select v-model="form.status"><el-option label="开放" :value="1"/><el-option label="停用" :value="0"/></el-select></el-form-item>
    </el-form>
    <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></div>
  </el-dialog>
</div></template>
<script>
import * as api from '@/api/schedule'
export default {
  data() { return { tableData: [], dialogVisible: false, form: { teacherId: null }, editTitle: '', isEdit: false } },
  created() { this.form.teacherId = this.$store.state.user.userId; this.loadData() },
  methods: {
    loadData() { api.getScheduleList({ teacherId: this.form.teacherId, pageNum: 1, pageSize: 100 }).then(r => this.tableData = (r.data && r.data.records) || []).catch(() => {}) },
    handleAdd() { this.form = { teacherId: this.$store.state.user.userId, maxCount: 1, status: 1 }; this.editTitle = '添加排班'; this.isEdit = false; this.dialogVisible = true },
    handleEdit(row) { this.form = { ...row }; this.editTitle = '编辑排班'; this.isEdit = true; this.dialogVisible = true },
    handleSave() {
      const req = this.isEdit ? api.updateSchedule(this.form) : api.saveSchedule(this.form)
      req.then(() => {
        this.dialogVisible = false
        this.loadData()
      }).catch(() => this.$message.error('操作失败'))
    },
    handleDelete(row) { this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => { api.deleteSchedule(row.scheduleId).then(() => this.loadData()).catch(() => this.$message.error('操作失败')) }).catch(() => {}) }
  }
}
</script>
