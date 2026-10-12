---
name: cube2-layer-platform
description: >
  Routes queries within Cube2 packages/platform (platform layer): submodules
  base, server, database, security, client, messaging, job, etc. Use when
  the user mentions platform layer, packages/platform, 平台层, Maven
  platform-*, or needs to choose among platform subpackages. Does not define
  business domains in apps/base; points to leaf skills under each subdirectory.
---

# packages/platform（分流层）

横切 **平台层** 文档入口：根据问题类型下钻到一级子目录下的叶子 `SKILL.md`。仓库级构建通则见 [.agents/skills/cube2-repo/SKILL.md](../../.agents/skills/cube2-repo/SKILL.md)；**原则与分层边界**见 [.agents/skills/cube2-repo/references/principles.md](../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：说明 `packages/platform` 下各子包分工，并引导到对应子模块 SKILL。
- **不负责**：具体业务需求与表设计（归属 `packages/apps` / `packages/base` 等领域）；单包实现细节（归属各叶子 SKILL）。

## 子模块分流

完整表格与链接待维护在 [references/submodule-index.md](./references/submodule-index.md)（含 **何时选哪条路径**）。

摘要（关键词 → 子目录）：

| 意图 | 先去 |
|------|------|
| 通用工具/DTO/异常/Jackson、子包 `base/*` | `base/` |
| 服务端聚合依赖、与 `//packages/platform/server` 对齐 | `server/` |
| MyBatis-Plus、数据源、持久层封装 | `database/` |
| Spring Security、OAuth 与认证侧封装 | `security/` |
| Feign、客户端侧通用配置 | `client/` |
| Liquibase 迁移集成 | `liquibase/` |
| RocketMQ | `rocketmq/` |
| Redis | `redis/` |
| 平台任务/调度、`job/init` | `job/` |
| 域事件基础 | `event/` |
| DCM（已弃用，遇到时跳过） | `dcm/`（暂保留，不维护） |
| 子系统配置 / 请求 SDK | `subsystem-config/`、`subsystem-requester-sdk/` |
| Web/Servlet 通用、web-extra | `web/`、`web-extra/` |
| Spring Cloud 侧集成（常随 `server`/`cloud` 传递依赖） | `cloud/` |
| BOM 聚合 | `bom/` |
| 仓储抽象 | `repository/` |
| Nacos 初始化 | `nacos/` |
| 许可证 | `license/` |

## 快速定位清单

1. 读 [references/submodule-index.md](./references/submodule-index.md) 锁定子目录。
2. 打开该子目录下 `SKILL.md` 按其「快速定位清单」继续。
3. 构建标签以各包 `BUILD.bazel` 为准；根目录仅有 `//packages/platform:generic-deps`。

## 构建与入口

- 根：[BUILD.bazel](./BUILD.bazel) → `generic-deps`。
- 各子包：见子模块 SKILL 与 [submodule-index](./references/submodule-index.md)。

## 对外契约

平台层以 **被其它 `packages/*` 引用的库与 BOM** 为主，无统一 HTTP 网关；应用对外路由见各 `packages/deploy` 与各业务 `server_info`。

## 数据与配置

各子包自有 `resources` 或代码内配置；不设全局「平台数据库」语义。

## 深度阅读

- [submodule-index.md](./references/submodule-index.md)
- 评估背景（可选）：[.agents/skills/cube2-repo/references/platform-assessment.md](../../.agents/skills/cube2-repo/references/platform-assessment.md)
