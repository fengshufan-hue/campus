<template>
  <div><el-card><div slot="header"><span>科普文章</span><el-button type="primary" size="small" style="float:right" @click="$router.push('/teacher/article/edit')">发布文章</el-button></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="articleId" label="ID" width="60"/><el-table-column prop="title" label="标题"/>
      <el-table-column prop="category" label="分类" width="120"/><el-table-column prop="viewCount" label="浏览" width="80"/>
      <el-table-column prop="status" label="状态" width="80"><template slot-scope="scope">{{ scope.row.status === 1 ? '已发布' : '草稿' }}</template></el-table-column>
      <el-table-column label="操作" width="180" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="$router.push('/teacher/article/edit?id=' + scope.row.articleId)">编辑</el-button>
          <el-button v-if="scope.row.status===0" type="text" size="small" style="color:#67C23A" @click="handlePublish(scope.row)">发布</el-button>
          <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card></div>
</template>
<script>
import * as api from '@/api/article'
export default {
  data() { return { tableData: [] } },
  created() { api.getArticleList({ pageNum: 1, pageSize: 100 }).then(r => this.tableData = (r.data && r.data.records) || []).catch(() => {}) },
  methods: {
    handlePublish(row) { api.publishArticle(row.articleId).then(() => { this.$message.success('发布成功'); this.loadData() }).catch(() => this.$message.error('操作失败')) },
    loadData() { api.getArticleList({ pageNum: 1, pageSize: 100 }).then(r => this.tableData = (r.data && r.data.records) || []).catch(() => {}) },
    handleDelete(row) { this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => { api.deleteArticle(row.articleId).then(() => this.loadData()).catch(() => this.$message.error('操作失败')) }).catch(() => {}) }
  }
}
</script>
