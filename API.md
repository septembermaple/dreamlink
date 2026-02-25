# API 文档

本文档整理了项目中所有需要数据请求的接口，包括请求参数和响应格式。

## 1. 认证相关 API

### 1.1 登录

**接口说明**：用户登录

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| username | string | 是 | 用户名或手机号 |
| password | string | 是 | 密码 |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 返回数据 |
| data.token | string | 登录令牌 |
| data.user | object | 用户信息 |
| message | string | 错误信息 |

**请求示例**：
```javascript
const response = await authAPI.login({
  username: 'admin1',
  password: 'admin1'
});
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "token": "mock-token-1",
    "user": {
      "id": 1,
      "username": "admin1",
      "nickname": "管理员1",
      "avatar": "https://api.dicebear.com/9.x/avataaars/svg?seed=Aries",
      "constellation": "白羊座",
      "isAdmin": true
    }
  }
}
```

### 1.2 注册

**接口说明**：用户注册

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |
| phone | string | 是 | 手机号 |
| constellation | string | 是 | 星座 |
| nickname | string | 否 | 昵称（默认使用用户名） |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 返回数据 |
| data.token | string | 登录令牌 |
| data.user | object | 用户信息 |
| message | string | 错误信息 |

**请求示例**：
```javascript
const response = await authAPI.register({
  username: 'testuser',
  password: '123456',
  phone: '13800138004',
  constellation: '双子座',
  nickname: '测试用户'
});
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "token": "mock-token-4",
    "user": {
      "id": 4,
      "username": "testuser",
      "nickname": "测试用户",
      "avatar": "https://via.placeholder.com/150",
      "constellation": "双子座",
      "isAdmin": false
    }
  }
}
```

### 1.3 获取用户信息

**接口说明**：获取用户详细信息

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId | number | 是 | 用户ID |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 用户信息 |
| message | string | 错误信息 |

**请求示例**：
```javascript
const response = await authAPI.getUserInfo(1);
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "id": 1,
    "username": "admin1",
    "nickname": "管理员1",
    "avatar": "https://api.dicebear.com/9.x/avataaars/svg?seed=Aries",
    "constellation": "白羊座",
    "isAdmin": true
  }
}
```

### 1.4 更新用户信息

**接口说明**：更新用户信息

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId | number | 是 | 用户ID |
| data | object | 是 | 要更新的用户信息 |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 更新后的用户信息 |
| message | string | 错误信息 |

**请求示例**：
```javascript
const response = await authAPI.updateUserInfo(1, {
  nickname: '新昵称',
  constellation: '金牛座'
});
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "id": 1,
    "username": "admin1",
    "nickname": "新昵称",
    "avatar": "https://api.dicebear.com/9.x/avataaars/svg?seed=Aries",
    "constellation": "金牛座",
    "isAdmin": true
  }
}
```

## 2. 梦境相关 API

### 2.1 创建梦境

**接口说明**：创建新梦境

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId | number | 是 | 用户ID |
| content | string | 是 | 梦境内容 |
| isPublic | boolean | 否 | 是否公开（默认false） |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 创建的梦境信息 |

**请求示例**：
```javascript
const response = await dreamAPI.createDream({
  userId: 1,
  content: '我梦见了一只会说话的猫',
  isPublic: true
});
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "id": 4,
    "userId": 1,
    "content": "我梦见了一只会说话的猫",
    "createdAt": "2026-02-24T12:00:00.000Z",
    "isPublic": true
  }
}
```

### 2.2 获取用户的梦境

**接口说明**：获取用户的所有梦境

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId | number | 是 | 用户ID |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | array | 梦境列表 |

**请求示例**：
```javascript
const response = await dreamAPI.getUserDreams(1);
```

**响应示例**：
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "userId": 1,
      "content": "我梦见自己在一个巨大的图书馆里...",
      "createdAt": "2026-02-23T12:00:00.000Z",
      "isPublic": true
    }
  ]
}
```

### 2.3 获取公开梦境

**接口说明**：获取所有公开的梦境（世界/广场）

**请求参数**：无

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | array | 公开梦境列表 |

**请求示例**：
```javascript
const response = await dreamAPI.getPublicDreams();
```

**响应示例**：
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "userId": 1,
      "content": "我梦见自己在一个巨大的图书馆里...",
      "createdAt": "2026-02-23T12:00:00.000Z",
      "isPublic": true
    },
    {
      "id": 2,
      "userId": 2,
      "content": "昨晚我梦见了一只会说话的猫...",
      "createdAt": "2026-02-24T00:00:00.000Z",
      "isPublic": true
    }
  ]
}
```

### 2.4 获取梦境详情

**接口说明**：获取单个梦境的详细信息

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| dreamId | number | 是 | 梦境ID |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 梦境详情 |
| message | string | 错误信息 |

**请求示例**：
```javascript
const response = await dreamAPI.getDreamDetail(1);
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "id": 1,
    "userId": 1,
    "content": "我梦见自己在一个巨大的图书馆里，书架一直延伸到天花板，每本书都闪烁着微弱的光芒...",
    "createdAt": "2026-02-23T12:00:00.000Z",
    "isPublic": true
  }
}
```

### 2.5 更新梦境

**接口说明**：更新梦境信息

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| dreamId | number | 是 | 梦境ID |
| data | object | 是 | 要更新的梦境信息 |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 更新后的梦境信息 |
| message | string | 错误信息 |

**请求示例**：
```javascript
const response = await dreamAPI.updateDream(1, {
  content: '更新后的梦境内容',
  isPublic: false
});
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "id": 1,
    "userId": 1,
    "content": "更新后的梦境内容",
    "createdAt": "2026-02-23T12:00:00.000Z",
    "isPublic": false
  }
}
```

### 2.6 删除梦境

**接口说明**：删除梦境

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| dreamId | number | 是 | 梦境ID |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| message | string | 操作结果或错误信息 |

**请求示例**：
```javascript
const response = await dreamAPI.deleteDream(1);
```

**响应示例**：
```json
{
  "success": true,
  "message": "删除成功"
}
```

## 3. 消息相关 API

### 3.1 获取用户的消息

**接口说明**：获取用户的所有消息

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId | number | 是 | 用户ID |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | array | 消息列表 |

**请求示例**：
```javascript
const response = await messageAPI.getUserMessages(1);
```

**响应示例**：
```json
{
  "success": true,
  "data": [
    {
      "id": 4,
      "type": "match",
      "content": "您的梦境与用户 管理员2 的梦境匹配度超过70%！",
      "userId": 1,
      "otherUserId": 2,
      "dreamId": 1,
      "otherDreamId": 2,
      "createdAt": "2026-02-24T05:00:00.000Z",
      "isRead": false
    },
    {
      "id": 1,
      "type": "chat",
      "content": "你好，我们的梦境匹配了！",
      "userId": 2,
      "otherUserId": 1,
      "createdAt": "2026-02-24T08:00:00.000Z",
      "isRead": false
    }
  ]
}
```

### 3.2 获取聊天记录

**接口说明**：获取两个用户之间的聊天记录

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| userId1 | number | 是 | 用户1的ID |
| userId2 | number | 是 | 用户2的ID |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | array | 聊天记录列表 |

**请求示例**：
```javascript
const response = await messageAPI.getChatHistory(1, 2);
```

**响应示例**：
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "type": "chat",
      "content": "你好，我们的梦境匹配了！",
      "userId": 2,
      "otherUserId": 1,
      "createdAt": "2026-02-24T08:00:00.000Z",
      "isRead": false
    },
    {
      "id": 2,
      "type": "chat",
      "content": "是的，真的很神奇！我们都梦见了会说话的动物。",
      "userId": 1,
      "otherUserId": 2,
      "createdAt": "2026-02-24T08:10:00.000Z",
      "isRead": false
    }
  ]
}
```

### 3.3 发送消息

**接口说明**：发送消息

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| data | object | 是 | 消息信息 |
| data.content | string | 是 | 消息内容 |
| data.userId | number | 是 | 发送者ID |
| data.otherUserId | number | 是 | 接收者ID |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 发送的消息信息 |

**请求示例**：
```javascript
const response = await messageAPI.sendMessage({
  content: '你好，最近怎么样？',
  userId: 1,
  otherUserId: 2
});
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "id": 6,
    "type": "chat",
    "content": "你好，最近怎么样？",
    "userId": 1,
    "otherUserId": 2,
    "createdAt": "2026-02-24T12:00:00.000Z",
    "isRead": false
  }
}
```

### 3.4 标记消息为已读

**接口说明**：标记消息为已读

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| messageId | number | 是 | 消息ID |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 更新后的消息信息 |
| message | string | 错误信息 |

**请求示例**：
```javascript
const response = await messageAPI.markAsRead(1);
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "id": 1,
    "type": "chat",
    "content": "你好，我们的梦境匹配了！",
    "userId": 2,
    "otherUserId": 1,
    "createdAt": "2026-02-24T08:00:00.000Z",
    "isRead": true
  }
}
```

## 4. 评论相关 API

### 4.1 创建评论

**接口说明**：创建评论

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| data | object | 是 | 评论信息 |
| data.dreamId | number | 是 | 梦境ID |
| data.userId | number | 是 | 用户ID |
| data.content | string | 是 | 评论内容 |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 创建的评论信息 |

**请求示例**：
```javascript
const response = await commentAPI.createComment({
  dreamId: 1,
  userId: 1,
  content: '这个梦真的很有趣！'
});
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "id": 3,
    "dreamId": 1,
    "userId": 1,
    "content": "这个梦真的很有趣！",
    "createdAt": "2026-02-24T12:00:00.000Z"
  }
}
```

### 4.2 获取梦境的评论

**接口说明**：获取指定梦境的所有评论

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| dreamId | number | 是 | 梦境ID |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | array | 评论列表 |

**请求示例**：
```javascript
const response = await commentAPI.getDreamComments(1);
```

**响应示例**：
```json
{
  "success": true,
  "data": [
    {
      "id": 2,
      "dreamId": 1,
      "userId": 3,
      "content": "意识图书馆，听起来很神奇！",
      "createdAt": "2026-02-24T05:00:00.000Z"
    },
    {
      "id": 1,
      "dreamId": 1,
      "userId": 2,
      "content": "这个梦真的很有趣，我也曾经做过类似的梦！",
      "createdAt": "2026-02-24T03:00:00.000Z"
    }
  ]
}
```

## 5. 举报相关 API

### 5.1 创建举报

**接口说明**：创建举报

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| data | object | 是 | 举报信息 |
| data.dreamId | number | 是 | 梦境ID |
| data.reporterId | number | 是 | 举报人ID |
| data.reason | string | 是 | 举报原因 |
| data.description | string | 否 | 详细描述 |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 创建的举报信息 |

**请求示例**：
```javascript
const response = await reportAPI.createReport({
  dreamId: 2,
  reporterId: 1,
  reason: '内容不当',
  description: '梦见的内容涉及敏感话题'
});
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "id": 2,
    "dreamId": 2,
    "reporterId": 1,
    "reason": "内容不当",
    "description": "梦见的内容涉及敏感话题",
    "status": "pending",
    "createdAt": "2026-02-24T12:00:00.000Z",
    "processedAt": null,
    "processedBy": null
  }
}
```

### 5.2 获取所有举报

**接口说明**：获取所有举报记录

**请求参数**：无

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | array | 举报记录列表 |

**请求示例**：
```javascript
const response = await reportAPI.getAllReports();
```

**响应示例**：
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "dreamId": 2,
      "reporterId": 1,
      "reason": "内容不当",
      "description": "梦见的内容涉及敏感话题",
      "status": "pending",
      "createdAt": "2026-02-24T05:00:00.000Z",
      "processedAt": null,
      "processedBy": null
    }
  ]
}
```

### 5.3 获取举报详情

**接口说明**：获取单个举报的详细信息

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| reportId | number | 是 | 举报ID |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 举报详情 |
| message | string | 错误信息 |

**请求示例**：
```javascript
const response = await reportAPI.getReportDetail(1);
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "id": 1,
    "dreamId": 2,
    "reporterId": 1,
    "reason": "内容不当",
    "description": "梦见的内容涉及敏感话题",
    "status": "pending",
    "createdAt": "2026-02-24T05:00:00.000Z",
    "processedAt": null,
    "processedBy": null
  }
}
```

### 5.4 更新举报状态

**接口说明**：更新举报状态

**请求参数**：
| 参数名 | 类型 | 必填 | 说明 |
| ------ | ---- | ---- | ---- |
| reportId | number | 是 | 举报ID |
| data | object | 是 | 状态更新信息 |
| data.status | string | 是 | 新状态（pending, processed, dismissed） |
| data.processedBy | number | 是 | 处理人ID |

**响应格式**：
| 字段名 | 类型 | 说明 |
| ------ | ---- | ---- |
| success | boolean | 请求是否成功 |
| data | object | 更新后的举报信息 |
| message | string | 错误信息 |

**请求示例**：
```javascript
const response = await reportAPI.updateReportStatus(1, {
  status: 'processed',
  processedBy: 1
});
```

**响应示例**：
```json
{
  "success": true,
  "data": {
    "id": 1,
    "dreamId": 2,
    "reporterId": 1,
    "reason": "内容不当",
    "description": "梦见的内容涉及敏感话题",
    "status": "processed",
    "createdAt": "2026-02-24T05:00:00.000Z",
    "processedAt": "2026-02-24T12:00:00.000Z",
    "processedBy": 1
  }
}
```

## 2. 数据结构

### 2.1 用户结构

```json
{
  "id": 1,
  "username": "admin1",
  "password": "admin1",
  "nickname": "管理员1",
  "avatar": "https://api.dicebear.com/9.x/avataaars/svg?seed=Aries",
  "constellation": "白羊座",
  "phone": "13800138001",
  "isAdmin": true
}
```

### 2.2 梦境结构

```json
{
  "id": 1,
  "userId": 1,
  "content": "我梦见自己在一个巨大的图书馆里...",
  "createdAt": "2026-02-23T12:00:00.000Z",
  "isPublic": true
}
```

### 2.3 消息结构

```json
{
  "id": 1,
  "type": "chat",
  "content": "你好，我们的梦境匹配了！",
  "userId": 2,
  "otherUserId": 1,
  "dreamId": 1,
  "otherDreamId": 2,
  "createdAt": "2026-02-24T08:00:00.000Z",
  "isRead": false
}
```

### 2.4 评论结构

```json
{
  "id": 1,
  "dreamId": 1,
  "userId": 2,
  "content": "这个梦真的很有趣，我也曾经做过类似的梦！",
  "createdAt": "2026-02-24T03:00:00.000Z"
}
```

### 2.5 举报结构

```json
{
  "id": 1,
  "dreamId": 2,
  "reporterId": 1,
  "reason": "内容不当",
  "description": "梦见的内容涉及敏感话题",
  "status": "pending",
  "createdAt": "2026-02-24T05:00:00.000Z",
  "processedAt": null,
  "processedBy": null
}
```
