<template>
  <div>
    <el-card>
      <div slot="header"><span>公告管理</span><el-button type="primary" size="small" style="float:right" @click="handleAdd">发布公告</el-button></div>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="announcementId" label="ID" width="60"/><el-table-column prop="title" label="标题"/>
        <el-table-column prop="type" label="类型" width="80"/><el-table-column prop="author" label="发布者" width="100"/>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">{{ scope.row.status === 1 ? '已发布' : '草稿' }}</template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" width="160">
          <template slot-scope="scope">{{ scope.row.publishTime | formatDate }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.status===0" type="text" size="small" style="color:#67C23A" @click="handlePublish(scope.row)">发布</el-button>
            <el-button type="text" size="small" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top:15px;text-align:right" layout="total, prev, pager, next"
                     :total="total" :page-size="10" @current-change="p=>{query.pageNum=p;loadData()}"/>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title"/></el-form-item>
        <el-form-item label="类型"><el-select v-model="form.type"><el-option label="通知" value="通知"/><el-option label="活动" value="活动"/><el-option label="其他" value="其他"/></el-select></el-form-item>
        <el-form-item label="内容"><el-input type="textarea" :rows="8" v-model="form.content"/></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="handleSave">确定</el-button></div>
    </el-dialog>
  </div>
</template>
<script>
import * as api from '@/api/announcement'
export default {
  data() { return { query: { pageNum: 1, pageSize: 10 }, tableData: [], total: 0, dialogVisible: false, dialogTitle: '', form: {}, isEdit: false } },
  created() { this.loadData() },
  methods: {
    loadData() { api.getAnnouncementList(this.query).then(r => { this.tableData = (r.data && r.data.records) || []; this.total = (r.data && r.data.total) || 0 }).catch(() => {}) },
    handleAdd() { this.form = { author: this.$store.state.user.name, status: 0 }; this.dialogTitle = '发布公告'; this.isEdit = false; this.dialogVisible = true },
    handleEdit(row) { this.form = { ...row }; this.dialogTitle = '编辑公告'; this.isEdit = true; this.dialogVisible = true },
    async handleSave() {
      try {
        if (this.isEdit) {
          await api.updateAnnouncement(this.form);
        } else {
          await api.saveAnnouncement(this.form);
        }
        this.$message.success(this.isEdit ? '更新成功' : '添加成功');
        this.dialogVisible = false;
        await this.loadData();
      } catch (e) {
        this.$message.error('操作失败')
      }
    },
    handlePublish(row) { api.publishAnnouncement(row.announcementId).then(() => { this.$message.success('发布成功'); this.loadData() }).catch(() => this.$message.error('操作失败')) },
    handleDelete(row) { this.$confirm('确认删除?', '提示', { type: 'warning' }).then(() => { api.deleteAnnouncement(row.announcementId).then(() => this.loadData()).catch(() => this.$message.error('操作失败')) }).catch(() => {}) }
  }
}
</script>
