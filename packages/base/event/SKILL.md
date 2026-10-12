---
name: cube2-base-event
description: >
  Cube2 base-event（eventSync）：业务侧事件同步服务，消费触发 Topic、落库并异步再投递；
  与 platform/event（事件总线/启动信号等基础设施库）区分。触发词：packages/base/event、
  eventSync、业务事件同步、cube_base_event、BUSINESS_DATA_EVENT_TRIGGER。
---

# base/event

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **本模块（`packages/base/event`）**：面向 **业务事件数据的同步管道**：从 RocketMQ 摄入「业务数据事件触发」消息、写入 MySQL（`cube_base_event`），在消费者模式下由后台线程按批读取未处理记录并 **按记录中的目标 Topic 动态再发送**；使用 Redis 记录消费游标。配置项 `event.producer` 用于定义当前实例是否作为**生产者**（写入并落库上游消息）；`event.consumer` 用于定义当前实例是否作为**消费者**（启动再投递线程读取未处理记录并动态发送至目标 Topic）。网关注册前缀为 **`/event/`**（见 `server_info`）。
- **`packages/platform/event`（勿混淆）**：平台层 **事件基础设施与通用能力**（如 `@EventBusListen`、`SysReadEvent` 等），被本服务作为 **依赖库** 引用，用于在系统就绪后启动消费线程；**不是**本业务服务的替代实现。
- **不负责**：平台 RocketMQ 客户端封装本身（`packages/platform/rocketmq`）、统一安全与 Nacos 配置细节；本仓库目录下 **未发现自研 `@RestController`**，对外 HTTP 契约以网关与平台通用端点为准（若有）。

## 子模块分流

| 路径 / 标签 | 职责 |
|-------------|------|
| `variables.bzl` | `VERSION`、`PACKAGE_GROUP`（Maven 坐标分组）。 |
| `server/BUILD.bazel` | `deps`、`server`、`server-bin`、`server-exec`、`package`、`server-info`。 |
| `server/src/config` | `event.*` 配置绑定（`EventProperties`）。 |
| `server/src/producer/rocketmq` | 上游消息模型与 **RocketMQ 监听消费**（`BusinessEventConsumer`）。 |
| `server/src/producer/repository` | 业务事件落库（MyBatis-Plus，`BusinessEventRecord` → 表 `event_record`）。 |
| `server/src/consumer/publisher` | 再投递抽象与 `RocketMqPublisher`（`RocketMQContextHolder.send`）。 |
| `server/src/consumer/repostiory` | 下游消费进度实体/Mapper/Service（包名拼写为代码现状 `repostiory`）。 |
| `server/src/consumer/work` | 后台线程 `EventRecordConsumerThread` 与启动/销毁（`EventConsumerWorkConfig`，依赖 platform/event 的 EventBus）。 |

模块根目录 `BUILD.bazel` 当前无 Java 目标；构建入口在 `server/`。

## 快速定位清单

1. **网关前缀、库名、server_id**：`server/BUILD.bazel` → `server_info`（`gw_prefix = "/event/"`，`database = "cube_base_event"`，`server_id = "eventSync"`）。
2. **应用名**：`server/resources/bootstrap.yaml` → `spring.application.name: eventSync`。
3. **上游 RocketMQ 消费**：`server/src/producer/rocketmq/consumer/BusinessEventConsumer.java`（Topic 常量见 `BusinessEventMessage.TOPIC`）。
4. **下游再投递 Topic**：`EventRecordConsumerThread` 中 `eventPublisher.publish(businessEventRecord.getTopic(), …)`，Topic **来自库表字段**，非单一常量。
5. **生产者/消费者开关**：`EventProperties`（`event.producer` 定义是否为生产者、`event.consumer` 定义是否为消费者）；对应 `BusinessEventConsumer`、`EventConsumerWorkConfig` 中分支逻辑。
6. **数据源与 Liquibase**：`server/resources/application.yaml`（JDBC URL、Liquibase 开关）；Liquibase Bean 名见 `EventConfiguration`（`@Bean("parking")`）。

## 构建与入口

- **库与可执行**：`//packages/base/event/server:server`；`//packages/base/event/server:server-bin`（`java_binary`）；`//packages/base/event/server:server-exec`（`springboot` fat-jar）。
- **发布**：`//packages/base/event/server:package`（`artifactId = cube-biz-event`，`PACKAGE_GROUP` 见 `variables.bzl`）。
- **部署元数据**：`//packages/base/event/server:server-info`。
- **主类**：`com.guhecloud.cube.base.event.server.EventServer`。

## 对外契约

- **HTTP**：网关层前缀 **`/event/`**（`server_info`）。应用源码中 **无自研 REST Controller**；若需完整对外 API 列表，待团队补充（含平台统一端点/Actuator 等）。
- **RocketMQ**：
  - **订阅（固定）**：Topic **`BUSINESS_DATA_EVENT_TRIGGER`**，ConsumerGroup **`BUSINESS_EVENT`**，`selectorType = TAG`（代码中未给出 `selectorExpression`，具体 Tag 规则待团队补充）。
  - **发送（动态）**：再投递时使用消息体内的 **目标 Topic**（持久化字段 `BusinessEventRecord.topic`），无单一常量；合法 Topic 集合待团队补充。

## 数据与配置

- **数据库名**：`cube_base_event`（`server_info` 与 `application.yaml` 中 JDBC URL 一致）。
- **spring.application.name**：`eventSync`（`bootstrap.yaml`）。
- **端口**：`application.yaml` 中 `server.port` 引用 `common-env.event.port`，默认占位 **`10223`**（以 Nacos 为准）。
- **其他**：`event.producer` / `event.consumer`、Redis key `LAST_CONSUMER_BUSINESS_EVENT_ID` 等行为见源码与运行环境配置。

## 深度阅读

- [references/external-api.md](./references/external-api.md)（Bazel 标签、路由前缀、MQ）
- [references/overview.md](./references/overview.md)（目录树、目标、本地运行命令）
