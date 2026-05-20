<template>
  <div>
    <el-card>
      <div slot="header"><span>心理测评</span></div>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="assessmentId" label="ID" width="60"/><el-table-column prop="title" label="测评名称"/>
        <el-table-column prop="description" label="说明" show-overflow-tooltip/>
        <el-table-column prop="passScore" label="及格分" width="80"/>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="$router.push('/student/assessment/take/' + scope.row.assessmentId)">开始测评</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script>
import * as api from '@/api/assessment'
export default {
  data() { return { tableData: [] } },
  created() { api.getEnabledAssessments().then(r => this.tableData = r.data || []).catch(() => {}) }
}
</script>
