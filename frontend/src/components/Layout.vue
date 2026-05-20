<template>
  <div class="layout">
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <h2>校园心理健康服务平台</h2>
        </div>
        <div class="header-right">
          <span class="username">欢迎，{{ user.name }}</span>
          <el-button type="text" style="color: #fff;" @click="handleLogout">
            <i class="el-icon-switch-button"></i> 退出登录
          </el-button>
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link" style="color: #fff; cursor: pointer;">
              <i class="el-icon-setting"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="220px">
          <el-menu
            :default-active="$route.path"
            router
            background-color="#304156"
            text-color="#bfcbd9"
            active-text-color="#409EFF"
          >
            <template v-if="user.role === 'admin'">
              <el-menu-item index="/admin/dashboard"><i class="el-icon-s-home"></i>首页</el-menu-item>
              <el-menu-item index="/admin/student"><i class="el-icon-user"></i>学生管理</el-menu-item>
              <el-menu-item index="/admin/counselor"><i class="el-icon-user-solid"></i>辅导员管理</el-menu-item>
              <el-menu-item index="/admin/teacher"><i class="el-icon-s-custom"></i>心理教师管理</el-menu-item>
              <el-menu-item index="/admin/announcement"><i class="el-icon-bell"></i>公告管理</el-menu-item>
            </template>
            <template v-if="user.role === 'student'">
              <el-menu-item index="/student/dashboard"><i class="el-icon-s-home"></i>首页</el-menu-item>
              <el-menu-item index="/student/assessment"><i class="el-icon-document"></i>心理测评</el-menu-item>
              <el-menu-item index="/student/assessment/record"><i class="el-icon-notebook-2"></i>测评记录</el-menu-item>
              <el-menu-item index="/student/booking"><i class="el-icon-date"></i>我的预约</el-menu-item>
              <el-menu-item index="/student/booking/new"><i class="el-icon-plus"></i>预约咨询</el-menu-item>
              <el-menu-item index="/student/consultation/record"><i class="el-icon-chat-dot-round"></i>咨询记录</el-menu-item>
              <el-menu-item index="/student/article"><i class="el-icon-reading"></i>知识科普</el-menu-item>
              <el-menu-item index="/student/feedback"><i class="el-icon-edit-outline"></i>反馈评价</el-menu-item>
              <el-menu-item index="/student/announcement"><i class="el-icon-bell"></i>公告</el-menu-item>
            </template>
            <template v-if="user.role === 'counselor'">
              <el-menu-item index="/counselor/dashboard"><i class="el-icon-s-home"></i>首页</el-menu-item>
              <el-menu-item index="/counselor/warning"><i class="el-icon-warning"></i>预警管理</el-menu-item>
              <el-menu-item index="/counselor/intervention"><i class="el-icon-s-order"></i>干预记录</el-menu-item>
              <el-menu-item index="/counselor/student"><i class="el-icon-user"></i>学生列表</el-menu-item>
              <el-menu-item index="/counselor/assessment/record"><i class="el-icon-notebook-2"></i>测评结果</el-menu-item>
              <el-menu-item index="/counselor/announcement"><i class="el-icon-bell"></i>公告</el-menu-item>
            </template>
            <template v-if="user.role === 'teacher'">
              <el-menu-item index="/teacher/dashboard"><i class="el-icon-s-home"></i>首页</el-menu-item>
              <el-menu-item index="/teacher/assessment"><i class="el-icon-document"></i>测评管理</el-menu-item>
              <el-menu-item index="/teacher/schedule"><i class="el-icon-date"></i>排班管理</el-menu-item>
              <el-menu-item index="/teacher/booking"><i class="el-icon-s-order"></i>预约管理</el-menu-item>
              <el-menu-item index="/teacher/consultation/record"><i class="el-icon-chat-dot-round"></i>咨询记录</el-menu-item>
              <el-menu-item index="/teacher/article"><i class="el-icon-reading"></i>科普文章</el-menu-item>
              <el-menu-item index="/teacher/feedback"><i class="el-icon-edit-outline"></i>反馈评价</el-menu-item>
              <el-menu-item index="/teacher/announcement"><i class="el-icon-bell"></i>公告</el-menu-item>
            </template>
          </el-menu>
        </el-aside>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'Layout',
  computed: {
    user() {
      return this.$store.state.user || {}
    }
  },
  methods: {
    handleLogout() {
      this.$store.dispatch('logout')
      this.$router.push('/login')
      this.$message.success('已退出登录')
    },
    handleCommand(command) {
      if (command === 'profile') {
        this.$router.push('/' + this.user.role + '/profile')
      }
    }
  }
}
</script>

<style scoped>
.layout {
  height: 100vh;
}
.header {
  background-color: #545c64;
  color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header h2 {
  font-size: 18px;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
  color: #fff;
}
.el-aside {
  background-color: #304156;
  height: calc(100vh - 60px);
  overflow-y: auto;
}
.el-menu {
  border-right: none;
}
.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
