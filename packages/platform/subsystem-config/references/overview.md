# subsystem-config 综述

- 依赖 `//packages/platform/base`、`//packages/platform/rocketmq` 等；与 `subsystem-requester-sdk` 配套使用常见。

## 模块综述与边界

本模块为 **子系统配置库**：提供子系统端点/适配相关的配置模型、请求上下文（ThreadLocal）、Web/MQ 监听与 Redis 等辅助能力，供业务应用通过 Maven/Bazel 依赖引入。文档开头已列出典型依赖与配套关系。

- **不负责**：各业务子系统的具体配置项语义、持久化与运维策略；不负责替代 `subsystem-requester-sdk` 的客户端职责（二者常配套使用）。更细的边界见模块根 [SKILL.md](../SKILL.md)。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

顶层目录：

| 路径 | 说明 |
|------|------|
| `references/` | 模块开发手册（含本文） |
| `src/` | Java 源码根（本模块 BUILD 使用 `glob(["src/**/*.java"])`，未采用 `src/main/java` 布局） |

`src/` 下按包根 `com.guhecloud.subsystem.config` 展开（列至下 2 层包名，用途说明）：

| 包路径（相对 `...subsystem.config`） | 用途 |
|----------------------------------------|------|
| `constant/` | 子系统类型等常量 |
| `context/` | 子系统请求上下文 Holder（如 `SubsystemContextHolder`） |
| `redis/` | Redis 相关工具 |
| `util/` | 通用工具方法 |
| `listener.web` | HTTP 常量、请求模型、Web 侧 Listener |
| `listener.web.model` / `listener.web.http` | 请求方模型与 HTTP 常量 |
| `listener.mq` | RocketMQ 消费与初始化消息类型 |
| `listener.nacos.entity` / `listener.nacos.enumeration` | Nacos 侧端点/适配器配置实体与枚举 |
| `listener.message` | 子系统端点消息模型 |

## BUILD target 说明

| Target（Bazel label） | 用途 |
|------------------------|------|
| `//packages/platform/subsystem-config:subsystem-config` | 主 `java_library`，对外可见性为 `//packages:__subpackages__` |
| `//packages/platform/subsystem-config:export` | `java_export`，发布 Maven 坐标（`artifactId` = `subsystem-config`） |
| `//packages/platform/subsystem-config:bom` | `maven_bom`，聚合本模块 export 的 BOM |

本模块无 `bazel run` 型可执行目标；消费方通过依赖 `:subsystem-config` 或 Maven 坐标引入。

## 本地运行

本模块为纯库，不独立运行。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

以下为 `BUILD.bazel` 中 `java_library` 声明的主要依赖（每条 label 与能力说明；第三方以组概括）：

| 依赖 | 说明 |
|------|------|
| `//packages/platform/base` | 平台基础能力 |
| `//packages/platform/rocketmq` | RocketMQ 封装 |
| `//packages/platform/base/utils:object` / `:string` / `:map` / `:json` | 对象、字符串、Map、JSON 工具 |
| `//third-party/java/slf4j` | 日志 API |
| `//third-party/java/google:guava` | Guava 工具库 |
| `//third-party/java/aspectj:aspectjweaver` | AspectJ 织入 |
| `//third-party/java/jackson` | JSON 绑定 |
| `//third-party/java/spring:springframework` / `spring_boot` / `spring_data` | Spring 核心、Boot、Data |
| `//third-party/java:lombok` | 样板代码生成 |
| `//third-party/java/apache:tomcat` | Servlet/Tomcat 相关 API（编译期依赖用途以源码为准） |
| `//third-party/java/apache:rocketmq` | RocketMQ 客户端 |

## 常见开发场景

1. **调整 Nacos 端点/适配器配置结构**  
   修改 `src/listener/nacos/entity/`、`enumeration/` 下实体与枚举；注意与消费方序列化约定一致。涉及文件示例：`SubsystemEndpointConfig.java`、`SubsystemAdapterConfig.java`、`EndpointType.java`。

2. **调整 HTTP 请求上下文或 Web 监听行为**  
   修改 `src/listener/web/`（含 `model/`、`http/`）与 `SubsystemRequestContextListener.java`；上下文存取与 `src/context/SubsystemContextHolder.java` 配合。

3. **调整 MQ 订阅或端点消息模型**  
   修改 `src/listener/mq/`（如 `SubsystemEndpointConsumer.java`、`SubscribeInitMessage.java`）及 `src/listener/message/SubsystemEndpointMessage.java`。

## 注意事项

- **线程上下文**：`SubsystemContextHolder` 使用 `ThreadLocal`，调用链上须在合适时机清理（如 Filter/Listener 中），避免线程池场景泄漏；细节待团队补充。
- **与 SDK 配套**：与 `subsystem-requester-sdk` 配套时，版本与配置前缀以团队发布说明为准；冲突时待团队补充。
- **生成类与禁止手改**：本模块未在 overview 中登记代码生成产物路径；若后续引入生成代码，应在此补充「禁止手改」路径。当前以源码为准。
