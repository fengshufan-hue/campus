<template>
  <div><el-card><div slot="header"><span>公告列表</span></div>
    <div v-for="a in tableData" :key="a.announcementId" style="padding:15px 0;border-bottom:1px solid #eee">
      <h3>{{ a.title }}</h3>
      <div style="color:#999;font-size:12px;margin:5px 0">{{ a.author }} | {{ a.publishTime | formatDate }}</div>
      <div style="margin-top:10px">{{ a.content }}</div>
    </div>
  </el-card></div>
</template>
<script>
import * as api from '@/api/announcement'
export default {
  data() { return { tableData: [] } },
  created() { api.getPublishedAnnouncements({ pageNum: 1, pageSize: 100 }).then(r => this.tableData = (r.data && r.data.records) || []).catch(() => {}) }
}
</script>
