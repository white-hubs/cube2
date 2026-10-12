# message 模块对外契约（外部消费者视角）

本文档基于仓库内 `BUILD.bazel`、Controller、`@FeignClient`、`@RocketMQMessageListener` 及 `server_info` 扫描整理；与网关、Nacos 不一致时以部署环境为准。

## 对外 Bazel 标签（常用）

| 用途 | 标签 |
|------|------|
| message2 服务实现 | `//packages/base/message/server:server` |
| message2 可运行（java_binary） | `//packages/base/message/server:server-bin` |
| message2 Spring Boot 可执行 | `//packages/base/message/server:server-exec` |
| message2 Maven 发布 | `//packages/base/message/server:package` |
| Feign 客户端（message2） | `//packages/base/message/client:client` |
| message2-client Maven 发布 | `//packages/base/message/client:package` |
| 业务 SDK | `//packages/base/message/sdk:sdk` |
| SDK Maven 发布 | `//packages/base/message/sdk:package` |
| 共享 metadata | `//packages/base/message/metadata:metadata` |
| bracing 服务 | `//packages/base/message/bracing/server:server` |
| bracing 可运行 | `//packages/base/message/bracing/server:server-bin` |
| bracing Spring Boot | `//packages/base/message/bracing/server:server-exec` |
| bracing Maven 发布 | `//packages/base/message/bracing/server:package` |
| bracing Feign 客户端 | `//packages/base/message/bracing/client:client` |
| bracing 客户端 export | `//packages/base/message/bracing/client:export`（及 `:export-dev`、`:export-test`） |

根目录 `//packages/base/message` **无**聚合 `java_library` / `export` 目标（根 `BUILD.bazel` 为空）。

## HTTP 接口与网关前缀

### message2（主消息服务）

- **server_info**：`gw_prefix = /message2/`（定义于 `packages/base/message/server/BUILD.bazel` 的 `server_info`）。
- **Controller 路径**：类上普遍**无**类级别 `@RequestMapping`，以下为应用内路径（网关下通常加前缀 `/message2`；若以 StripPrefix 等方式配置，以环境为准）。

| 区域 | 典型路径（节选） |
|------|------------------|
| base | `POST /msg-channel`，`GET /msg-channels`，`GET /msg-channel/{code}`，`POST /msg-scene`，`GET /msg-scenes`，`GET /msg-record/...`，`POST /msg-tmp`，等 |
| sat | `POST /sat-msg-send/send`，`POST /sat-msg-site/send`，`POST /sat-msg-site/broadcast/send`，`GET /sat-msg-applications`，`POST /sat-msg-channel`，等 |
| code | `POST /send/code`，`POST /verify/code` |
| bff | `GET /msg-record/custom-group/...` 等 |

完整方法级映射请直接在 `packages/base/message/server/src/**/controller/*.java` 中检索 `@GetMapping` / `@PostMapping`。

### WebSocket（message2）

- 应用注册：`/websocket/msg` 与 `/websocket/msg/`（`WebsocketConfig`）。
- **server_info** 中 `public_path_patterns` 包含 `/websocket/**` 与 `//message/websocket/**`（供网关/部署工具引用）。

### bracing（业务消息发送配置服务）

- **server_info**：`gw_prefix = /bracing/`。
- **Controller**：`MessageAssemblyNoticeController` — `POST /message/addConfig`，`GET /message/getConfigInfo`，`POST /message/getSendConfig`，`POST /message/send`（详见该文件）。

## Feign Client 接口名（仓库内）

### 调用 message2（`name = "message2"`）

| 接口 | 文件 |
|------|------|
| `CodeClient` | `client/src/CodeClient.java` |
| `SatSendClient` | `client/src/SatSendClient.java` |
| `SatMsgSiteSendClient` | `client/src/SatMsgSiteSendClient.java` |
| `SatMsgSceneReceiveClient` | `client/src/SatMsgSceneReceiveClient.java` |
| `MsgRecordClient` | `client/src/MsgRecordClient.java` |

### 调用 bracing（`messageBracing`）

| 接口 | 文件 |
|------|------|
| `AssemblyNoticeClient` | `bracing/client/src/api/AssemblyNoticeClient.java` |

## RocketMQ：Topic、ConsumerGroup 与说明

### `@RocketMQMessageListener`（message2 服务内）

| ConsumerGroup | Topic（常量或字面量） | 说明 |
|---------------|----------------------|------|
| `sat_msg_channel` | `DICTIONARY`（`MsgDictionaryConfigMessage.TOPIC`） | 字典变更驱动通道配置 |
| `sat_msg_channel_init` | `TENANT_INIT` | 租户初始化 |
| `sat_msg_channel_change` | `MSG_CHANNEL_MANUFACTURER_MODIFY` | 通道变更 |
| `sat_msg_scene_receive` | `MSG_CHANNEL_MANUFACTURER_MODIFY`（同上 Topic，不同消费者） | 场景接收侧初始化 |
| `msg_tmp_add` | `MSG_TMP_ADD` | 模板新增 |
| `sat_msg_tmp_init` | `TENANT_ADD_APPLICATION` | 租户应用维度模板初始化 |
| `msg_send_task` | `MSG_SEND_TASK` | 异步发送任务（CLUSTERING） |
| `web_site_socket_send` | `WEB_SITE_SOCKET_SEND` | 站内 WebSocket 投递（BROADCASTING） |

注解中多为 `selectorType = TAG` 且**未**写明 `selectorExpression` 时，具体 Tag 规则需结合发送端 `RocketMQContextHolder` 封装或团队约定；**待团队补充**逐 Topic 的 Tag 列表。

### 发布（生产者，代码中出现的 Topic）

| Topic | 出现位置（示例） |
|-------|------------------|
| `MSG_CHANNEL_MANUFACTURER_MODIFY` | `SatMsgChannelController`、`MsgChannelInitConsumer`（事务消息） |
| `MSG_SEND_TASK` | 验证码发送、`AfterMsgMqSendAction`（异步发送）、`SmsCodeServiceImpl` 等 |
| `WEB_SITE_SOCKET_SEND` | `SiteSender`、`SatMsgSiteSendController`、`MsgRecordController` 等 |
| `MSG_TMP_ADD` | `MsgTmpServiceImpl`（事务消息） |
| `MSG_RECORD_2` | `MsgRecordServiceImpl` 发送 `MsgRecordMqMessage` |

说明：仓库内**未见**针对 `MSG_RECORD_2` 的 `@RocketMQMessageListener` 消费者，消费方可能在其他模块或服务；**待团队补充**。

## SDK 使用方式（`sdk/`）

- **Maven/Bazel 依赖**：优先引用 `//packages/base/message/sdk:sdk` 或 `//packages/base/message/sdk:package` 产出的 `cube-base-message-sdk`（坐标以发布配置为准）。
- **主要入口类**（Spring Bean，非 Feign 接口）：
  - `com.guhecloud.cube.base.message.sdk.MessageSendClient` — 场景发送等，内部注入 `SatSendClient`、`SatMsgSceneReceiveClient` 等。
  - `com.guhecloud.cube.base.message.sdk.MessageSceneClient` — 场景与通道查询。
  - `com.guhecloud.cube.base.message.sdk.MessageReceiverSendClient`、`SiteMessageSendClient` — 接收方/站内相关发送封装。
- **配套类型**：`sdk/api/request`、`sdk/api/response`、`sdk/api/channel`（各通道 `*ChannelReceiver`）等。
- 使用方应用需启用 Feign 并保证能发现 `message2` 服务（与 `Message2Server` 的 `@EnableFeignClients` 配置一致）。
