---
name: cube2-base-device-center
description: >
  Cube2 IoT device-center domain (设备中心 / device center): device instances,
  product model, north–south IoT bridge, supervision/ledger, shared metadata and
  outbound Feign clients. Use for packages/base/device-center, 设备中心,
  物模型, 北向/南向, DeviceCenterServer, product-model, supervise, or IoT
  integration in this monorepo.
---

# device-center

术语见 [仓库词汇表](../../../.agents/skills/cube2-repo/references/glossary.md)（**device-center**：设备中心业务域）。原则见 [cube2-repo principles](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**（整体）：Cube2 **设备中心（device-center）** 域——设备实例与标签/分组/规则、物模型（旧版 `mgt` 与新版 `product-model`）、设备台账与健康/生命周期（`supervise`）、经 **north** 与 IoT 南向运行时/平台的桥接、对其它微服务的 **Feign 契约（`client`）**，以及跨包共享的 **DTO/事件常量（`metadata`）**。
- **不负责**：统一账号（**ouaa**）、通用平台中间件实现（见 `packages/platform/*`）；具体网关路由映射以部署/Nacos 为准，本 skill 仅汇总 `server_info` 与代码中的可核实项。
- **多服务形态**：`server`、`product-model`、`supervise` 为独立 Spring Boot 进程；`mgt` 以库形式并入 `server` 依赖，另提供 `mgt/server` 仅用于本地单独拉起旧版物模型服务测试。

## 子模块分流

| 子目录 | 职责摘要 | 主要 Bazel 标签（路径均为 `//packages/base/device-center/...`） |
|--------|----------|------------------------------------------------------------------|
| **access/** | 接入侧 DB 初始化资源（`access/init` 将 SQL 打包给 `deploy/database` 消费）；根目录 `BUILD.bazel` 当前为空文件。 | `//packages/base/device-center/access/init:init-mysql` |
| **client/** | 对其它微服务暴露的 **OpenFeign** 接口（设备查询、标签、分组、统计等），依赖 `metadata`。 | `:client`（库）、`:package`（`java_export` 发布） |
| **metadata/** | **无进程**：共享 `Const`（含 `CLIENT_NAME`、MQ Topic 名）、DTO/VO/Param、领域事件类型等。 | `:metadata`、`:package` |
| **mgt/** | 物模型与设备管理 **旧版实现**（things/device/north/transfer 等），作为 `java_library` 被 `server` 依赖；含 RocketMQ 消费（如租户字典、内部数据代理）。 | `:mgt`、`:serializer-tests` |
| **mgt/server/** | 仅本地/测试：独立启动 `IotApplication` 的瘦入口。 | `:server`（`java_library`）、`:server-bin` |
| **north/** | 聚合目录；**北向桥接**拆在 `north/client`（调用 IoT 南向平台的 Feign）与 `north/consumer`（基于 **RabbitMQ/Spring AMQP** 的订阅 SDK 实现，见对外开发指南）。根 `BUILD.bazel` 无目标。 | `north/client:client`、`north/client:serializer-tests`；`north/consumer:consumer`、`north/consumer:filter-tests` |
| **product-model/** | 物模型 **v3** 独立服务（`ProductModelApplication`），Liquibase + 独立库 `cube_base_iot_product_model`。 | `:deps`、`:server`、`:product-model`（v3 子集库）、`:server-bin`、`:server-info`；**注意**：当前 `BUILD.bazel` **未**声明 `springboot` / `export`（与 `server`、`supervise` 不同）。 |
| **server/** | **主服务**：设备实例、标签规则、场景、统计、可视化等；`DeviceCenterServer`；DB `cube_base_iot_center`。 | `:server`、`:server-bin`、`:server-exec`（`springboot`）、`:package`、`:server-info` |
| **supervise/** | **设备监控/台账** 独立服务（`DeviceSuperviseServer`）；RocketMQ 生命周期消息；对外 Feign `HealthValueClient` 等。 | `supervise/server:server`、`supervise/server:server-bin`、`supervise/server:server-exec`、`supervise/server:package`、`supervise/server:server-info`；`supervise/client:client`、`supervise/client:package` |

## 快速定位清单

1. **主 HTTP 设备实例 API**：`server/src/core/controller/*`，网关前缀见 `server` 的 `server_info`（`/device-center/`、`/iot/`）及 `metadata.lang.Const` 中的前后缀常量（多为空串，实际路径以前缀拼接为准）。
2. **物模型 v3 HTTP**：`product-model/src/v3/controller/`，前缀模式 `/v3/things/*`（见 `Device Center Architecture.md`）。
3. **旧版物模型与设备管理（随主服务打包）**：`mgt/src/things/`、`mgt/src/device/`、`mgt/src/base/`。
4. **调用 IoT 南向平台（Feign）**：`north/client/src/Iot*.java`，注册名使用 `Profile.CLIENT_NAME`（同 `metadata.Const.CLIENT_NAME`，当前为 `iotDeviceCenter`）。
5. **对外 Feign（其它服务调设备中心）**：`client/src`，标准入口在 `std/devcenter/*`（`DeviceCenterClient` 已 `@Deprecated`）。
6. **RocketMQ Topic 名**：优先查 `metadata/src/lang/Const.java` 与各 `metadata.event.*` / `DeviceLifeEventMessage` 等类中的 `TOPIC` 常量。
7. **北向推送给外部系统（RabbitMQ）**：读 `Consumer-SDK-Development-Guide.md` 与 `north/consumer` 源码（非 RocketMQ Topic 模型）。

## 构建与入口

| 角色 | 入口类（包名核实自源码） | 典型 Bazel 目标 |
|------|--------------------------|-----------------|
| 设备中心主服务 | `com.guhecloud.cube.base.device.center.DeviceCenterServer` | `//packages/base/device-center/server:server-bin` 或 `:server-exec` |
| 物模型 v3 | `com.guhecloud.cube.base.iot.product.model.ProductModelApplication` | `//packages/base/device-center/product-model:server-bin` |
| 设备监控 | `com.guhecloud.cube.srv.gen.device.supervise.server.DeviceSuperviseServer` | `//packages/base/device-center/supervise/server:server-bin` 或 `:server-exec` |
| 旧版 IoT 管理（单独测） | `com.guhecloud.cube.iot.mgt.IotApplication` | `//packages/base/device-center/mgt/server:server-bin` |

- **Maven 坐标**：根 `variables.bzl` 中 `PACKAGE_GROUP = "com.guhecloud.cube.base.device.center"`、`VERSION` 与各子包 `export` 目标artifactId 以对应 `BUILD.bazel` 为准（`product-model`、`north/client` 等若未声明 `export`，对外坐标待团队补充）。

## 对外契约

- **HTTP 网关前缀**（`server_info`）：  
  - 主服务：`gw_prefix = "/device-center/,/iot/"`（`//packages/base/device-center/server:server-info`）  
  - 物模型 v3：`/product-model/`（`product-model:server-info`）  
  - 台账/监控：`/device-center/supervise/`（`supervise/server:server-info`）  
  具体 Controller 级路径需结合代码中 `@RequestMapping` 与常量拼接。
- **Feign Client（消费 device-center）**：`metadata.Const.CLIENT_NAME = "iotDeviceCenter"`；接口见 `client/src`（含 `DeviceCenterStdClient`、`DeviceGroupStdClient`、`HealthValueClient` 在 supervise 子模块等）。Supervise Feign 使用 `ServiceRoute.TAG = "deviceSupervise"`。
- **Feign Client（device-center 调用 IoT 南向）**：`north/client` 中 `IotDeviceClient`、`IotDevicePropClient`、`IotCommandClient`、`IotProductClient`、`IotProductMethodClient`、`IotSubscribeClient`、`IotLinkageClient`、`IotDeviceEventClient`、`IotDeviceEventExtClient` 等，`@FeignClient(name = Profile.CLIENT_NAME)`。
- **RocketMQ**（代码中出现的 Topic 常量示例）：`DEVICE_CENTER_DEVICE_CHANGE_TOPIC`、`DEVICE_CENTER_PRODUCT_CHANGE_TOPIC`、`IOT_INNER_DATA_CHANGE`、`IOT_INNER_DATA_CHANGE_PROXY`（mgt 内定义）、`TENANT_INIT`、`DEVICE_LIFE_EVENT`、`DEVICE_LIFE_EVENT_DEL`、`COMMAND_EXECUTE_CALLBACK`、`DEVICE_GROUP_CHANGE`、`DEVICE_TAG_RULE_CHANGE` 等；**Tag 过滤**多处使用 `SelectorType.TAG`，具体 tag 字符串需下钻各 `onMessage` 或发送端。**设备变更场景**等另使用 `DeviceChangeEventMessage.TOPIC`（等于 `DEVICE_CHANGE_TOPIC`）。
- **RabbitMQ（北向对外 SDK）**：见 `Consumer-SDK-Development-Guide.md`（外部消费组、队列行为），Maven 文档坐标为 `device-center-north-consumer`；Bazel 侧 `north/consumer` **未**声明 `export` 目标。

## 数据与配置

| 服务 | 数据库（`application.yaml` 中 JDBC 路径） | `server_info` / 备注 |
|------|-------------------------------------------|------------------------|
| `server` | `cube_base_iot_center` | `server_id = "iotDeviceCenter"`；Liquibase changelog 路径见 `application.yaml` |
| `product-model` | `cube_base_iot_product_model` | `server_id = "productModel"` |
| `supervise` | `cube-srv-gen-device-supervise` | `server_id = "deviceSupervise"` |

- **配置来源**：各服务 `resources/application.yaml` 以 **Nacos** `spring.config.import` 为主；本地端口占位如 `common-env.device-center.port`（主服务默认 `10308`）、`device-center-product-model.port`（默认 `10319`）、`device-center-supervise.port`（默认 `10309`）。
- **`spring.application.name`**：仓库内 `application.yaml` **未**写死；运行时名以 Nacos/common-env 为准，或与 Feign `CLIENT_NAME`/`server_id` 对齐（**待团队补充**明确 YAML 键名）。

## 深度阅读

- [Device Center Architecture.md](./Device%20Center%20Architecture.md)（内部架构总览）
- [Consumer-SDK-Development-Guide.md](./Consumer-SDK-Development-Guide.md)（北向 RabbitMQ Consumer SDK）
- [references/overview.md](./references/overview.md)（目录树与入口类路径）
- [references/external-api.md](./references/external-api.md)（对外 Bazel 标签与契约汇总）
- `mgt/doc/downlink_command_logic.md`（下行命令逻辑说明）
