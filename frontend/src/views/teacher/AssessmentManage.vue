<template>
  <div><el-card><div slot="header"><span>测评管理</span><el-button type="primary" size="small" style="float:right" @click="handleAdd">创建测评</el-button></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="assessmentId" label="ID" width="60"/><el-table-column prop="title" label="名称"/>
      <el-table-column prop="status" label="状态" width="80"><template slot-scope="scope">{{ scope.row.status === 1 ? '启用' : '停用' }}</template></el-table-column>
      <el-table-column prop="passScore" label="及格分" width="80"/>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="$router.push('/teacher/assessment/edit/' + scope.row.assessmentId)">编辑题目</el-button>
          <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <el-dialog title="创建测评" :visible.sync="dialogVisible" width="500px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="名称"><el-input v-model="form.title"/></el-form-item>
      <el-form-item label="说明"><el-input type="textarea" :rows="3" v-model="form.description"/></el-form-item>
      <el-form-item label="及格分"><el-input-number v-model="form.passScore" :min="0" :max="100"/></el-form-item>
    </el-form>
    <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></div>
  </el-dialog>
</div></template>
<script>
import * as api from '@/api/assessment'
export default {
  data() { return { tableData: [], dialogVisible: false, form: { teacherId: null } } },
  created() {
    this.form.teacherId = this.$store.state.user.userId;
    this.loadData();
  },
  methods: {
    loadData() {
      api.getAssessmentList({ pageNum: 1, pageSize: 100 })
        .then(r => { this.tableData = r.data.records || []; })
        .catch(err => { this.$message.error('加载测评列表失败'); console.error(err); });
    },
    handleAdd() { this.form = { teacherId: this.$store.state.user.userId, status: 1 }; this.dialogVisible = true },
    handleSave() {
      api.saveAssessment(this.form)
        .then(() => { this.$message.success('创建成功'); this.dialogVisible = false; this.loadData(); })
        .catch(err => { this.$message.error('创建失败'); console.error(err); });
    },
    handleDelete(row) {
      this.$confirm('确认删除?', '提示', { type: 'warning' })
        .then(() => {
          api.deleteAssessment(row.assessmentId)
            .then(() => { this.$message.success('删除成功'); this.loadData(); })
            .catch(err => { this.$message.error('删除失败'); console.error(err); });
        })
        .catch(() => {});
    }
  }
}
</script>
