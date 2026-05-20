<template>
  <div>
    <el-card>
      <div slot="header">
        <span>学生管理</span>
        <el-button type="primary" size="small" style="float:right" @click="handleAdd">添加学生</el-button>
      </div>
      <el-form :inline="true" :model="query">
        <el-form-item><el-input v-model="query.keyword" placeholder="搜索学号/姓名"></el-input></el-form-item>
        <el-form-item><el-button type="primary" @click="loadData">搜索</el-button></el-form-item>
      </el-form>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="studentId" label="学号" width="80"/>
        <el-table-column prop="studentName" label="姓名"/>
        <el-table-column prop="gender" label="性别" width="60"/>
        <el-table-column prop="age" label="年龄" width="60"/>
        <el-table-column prop="phoneNum" label="手机号"/>
        <el-table-column prop="college" label="学院"/>
        <el-table-column prop="major" label="专业"/>
        <el-table-column prop="grade" label="年级" width="80"/>
        <el-table-column label="操作" width="180" align="center">
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="姓名"><el-input v-model="form.studentName" placeholder="请输入姓名"/></el-form-item>
        <el-form-item label="性别"><el-select v-model="form.gender" placeholder="请选择性别"><el-option label="男" value="男"/><el-option label="女" value="女"/></el-select></el-form-item>
        <el-form-item label="年龄"><el-input-number v-model="form.age" placeholder="请输入年龄"/></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phoneNum" placeholder="请输入手机号"/></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" placeholder="请输入邮箱"/></el-form-item>
        <el-form-item label="学院"><el-input v-model="form.college" placeholder="请输入学院"/></el-form-item>
        <el-form-item label="专业"><el-input v-model="form.major" placeholder="请输入专业"/></el-form-item>
        <el-form-item label="年级"><el-input v-model="form.grade" placeholder="请输入年级"/></el-form-item>
        <el-form-item label="班级"><el-input v-model="form.className" placeholder="请输入班级"/></el-form-item>
        <el-form-item label="辅导员"><el-select v-model="form.counselorId" placeholder="选择辅导员" clearable><el-option v-for="c in counselors" :key="c.counselorId" :label="c.counselorName + ' (' + c.department + ')'" :value="c.counselorId"/></el-select></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSave">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import * as userApi from '@/api/user'
export default {
  data() { return { query: { pageNum: 1, pageSize: 10 }, tableData: [], total: 0, counselors: [], dialogVisible: false, dialogTitle: '', form: {}, isEdit: false } },
  created() { this.loadData(); userApi.getCounselorList({ pageNum: 1, pageSize: 100 }).then(r => this.counselors = (r.data && r.data.records) || []).catch(() => {}) },
  methods: {
    loadData() { userApi.getStudentList(this.query).then(r => { this.tableData = (r.data && r.data.records) || []; this.total = (r.data && r.data.total) || 0 }).catch(() => {}) },
    handleAdd() { this.form = {}; this.dialogTitle = '添加学生'; this.isEdit = false; this.dialogVisible = true },
    handleEdit(row) { this.form = { ...row }; this.dialogTitle = '编辑学生'; this.isEdit = true; this.dialogVisible = true },
    handleSave() {
      if (this.isEdit) { userApi.updateStudent(this.form).then(() => { this.$message.success('更新成功'); this.dialogVisible = false; this.loadData() }).catch(() => this.$message.error('操作失败')) }
      else { userApi.saveStudent(this.form).then(() => { this.$message.success('添加成功'); this.dialogVisible = false; this.loadData() }).catch(() => this.$message.error('操作失败')) }
    },
    handleDelete(row) { this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => { userApi.deleteStudent(row.studentId).then(() => { this.$message.success('删除成功'); this.loadData() }).catch(() => this.$message.error('操作失败')) }).catch(() => {}) },
    handleResetPwd(row) { userApi.resetStudentPwd(row.studentId).then(() => this.$message.success(row.studentName + '的密码已重置为123456')).catch(() => this.$message.error('操作失败')) }
  }
}
</script>
