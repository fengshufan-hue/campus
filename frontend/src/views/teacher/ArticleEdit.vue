<template>
  <div><el-card><div slot="header"><span>{{ isEdit ? '编辑文章' : '发布文章' }}</span></div>
    <el-form :model="form" label-width="80px" style="max-width:800px">
      <el-form-item label="标题"><el-input v-model="form.title" placeholder="请输入标题"/></el-form-item>
      <el-form-item label="分类"><el-select v-model="form.category"><el-option label="压力管理" value="压力管理"/><el-option label="情绪调节" value="情绪调节"/><el-option label="人际关系" value="人际关系"/><el-option label="自我认知" value="自我认知"/><el-option label="其他" value="其他"/></el-select></el-form-item>
      <el-form-item label="内容"><el-input type="textarea" :rows="15" v-model="form.content"/></el-form-item>
      <el-form-item><el-button type="primary" @click="handleSave">保存</el-button><el-button @click="$router.push('/teacher/article')">返回</el-button></el-form-item>
    </el-form>
  </el-card></div>
</template>
<script>
import * as api from '@/api/article'
export default {
  data() { return { form: { authorId: null, authorName: '', status: 0 }, isEdit: false } },
  created() {
    const user = this.$store.state.user
    this.form.authorId = user.userId
    this.form.authorName = user.name
    if (this.$route.query.id) {
      this.isEdit = true
      api.getArticle(this.$route.query.id).then(r => { this.form = { ...r.data }; this.form.id = (r.data && r.data.articleId) }).catch(() => this.$message.error('加载失败'))
    }
  },
  methods: {
    handleSave() {
      if (this.isEdit) {
        this.form.articleId = this.form.id
        api.updateArticle(this.form).then(() => { this.$message.success('更新成功'); this.$router.push('/teacher/article') }).catch(() => this.$message.error('操作失败'))
      } else {
        api.saveArticle(this.form).then(() => { this.$message.success('保存成功'); this.$router.push('/teacher/article') }).catch(() => this.$message.error('操作失败'))
      }
    }
  }
}
</script>
