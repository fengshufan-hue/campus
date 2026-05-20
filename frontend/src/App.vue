<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  font-family: 'Microsoft YaHei', Arial, sans-serif;
  background-color: #f0f2f5;
}

/* Empty table styling */
.el-table__empty-text {
  display: block;
  text-align: center;
  color: #909399;
  font-size: 14px;
  line-height: 1.6;
}
.el-table__empty-text::before {
  content: '\e6c9';
  font-family: 'element-icons';
  display: block;
  font-size: 40px;
  color: #c0c4cc;
  margin-bottom: 8px;
}
</style>
