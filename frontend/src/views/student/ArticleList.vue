<template>
  <div><el-card><div slot="header"><span>知识科普</span></div>
    <el-table :data="tableData" border stripe>
      <el-table-column prop="title" label="标题"/><el-table-column prop="category" label="分类" width="120"/>
      <el-table-column prop="authorName" label="作者" width="100"/><el-table-column prop="viewCount" label="浏览" width="80"/>
      <el-table-column label="操作" width="100" align="center"><template slot-scope="scope"><el-button type="text" size="small" @click="$router.push('/student/article/' + scope.row.articleId)">查看</el-button></template></el-table-column>
    </el-table>
  </el-card></div>
</template>
<script>
import * as api from '@/api/article'
export default {
  data() { return { tableData: [] } },
  created() { api.getPublishedArticles({ pageNum: 1, pageSize: 100 }).then(r => this.tableData = (r.data && r.data.records) || []).catch(() => {}) }
}
</script>
