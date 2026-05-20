# 校园心理健康服务平台 - 开发文档

## 项目概述

校园心理健康服务平台是一个面向高校的心理健康服务管理系统，支持学生、辅导员、心理教师和管理员四种角色，提供心理测评、预约咨询、预警管理、知识科普等功能。

---

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.14
- **JDK**: Java 8
- **数据库**: MySQL 8.0
- **ORM**: MyBatis-Plus 3.5.3.1
- **安全**: Spring Security (BCrypt加密) + JWT (jjwt 0.11.5)
- **构建工具**: Maven

### 前端
- **框架**: Vue 2.6.14
- **UI组件库**: Element UI 2.15.13
- **状态管理**: Vuex 3.6.2
- **路由**: Vue Router 3.5.4
- **HTTP客户端**: Axios 0.27.2
- **构建工具**: Vue CLI 5.0

---

## 项目结构

```
campus-mental-health/
├── backend/                          # 后端项目
│   ├── pom.xml                       # Maven配置
│   └── src/main/
│       ├── java/com/campus/mentalhealth/
│       │   ├── MentalHealthApplication.java    # 启动类
│       │   ├── common/               # 公共工具类
│       │   │   ├── JwtUtil.java      # JWT工具
│       │   │   ├── Result.java       # 统一响应封装
│       │   │   └── PageRequest.java  # 分页请求封装
│       │   ├── config/               # 配置类
│       │   │   ├── AuthInterceptor.java      # JWT拦截器
│       │   │   ├── CorsConfig.java           # 跨域配置
│       │   │   ├── GlobalExceptionHandler.java # 全局异常处理
│       │   │   └── WebMvcConfig.java         # Web配置
│       │   ├── controller/           # 控制器层 (21个)
│       │   ├── entity/               # 实体类 (16个)
│       │   ├── mapper/               # Mapper接口 (17个)
│       │   ├── service/              # 服务层
│       │   │   ├── impl/             # 实现类
│       │   └── vo/                   # 视图对象
│       └── resources/
│           ├── application.yml       # 应用配置
│           └── mapper/               # XML映射文件
├── frontend/                         # 前端项目
│   ├── package.json                  # npm配置
│   ├── vue.config.js                 # Vue CLI配置
│   ├── babel.config.js               # Babel配置
│   └── src/
│       ├── main.js                   # 入口文件
│       ├── App.vue                   # 根组件
│       ├── api/                      # API接口 (9个模块)
│       ├── components/               # 公共组件
│       ├── router/index.js           # 路由配置
│       ├── store/index.js            # Vuex状态管理
│       ├── utils/request.js          # Axios封装
│       └── views/                    # 页面视图
│           ├── common/               # 公共页面
│           ├── student/              # 学生端
│           ├── teacher/              # 心理教师端
│           ├── counselor/            # 辅导员端
│           └── admin/                # 管理员端
└── db/
    └── init.sql                      # 数据库初始化脚本
```

---

## 环境搭建

### 1. 数据库准备

```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE campus_mental_health CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据
mysql -u root -p campus_mental_health < db/init.sql
```

### 2. 后端启动

```bash
cd backend

# 编译
mvn clean install

# 启动开发服务器
mvn spring-boot:run
```

后端服务默认运行在 `http://localhost:8080`，API前缀为 `/api`

### 3. 前端启动

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run serve
```

前端服务默认运行在 `http://localhost:8081`，已配置代理转发 `/api` 到后端

---

## 配置说明

### 后端配置 (application.yml)

```yaml
server:
  port: 8080                      # 服务端口
  servlet:
    context-path: /api            # API前缀

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_mental_health
    username: root                # 修改为你的数据库用户名
    password: admin               # 修改为你的数据库密码

# JWT密钥，生产环境请修改
jwt:
  secret: your-secret-key
  expiration: 86400000            # 24小时
```

### 前端配置 (vue.config.js)

```javascript
devServer: {
  port: 8081,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

---

## 核心功能模块

### 1. 用户认证模块
- **登录**: 支持学生、辅导员、心理教师、管理员四种角色
- **JWT鉴权**: 使用 jjwt 生成和验证 Token
- **密码加密**: 使用 BCrypt 加密存储

### 2. 心理测评模块
- 测评问卷管理 (教师)
- 学生在线测评
- 自动评分和预警生成
- 测评记录查询

### 3. 预约咨询模块
- 教师设置可预约时段
- 学生在线预约
- 咨询记录管理
- 预约状态跟踪

### 4. 预警管理模块
- 系统自动生成预警 (基于测评分数)
- 预警分级 (高/中/低)
- 干预记录跟踪
- 预警状态管理

### 5. 知识科普模块
- 文章发布与管理
- 分类浏览
- 阅读统计
- XSS安全防护

---

## API 接口规范

### 统一响应格式

```java
public class Result<T> {
    private Integer code;       // 200成功，其他失败
    private String message;     // 提示信息
    private T data;             // 返回数据
}
```

### 主要接口列表

| 接口 | 说明 |
|------|------|
| POST /api/login | 用户登录 |
| GET /api/student/** | 学生相关接口 |
| GET /api/counselor/** | 辅导员相关接口 |
| GET /api/teacher/** | 心理教师相关接口 |
| GET /api/admin/** | 管理员相关接口 |

---

## 开发规范

### 代码分层
- **Controller**: 处理 HTTP 请求，参数校验
- **Service**: 业务逻辑处理
- **Mapper**: 数据访问层
- **Entity**: 数据库实体
- **VO**: 视图对象，用于接口返回

### 安全规范
1. **XSS防护**: 前端使用 `v-html` 时必须先过滤
2. **SQL注入**: 使用 MyBatis-Plus，禁止拼接 SQL
3. **敏感数据**: 密码必须加密存储
4. **跨域**: 生产环境配置具体的允许域名

### 异常处理
- 统一使用 `GlobalExceptionHandler` 处理异常
- 业务异常使用 `Result.error()` 返回
- 系统异常记录日志并返回友好提示

---

## 测试账号

| 角色 | 账号 | 密码 |
|------|------|------|
| 学生 | student | 123456 |
| 辅导员 | counselor | 123456 |
| 心理教师 | teacher | 123456 |
| 管理员 | admin | 123456 |

---

## 部署指南

### 后端部署

```bash
cd backend
mvn clean package

# 生成的jar包在 target/mental-health-1.0.0.jar
java -jar target/mental-health-1.0.0.jar
```

### 前端部署

```bash
cd frontend
npm run build

# 生成的静态文件在 dist/ 目录，可部署到 Nginx
```

### Nginx 配置示例

```nginx
server {
    listen 80;
    server_name localhost;

    location / {
        root /path/to/frontend/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

---

## 常见问题

### 1. 数据库连接失败
- 检查 MySQL 服务是否启动
- 检查 application.yml 中的数据库配置
- 确认数据库 `campus_mental_health` 已创建

### 2. 前端无法访问后端
- 检查后端服务是否启动
- 确认 vue.config.js 中的代理配置
- 检查防火墙设置

### 3. 登录失败
- 确认数据库中有用户数据
- 检查密码是否正确（加密后的密码）
- 查看后端日志获取详细错误

---

## 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2024-04 | 初始版本，完成基础功能 |

---

## 联系方式

如有问题，请联系项目维护者。
