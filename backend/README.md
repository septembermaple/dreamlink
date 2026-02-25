# 梦境匹配系统后端服务

这是一个基于Spring Boot的梦境匹配系统后端服务，提供用户认证、梦境管理、消息系统、评论系统和举报系统等功能。

## 技术栈

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database
- Spring Security
- JWT

## 项目结构

```
backend/
├── pom.xml                  # Maven配置文件
├── src/
│   ├── main/
│   │   ├── java/com/dreamlink/
│   │   │   ├── config/       # 配置类
│   │   │   ├── controller/   # 控制器
│   │   │   ├── dto/          # 数据传输对象
│   │   │   ├── entity/       # 实体类
│   │   │   ├── filter/       # 过滤器
│   │   │   ├── repository/   # 数据访问层
│   │   │   ├── service/      # 服务层
│   │   │   ├── utils/        # 工具类
│   │   │   └── DreamlinkApplication.java  # 应用主类
│   │   └── resources/
│   │       └── application.properties  # 应用配置文件
│   └── test/                # 测试类
└── README.md                # 项目说明文件
```

## 构建和运行

### 构建项目

```bash
# 进入backend目录
cd backend

# 构建项目
mvn clean package
```

### 运行项目

```bash
# 运行项目
java -jar target/dreamlink-backend-1.0-SNAPSHOT.jar

# 或使用Maven运行
mvn spring-boot:run
```

项目默认运行在 `http://localhost:8080`。

## API接口

### 1. 认证相关 API

- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `GET /api/auth/user/{userId}` - 获取用户信息
- `PUT /api/auth/user/{userId}` - 更新用户信息

### 2. 梦境相关 API

- `POST /api/dream` - 创建梦境
- `GET /api/dream/user/{userId}` - 获取用户的梦境
- `GET /api/dream/public` - 获取公开梦境
- `GET /api/dream/{dreamId}` - 获取梦境详情
- `PUT /api/dream/{dreamId}` - 更新梦境
- `DELETE /api/dream/{dreamId}` - 删除梦境

### 3. 消息相关 API

- `GET /api/message/user/{userId}` - 获取用户的消息
- `GET /api/message/chat/{userId1}/{userId2}` - 获取聊天记录
- `POST /api/message` - 发送消息
- `PUT /api/message/read/{messageId}` - 标记消息为已读

### 4. 评论相关 API

- `POST /api/comment` - 创建评论
- `GET /api/comment/dream/{dreamId}` - 获取梦境的评论

### 5. 举报相关 API

- `POST /api/report` - 创建举报
- `GET /api/report` - 获取所有举报
- `GET /api/report/{reportId}` - 获取举报详情
- `PUT /api/report/{reportId}` - 更新举报状态

## 测试账号

应用启动时会自动初始化以下测试账号：

| 用户名 | 密码 | 角色 |
|-------|------|------|
| admin1 | admin1 | 管理员 |
| admin2 | admin2 | 管理员 |
| admin3 | admin3 | 普通用户 |

## 数据库

项目使用H2内存数据库，应用启动时会自动创建表结构并初始化测试数据。

可以通过以下地址访问H2控制台：
- 地址：`http://localhost:8080/h2-console`
- JDBC URL：`jdbc:h2:mem:dreamlink`
- 用户名：`sa`
- 密码：空

## JWT认证

API接口（除了登录和注册）需要在请求头中携带JWT令牌：

```
Authorization: Bearer {token}
```

令牌可以通过登录接口获取。
