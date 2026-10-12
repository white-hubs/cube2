# packages/base/message 内部开发者视角

## 模块综述与边界

- **职责（一句话）**：`packages/base/message` 提供 Cube2 消息域能力——多通道通知（短信、邮件、站内、WebSocket 等）、租户侧 SAT 消息编排与记录、验证码；并包含独立的 **message2** 服务与 **bracing** 装配通知子服务，以及供他方集成的 **metadata / client / sdk**。
- **负责**：message2 侧通道/模板/记录/厂商与发送链路、`handler/` 多通道实现与 RocketMQ 消费、WebSocket 站内信；bracing 用于业务侧配置某场景的消息发送规则，是对 SDK 的再次封装，为业务模块提供自定义消息发送的便捷配置能力；共享枚举与 Feign/SDK 契约。
- **不负责**：统一认证实现（使用 `//packages/base/ouaa/client`）；网关与注册中心的完整路由清单（以 Nacos 与各环境 `common-env.yaml` 等为准）；非本仓库注册的其他消费者或外部推送平台运维。

## 当前分层架构

本模块 **现状** 采用典型 Spring Boot 分层，子域按包拆分（**现状，未统一重构**；与仓库「最佳实践」若有差异以代码为准）：

| 层次 / 概念 | message2（`server/src`） | bracing（`bracing/server/src`） |
|-------------|--------------------------|-----------------------------------|
| **接入** | `*/controller`、`bff/controller` | `controller` |
| **应用服务** | `*/service`、`initialization/` | `service`（若有） |
| **领域与数据** | `entity`、`dao`、MyBatis XML（`resources/mapper/**/*.xml`） | 实体与数据访问（待团队按包名核对） |
| **通道与集成** | `handler/`（各通道发送、RocketMQ、pending）、`factory/` | 与 message2 SDK/DB 协同 |
| **横切** | `config/`、`job/`、`base/rocketmq` 等 | Spring 配置与 Liquibase |

持久层目录名为 **`dao`**（非 `mapper` 子包）；XML 映射文件在 `resources/mapper/`，与包职责等价。

## 目录与包结构

以下为仓库内 **约两层** 子模块视图（再向下为 `src/`、`resources/` 等标准布局）：

```text
packages/base/message/
├── BUILD.bazel                 # 当前为空
├── variables.bzl               # VERSION、PACKAGE_GROUP（根模块版本元数据）
├── SKILL.md
├── references/
│   ├── external-api.md
│   └── overview.md
├── bracing/
│   ├── variables.bzl
│   ├── client/
│   │   ├── BUILD.bazel
│   │   └── src/                # Feign：AssemblyNoticeClient 等
│   └── server/
│       ├── BUILD.bazel
│       ├── resources/          # application.yaml 等
│       ├── init/               # init-db.mysql.sql → pkg_files
│       └── src/                # BracingServer、controller、…
├── client/
│   ├── BUILD.bazel
│   └── src/                    # Feign + DTO/Param/枚举
├── metadata/
│   ├── BUILD.bazel
│   ├── constant/
│   └── enums/                  # 如 ChannelEnum
├── sdk/
│   ├── BUILD.bazel
│   ├── variables.bzl           # SDK 独立版本号
│   └── src/                    # MessageSendClient、api/*、common/*
└── server/
    ├── BUILD.bazel
    ├── resources/              # application.yaml、mapper、Liquibase changelog
    ├── init/
    └── src/                    # Message2Server 与业务分包（见下）
```

### message2 `server/src` 分包（扫描摘要）

| 包/目录 | 角色 |
|---------|------|
| `base/controller`、`base/service`、`base/entity`、`base/dao`、`base/vo`、`base/param` | 核心领域：通道、模板、记录、厂商等 |
| `sat/controller`、`sat/service`、`sat/entity`、`sat/dao` | 租户 SAT 侧消息编排与发送 |
| `code/controller`、`code/service` | 短信验证码 |
| `bff/controller`、`bff/service` | BFF 聚合（如记录统计分页） |
| `handler/` | 通道发送实现（SMS/邮件/站内/WebSocket 等）、RocketMQ 消费者、pending 任务 |
| `initialization/` | 租户与通道初始化、RocketMQ 消息与 consumer |
| `config/`、`job/`、`factory/` | Spring 配置、定时任务、工厂 |
| `Message2Server.java` | Spring Boot 入口 |

说明：持久层目录名为 `dao`，MyBatis XML 位于 `resources/mapper/**/*.xml`（非 `mapper` 子包名，但职责等同数据访问层）。

### 各子模块额外说明

- **`metadata/`**：`enums/`（如 `ChannelEnum`）、`constant/`（模板参数键、Redis 等常量）；Bazel 目标 `//packages/base/message/metadata:metadata`。
- **`client/src/`**：`dto/`、`enumeration/`、`param/` 等，Feign 接口与对外参数模型。
- **`sdk/src/`**：`api/`（按 channel、model、request、response 等分子包）、`common/`（注解、异常等）；对外封装入口见 `MessageSendClient` 等类。
- **`bracing/client/src`**：Feign（如装配通知相关 Client），见该目录下 `*.java`。
- **初始化 SQL**：`server/init/`、`bracing/server/init/` 通过 `pkg_files` 供 deploy 数据库侧使用（文件名见各 `BUILD.bazel`）。

### 开发入口

- **message2**：从 `server/src/Message2Server.java` 与各 `*Controller` 入手；发送链路经 `handler/` 与 `sat/process/pipeline`。
- **SDK**：从 `sdk/src/MessageSendClient.java`、`MessageSceneClient.java` 入手，向下跟踪到 `client` Feign。
- **装配 bracing**：`bracing/server/src/BracingServer.java`、`controller/MessageAssemblyNoticeController.java`。

## BUILD target 说明

**命名约定**（跨子模块通用）：

| 目标 | 含义 |
|------|------|
| `:deps`（server / sdk 私有） | 依赖聚合 `java_library`，供同目录 `server`/`sdk` 引用 |
| `:server` | 各服务子目录的**主 Java 库**（glob `src/**/*.java` + resources） |
| `:server-bin` | `java_binary`，`main_class` 指向对应 `*Server` |
| `:server-exec` | springboot 宏生成的可执行 Fat Jar 目标 |
| `:package` | `export` 宏，Maven 发布（artifactId 见各 BUILD） |
| `:server-info` | `server_info` 宏：数据库名、网关前缀、server_id、主类等 |
| `client` / `bracing/client` 的 `:export*` | `java_export`，bracing-client 使用 dev/test 坐标变体 |

下表按 **Bazel 包**列出本模块内常用目标（名称 + 用途）；私有 `deps` 库不对外引用：

| Bazel 包 | 目标 | 用途 |
|----------|------|------|
| `//packages/base/message/server` | `:server` | message2 主 Java 库（`src` + `resources`） |
| 同上 | `:server-bin` | 本地运行：`java_binary`，`main_class` 为 `Message2Server` |
| 同上 | `:server-exec` | Spring Boot 可执行（`springboot` 宏） |
| 同上 | `:package` | Maven 发布（`artifactId`：`message2-server`） |
| 同上 | `:server-info` | 部署元数据：库名 `cube_base_message2`、`gw_prefix` `/message2/`、`server_id` `message2` 等 |
| `//packages/base/message/server/init` | `:init-mysql` | 初始化 SQL 打包（`pkg_files`） |
| `//packages/base/message/client` | `:client` / `:package` | Feign 客户端库 / Maven 发布（`message2-client`） |
| `//packages/base/message/sdk` | `:sdk` / `:package` | SDK 库 / Maven 发布（`cube-base-message-sdk`） |
| `//packages/base/message/metadata` | `:metadata` | 共享枚举与常量库 |
| `//packages/base/message/bracing/server` | `:server`、`:server-bin`、`:server-exec`、`:package`、`:server-info` | bracing 服务；`server-info` 中库名 `cube_base_bracing`、`gw_prefix` `/bracing/`、`server_id` `messageBracing` |
| `//packages/base/message/bracing/server/init` | `:init-mysql` | bracing 库表初始化 SQL 打包 |
| `//packages/base/message/bracing/client` | `:client` | bracing Feign 实现库 |
| 同上 | `:export` / `:export-dev` / `:export-test` | Maven 坐标变体发布（`java_export`） |

根目录 `//packages/base/message:BUILD.bazel` 当前无目标（占位）。

## 本地运行

在仓库根目录执行（需已配置 Bazel 与 Java 工具链）：

```bash
# message2 直接运行（java_binary）
bazel run //packages/base/message/server:server-bin

# message2 Spring Boot 可执行目标（若流水线以该目标为准）
bazel run //packages/base/message/server:server-exec

# bracing
bazel run //packages/base/message/bracing/server:server-bin
bazel run //packages/base/message/bracing/server:server-exec
```

**环境与中间件前提（摘要）**：

- **Nacos**：`application.yaml` 通过 `spring.config.import` 拉取多份 `common-*.yaml`（地址 `NACOS_ADDR`、命名空间 `NACOS_NAMESPACE` 等）；本地需可连 Nacos 或按团队方式禁用/替换，**细节待团队补充**。
- **MySQL**：message2 默认 JDBC 库名 `cube_base_message2`；bracing 为 `cube_base_bracing`（亦见各 `server-info` 的 `database`）。
- **RocketMQ / Redis / 安全资源**：由 Nacos 中 `common-rocketmq.yaml`、`common-redis.yaml`、`common-security-*.yaml` 等提供；本地全链路启动前请与团队确认 Topic 与消费者组是否已创建。
- **Liquibase**：message2 的 `spring.liquibase.enabled` 在仓库 `application.yaml` 中为 `false`，是否由 Nacos 打开以 `common-env.liqui-base.execute` 等为准，**待团队补充**。

## 数据层

| 服务 | 数据库名（与 `server_info.database` 一致） | Liquibase / 迁移 |
|------|--------------------------------------------|------------------|
| message2 | `cube_base_message2` | 配置项 `spring.liquibase.change-log: db.com.guhecloud.cube.base.message2/changelog-master.xml`（`server/resources/application.yaml`）；**仓库内若未检出对应 `db/...` 目录，以构建产物或 Nacos 为准**，待团队补充 |
| bracing | `cube_base_bracing` | `db.com.guhecloud.cube.base.bracing/changelog-master.xml`；变更文件位于 `bracing/server/resources/db/com.guhecloud.cube.base.bracing/`（含 `changelog-*.mysql.sql`） |

**初始化 SQL**：`server/init/init-db.mysql.sql`、`bracing/server/init/` 下 SQL，经 `init:init-mysql` 供 `//packages/deploy/database` 使用。

**核心表 / 领域对象**：消息通道、模板、发送记录、SAT 场景与验证码等相关实体与 Mapper，分布在 `base/`、`sat/`、`code/` 等包及 `resources/mapper/*.xml`；**表级清单待团队补充**或见专文。

## 直接依赖说明

以下为 **message2 `server` 的 `deps`** 中代表性 Bazel 依赖（完整列表以 `server/BUILD.bazel` 为准），说明「提供什么能力」：

| Bazel label | 能力 |
|-------------|------|
| `//packages/base/dictionary/client` | 数据字典等 Feign |
| `//packages/base/message/client` | 本模块 Feign 契约 |
| `//packages/base/ouaa/client` | 账号/组织等上下文客户端 |
| `//packages/platform/base/model:*` | 分页、周期、请求者等通用模型 |
| `//packages/platform/base/standard:exception` | 标准异常 |
| `//packages/platform/base/utils:*` | JSON、日期、集合、字符串等工具 |
| `//packages/platform/database` | 数据访问基础设施（与 MyBatis 等配合） |
| `//packages/platform/job` | 定时任务支持 |
| `//packages/platform/liquibase` | Liquibase 集成 |
| `//packages/platform/rocketmq` | RocketMQ 生产/消费 |
| `//packages/platform/security` | 安全相关 |
| `//packages/platform/server` | 平台服务端基础能力 |
| `//third-party/java/aliyun:*` | 阿里云短信/语音/推送等 |
| `//third-party/java/tencent:tencentcloud_sms` | 腾讯云短信 |
| 其他 `third-party/java/*` | 校验、邮件 API、Guava、Hutool 等 |

**bracing `server` 的 `deps`**（见 `bracing/server/BUILD.bazel`）主要包括：`//packages/base/message/bracing/client`、`//packages/base/message/sdk`、`//packages/platform/database`、`//packages/platform/liquibase`、`//packages/platform/server` 及少量 utils 与 JDBC。

**client / sdk** 依赖见各自 `BUILD.bazel`（含 `//packages/platform/client`、`//packages/base/message/metadata` 等）。

## 常见开发场景

1. **新增或修改 HTTP 接口（message2）**
   - 在对应子域包下改 `*Controller`（如 `base/controller`、`sat/controller`、`code/controller`、`bff/controller`）。
   - 若需新业务逻辑：补 `service`、`dao`、`entity`、MyBatis XML（`resources/mapper/`）。
   - 网关前缀为 `/message2/` + Controller 路径；精确契约见 `references/external-api.md`。

2. **对外暴露或调整 Feign / SDK**
   - Feign 接口与 DTO：`client/src/**/*.java`。
   - 业务方 Spring 封装：`sdk/src/**/*.java`（保持对 `client` + `metadata` 的兼容）。
   - bracing：`bracing/client/src`。

3. **新增通道或调整发送链路**
   - 实现或修改 `handler/handler/` 下各通道、`handler/rocketmq`、`initialization/` 与 `sat/process` 等；RocketMQ Topic/Group 与 `references/external-api.md`、平台规范对齐。

## 注意事项

- **配置来源**：大量行为由 **Nacos** 中 `common-env.yaml`、`common-datasource.yaml`、`common-rocketmq.yaml` 等覆盖，**勿仅依赖仓库内默认 YAML 判断生产行为**。
- **Liquibase**：仓库默认可能关闭自动迁移；环境是否执行以 Nacos / `liqui-base.execute` 为准，变更数据库前与 **DBA/平台** 确认。
- **拼写与历史路径**：例如 RocketMQ 消费包存在 `comsumer` 目录拼写，**修改时保持与现有 Bean 扫描路径一致**，避免重复注册或漏扫。
- **Bazel 与根 `BUILD.bazel`**：模块根 `BUILD.bazel` 为空，**不要**在根目录添加业务目标而不与团队约定对齐；可构建目标均在子包 `BUILD.bazel`。
- **生成物与锁文件**：若团队对 OpenAPI、MapStruct 等有生成步骤，**禁止手改生成代码**（以项目实际为准，待团队补充清单）。
