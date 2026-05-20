<template>
  <div class="profile-page">
    <el-card>
      <div slot="header"><span>个人信息</span></div>
      <el-form :model="form" label-width="100px" style="max-width:600px">
        <el-form-item label="姓名">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="form.age" :min="1" :max="100"></el-input-number>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phoneNum"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top:20px">
      <div slot="header"><span>修改密码</span></div>
      <el-form :model="pwdForm" label-width="100px" style="max-width:600px">
        <el-form-item label="旧密码">
          <el-input v-model="pwdForm.oldPwd" type="password"></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPwd" type="password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handlePwdChange">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import * as userApi from '@/api/user'

export default {
  name: 'Profile',
  data() {
    return {
      form: { name: '', gender: '', age: null, phoneNum: '', email: '' },
      pwdForm: { oldPwd: '', newPwd: '' },
      userIdField: ''
    }
  },
  created() {
    const user = this.$store.state.user
    if (user && user.name) this.form.name = user.name
    this.loadProfile()
  },
  methods: {
    loadProfile() {
      const user = this.$store.state.user
      if (!user) return
      const role = user.role
      if (role === 'student') {
        userApi.getStudent(user.userId).then(res => { this.form = { ...this.form, ...(res.data || {}) }; this.userIdField = 'studentId' }).catch(() => this.$message.error('加载个人信息失败'))
      } else if (role === 'counselor') {
        userApi.getCounselor(user.userId).then(res => { this.form = { ...this.form, ...(res.data || {}) }; this.userIdField = 'counselorId' }).catch(() => this.$message.error('加载个人信息失败'))
      } else if (role === 'teacher') {
        userApi.getTeacher(user.userId).then(res => { this.form = { ...this.form, ...(res.data || {}) }; this.userIdField = 'teacherId' }).catch(() => this.$message.error('加载个人信息失败'))
      }
    },
    handleSave() {
      const user = this.$store.state.user
      if (!user) { this.$message.error('请先登录'); return }
      const data = { ...this.form }
      if (user.role === 'student') {
        data.studentId = user.userId
        userApi.updateStudent(data).then(() => {
          user.name = this.form.name
          localStorage.setItem('user', JSON.stringify(user))
          this.$store.commit('SET_USER', user)
          this.$message.success('保存成功')
        }).catch(() => this.$message.error('保存失败'))
      } else if (user.role === 'counselor') {
        data.counselorId = user.userId
        userApi.updateCounselor(data).then(() => {
          user.name = this.form.name
          localStorage.setItem('user', JSON.stringify(user))
          this.$store.commit('SET_USER', user)
          this.$message.success('保存成功')
        }).catch(() => this.$message.error('保存失败'))
      } else if (user.role === 'teacher') {
        data.teacherId = user.userId
        userApi.updateTeacher(data).then(() => {
          user.name = this.form.name
          localStorage.setItem('user', JSON.stringify(user))
          this.$store.commit('SET_USER', user)
          this.$message.success('保存成功')
        }).catch(() => this.$message.error('保存失败'))
      }
    },
    handlePwdChange() {
      const { oldPwd, newPwd } = this.pwdForm
      if (!oldPwd) { this.$message.warning('请输入旧密码'); return }
      if (!newPwd) { this.$message.warning('请输入新密码'); return }
      if (newPwd.length < 6) { this.$message.warning('新密码至少6位'); return }
      if (oldPwd === newPwd) { this.$message.warning('新旧密码不能相同'); return }
      const user = this.$store.state.user
      if (!user) { this.$message.error('请先登录'); return }
      const roleMap = { student: 'student', counselor: 'counselor', teacher: 'teacher', admin: 'admin' }
      const role = roleMap[user.role]
      if (!role) { this.$message.error('未知用户类型'); return }
      userApi.changePassword(role, { userId: user.userId, oldPwd, newPwd }).then(() => {
        this.$message.success('密码修改成功')
        this.pwdForm = { oldPwd: '', newPwd: '' }
      }).catch(() => this.$message.error('密码修改失败'))
    }
  }
}
</script>
