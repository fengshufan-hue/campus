<template>
  <div>
    <el-card>
      <div slot="header"><span>{{ assessment.title || '加载中...' }}</span></div>
      <div v-if="questions.length">
        <div v-for="q in questions" :key="q.questionId" style="margin-bottom:20px;padding:15px;border:1px solid #eee;border-radius:5px">
          <p style="font-weight:bold;margin-bottom:10px">{{ q.questionOrder }}. {{ q.questionContent }}</p>
          <el-radio-group v-model="answers[q.questionId]">
            <el-radio :label="'A'">A. {{ q.optionA }}</el-radio>
            <el-radio :label="'B'">B. {{ q.optionB }}</el-radio>
            <el-radio :label="'C'">C. {{ q.optionC }}</el-radio>
            <el-radio :label="'D'">D. {{ q.optionD }}</el-radio>
          </el-radio-group>
        </div>
        <el-button type="primary" @click="handleSubmit">提交</el-button>
      </div>
      <div v-else style="text-align:center;color:#999;padding:40px">加载中...</div>
    </el-card>
  </div>
</template>
<script>
import * as api from '@/api/assessment'
export default {
  data() { return { assessment: {}, questions: [], answers: {} } },
  created() { this.loadData() },
  methods: {
    loadData() {
      const id = this.$route.params.id
      api.getAssessment(id).then(r => this.assessment = r.data).catch(() => this.$message.error('加载失败'))
      api.getQuestions(id).then(r => this.questions = r.data || []).catch(() => {})
    },
    handleSubmit() {
      const uid = this.$store.state.user.userId
      const aid = this.$route.params.id
      let totalScore = 0
      const answersDetail = {}
      for (const q of this.questions) {
        const ans = this.answers[q.questionId]
        if (!ans) { this.$message.error('请完成所有题目'); return }
        let score = 0
        if (ans === 'A') score = q.scoreA || 1
        else if (ans === 'B') score = q.scoreB || 2
        else if (ans === 'C') score = q.scoreC || 3
        else if (ans === 'D') score = q.scoreD || 4
        totalScore += score
        answersDetail[q.questionId] = { answer: ans, score }
      }
      let resultLevel = '正常'
      if (totalScore >= 30) resultLevel = '重度'
      else if (totalScore >= 25) resultLevel = '中度'
      else if (totalScore >= 20) resultLevel = '轻度'

      api.submitAssessment({ assessmentId: aid, studentId: uid, totalScore, resultLevel, answers: answersDetail })
        .then(() => { this.$message.success('提交成功，得分：' + totalScore); this.$router.push('/student/assessment/record') })
        .catch(() => this.$message.error('提交失败'))
    }
  }
}
</script>
