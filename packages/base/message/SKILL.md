---
name: cube2-base-message
description: >
  Cube2 base message domain: multi-channel notification (SMS/email/site/WebSocket
  等)、租户侧 SAT 消息编排、验证码与消息记录；含独立 message2 服务与 bracing 装配子域。
  触发词：message、message2、站内信、消息中心、RocketMQ、Feign message2、bracing、装配通知。
---

# packages/base/message

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。本模块根目录 `BUILD.bazel` 当前为空；**可构建目标分布在** `client/`、`metadata/`、`sdk/`、`server/`、`bracing/*` 子目录的 `BUILD.bazel` 中。

## 模块边界与职责

- **负责**：
  - **消息领域服务（message2）**：通道/场景/模板/厂商、消息发送与记录、验证码、多通道发送编排（`handler/`）、RocketMQ 异步任务与初始化消费、WebSocket 站内信（`/websocket/msg`）。
  - **装配通知子域（bracing）**：独立 Spring Boot 服务 `BracingServer`，提供消息发送配置查询与发送入口（与 message2 SDK 协同）。
  - **共享契约**：`metadata`（如 `ChannelEnum` 等枚举与常量）、`client`（Feign API）、`sdk`（面向业务方的发送/场景封装，依赖 client + metadata）。
- **不负责**：统一账号认证实现（使用 `ouaa` client）；网关路由与注册中心的具体 YAML（多来自 Nacos `common-env.yaml` 等外部配置）；非本仓库的消息消费者（如 `MSG_RECORD_2` 若由其他服务消费）。

## 子模块分流

| 子目录 | Bazel 标签（节选） | 职责 |
|--------|-------------------|------|
| `metadata/` | `//packages/base/message/metadata:metadata` | 跨 client/server/sdk 共享的枚举与常量（如通道枚举）。 |
| `client/` | `//packages/base/message/client:client`、`//packages/base/message/client:package` | OpenFeign 接口与 DTO/Param，供他服务调用 message2（`@FeignClient(name = "message2")`）。 |
| `sdk/` | `//packages/base/message/sdk:sdk`、`//packages/base/message/sdk:package` | 基于 client 的高层 `MessageSendClient`、`MessageSceneClient` 等 Spring `@Service` 封装。 |
| `server/` | `//packages/base/message/server:server`、`//packages/base/message/server:server-bin`、`//packages/base/message/server:server-exec`、`//packages/base/message/server:package`、`//packages/base/message/server:server-info` | message2 主服务：`Message2Server`，MyBatis、Liquibase、RocketMQ、任务、WebSocket。 |
| `server/init/` | `//packages/base/message/server/init:init-mysql` | MySQL 初始化 SQL 打包（供 deploy 数据库侧使用）。 |
| `bracing/client/` | `//packages/base/message/bracing/client:client`、`//packages/base/message/bracing/client:export`（及 `export-dev` / `export-test`） | 调用 bracing 服务的 Feign（`@FeignClient("messageBracing")`）。 |
| `bracing/server/` | `//packages/base/message/bracing/server:server`、`//packages/base/message/bracing/server:server-bin`、`//packages/base/message/bracing/server:server-exec`、`//packages/base/message/bracing/server:package`、`//packages/base/message/bracing/server:server-info` | bracing 独立服务：`BracingServer`。 |
| `bracing/server/init/` | `//packages/base/message/bracing/server/init:init-mysql` | bracing 库表初始化 SQL 打包。 |

## 快速定位清单

1. **改 HTTP 行为 / 路由**：`server/src/**/controller`（如 `base/controller`、`sat/controller`、`code/controller`、`bff/controller`）；网关前缀见 `server_info` 的 `gw_prefix`（`/message2/`）。
2. **改 Feign 对外 API**：`client/src/*Client.java`；bracing 侧见 `bracing/client/src/api/AssemblyNoticeClient.java`。
3. **业务方通过 SDK 发消息**：`sdk/src/MessageSendClient.java`、`SiteMessageSendClient.java` 等（内部再调 Feign）。
4. **RocketMQ 订阅**：`server/src/**/rocketmq/**/consumer` 与 `initialization/rocketmq/comsumer`（注意目录名拼写为 `comsumer`）。
5. **站内 WebSocket**：`handler/handler/send/site/config/WebsocketConfig.java`（路径 `/websocket/msg`）、`MsgWebsocketHandler`。
6. **数据库与 Liquibase**：`server/resources/application.yaml` 中数据源与 changelog 包路径；库名亦见 `server:server-info` 的 `database`。
7. **装配子域**：`bracing/server/src/controller/MessageAssemblyNoticeController.java`。

## 构建与入口

- **message2 服务**：`//packages/base/message/server:server`（库）、`//packages/base/message/server:server-bin`（`java_binary`）、`//packages/base/message/server:server-exec`（springboot 可执行）、`//packages/base/message/server:package`（Maven 发布）。
- **message2 客户端 JAR**：`//packages/base/message/client:package`（artifact `message2-client`）。
- **SDK JAR**：`//packages/base/message/sdk:package`（artifact `cube-base-message-sdk`）。
- **metadata**：`//packages/base/message/metadata:metadata`。
- **bracing 服务**：`//packages/base/message/bracing/server:server-bin` / `:server-exec` / `:package`。
- **bracing 客户端发布**：`//packages/base/message/bracing/client:export`（及 dev/test 变体）。

主类：`com.guhecloud.cube.base.message2.server.Message2Server`；bracing：`com.guhecloud.cube.base.bracing.BracingServer`。

## 对外契约

- **HTTP / 网关**：`server_info` 中 message2 的 `gw_prefix` 为 `/message2/`；bracing 为 `/bracing/`。Controller 多为方法级路径（如 `/msg-channel`、`/sat-msg-send/send`），与网关前缀拼接后对外；**精确路由表**见 [references/external-api.md](./references/external-api.md)。
- **服务发现 / Feign**：client 使用 `@FeignClient(name = "message2")`；bracing client 使用 `@FeignClient("messageBracing")`（与 `server_info` 中 `server_id` 对应关系以注册中心为准）。
- **WebSocket**：`server_info.public_path_patterns` 含 `/websocket/**` 等；应用内注册路径为 `/websocket/msg`（见 `WebsocketConfig`）。
- **RocketMQ**：Topic、ConsumerGroup 与部分发布点见 [references/external-api.md](./references/external-api.md)；Tag 过滤多数在注解中未写 `selectorExpression`，需结合消息体或平台规范确认。

## 数据与配置

- **数据库（本地 YAML 中 JDBC）**：message2 → `cube_base_message2`（见 `server/resources/application.yaml` 与 `//packages/base/message/server:server-info`）；bracing → `cube_base_bracing`（见 `bracing/server/resources/application.yaml` 与 bracing `server-info`）。
- **spring.application.name**：仓库内 `application.yaml` **未**声明该字段；运行名通常与 Nacos / Feign 的 `message2`、`messageBracing` 一致，**待团队从 Nacos `common-env.yaml` 等配置核对**。
- **端口**：message2 默认 `10115`（`${common-env.message.port:10115}`）；bracing 默认 `10117`（`${common-env.message-bracing.port:10117}`）。

## 深度阅读

- [references/external-api.md](./references/external-api.md)（对外 Bazel 标签、HTTP、Feign、MQ、SDK）
- [references/overview.md](./references/overview.md)（目录树、开发入口、`bazel run`）
