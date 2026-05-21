# 地府生死簿管理系统

> 全栈项目 · Vue 3 + Spring Boot 3 + MySQL + Redis · 阴曹地府主题 RBAC 管理平台

---

## 项目简介

以中国神话阴曹地府为背景的全栈管理系统，实现魂魄生死簿的数字化管理——从勾魂、审判、复核到六道轮回，配合十八层地狱图鉴与百鬼录。

**核心工作流：** 无常勾魂 → 判官审判 → 阎王复核 → 分配轮回

**RBAC 四角色：** 无常(WUCHANG) / 判官(JUDGE) / 阎王(KING) / 管理员(ADMIN)，权限严格隔离

---

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3 + Element Plus + ECharts + Pinia + Vue Router |
| 后端 | Spring Boot 3.2.5 + Spring Security 6 + MyBatis-Plus 3.5.7 |
| 数据库 | MySQL 8 + Redis |
| API 文档 | Knife4j (Swagger) |
| 部署 | Nginx 反向代理 |

---

## 快速启动

### 环境要求
- Java 17+ / Node.js 18+ / MySQL 8.0+ / Redis 7.0+

### 1. 初始化数据库
```bash
mysql -u root -p < backend/src/main/resources/db/init.sql
```

### 2. 启动后端
```bash
cd backend
mvn spring-boot:run
# 启动在 http://localhost:8080
# API 文档: http://localhost:8080/doc.html
```

### 3. 启动前端（二选一）

**开发模式：**
```bash
cd frontend
npm install
npm run dev
# http://localhost:5173
```

**生产模式（Nginx）：**
```bash
cd frontend && npm install && npm run build
cd ../nginx
./nginx.exe
# http://localhost:80
```

### 默认账户

| 用户名 | 密码 | 角色 |
|--------|------|------|
| `wuchang1` | `123456` | 无常 |
| `judge1` | `123456` | 判官 |
| `king1` | `123456` | 阎王 |
| `admin` | `123456` | 管理员 |

---

## 前端亮点

- **古旧羊皮纸质感**：SVG 噪点薄膜 + 纸质纤维纹理 + `ZCOOL XiaoWei` 书法字体
- **登录页**：4 只 CSS Q版鬼怪（黑白无常/判官/小鬼）实时跟踪鼠标，官印鎏金扫光
- **数据大屏**：全屏实时监控，6 统计卡片 + 3 ECharts 图表 + 自动刷新
- **地狱页**：18 层递进 zigzag 布局，CSS 渐变场景替代外链图片
- **百鬼录**：卷轴展开式 masonry 画廊

## 后端亮点

- **AOP 自动操作日志**：`@OpLog` 注解自动记录操作人/IP/目标到数据库
- **Redis 接口限流**：`@RateLimit` 注解，登录接口每分钟 5 次限制
- **RBAC 权限模型**：role → role_permission → permission，URL 级别 `hasAuthority()` 鉴权
- **JWT 双通道**：Header 认证 + URL 参数（Excel 导出场景）
- **Redis 多级缓存**：不同业务独立 TTL 策略

---

## 项目结构

```
├── frontend/          # Vue 3 前端
│   └── src/
│       ├── views/     # 11 个页面组件
│       ├── api/       # API 请求封装
│       ├── layout/    # 主布局
│       ├── router/    # 路由配置
│       └── store/     # Pinia 状态管理
├── backend/           # Spring Boot 后端
│   └── src/main/java/com/hellcourt/
│       ├── controller/  # 10 个控制器
│       ├── service/     # 7 个服务
│       ├── entity/      # 11 个实体
│       ├── security/    # Spring Security + JWT
│       └── common/      # AOP / 注解 / 配置
└── nginx/             # Nginx 部署配置
```

---

## 数据库表

`role` `user` `permission` `role_permission` `soul` `judgment` `review` `rebirth` `operation_log` `hell_layer` `demon`

详细文档见 [地府生死簿管理系统-项目文档.md](./地府生死簿管理系统-项目文档.md)
