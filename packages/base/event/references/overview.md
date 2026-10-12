# base/event — 概览

## 模块综述与边界

- **职责（一句话）**：`base/event`（部署标识 `eventSync`）是 **业务事件同步服务**：从 RocketMQ 订阅上游触发消息、将事件持久化到 MySQL，并在消费者模式下按批读取未处理记录、**按记录中的目标 Topic 再投递**；使用 Redis 等维护消费进度（详见源码与配置）。
- **与 `packages/platform/event` 区分**：后者是进程内 **Guava EventBus** 等基础设施与通用能力（如启动后监听），本服务将其作为 **库依赖** 使用；**不是**同一套「业务事件管道」实现。
- **不负责**：RocketMQ 客户端与平台封装（见 `packages/platform/rocketmq`）、统一数据源/安全/Nacos 配置模板（见各 `common-*.yaml` 与平台文档）、网关路由实现本身；应用内 **无自研 `@RestController`**，对外 HTTP 以网关与平台通用端点为准（完整清单 **待团队补充**）。

## 当前分层架构

本模块为 **单服务、单 `java_library`**，未按经典 `controller/service` 拆分；**现状以数据流与职责分包为主**（未重构）：

| 侧 | 包 / 目录 | 作用 |
|----|-----------|------|
| 入口配置 | `server/src` 根、`config/` | Spring Boot 启动类、Liquibase、`event.*` 配置绑定等 |
| 上游摄入与落库 | `producer/rocketmq`、`producer/repository` | 固定 Topic 消费、实体 `BusinessEventRecord` 等落库（MyBatis-Plus） |
| 再投递与进度 | `consumer/work`、`consumer/publisher`、`consumer/repostiory` | 后台线程批读、动态 Topic 发送、消费进度实体/Mapper（包名 **`repostiory` 为历史拼写**） |

## 目录与包结构

`packages/base/event` 顶层目录（深度 2 层）：

```text
packages/base/event/
├── BUILD.bazel              # 当前无 java/export 目标
├── variables.bzl            # VERSION、PACKAGE_GROUP
├── SKILL.md
├── references/
│   ├── external-api.md
│   └── overview.md
└── server/
    ├── BUILD.bazel
    ├── resources/           # application.yaml、bootstrap.yaml 等
    └── src/                 # Java 源码（详见下节）
```

`server/src` 下 2 层子包：

```text
server/src/
├── EventConfiguration.java
├── EventServer.java
├── config/
├── consumer/
│   ├── publisher/
│   ├── repostiory/
│   └── work/
└── producer/
    ├── repository/
    └── rocketmq/
```

> 说明：本模块 **无** 名为 `controller` 的包；`consumer` / `producer` 分别对应「再投递侧」与「上游消费/落库侧」。

`server/resources` 含 `application.yaml`、`bootstrap.yaml` 及 **`server/resources/db/com.guhecloud.cube.base.event/`** 下 Liquibase SQL（见 [数据层](#数据层)）。

**开发入口**：
- **启动类**：`com.guhecloud.cube.base.event.server.EventServer`（`server/src/EventServer.java`）。
- **功能入口**：
  - 上游 MQ：`producer/rocketmq/consumer/BusinessEventConsumer.java`
  - 落库与再投递：`consumer/work/thread/EventRecordConsumerThread.java` + `EventConsumerWorkConfig.java`

## BUILD target 说明

模块根 `packages/base/event/BUILD.bazel` **当前为空文件**（无 `java_library` / `export`）；全部可构建目标在 **`server/BUILD.bazel`**：

| Target | 用途 |
|--------|------|
| `server` | 编译 Spring Boot 应用主体（对外依赖入口）。 |
| `deps` | 私有聚合依赖（platform database、**platform/event**、rocketmq、server 等）。 |
| `server-bin` | 可执行 `java_binary`（**用于本地运行**）。 |
| `server-exec` | Spring Boot 可执行 jar（`springboot` 规则，**用于本地运行**）。 |
| `package` | 服务端制品 Maven 发布（`cube-biz-event`，**用于制品发布**）。 |
| `server-info` | 供部署聚合使用的元数据（库名 `cube_base_event`、网关 `/event/`、`server_id` 等）。 |

## 本地运行

在仓库根目录执行（需已配置 Bazel/Java，见根 `README.md`）：

```bash
bazel run //packages/base/event/server:server-bin
```

或使用 Spring Boot 规则产物：

```bash
bazel run //packages/base/event/server:server-exec
```

**前提与环境变量**（来自 `server/resources/application.yaml`，实际值以 Nacos **`common-env.yaml`** 等为准）：

- **端口**：`server.port` ← `common-env.event.port`，占位默认 **10223**。
- **常用环境变量**：`NACOS_ADDR`、`NACOS_NAMESPACE`、`NACOS_CLOUD_CONFIG*`（多路 `common-*.yaml`）、以及数据源 URL 中的 `common-env.default-infer.ip`、`common-env.db.port` 等；完整本地最小集 **待团队补充**。
- **中间件**：MySQL（库名见 [数据层](#数据层)）、Nacos、Redis、RocketMQ；无本地依赖时启动可能失败。

## 数据层

- **数据库名**：`cube_base_event`（与 `//packages/base/event/server:server-info` 的 `database`、JDBC URL 一致）。
- **Liquibase**：`application.yaml` 中 `spring.liquibase.change-log` 指向 **`db.com.guhecloud.cube.base.event/changelog-master.xml`**；仓库 `server/resources/db/com.guhecloud.cube.base.event/` 当前可见 **`changelog-1.0.0.mysql.sql`**，`changelog-master.xml` **待团队补充** 或确认是否由其他制品打包引入。运行时 `spring.liquibase.enabled` 默认 **false**，`liqui-base.execute` 由 `common-env.liqui-base.execute` 控制。
- **核心表（来自已提交的 SQL 变更）**：
  - `event_record`：事件载荷、`business_event_type`、`topic`（再投递目标）、时间戳等。
  - `event_consumer_record`：与消费进度相关的记录。
- **Mapper XML**：`mybatis-plus.mapper-locations: classpath:mapper/**/*.xml`；具体路径 **待团队补充**（若仅注解 SQL 则可能无 XML）。

## 直接依赖说明

`server/BUILD.bazel` 中 `deps` 目标聚合以下 Bazel 依赖（私有可见性，对外一般引用 `:server` / `:package` 等）：

| Bazel label | 提供能力（简述） |
|-------------|------------------|
| `//packages/platform/base/utils:collection` | 集合工具类 |
| `//packages/platform/base/utils:date` | 日期工具 |
| `//packages/platform/base/utils:object` | 对象工具 |
| `//packages/platform/database` | 数据源、MyBatis-Plus 等数据访问栈 |
| `//packages/platform/event` | 进程内 EventBus 等，与本服务启动后再投递线程协作 |
| `//packages/platform/liquibase` | Liquibase 集成（本模块 `EventConfiguration` 等） |
| `//packages/platform/rocketmq` | RocketMQ 生产/消费封装 |
| `//packages/platform/server` | Spring Boot / 平台服务端基座能力 |

## 常见开发场景

1. **调整上游 MQ 消费（Topic/Tag/反序列化）**
   - 修改：`server/src/producer/rocketmq/`（如 `BusinessEventConsumer`）、相关 `message` 模型。
   - 配置：`EventProperties`（`event.producer`）、`application.yaml` / Nacos 中 RocketMQ 段。

2. **调整落库字段或 SQL**
   - 修改：`producer/repository` 下 entity、mapper、service；新增 Liquibase changeset：`server/resources/db/com.guhecloud.cube.base.event/*.sql`（并维护 master changelog，**待团队补充** 流程）。

3. **调整再投递行为（批量、间隔、动态 Topic）**
   - 修改：`consumer/work/thread/EventRecordConsumerThread.java`、`consumer/work/config/EventConsumerWorkConfig.java`、`consumer/publisher/`；进度与实体见 `consumer/repostiory/`。

## 注意事项

- **命名**：`consumer/repostiory` 包名为历史拼写，新增代码勿另起冲突包名；重构需团队共识。
- **模块边界**：排查「事件总线」问题时先区分 **`platform/event`（库）** 与 **`base/event`（本同步服务）**。
- **配置覆盖**：大量键来自 Nacos 导入的 `common-*.yaml`，本地 `application.yaml` 仅为基础；**以运行环境 Nacos 为准**。
- **Liquibase**：默认关闭或受 `liqui-base.execute` 控制，勿假设每次启动自动迁库。
- **网关**：对外前缀 **`/event/`**（`server_info`）；应用内无业务 Controller，运维/路由问题勿在本模块内「补假接口」。
