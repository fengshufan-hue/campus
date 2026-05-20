<template>
  <div><el-card><div slot="header"><span>编辑测评题目 - {{ assessmentTitle }}</span><el-button type="primary" size="small" style="float:right" @click="handleAdd">添加题目</el-button></div>
    <el-table :data="questions" border stripe>
      <el-table-column prop="questionOrder" label="序号" width="60"/><el-table-column prop="questionContent" label="题目内容"/>
      <el-table-column label="选项"><template slot-scope="scope">A.{{ scope.row.optionA }} B.{{ scope.row.optionB }} C.{{ scope.row.optionC }} D.{{ scope.row.optionD }}</template></el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <el-dialog :title="editDialog ? '编辑题目' : '添加题目'" :visible.sync="dialogVisible" width="600px">
    <el-form :model="form" label-width="80px">
      <el-form-item label="序号"><el-input-number v-model="form.questionOrder" :min="1"/></el-form-item>
      <el-form-item label="题目"><el-input v-model="form.questionContent"/></el-form-item>
      <el-form-item label="选项A"><el-input v-model="form.optionA"/></el-form-item>
      <el-form-item label="选项B"><el-input v-model="form.optionB"/></el-form-item>
      <el-form-item label="选项C"><el-input v-model="form.optionC"/></el-form-item>
      <el-form-item label="选项D"><el-input v-model="form.optionD"/></el-form-item>
      <el-form-item label="分值A"><el-input-number v-model="form.scoreA" :min="1"/></el-form-item>
      <el-form-item label="分值B"><el-input-number v-model="form.scoreB" :min="1"/></el-form-item>
      <el-form-item label="分值C"><el-input-number v-model="form.scoreC" :min="1"/></el-form-item>
      <el-form-item label="分值D"><el-input-number v-model="form.scoreD" :min="1"/></el-form-item>
    </el-form>
    <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSave">保存</el-button></div>
  </el-dialog>
</div></template>
<script>
import * as api from '@/api/assessment'
export default {
  data() { return { assessmentTitle: '', questions: [], dialogVisible: false, form: {}, editDialog: false, editId: null } },
  created() {
    const id = this.$route.params.id
    api.getAssessment(id).then(r => this.assessmentTitle = (r.data && r.data.title) || '').catch(() => {})
    api.getQuestions(id).then(r => this.questions = (r.data && r.data.records) || []).catch(() => {})
  },
  methods: {
    handleAdd() { this.form = { assessmentId: parseInt(this.$route.params.id), questionOrder: this.questions.length + 1, scoreA: 1, scoreB: 2, scoreC: 3, scoreD: 4 }; this.editDialog = false; this.dialogVisible = true },
    handleEdit(row) { this.form = { ...row }; this.editDialog = true; this.editId = row.questionId; this.dialogVisible = true },
    handleSave() {
      if (this.editDialog) {
        api.updateQuestion(this.form).then(() => { this.dialogVisible = false; api.getQuestions(this.$route.params.id).then(r => this.questions = (r.data && r.data.records) || []).catch(() => {}) }).catch(() => this.$message.error('操作失败'))
      } else {
        api.saveQuestions([this.form]).then(() => { this.dialogVisible = false; api.getQuestions(this.$route.params.id).then(r => this.questions = (r.data && r.data.records) || []).catch(() => {}) }).catch(() => this.$message.error('操作失败'))
      }
    },
    handleDelete(row) { this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => { api.deleteQuestion(row.questionId).then(() => api.getQuestions(this.$route.params.id).then(r => this.questions = (r.data && r.data.records) || []).catch(() => {})).catch(() => this.$message.error('操作失败')) }).catch(() => {}) }
  }
}
</script>
