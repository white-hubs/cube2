# base/todo 概览

## 模块综述与边界

**职责**：待办任务中心服务，支撑工作流相关任务的派发、状态流转与统计查询等业务侧待办能力（与任务、标签、业务跳转 URL、人员等域对象配合）。

**不负责**：通用工作流引擎编排与流程定义、跨域统一消息投递平台本身、组织/账号主数据（见 **OUAA** 等模块）、文件存储实现（仅通过 `//packages/base/file/client` 等客户端能力按需集成）。与其它系统的 HTTP/MQ 契约细节见 `references/external-api.md`。

## 当前分层架构

本模块服务端采用 **Spring MVC + Service + MyBatis（Mapper/XML）** 的常规分层，并叠加 **RocketMQ 消息** 与 **DCM 适配** 子树：

- **接入层**：`controller/`、`bim/controller/`；DCM 专用接口在 `src-dcm/controller/`。
- **应用层**：`service/`、`service/impl/`；DCM 在 `src-dcm/service/`、`src-dcm/service/impl/`。
- **领域与视图**：`entity/`（表映射）、`param/`、`vo/`、`enumeration/`。
- **数据访问**：`dao/` + `resources/mapper/**/*.xml`。
- **横切**：`config/`、`filter/`、`rocketmq/`（`message/`、`consumer/`）。
- **DCM**：`src-dcm/entity/`、`src-dcm/support/` 等与主域对象并行的适配与辅助逻辑。

**说明**：以上为仓库内**现状**分层（非严格 DDD 包名）；与仓库「最佳实践」若有差异，以现状为准，不在未评审情况下主动重构。

## 目录与包结构

模块根目录布局（约 2 层）：

```text
packages/base/todo/
├── BUILD.bazel                 # 当前无 java 目标
├── variables.bzl               # VERSION、PACKAGE_GROUP
├── SKILL.md
├── references/
│   ├── external-api.md
│   └── overview.md
├── client/
│   ├── BUILD.bazel
│   └── src/                    # Feign、message、param、enumeration
└── server/
    ├── BUILD.bazel
    ├── init/
    │   ├── BUILD.bazel
    │   └── init-db.mysql.sql
    ├── resources/
    │   ├── application.yaml
    │   ├── bootstrap.yaml
    │   ├── dcm/
    │   ├── db/com.guhecloud.cube.biz.todo/
    │   └── mapper/
    ├── src/                    # 主应用源码（controller/service/dao/entity/…）
    └── src-dcm/                # DCM 适配（controller/service/entity/support）
```

`server/src` 下一层（概念分层，非严格 DDD 包名）：

- **controller**：`controller/`、`bim/controller/`
- **service**：`service/`、`service/impl/`
- **实体**：`entity/`（表对应实体）
- **数据访问**：`dao/`（Mapper 接口）+ `resources/mapper/`（XML）
- **其它**：`param/`、`vo/`、`enumeration/`、`filter/`、`config/`、`rocketmq/`

Java 源码包路径说明（本模块 Bazel 使用 `server/src`、`server/src-dcm`、`client/src`，而非 Maven 的 `src/main/java`）：

| 路径 | 用途 |
|------|------|
| `client/src/` | Feign 接口（如 `TodoTaskClient`）等对外 API 定义 |
| `client/src/enumeration/` | 客户端侧枚举，与协议/展示一致 |
| `client/src/message/` | 与 MQ / 集成交互的消息 DTO |
| `client/src/param/` | 调用方入参模型 |
| `server/src/controller/`、`server/src/bim/controller/` | HTTP 接口 |
| `server/src/service/`、`server/src/service/impl/` | 业务服务与实现 |
| `server/src/dao/` | MyBatis Mapper 接口 |
| `server/src/entity/` | 数据库实体 |
| `server/src/param/`、`server/src/vo/` | 入参与返回视图对象 |
| `server/src/enumeration/` | 服务端枚举 |
| `server/src/filter/` | 请求/字段过滤等 |
| `server/src/config/` | Spring 配置类 |
| `server/src/rocketmq/message/`、`server/src/rocketmq/consumer/` | MQ 消息体与消费者 |
| `server/src-dcm/controller/`、`server/src-dcm/service/`、`server/src-dcm/entity/`、`server/src-dcm/support/` | DCM 相关接口、服务、实体与辅助类 |
| `server/resources/mapper/` | MyBatis XML |
| `server/resources/db/com.guhecloud.cube.biz.todo/` | Liquibase 变更脚本 |
| `server/init/` | 数据库初始化 SQL 打包（见 `init/BUILD.bazel`） |

**开发入口**：
- **Spring Boot 主类**：`com.guhecloud.cube.biz.todo.server.TodoServer`（`server/src/TodoServer.java`）
- **Feign 入口**：`com.guhecloud.cube.biz.todo.client.TodoTaskClient`（调用方依赖 `//packages/base/todo/client:client`）
- **MQ 消费入口**：`com.guhecloud.cube.biz.todo.server.rocketmq.consumer.TodoTaskFlowConsumer`

## BUILD target 说明

| Target | 作用 |
|--------|------|
| `//packages/base/todo/client:deps` | client 私有依赖聚合（`visibility: private`）。 |
| `//packages/base/todo/client:client` | 对外客户端库（其他模块依赖此 label）。 |
| `//packages/base/todo/client:package` | 发布 `com.guhecloud.cube.biz:todo-client`。 |
| `//packages/base/todo/server:deps` | server 私有依赖聚合。 |
| `//packages/base/todo/server:server` | 服务端 `java_library`（含 `src`、`src-dcm`、`resources`）。 |
| `//packages/base/todo/server:server-bin` | 可执行 `java_binary`（`main_class = TodoServer`），**用于本地运行**。 |
| `//packages/base/todo/server:server-exec` | Spring Boot fat-jar（`springboot` 规则），用于构建部署包。 |
| `//packages/base/todo/server:package` | 发布 `com.guhecloud.cube.biz:todo-server`。 |
| `//packages/base/todo/server:server-info` | 部署描述（库 `cube-srv-gen-todo`、网关前缀 `/todo/`、`server_id` `todoCenter` 等）。 |
| `//packages/base/todo/server/init:init-mysql` | 初始化 SQL 文件打包。 |

## 本地运行

在仓库根目录执行：

```bash
bazel run //packages/base/todo/server:server-bin
```

或使用 Spring Boot 打包目标（团队若常用 fat-jar 启动）：

```bash
bazel build //packages/base/todo/server:server-exec
```

**说明**：运行前需可用的 **MySQL**（库名 `cube-srv-gen-todo`）、**Nacos** 及 `application.yaml` 中 import 的各 `common-*.yaml`；本地最小环境变量与 profile **待团队补充**（若无可复用的 compose/文档）。

## 数据层

- **数据库名**：`cube-srv-gen-todo`（与 `server/BUILD.bazel` 中 `server_info(database = ...)` 及 `application.yaml` 中 JDBC URL 一致）。
- **Liquibase**：变更脚本目录为 `server/resources/db/com.guhecloud.cube.biz.todo/`（`changelog-*.mysql.sql` 等）；`application.yaml` 中 `spring.liquibase.change-log` 指向 `db.com.guhecloud.cube.biz.todo/changelog-master.xml`。默认配置里 `spring.liquibase.enabled` 为 `false`，是否由环境/Nacos 打开 **待团队补充**。
- **一次性初始化 SQL**：`server/init/init-db.mysql.sql`（经 `//packages/base/todo/server/init:init-mysql` 打包）；脚本内说明部分历史表曾未纳入 Liquibase，新表与数据初始化应走 Liquibase（见文件头注释）。
- **核心表（概述）**：与实体 `TodoTask`、`TodoTag`、`TodoPerson`、`TodoBusinessUrl`、`TodoTagReferBusinessType` 等对应；详细列定义 **待团队补充** 或见 `init-db.mysql.sql` / Liquibase 脚本。

## 直接依赖说明

以下为 `server/BUILD.bazel` 中 `deps` 聚合的主要 Bazel 依赖及其能力（`client` 见 `client/BUILD.bazel` 的 `:deps`）：

| Bazel label（或等价） | 能力说明 |
|------------------------|----------|
| `//packages/platform/base/model:...` | 分页、周期等通用模型 |
| `//packages/platform/base/utils:...` | 集合、日期、JSON、字符串等工具 |
| `//packages/platform/database` | 数据访问基础设施（与 MyBatis-Plus 等配合） |
| `//packages/platform/liquibase` | Liquibase 集成 |
| `//packages/platform/rocketmq` | RocketMQ 生产/消费支持 |
| `//packages/platform/security` | 安全与资源服务相关能力 |
| `//packages/platform/server` | 服务端公共启动与横切能力 |
| `//packages/platform/dcm/server-support` | DCM 服务端适配支持 |
| `//packages/base/file/client` | 文件服务 Feign 客户端 |
| `//packages/base/ouaa/client` | 组织/账号等 OUAA 客户端 |
| `//packages/base/message/sdk` | 站内消息等 SDK |
| `//packages/platform/client`（client 模块） | Feign 等客户端基础设施 |
| `//third-party/java/...` | Fastjson2、Guava、Jackson、Spring JDBC 等第三方库 |

## 常见开发场景

1. **新增或修改 HTTP 接口**  
   - 在 `server/src/controller/`（或 `bim/controller/`）增加或调整 Controller。  
   - 在 `server/src/service/`、`server/src/service/impl/` 实现业务；按需增加 `param/`、`vo/`。  
   - 若涉及持久化：扩展 `entity/`、`dao/`、`resources/mapper/*.xml`；数据库变更同步 `server/resources/db/com.guhecloud.cube.biz.todo/`（及按需更新 `init` SQL 策略与团队约定）。

2. **新增 Feign 契约供其它模块调用**  
   - 在 `client/src/` 定义接口与 DTO（`param/`、`enumeration/`、`message/` 等）；确认 `client/BUILD.bazel` 中 `client` 目标包含新源码。  
   - 消费方依赖 `//packages/base/todo/client:client`（或发布坐标 `com.guhecloud.cube.biz:todo-client`）。

3. **处理任务流转类 MQ 消息**  
   - 消息体可参考 `server/src/rocketmq/message/` 与 `client/src/message/` 对齐；消费逻辑在 `server/src/rocketmq/consumer/`（如 `TodoTaskFlowConsumer`）。  
   - Topic / Tag 等契约见 `references/external-api.md`。

## 注意事项

- **配置来源**：大量行为依赖 Nacos 中的 `common-*.yaml`（见 `server/resources/application.yaml` 的 `spring.config.import`）；本地或联调时需正确的 `NACOS_ADDR`、`NACOS_NAMESPACE` 等，**具体清单待团队补充**。  
- **Liquibase 与 init SQL**：关注 `init-db.mysql.sql` 中关于历史表与 Liquibase 迁移策略的说明；避免绕过团队约定手工改生产库。  
- **DCM 代码路径**：业务主逻辑在 `server/src/`，DCM 专用实现集中在 `server/src-dcm/`，修改时注意两侧一致性。  
- **生成类 / 禁止手改**：若团队对 Mapper 或 OpenAPI 有代码生成流程，**待团队补充**具体路径与约束。
