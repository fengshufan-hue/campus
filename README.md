# 校园心理健康服务平台

一个面向高校的心理健康服务管理系统，支持学生、辅导员、心理教师和管理员四种角色，提供心理测评、预约咨询、预警管理、知识科普等功能。

## 功能特性

- **心理测评**：在线心理健康测评，自动评分和预警生成
- **预约咨询**：在线预约心理咨询服务，支持排班管理
- **预警管理**：基于测评结果自动生成预警，支持分级干预
- **知识科普**：心理健康知识文章发布与浏览
- **多角色支持**：学生、辅导员、心理教师、管理员四种角色
- **数据安全**：JWT 鉴权 + BCrypt 密码加密

## 技术栈

### 后端
- **框架**: Spring Boot 2.7.14
- **JDK**: Java 8
- **数据库**: MySQL 8.0
- **ORM**: MyBatis-Plus 3.5.3.1
- **安全**: Spring Security + JWT (jjwt 0.11.5)
- **构建工具**: Maven

### 前端
- **框架**: Vue 2.6.14
- **UI组件库**: Element UI 2.15.13
- **状态管理**: Vuex 3.6.2
- **路由**: Vue Router 3.5.4
- **HTTP客户端**: Axios 0.27.2
- **构建工具**: Vue CLI 5.0

## 快速开始

### 环境要求

- JDK 1.8+
- Node.js 14+
- MySQL 8.0+
- Maven 3.6+

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

# 修改配置文件 application.yml 中的数据库连接信息
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

前端服务默认运行在 `http://localhost:8081`

## 测试账号

| 角色 | 账号 | 密码 |
|------|------|------|
| 学生 | student | 123456 |
| 辅导员 | counselor | 123456 |
| 心理教师 | teacher | 123456 |
| 管理员 | admin | 123456 |

## 项目结构

```
campus-mental-health/
├── backend/                          # 后端项目
│   ├── pom.xml                       # Maven配置
│   └── src/main/
│       ├── java/com/campus/mentalhealth/
│       │   ├── MentalHealthApplication.java    # 启动类
│       │   ├── common/               # 公共工具类
│       │   ├── config/               # 配置类
│       │   ├── controller/           # 控制器层
│       │   ├── entity/               # 实体类
│       │   ├── mapper/               # Mapper接口
│       │   ├── service/              # 服务层
│       │   └── vo/                   # 视图对象
│       └── resources/
│           ├── application.yml       # 应用配置
│           ── mapper/               # XML映射文件
├── frontend/                         # 前端项目
│   ├── package.json                  # npm配置
│   ── src/
│       ├── api/                      # API接口
│       ├── components/               # 公共组件
│       ├── router/                   # 路由配置
│       ├── store/                    # Vuex状态管理
│       ├── utils/                    # 工具类
│       └── views/                    # 页面视图
├── db/
│   └── init.sql                      # 数据库初始化脚本
├── DEVELOPMENT.md                    # 开发文档
└── USER_GUIDE.md                     # 使用手册
```

## 核心功能模块

### 1. 用户认证模块
- 支持四种角色登录
- JWT 鉴权
- BCrypt 密码加密

### 2. 心理测评模块
- 测评问卷管理（教师）
- 学生在线测评
- 自动评分和预警生成
- 测评记录查询

### 3. 预约咨询模块
- 教师设置可预约时段
- 学生在线预约
- 咨询记录管理
- 预约状态跟踪

### 4. 预警管理模块
- 系统自动生成预警（基于测评分数）
- 预警分级（高/中/低）
- 干预记录跟踪
- 预警状态管理

### 5. 知识科普模块
- 文章发布与管理
- 分类浏览
- 阅读统计

## 部署

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

## 文档

- [开发文档](DEVELOPMENT.md) - 详细的开发指南和 API 文档
- [使用手册](USER_GUIDE.md) - 用户操作指南

## 常见问题

### 数据库连接失败
- 检查 MySQL 服务是否启动
- 检查 application.yml 中的数据库配置
- 确认数据库 `campus_mental_health` 已创建

### 前端无法访问后端
- 检查后端服务是否启动
- 确认 vue.config.js 中的代理配置
- 检查防火墙设置

### 登录失败
- 确认数据库中有用户数据
- 检查密码是否正确
- 查看后端日志获取详细错误

## 许可证

本项目仅供学习和研究使用。

## 联系方式

如有问题，请联系项目维护者。
