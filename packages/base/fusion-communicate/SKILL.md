---
name: cube2-base-fusion-communicate
description: >
  Cube2 base 层融合通讯微服务：融合通讯账号与文件、会议房间编排、GPS、腾讯 TRTC
  UserSig/房间管控及迅一（XunYi）账号对接；网关前缀 /fusion-communicate/。触发词：
  fusion-communicate、融合通讯、TRTC、会议房间、UserSig、迅一、GPS 上传。
---

# base/fusion-communicate

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)；术语见 [.agents/skills/cube2-repo/references/glossary.md](../../../.agents/skills/cube2-repo/references/glossary.md)。

## 模块边界与职责

- **负责**：
  - **融合通讯账号与文件**：账号分页、与系统用户绑定/分配、保活与禁用、通讯侧文件分页与更新（`controller/` + `service/`）。
  - **会议房间**：创建/提议/邀请/加入/拒绝/离开/移人/关闭及房间查询（`conference/`，DDD 分层：`interfaces` / `application` / `domain` / `infrastructure`）。
  - **GPS**：用户 GPS 上报与分页查询（`gps/`）。
  - **第三方融合**：腾讯实时音视频（TRTC）UserSig 与 TRTC OpenAPI（房间解散、移人等）；迅一账号相关 HTTP；多厂商账号策略（`third/`）。
  - **定时任务**：腾讯/迅一账号状态、配置刷新等（`@JobAnnotation`，依赖 `platform/job`）。
- **不负责**：统一认证与资源服务器实现（依赖 Nacos 中的 security 配置与 `platform/security`）；网关路由与租户级第三方密钥细节（见 Nacos）；客户端 SDK 的 TRTC 推拉流实现（仅服务端签权与房间管控）。

## 子模块分流

本模块**无**独立 `client/` 子包；Maven 发布与可执行入口均在 `server/`。数据库初始化脚本在 `server/init/`。

| 路径 / 标签 | 职责 |
|-------------|------|
| `server/src/controller`、`server/src/service` | 融合通讯账号、文件 REST 与业务服务。 |
| `server/src/conference` | 会议房间领域与 REST。 |
| `server/src/gps` | GPS 控制器、Mapper、Service、PO/VO。 |
| `server/src/third/tencent` | 腾讯 TRTC API、UserSig、相关 Job。 |
| `server/src/third/xunyi` | 迅一账号 HTTP 与同步 Job。 |
| `server/src/third/account` | 第三方融合账号分配等应用服务。 |
| `server/src/config` | 模块级 Spring 配置（如 `CommunicateConfiguration`）。 |
| `//packages/base/fusion-communicate/server:package` | `fusioncommunicate-server` Maven 发布。 |
| `//packages/base/fusion-communicate/server/init:init-mysql` | 初始化 SQL 的 `pkg_files`（供 deploy/database 使用）。 |

## 快速定位清单

1. **网关前缀、库名、server_id**：`server/BUILD.bazel` 中 `server_info` → `gw_prefix = "/fusion-communicate/"`，`database = "cube_biz_fusioncommunicate"`，`server_id = "fusionCommunicate"`。
2. **应用名与端口**：`server/resources/bootstrap.yaml`（`spring.application.name`）；`server/resources/application.yaml`（`server.port` 默认 `10222`，以 Nacos `common-env` 为准）。
3. **融合通讯账号与文件 API**：`FusionCommunicateAccountController.java`、`FusionCommunicateFileController.java`。
4. **会议房间 API**：`conference/interfaces/ConferenceRoomController.java`。
5. **腾讯 TRTC 签权**：`third/tencent/controller/TencentCommunicationController.java`（`/tencent/communication/sig`）。
6. **迅一账号 API**：`third/xunyi/controller/XunYiAccountController.java`（`/xun-yi/fusion-communicate/...`）。
7. **GPS**：`gps/controller/UserGpsController.java`。
8. **入口主类**：`FusionCommunicateServer.java`（`server/BUILD.bazel` 中 `_MAIN_CLASS`）。

## 构建与入口

- **服务端库**：`//packages/base/fusion-communicate/server:server`（源码 + `resources`）。
- **可运行**：`//packages/base/fusion-communicate/server:server-bin`（`java_binary`）；`//packages/base/fusion-communicate/server:server-exec`（`springboot` fat-jar）。
- **发布**：`//packages/base/fusion-communicate/server:package`（artifactId `fusioncommunicate-server`，`PACKAGE_GROUP` 见模块根 `variables.bzl`）。
- **部署元数据**：`//packages/base/fusion-communicate/server:server-info`。
- **模块根 `BUILD.bazel`**：当前无 `java_library` / `export` 目标，仅注释；构建入口在 `server/` 与 `server/init/`。

## 对外契约

- **HTTP**：网关层前缀 **`/fusion-communicate/`**（`server_info`）；应用内各 Controller 方法路径见 [references/external-api.md](./references/external-api.md)。本模块**未提供**独立 Feign `client` 子包；跨服务调用方式以团队网关/OpenAPI 约定为准（待团队补充是否在其他包声明 `@FeignClient`）。
- **RocketMQ**：`application.yaml` 从 Nacos 引入 `common-rocketmq.yaml`，且 `deps` 含 `//packages/platform/rocketmq`；**本仓库源码中未发现** `@RocketMQMessageListener` 等消费者。是否在运行时使用 MQ（生产/消费 Topic、Tag）**待团队补充**（以 Nacos 与线上配置为准）。
- **融合通信协议**：
  - **HTTP**：REST JSON，路径见 external-api。
  - **腾讯 TRTC**：服务端通过 TLSSigAPIv2 生成 **UserSig**，并通过腾讯云 TRTC OpenAPI（如 `DismissRoomByStrRoomId`、`RemoveUserByStrRoomId`）做房间侧管控；客户端实际音视频传输为 TRTC SDK 协议（非本仓库实现）。
  - **会议房间**：业务层房间号与状态由本服务持久化/编排，与 TRTC 房间 ID 的映射关系见 `conference` 与 `third/tencent` 实现细节。

## 数据与配置

- **数据库名**：`cube_biz_fusioncommunicate`（`server_info`、`application.yaml` 中 JDBC URL 一致）。
- **spring.application.name**：`fusionCommunicate`（`server/resources/bootstrap.yaml`）。
- **端口**：`server/resources/application.yaml` 中 `server.port` 引用 `common-env.fusion-communicate.port`，默认 `10222`（以环境为准）。
- **Liquibase**：`spring.liquibase.enabled: false`；`change-log` 仍指向 `db.com.guhecloud.cube.biz.fusion.communicate/changelog-master.xml`；是否在其他环境启用**待团队补充**。
- **配置中心**：大量运行时配置来自 Nacos（`common-env`、`common-datasource`、`common-security-*` 等），本地需可用的 Nacos/数据库环境。

## 深度阅读

- [references/external-api.md](./references/external-api.md)（Bazel 对外标签、HTTP 路径族、MQ、融合协议摘要）
- [references/overview.md](./references/overview.md)（目录树、BUILD 目标、本地运行命令）
