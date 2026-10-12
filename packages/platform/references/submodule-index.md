# platform 子模块索引

- **本层路径**：`packages/platform`
- **本层 `SKILL.md` name**：`cube2-layer-platform`
- **最后更新**：2026-03-31

## 子模块索引表

| 子目录 | 类型 | 子模块 `name` | 一句话职责 | 何时选这条路径 | 链到子 SKILL |
|--------|------|---------------|------------|----------------|--------------|
| `base/` | 叶子 | `cube2-platform-base` | 通用库、model/utils/standard/constants、export | 工具类、分页、异常、Jackson、子标签 `base/*` | [../base/SKILL.md](../base/SKILL.md) |
| `server/` | 叶子 | `cube2-platform-server` | 服务端依赖聚合 exports | `deps` 里写 `//packages/platform/server` | [../server/SKILL.md](../server/SKILL.md) |
| `database/` | 叶子 | `cube2-platform-database` | MyBatis-Plus、JDBC、多数据源 | 持久层、Mapper、`//packages/platform/database` | [../database/SKILL.md](../database/SKILL.md) |
| `security/` | 叶子 | `cube2-platform-security` | Security/OAuth 封装 | 认证授权、安全过滤器 | [../security/SKILL.md](../security/SKILL.md) |
| `client/` | 叶子 | `cube2-platform-client` | Feign、客户端配置 | OpenFeign、下游调用错误解码 | [../client/SKILL.md](../client/SKILL.md) |
| `liquibase/` | 叶子 | `cube2-platform-liquibase` | Liquibase 集成 | 迁移、changelog | [../liquibase/SKILL.md](../liquibase/SKILL.md) |
| `rocketmq/` | 叶子 | `cube2-platform-rocketmq` | RocketMQ 集成 | 消息生产消费 | [../rocketmq/SKILL.md](../rocketmq/SKILL.md) |
| `redis/` | 叶子 | `cube2-platform-redis` | Redis 集成 | 缓存、分布式锁等 | [../redis/SKILL.md](../redis/SKILL.md) |
| `job/` | 叶子 | `cube2-platform-job` | 任务/调度 | `@Scheduled`、平台 job、`job/init` | [../job/SKILL.md](../job/SKILL.md) |
| `event/` | 叶子 | `cube2-platform-event` | 事件基础能力 | 领域事件基础设施 | [../event/SKILL.md](../event/SKILL.md) |
| `dcm/` | 叶子 | `cube2-platform-dcm` | DCM common/cli/server-support | DCM 脚本、服务端支撑 | [../dcm/SKILL.md](../dcm/SKILL.md) |
| `subsystem-config/` | 叶子 | `cube2-platform-subsystem-config` | 子系统配置 | 多子系统配置模型 | [../subsystem-config/SKILL.md](../subsystem-config/SKILL.md) |
| `subsystem-requester-sdk/` | 叶子 | `cube2-platform-subsystem-requester-sdk` | 子系统请求 SDK | MQ + Feign 等编排 | [../subsystem-requester-sdk/SKILL.md](../subsystem-requester-sdk/SKILL.md) |
| `web/` | 叶子 | `cube2-platform-web` | Servlet/Web 通用 | 常随 `server` 传递；Web 工具与异常响应 | [../web/SKILL.md](../web/SKILL.md) |
| `web-extra/` | 叶子 | `cube2-platform-web-extra` | Web 扩展（依 web/database） | Excel 等扩展能力 | [../web-extra/SKILL.md](../web-extra/SKILL.md) |
| `cloud/` | 叶子 | `cube2-platform-cloud` | Spring Cloud/Nacos 集成库 | 微服务集成；常与 `server`/`redis`/`rocketmq` 同现 | [../cloud/SKILL.md](../cloud/SKILL.md) |
| `repository/` | 叶子 | `cube2-platform-repository` | 仓储接口与实现分层 | 通用仓储抽象 | [../repository/SKILL.md](../repository/SKILL.md) |
| `bom/` | 叶子 | `cube2-platform-bom` | 顶层聚合 Maven BOM | 依赖 BOM、无业务源码 | [../bom/SKILL.md](../bom/SKILL.md) |
| `license/` | 叶子 | `cube2-platform-license` | 许可证相关库 | License 校验/展示等 | [../license/SKILL.md](../license/SKILL.md) |
| `nacos/` | 叶子 | `cube2-platform-nacos` | Nacos 初始化 | `nacos/init`、启动期配置 | [../nacos/SKILL.md](../nacos/SKILL.md) |

## 同步检查

- [x] 与磁盘一级子目录一致（至 2026-03-31）。
- [x] 已同步 `.agents/skills/cube2-repo/references/module-skill-index.md`。
