<template>
  <div><el-card v-if="article && article.title"><div slot="header"><span>{{ article.title }}</span></div>
    <div style="color:#999;margin-bottom:15px">作者：{{ article.authorName || '-' }} | 浏览：{{ article.viewCount || 0 }}</div>
    <div style="line-height:1.8" v-html="sanitizedContent"></div>
  </el-card></div>
</template>
<script>
import * as api from '@/api/article'
export default {
  data() { return { article: null } },
  computed: {
    sanitizedContent() {
      if (!this.article || !this.article.content) return ''
      // Basic XSS protection - remove script tags and dangerous attributes
      return this.article.content
        .replace(/<script[^>]*>.*?<\/script>/gi, '')
        .replace(/on\w+\s*=/gi, '')
        .replace(/javascript:/gi, '')
    }
  },
  created() {
    const id = this.$route.params.id
    api.getArticle(id).then(r => { this.article = r.data || {} }).catch(() => this.$message.error('加载文章失败'))
    api.incrementView(id).catch(() => {})
  }
}
</script>
