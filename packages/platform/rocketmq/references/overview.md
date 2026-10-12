# platform/rocketmq 综述

- 与 `subsystem-config`、`subsystem-requester-sdk` 存在依赖关系；排查链路时可 `bazel query` 或 deps-server。

## 模块综述与边界

本模块为平台层 **RocketMQ 集成库**：在 Spring Boot 下提供 RocketMQ 消息转换（含 JWT 与 `Requester` 包装）、事务消息相关接口与 `RocketMQContextHolder` 发送封装，以及消息环境（如 profile）配置。**不负责** Topic 命名规范、业务消息体定义、消费者组在各应用中的注册策略——这些由业务模块与团队约定文档承担；与配置中心、请求方模型的联动通过 `platform/base` 等依赖间接体现，完整调用链可配合 `subsystem-config`、`subsystem-requester-sdk` 用 `bazel query` 或 deps-server 排查。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

```text
packages/platform/rocketmq/
├── BUILD.bazel              # java_library、java_export、maven_bom
├── resources/               # 客户端日志等资源（如 rmq.client.logback.xml）
├── references/
│   └── overview.md          # 本文档
└── src/
    ├── config/              # RocketMQ 与消息环境 Spring 配置（RocketMQConfigurer、MessageEnvironment 等）
    ├── context/             # RocketMQContextHolder：模板发送、事务监听与回调编排
    ├── converter/           # MessageWrapper、Wrapped* 消息转换与包装
    └── producer/            # 事务消息 Handler / Executor / Checker 接口与契约
```

`src/` 下 Java 包为 `com.guhecloud.platform.rocketmq.<子包>`，与上述目录一一对应。

## BUILD target 说明

| Target | Bazel label | 用途 |
|--------|-------------|------|
| `rocketmq` | `//packages/platform/rocketmq:rocketmq` | 主库：聚合 `src/**/*.java` 与 `resources/**`，并 re-export Apache RocketMQ 依赖供依赖方直接使用同一 API。 |
| `export` | `//packages/platform/rocketmq:export` | `java_export`：发布 Maven 坐标 `…:platform-rocketmq:<PLATFORM_VERSION>`（`tags` 含 `no-javadocs`）。 |
| `bom` | `//packages/platform/rocketmq:bom` | `maven_bom`：对齐同一版本的平台 RocketMQ 制品与 BOM 坐标。 |

本模块无 `java_binary` / 可执行入口；消费方应用通过依赖 `:rocketmq` 或发布的 `export` 使用。

## 本地运行

本模块为纯库，不独立运行。联调与验证在引用本库的应用进程中进行（待团队补充具体应用与配置）。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

| Bazel label | 说明 |
|-------------|------|
| `//packages/platform/base` | 应用上下文、工具类等平台基础能力。 |
| `//packages/platform/base/model:requester` | `Requester` 等与消息包装字段对齐。 |
| `//packages/platform/base/standard:jackson` | Jackson 相关标准封装。 |
| `//packages/platform/base/utils:collection` / `:json` / `:map` / `:string` | 集合、JSON、Map、字符串工具。 |
| `//packages/platform/security` | 与 JWT 等安全组件协作（如 `RocketMQConfigurer` 注入 `JwtDecoder`）。 |
| `//third-party/java/apache:rocketmq` | Apache RocketMQ 客户端与 Spring 集成（本 target 同时 `exports` 给下游）。 |
| `//third-party/java:lombok` | Lombok 注解。 |
| `//third-party/java/aspectj:aspectjweaver` | AspectJ 编织（与 Spring/AOP 生态一致）。 |
| `//third-party/java/google:guava` | Guava 工具（如 `Maps`）。 |
| `//third-party/java/jackson` | Jackson 核心。 |
| `//third-party/java/slf4j` | 日志门面。 |
| `//third-party/java/spring:spring_boot` / `:spring_boot_autoconfigure` | Spring Boot 与自动配置。 |
| `//third-party/java/spring:spring_security` | Spring Security（如 OAuth2 JWT）。 |
| `//third-party/java/spring:springframework` | Spring Framework 核心。 |

## 常见开发场景

1. **调整消息体与请求方上下文一起序列化**  
   - 关注 `converter/` 下 `MessageWrapper`、`WrappedRocketMQMessageConverter` 等，以及 `config/RocketMQConfigurer` 中 `WrappedRocketMQMessageConverter` 的装配；确认与 `Requester`、JWT 行为一致。

2. **扩展或实现事务消息处理**  
   - 在业务模块实现 `producer/` 中的 `RocketMQTransactionHandler`、`RocketMQTransactionExecutor`、`RocketMQTransactionChecker` 等接口，并通过 `context/RocketMQContextHolder` 与 RocketMQ 事务监听协作；Topic、Payload 类型在实现类中声明。

3. **按环境区分消息行为**  
   - 查看 `config/MessageEnvironment`、`MessageEnvironmentConfig` 等与 profile/环境相关的配置与使用点。

## 注意事项

- 本库在 `RocketMQConfigurer` 静态块中设置 `mq.consoleAppender.enabled`；与日志、运维规范冲突时待团队补充统一约定。
- `java_export` 带 `no-javadocs`，发布物 Javadoc 行为以构建配置为准。
- 与 `subsystem-config`、`subsystem-requester-sdk` 的依赖与运行时顺序以各应用装配为准；变更消息格式或安全相关字段时需跨模块回归。
- Topic、消费组、重试与死信等运维与治理策略不在本库内硬编码，由业务与基础设施约定维护。
