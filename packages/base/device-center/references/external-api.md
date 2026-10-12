# device-center — 外部消费者视角

本文汇总 **可被其它仓库/服务引用的 Bazel 标签** 与 **契约线索**（HTTP 前缀、Feign、MQ）。未在 `BUILD.bazel` 出现的 Maven 发布目标标为 **待团队补充**。

## 对外 Bazel 标签（依赖与发布）

| 模块路径 | 库目标 | `java_export` / `export`（`:package`） |
|----------|--------|--------------------------------------|
| `//packages/base/device-center/client` | `:client` | `:package`（`artifactId = cube-base-device-center-client`） |
| `//packages/base/device-center/metadata` | `:metadata` | `:package`（`cube-base-device-center-metadata`） |
| `//packages/base/device-center/server` | `:server` | `:package`（`cube-base-device-center-server`） |
| `//packages/base/device-center/supervise/client` | `:client` | `:package`（`cube-srv-gen-device-supervise-client`） |
| `//packages/base/device-center/supervise/server` | `:server` | `:package`（`cube-srv-gen-device-supervise-server`） |
| `//packages/base/device-center/north/client` | `:client` | **无** `:package`（待团队补充是否仅源码依赖） |
| `//packages/base/device-center/north/consumer` | `:consumer` | **无** `:package`（对外文档 Maven 名为 `device-center-north-consumer`，与 Bazel 对齐待确认） |
| `//packages/base/device-center/product-model` | `:server` / `:product-model` | **无** `:package`（BUILD 未声明 export） |

可执行/镜像构建（运维或本地）：`server:server-exec`、`supervise/server:server-exec`（`springboot`）；`product-model` 当前仅有 `:server-bin`（无 `server-exec`）。

## HTTP 接口路由前缀（网关层）

来自各包 `server_info` 与架构文档的 **粗粒度前缀**（实际 Controller 路径需拼接）：

| 服务 | 网关前缀（`server_info.gw_prefix` 或文档） |
|------|---------------------------------------------|
| 设备中心主服务 | `/device-center/` **与** `/iot/`（并存） |
| 物模型 v3 | `/product-model/` |
| 设备监控/台账 | `/device-center/supervise/` |

`metadata.lang.Const` 中 `DEVICE_CENTER_MGT_FRONTEND_SUFFIX` 等当前为空串，管理端路径以前端网关与 Controller 为准。

## Feign Client — 消费 device-center（其它微服务 → 本域）

- **注册服务名 / Feign name**：`com.guhecloud.cube.base.device.center.metadata.lang.Const.CLIENT_NAME` = **`iotDeviceCenter`**。
- **标签**：`//packages/base/device-center/client:client`

主要接口类（包 `com.guhecloud.cube.base.device.center` 及子包）：

| 接口 | 说明 |
|------|------|
| `DeviceCenterClient` | 旧版聚合接口（`@Deprecated`） |
| `DeviceCenterStdClient` | 标准设备查询 |
| `DeviceGroupStdClient` | 设备分组 |
| `DeviceCenterTagStdClient` | 标签 |
| `DeviceCenterAttrCtlStdClient` | 属性控制 |
| `DeviceCenterAttrCmdLogClient` | 属性指令日志 |
| `DeviceCenterAssociatedStdClient` | 设备关联 |
| `DeviceBimSceneStdClient` | BIM 场景 |
| `ProductClient` / `ProductCategoryClient` | 产品/品类 |
| `DeviceBaseStatisticsClient` | 统计 |

**设备监控服务**（独立注册名）：

| 接口 | Feign `name` | 标签 |
|------|--------------|------|
| `HealthValueClient` | `ServiceRoute.TAG` = **`deviceSupervise`** | `//packages/base/device-center/supervise/client:client` |

## Feign Client — device-center 调用 IoT 南向（本域 → IoT 平台）

- **标签**：`//packages/base/device-center/north/client:client`
- **Feign name**：`Profile.CLIENT_NAME`（同 **`iotDeviceCenter`**，与设备中心 Feign 名一致；若生产环境分离注册，**待团队补充** Nacos 服务名映射）

| 接口类 | 职责（简述） |
|--------|----------------|
| `IotDeviceClient` | 设备分页/详情/添加等 |
| `IotDevicePropClient` | 属性读写、历史 |
| `IotDeviceEventClient` / `IotDeviceEventExtClient` | 事件查询 |
| `IotCommandClient` | 指令下发与状态 |
| `IotProductClient` / `IotProductMethodClient` | 产品与物模型方法 |
| `IotSubscribeClient` | 订阅 |
| `IotLinkageClient` | 联动规则 |

## 北向 / 南向（语义）

- **南向**：IoT 运行时 / 平台（文档中称 PJing 等），经 **HTTP/Feign** 由 `north/client` 访问。
- **北向（对外系统集成）**：面向第三方业务系统的 **RabbitMQ 消息订阅**，使用 **`north/consumer`** 提供的 Spring AMQP 组件与 `Consumer-SDK-Development-Guide.md` 中的 API（非 RocketMQ Topic 列表模型）。

## MQ — RocketMQ Topic（代码常量）

以下为源码中出现的 **Topic 字符串**（完整 Tag 语义需结合发送端/消费者）：

| Topic 常量 | 说明来源 |
|------------|----------|
| `DEVICE_CENTER_DEVICE_CHANGE_TOPIC` | `metadata.Const`；标签规则消费 `TAG_RULE_DEVICE_CHANGE` |
| `DEVICE_CENTER_PRODUCT_CHANGE_TOPIC` | `metadata.Const` |
| `IOT_INNER_DATA_CHANGE` | `metadata.Const`；消费组 `DEVICE_CENTER_INNER_DATA` |
| `IOT_INNER_DATA_CHANGE_PROXY` | `mgt` `RocketConstToApps`；消费组 `DEVICE_CENTER_INNER_DATA_PROXY` |
| `TENANT_INIT` | `mgt` `RocketMqConst.TENANT_INIT_TOPIC`；租户初始化字典 |
| `DEVICE_LIFE_EVENT` / `DEVICE_LIFE_EVENT_DEL` | supervise client message 类 |
| `COMMAND_EXECUTE_CALLBACK` | `CommandExecuteCallbackEvent.TOPIC` |
| `DEVICE_GROUP_CHANGE` | `DeviceGroupChange.TOPIC` |
| `DEVICE_TAG_RULE_CHANGE` | `DeviceTagRuleChange.TOPIC` |
| `DeviceChangeEventMessage.TOPIC` | 等同设备变更 Topic，用于场景消费者 |

`mgt` 中另有南向运行时相关发送常量（如 `TO_RUNTIME_*`），详见 `RocketConstToApps` / `SouthInnerBusImpl`（**待团队补充**对外是否稳定契约）。

## MQ — RabbitMQ（北向 SDK）

不涉及统一 Topic/Tag 表；请参阅 [Consumer-SDK-Development-Guide.md](../Consumer-SDK-Development-Guide.md)。
