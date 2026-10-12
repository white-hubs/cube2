---
name: cube2-base-dictionary
description: >
  Guides the Cube2 base dictionary module: REST APIs and Feign clients for
  system/data/cascade/user dictionaries, DCM config bridge, Redis cache, and
  RocketMQ change notifications. Use for packages/base/dictionary, 字典服务,
  数据字典, Feign dictionary, or gateway path /dictionary/.
---

# base/dictionary

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。术语见 [.agents/skills/cube2-repo/references/glossary.md](../../../.agents/skills/cube2-repo/references/glossary.md)。

## 模块边界与职责

- **负责**：
  - **多类字典数据与 API**：系统字典、列表（数据）字典、树型（级联）字典、用户级字典的增删改查与分页查询（`server/src/controller`、`service`）。
  - **字典项与缓存刷新**：字典项查询、公开只读接口、缓存刷新入口（见各 `*Controller`）。
  - **变更通知**：按字典项配置的 `changeNotice`，通过 **Redis** 与/或 **RocketMQ** 下发变更（`ChangeNoticeServiceImpl`、`event/DictionaryMessage`）。
  - **DCM 集成**：设备/平台配置侧拉取字典类型与字典项（`src-dcm/`，`/dict/dcm/config`）。
  - **对外 Java 契约**：`client/` 下 Feign 接口、DTO、参数模型与部分 Adapter/Fallback。
- **不负责**：网关路由与鉴权策略的落地（以 Nacos/common-security 与部署网关为准）；**MQ 消费方实现**（本仓库该模块内无 `@RocketMQMessageListener`，仅发送）；其他业务域的主数据与流程编排。

## 子模块分流

| 子目录 | Bazel 标签（包路径前缀 `//packages/base/dictionary/`） | 职责 |
|--------|--------------------------------------------------------|------|
| `client/` | `:client`、`client:export`、`client:export-dev`、`client:export-test` | Feign 客户端、DTO/参数、Adapter、Fallback；Maven 发布用 `java_export`。 |
| `server/` | `server:deps`（private）、`server:server`、`server:server-bin`、`server:package`、`server:server-exec`、`server:server-info` | Spring Boot 服务实现、Liquibase/MyBatis 资源、可执行与发布。 |
| `server/init/` | `server/init:init-mysql` | 初始化 SQL 打包（供 deploy database 等引用）。 |

根目录 `BUILD.bazel` 当前为空文件，**无**根级 `java_library`/`export` 聚合目标；引用时以 `client/` 与 `server/` 下目标为准。

## 快速定位清单

1. **服务名与网关前缀**：`bootstrap.yaml` 中 `spring.application.name` 为 `dictionary`；`server_info` 声明 `gw_prefix` 为 `/dictionary/`（见 `server/BUILD.bazel` 中 `server_info`）。
2. **Feign 调用别的服务**：入口在 `client/src/client/*Client.java`，统一指向服务名 `dictionary`。
3. **HTTP Controller 路径**：`server/src/controller/*.java`；用户字典有类级前缀 `/user-dictionary`，其余多为根路径方法映射（与 Feign 中路径一致）。
4. **数据库与 Liquibase**：库名见 `application.yaml` JDBC URL；changelog 在 `server/resources/db/com.guhecloud.cube.base.dictionary/`。
5. **DCM**：`server/src-dcm/controller/DictionaryDcmController`，`@RequestMapping("/dict/dcm/config")`。
6. **MQ 发送常量**：`DictionaryMessage.TOPIC`、`DictionaryDeleteMessage.TOPIC`（`server/src/event/`）。
7. **发布 Maven 坐标**：`variables.bzl` 中 `PACKAGE_GROUP`、`VERSION`；服务端 `server:package`，客户端 `client:export*`。

## 构建与入口

- **服务端库与运行**：`//packages/base/dictionary/server:server`；本地 JVM：`//packages/base/dictionary/server:server-bin`；Spring Boot 可执行：`//packages/base/dictionary/server:server-exec`。
- **服务端发布**：`//packages/base/dictionary/server:package`（`export` 宏）。
- **服务端元数据**（数据库名、网关前缀等）：`//packages/base/dictionary/server:server-info`（供 `server-one` / deploy 消费，见 glossary `server_info`）。
- **客户端**：`//packages/base/dictionary/client:client`；对外 Maven：`//packages/base/dictionary/client:export`（及 `-dev`/`-test`）。
- **初始化 SQL 包**：`//packages/base/dictionary/server/init:init-mysql`。

## 对外契约

- **HTTP**：应用层路径以 Controller 为准；经网关时前缀为 **`/dictionary/`**（与 `server_info` 一致）。用户字典 API 在 **`/dictionary/user-dictionary/**`**；其余见 `references/external-api.md`。
- **Feign Client**：`UserDictionaryClient`、`SysDictionaryClient`、`DataDictionaryClient`（含 fallback）、`CascadeDictionaryClient`；服务名 **`dictionary`**（`@FeignClient`）。
- **RocketMQ**：本模块**未发现** `@RocketMQMessageListener` 消费者；生产侧 Topic 见 `DictionaryMessage` / `DictionaryDeleteMessage`（`references/external-api.md`）。

## 数据与配置

- **数据库名**：`cube-base-dictionary`（`server_info` 与 `application.yaml` 中 JDBC URL 一致）。
- **spring.application.name**：`dictionary`（`server/resources/bootstrap.yaml`）。
- **端口**：`server/resources/application.yaml` 使用 `common-env.dictionary.port`，默认占位待 Nacos `common-env` 解析；直连本地端口以运行环境为准。

## 深度阅读

- [references/overview.md](./references/overview.md)（目录树、开发入口、本地运行）
- [references/external-api.md](./references/external-api.md)（Bazel 对外标签、路由、Feign、MQ 发送）
