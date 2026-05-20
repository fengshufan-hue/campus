<template>
  <div class="login-container">
    <div class="login-box">
      <h2>校园心理健康服务平台</h2>

      <el-select v-model="form.role" placeholder="选择身份" style="width:100%;margin-bottom:15px">
        <el-option label="学生" value="student"/>
        <el-option label="辅导员" value="counselor"/>
        <el-option label="心理教师" value="teacher"/>
        <el-option label="管理员" value="admin"/>
      </el-select>
      <el-input v-model="form.username" placeholder="请输入账号" style="margin-bottom:15px" prefix-icon="el-icon-user"></el-input>
      <el-input v-model="form.password" placeholder="请输入密码" type="password" show-password style="margin-bottom:15px" prefix-icon="el-icon-lock"></el-input>

      <el-button type="primary" style="width:100%" :loading="loading" @click="handleLogin">登 录</el-button>

      <div class="divider">快捷登录</div>

      <div class="quick-section">
        <div class="section-label"><span class="badge badge-admin">管理员</span></div>
        <el-button size="mini" class="quick-btn" type="danger" plain @click="quickLogin('admin','1','123456')">系统管理员</el-button>
      </div>

      <div class="quick-section">
        <div class="section-label"><span class="badge badge-counselor">辅导员</span></div>
        <el-button size="mini" class="quick-btn" type="warning" plain @click="quickLogin('counselor','1','123456')">张老师</el-button>
        <el-button size="mini" class="quick-btn" type="warning" plain @click="quickLogin('counselor','2','123456')">李老师</el-button>
      </div>

      <div class="quick-section">
        <div class="section-label"><span class="badge badge-teacher">心理教师</span></div>
        <el-button size="mini" class="quick-btn" type="primary" plain @click="quickLogin('teacher','1','123456')">王教授</el-button>
        <el-button size="mini" class="quick-btn" type="primary" plain @click="quickLogin('teacher','2','123456')">赵老师</el-button>
      </div>

      <div class="quick-section">
        <div class="section-label"><span class="badge badge-student">学生</span></div>
        <el-button size="mini" class="quick-btn" type="success" plain @click="quickLogin('student','1','123456')">小明</el-button>
        <el-button size="mini" class="quick-btn" type="success" plain @click="quickLogin('student','2','123456')">小红</el-button>
        <el-button size="mini" class="quick-btn" type="success" plain @click="quickLogin('student','3','123456')">小刚</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/login'

export default {
  name: 'Login',
  data() {
    return {
      form: { role: 'student', username: '', password: '' },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.loading = true
      login(this.form).then(res => {
        const user = { role: res.data.role, userId: res.data.userId, name: res.data.name, token: res.data.token }
        this.$store.dispatch('login', user)
        const map = { student: '/student', counselor: '/counselor', teacher: '/teacher', admin: '/admin' }
        this.$router.push(map[res.data.role] + '/dashboard')
      }).catch(err => {
        console.error('Login failed:', err)
      }).finally(() => { this.loading = false })
    },
    quickLogin(role, username, password) {
      this.form = { role, username, password }
      this.handleLogin()
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.login-container::before,
.login-container::after {
  content: '';
  position: absolute;
  border-radius: 50%;
  opacity: 0.08;
  background: #fff;
}
.login-container::before {
  width: 400px;
  height: 400px;
  top: -120px;
  right: -120px;
}
.login-container::after {
  width: 300px;
  height: 300px;
  bottom: -80px;
  left: -80px;
}

.login-box {
  width: 420px;
  max-height: 90vh;
  padding: 36px 32px 28px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  position: relative;
  z-index: 1;
  overflow-y: auto;
}

.login-box::-webkit-scrollbar {
  width: 4px;
}
.login-box::-webkit-scrollbar-thumb {
  background: #dcdfe6;
  border-radius: 2px;
}

.login-box h2 {
  text-align: center;
  color: #303133;
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 24px;
  letter-spacing: 1px;
}

.login-box .el-input,
.login-box .el-select {
  width: 100%;
}

.login-box .el-input >>> .el-input__inner {
  border-radius: 8px;
  height: 40px;
}

.login-box .el-select .el-input__inner {
  border-radius: 8px;
  height: 40px;
}

.login-box .el-button--primary {
  height: 40px;
  font-size: 15px;
  border-radius: 8px;
  letter-spacing: 2px;
}

.divider {
  text-align: center;
  color: #909399;
  font-size: 12px;
  margin: 20px 0 14px;
  position: relative;
}
.divider::before,
.divider::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 50px;
  height: 1px;
  background: #ebeef5;
}
.divider::before { left: 20px; }
.divider::after  { right: 20px; }

.quick-section {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.section-label {
  width: 56px;
  flex-shrink: 0;
}

.badge {
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
  color: #fff;
  white-space: nowrap;
}
.badge-admin    { background: #F56C6C; }
.badge-counselor { background: #E6A23C; }
.badge-teacher  { background: #409EFF; }
.badge-student  { background: #67C23A; }

.quick-btn {
  font-size: 12px !important;
  padding: 6px 0 !important;
  flex: 1;
  border-radius: 6px !important;
  transition: all 0.2s !important;
}
</style>
