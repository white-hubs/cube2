# platform/event 综述

- 常随 `job`、`rocketmq` 等一并出现在服务依赖列表。

## 模块综述与边界

本模块提供基于 **Guava EventBus** 的进程内事件总线能力：通过 `EventPublisher` 发布/注册、`@EventBusListen` 声明订阅者、`InnerEvent` 维护默认总线标识，并与 Spring（`EventBusConfig`、`EventBusPostProcessor`）集成，用于**同一进程内**模块间松耦合通信。

**不负责**：跨进程/跨服务的消息投递、业务事件落库与持久化（见 `packages/base/event` 等业务事件能力）；也不替代 RocketMQ 等中间件。若需分布式或审计级事件流，应在对应业务或消息模块实现。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

物理目录（`src/` 下两层）与用途：

| 路径 | 说明 |
|------|------|
| `src/`（根包 `com.guhecloud.platform.event.src`） | `EventPublisher` 发布接口 |
| `src/annotation/` | `@EventBusListen` 等监听相关注解 |
| `src/common/` | `InnerEvent`（内部事件总线标识注册）、`SysReadEvent` 等公共事件类型 |
| `src/config/` | Spring 配置：`EventBusConfig`（`EventPublisher` Bean）、`EventBusPostProcessor`（扫描并注册 `@EventBusListen`） |
| `src/entity/` | `EventIdentity` 等事件身份模型 |
| `src/enumeration/` | `InnerEventEnum`、`PublisherTypeEnum` 等枚举 |
| `src/guava/` | `GuavaEventPublisher` 实现 |
| `src/guava/utils/` | Guava EventBus 工具（如创建 `EventBus`） |

## BUILD target 说明

| Target | 类型 | 用途 |
|--------|------|------|
| `//packages/platform/event:event` | `java_library` | 本模块主库；服务通过 `deps` 引用 |
| `//packages/platform/event:export` | `java_export` | Maven 坐标发布（`platform-event`），供外部或 Gradle/Maven 消费 |
| `//packages/platform/event:bom` | `maven_bom` | 与 `export` 配套的 BOM 坐标 |

本模块无 `java_binary` / 可执行入口；本地运行见下节。

## 本地运行

本模块为纯库，不独立运行。接入方在自身 Spring Boot 服务中依赖 `:event` 或发布的 Maven 构件后随应用进程加载。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

| Bazel label | 说明 |
|-------------|------|
| `//packages/platform/base` | 平台基础能力（本模块构建链所需） |
| `//packages/platform/base/utils:collection` | 集合相关工具 |
| `//packages/platform/base/utils:string` | 字符串工具 |
| `//third-party/java:lombok` | Lombok（如日志等） |
| `//third-party/java/google:guava` | Guava（`EventBus` 等）；本库同时 **exports** 该依赖，供使用方直接使用 Guava 类型 |
| `//third-party/java/slf4j` | 日志门面 |
| `//third-party/java/spring:spring_boot` | Spring Boot 集成（配置、事务同步等） |
| `//third-party/java/spring:springframework` | Spring Framework（如 `BeanPostProcessor`、事务 API） |

## 常见开发场景

1. **新增一类进程内事件并在监听方处理**  
   定义事件 POJO → 在发布侧注入 `EventPublisher`，按 `EventIdentity`（与 `InnerEvent`/`InnerEventEnum` 约定一致或扩展注册）调用 `publish` / `transactionPublish` → 在监听类上标注 `@EventBusListen`（可选 `key`、`publisherType`、`order`），在 Guava 约定方法上接收事件。涉及文件通常在业务模块；本库内扩展总线标识时需改 `InnerEvent`/`InnerEventEnum` 等（待团队补充是否允许新增总线 key 的流程）。

2. **事务提交后再发布**  
   使用 `transactionPublish`；实现依赖 Spring 事务同步（见 `GuavaEventPublisher`）。需保证调用处于 Spring 管理的事务中，否则行为以代码为准。

3. **排查监听未生效**  
   确认 Spring 扫描到 `@EventBusListen` 类、`EventBusPostProcessor` 与 `EventBusConfig` 已加载；核对 `key`/`publisherType` 与发布侧 `EventIdentity` 一致。详细排障步骤待团队补充。

## 注意事项

- 与 **`packages/base/event`** 区分：本模块是 **进程内** Guava EventBus，不是业务事件持久化或对外消息 topic。
- 订阅者注册顺序受 `@EventBusListen.order` 影响；异步/同步行为由 `PublisherTypeEnum` 等与 Guava 工具实现共同决定，升级 Guava 或改动工具类前需回归。
- 无应用级 `application.yml` 约定由本库单独维护；若团队有统一开关或 Nacos 项，待团队补充。
