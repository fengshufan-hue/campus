<template>
  <div>
    <el-card>
      <div slot="header"><span>心理教师管理</span><el-button type="primary" size="small" style="float:right" @click="handleAdd">添加教师</el-button></div>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="teacherId" label="ID" width="60"/><el-table-column prop="teacherName" label="姓名"/>
        <el-table-column prop="gender" label="性别" width="60"/><el-table-column prop="title" label="职称"/>
        <el-table-column prop="phoneNum" label="手机号"/><el-table-column prop="specialty" label="专业方向"/>
        <el-table-column label="操作" width="140" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" style="color:#E6A23C" @click="handleResetPwd(scope.row)">重置密码</el-button>
            <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:15px;text-align:right" layout="total, prev, pager, next"
                     :total="total" :page-size="10" @current-change="p=>{query.pageNum=p;loadData()}"/>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="姓名"><el-input v-model="form.teacherName"/></el-form-item>
        <el-form-item label="性别"><el-select v-model="form.gender"><el-option label="男" value="男"/><el-option label="女" value="女"/></el-select></el-form-item>
        <el-form-item label="职称"><el-input v-model="form.title"/></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phoneNum"/></el-form-item>
        <el-form-item label="专业方向"><el-input v-model="form.specialty"/></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSave">确定</el-button></div>
    </el-dialog>
  </div>
</template>
<script>
import * as userApi from '@/api/user'
export default {
  data() { return { query: { pageNum: 1, pageSize: 10 }, tableData: [], total: 0, dialogVisible: false, dialogTitle: '', form: {}, isEdit: false } },
  created() { this.loadData() },
  methods: {
    loadData() { userApi.getTeacherList(this.query).then(r => { this.tableData = (r.data && r.data.records) || []; this.total = (r.data && r.data.total) || 0 }).catch(() => {}) },
    handleAdd() { this.form = {}; this.dialogTitle = '添加教师'; this.isEdit = false; this.dialogVisible = true },
    handleEdit(row) { this.form = { ...row }; this.dialogTitle = '编辑教师'; this.isEdit = true; this.dialogVisible = true },
    handleSave() {
      if (this.isEdit) { userApi.updateTeacher(this.form).then(() => { this.$message.success('更新成功'); this.dialogVisible = false; this.loadData() }).catch(() => this.$message.error('操作失败')) }
      else { userApi.saveTeacher(this.form).then(() => { this.$message.success('添加成功'); this.dialogVisible = false; this.loadData() }).catch(() => this.$message.error('操作失败')) }
    },
    handleDelete(row) { this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => { userApi.deleteTeacher(row.teacherId).then(() => this.loadData()).catch(() => this.$message.error('操作失败')) }).catch(() => {}) },
    handleResetPwd(row) { userApi.resetTeacherPwd(row.teacherId).then(() => this.$message.success(row.teacherName + '的密码已重置为123456')).catch(() => this.$message.error('操作失败')) }
  }
}
</script>
