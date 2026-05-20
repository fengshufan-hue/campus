<template>
  <div><el-card><div slot="header"><span>学生列表</span></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="studentId" label="学号"/><el-table-column prop="studentName" label="姓名"/>
      <el-table-column prop="gender" label="性别" width="60"/><el-table-column prop="major" label="专业"/>
      <el-table-column prop="className" label="班级"/><el-table-column prop="phoneNum" label="手机号"/>
    </el-table>
  </el-card></div>
</template>
<script>
import * as userApi from '@/api/user'
export default {
  data() { return { tableData: [] } },
  created() { const cid = this.$store.state.user.userId; userApi.getStudentList({ counselorId: cid, pageNum: 1, pageSize: 100 }).then(r => this.tableData = (r.data && r.data.records) || []).catch(() => {}) }
}
</script>
