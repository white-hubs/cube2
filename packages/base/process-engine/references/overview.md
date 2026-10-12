# base/process-engine 概览

## 模块综述与边界

本模块为 **Cube2 流程引擎服务**：基于 **Activiti** 提供流程定义、流程实例与任务等能力，并扩展 **表单流（context/form）**、工单统计、Webhook、DCM 配置、RocketMQ 集成等；对外通过网关前缀 **`/flow/`** 暴露 HTTP，并通过 **`//packages/base/process-engine/client:client`** 提供 Feign 客户端。

**不负责**：其它业务域的审批规则与页面编排（由调用方或上层应用负责）；通用身份认证与用户目录（依赖 **OUAA** 等基础模块）；跨应用的统一待办产品形态（可与 **todo/work-center** 等协同，但本模块不替代其产品定义）。更细的 HTTP/Feign/MQ 契约见 [external-api.md](./external-api.md)。

## 当前分层架构

**现状，未重构**：本模块未采用单一的 `controller/service/impl` 全仓统一命名，而是以业务域子包为主，常见层次如下（同一域内可能并存多种后缀）。

| 层次/角色 | 典型包或目录 | 说明 |
|-----------|----------------|------|
| Web / 接入 | `*/controller`、`FlowServer` 所在根包 | HTTP 入口；部分能力以 `commandgateway` / `querygateway` 命名 |
| 应用服务 | `commandgateway`、`querygateway`、`*Service` | 命令/查询分离；表单、流程、上下文等各有一套 |
| 领域与基础设施 | `flowable/**/infrastructure`、`dao`、`po`、`mapper`（XML） | 流程与业务表持久化；Activiti 引擎表由引擎与配置维护 |
| 集成 | `rocketmq`、`webhooks`、`dcm`、`form/rocketmq` | 消息消费、Webhook、DCM |

`src-activiti/` 为 **Activiti 引擎侧扩展/补丁** 源码，与 `server` 一并编译，**非**独立分层。

## 目录与包结构

```text
packages/base/process-engine/
├── BUILD.bazel                 # 当前无 java 目标（空文件）
├── variables.bzl               # VERSION、PACKAGE_GROUP
├── SKILL.md
├── references/
│   ├── external-api.md
│   └── overview.md
├── client/
│   ├── BUILD.bazel
│   └── src/                    # Feign、command、DTO、vo（含 com.guhecloud.../client 与 instance/ 等）
└── server/
    ├── BUILD.bazel
    ├── init/
    │   ├── BUILD.bazel
    │   └── init-db.mysql.sql
    ├── resources/
    │   ├── application.yaml
    │   ├── bootstrap.yaml
    │   ├── dcm/
    │   └── mapper/              # MyBatis XML（如 process、task、statistics）
    ├── src/                     # 主应用源码
    ├── src-activiti/            # Activiti 相关扩展（与 server 一同编译）
    └── test/                    # 测试（若有）
```

**`server/src` 主要子包**：

| 子目录 | 角色 |
|--------|------|
| `context/` | 表单流命令/查询网关、DAO、提交、第三方接口、工具 |
| `flowable/` | 流程定义、流程实例、任务、统计、场景、作业、变量等 |
| `form/` | 表单定义/实例/控件、RocketMQ 生产 |
| `dcm/` | DCM 控制器与服务 |
| `webhooks/` | Webhook 配置与 MQ 消费 |
| `work/order/statistics/` | 工单统计 Controller/Service |
| `rocketmq/` | 流程相关消费者 |
| `refresh/` | 刷新相关命令网关 |

**`client/src` 展开约 2 层（相对模块根）**：

| 路径 | 用途 |
|------|------|
| `client/src/com/guhecloud/.../client` | Feign 接口、对外 DTO/契约相关（与包名对齐） |
| `client/src/instance/` | 流程实例侧指令模型：流程发起、审批、完成等操作的命令（command）及事件消息（event） |
| `client/src/vo`、`client/src/util`、`client/src/annotation`、`client/src/base/` | 通用 VO、工具、注解与常量 |

**开发入口**：

- **Spring Boot 主类**：`com.guhecloud.cube.base.flow.FlowServer`（`server/src/FlowServer.java`）
- **Feign 入口**：`com.guhecloud.cube.base.flow.client.FlowClient`、`ProcessInstanceClient`、`FormClient`（调用方依赖 `//packages/base/process-engine/client:client`）
- **MQ 消费入口（示例）**：`com.guhecloud.cube.base.flow.rocketmq.EventConsumer`、`FlowProcessTransferConsumer`；`webhooks.rocketmq.WebHooksConsumer`

## BUILD target 说明

| Target | 作用 |
|--------|------|
| `//packages/base/process-engine/client:deps` | client 私有依赖聚合（`visibility: private`）。 |
| `//packages/base/process-engine/client:client` | 对外客户端库（`glob src/**/*.java`）；**对外依赖**用此标签。 |
| `//packages/base/process-engine/client:package` | 发布 `com.guhecloud.cube.base:flow-client`（Maven 发布用）。 |
| `//packages/base/process-engine/server:deps` | server 私有依赖聚合。 |
| `//packages/base/process-engine/server:server` | 服务端 `java_library`（`src`、`src-activiti`、`resources`）。 |
| `//packages/base/process-engine/server:server-bin` | 可执行 `java_binary`（`main_class = com.guhecloud.cube.base.flow.FlowServer`）；**本地运行**用此目标。 |
| `//packages/base/process-engine/server:server-exec` | Spring Boot fat-jar（`springboot` 规则）；`bazel build` 产出可部署包。 |
| `//packages/base/process-engine/server:package` | 发布 `com.guhecloud.cube.base:flow-server`（Maven 发布用）。 |
| `//packages/base/process-engine/server:server-info` | 部署描述（库 `cube-base-flow`、网关前缀 `/flow/`、`server_id` `processEngine`、主类等）。 |
| `//packages/base/process-engine/server/init:init-mysql` | 初始化 SQL 文件打包（重命名为 `process_engine_server_init.mysql.sql`）。 |

根目录 `BUILD.bazel` 当前无 Java 目标（空文件）；可运行与发布目标均在 `client/`、`server/`、`server/init/` 下。

## 本地运行

在仓库根目录执行：

```bash
bazel run //packages/base/process-engine/server:server-bin
```

或使用 Spring Boot 打包目标：

```bash
bazel build //packages/base/process-engine/server:server-exec
```

**环境与中间件前提**：

- **Nacos**：`application.yaml` 中 `spring.config.import` 依赖多份 `common-*.yaml`（数据源、Redis、安全、RocketMQ、Feign 等），需可用的 Nacos 与命名空间；地址/命名空间默认值见 `application.yaml`，生产与本地覆盖方式 **待团队补充**。
- **MySQL**：库名 **`cube-base-flow`**（与 `server_info.database` 一致）。
- **可选**：Redis、RocketMQ、注册发现等与 `common-*` 配置一致，按环境是否启用。
- **本地最小环境变量与 profile**：**待团队补充**。

## 数据层

- **数据库名**：**`cube-base-flow`**（`server/BUILD.bazel` → `server_info.database`；JDBC 见 `server/resources/application.yaml`）。
- **初始化 SQL**：`server/init/init-db.mysql.sql`，由 Bazel 目标 `//packages/base/process-engine/server/init:init-mysql` 打包供 deploy/database 聚合（重命名为 `process_engine_server_init.mysql.sql`）。
- **Liquibase / 业务 DDL**：`application.yaml` 中 `spring.liquibase` 默认 **关闭**（`enabled: false`）；classpath 上变更脚本位于 **`server/resources/db/com.guhecloud.cube.base.flow/`**（多份 `changelog-*.mysql.sql`）。主 changelog 配置键为 `spring.liquibase.change-log: db.com.guhecloud.cube.base.flow/changelog-master.xml`（若仓库中未检出对应 XML，以团队实际发布物为准）。
- **Activiti 引擎表**：由 **`spring.activiti.database-schema-update`** 等配置驱动（见 `application.yaml`）；与业务表共存于同一库。
- **MyBatis**：XML 位于 **`server/resources/mapper/**`**（如 `process`、`task`、`statistics` 等），与 `mapper-locations` 一致。
- **核心表概述**：除 Activiti 标准表外，业务侧包括表单流/权限、流程定义扩展、任务与实例扩展、统计、Webhook 等（具体表名与 ER **待团队补充** 或见运维/数据字典）。

## 直接依赖说明

以下仅列 **`server` 的 `deps` 聚合**（`//packages/base/process-engine/server:deps`）中与本模块能力直接相关的 Bazel 依赖，完整列表以 `server/BUILD.bazel` 为准。

| Bazel label（节选） | 能力说明 |
|---------------------|----------|
| `//third-party/java/activiti` | Activiti 流程引擎 |
| `//packages/platform/database` | 平台数据访问、实体基类、分页等 |
| `//packages/platform/liquibase` | Liquibase 集成（与业务 changelog 配置配合） |
| `//packages/platform/rocketmq` | RocketMQ 生产与消费 |
| `//packages/platform/security`、`//packages/platform/server` | 安全与 Spring Boot 服务基础设施 |
| `//packages/platform/event` | 领域事件相关能力 |
| `//packages/platform/job` | 定时/作业能力 |
| `//packages/platform/dcm/server-support` | DCM 场景支持 |
| `//packages/base/file/client` | 文件客户端 |
| `//packages/base/message/sdk` | 消息 SDK |
| `//packages/base/ouaa/client` | 组织与用户等认证/目录调用 |
| `//packages/base/todo/client`、`//packages/base/work-center/client` | 待办、工作中心协同 |
| `//packages/base/process-engine/client` | 本模块对外 client 复用与对齐 |
| `//packages/platform/base/model:*`、`//packages/platform/base/utils:*` | 分页、周期、请求方、JSON/日期等工具模型 |

**`client` 的 `deps`**（`//packages/base/process-engine/client:deps`）：以 **`//packages/platform/client`** 与 Jackson/Guava 等为主，供 Feign DTO 与轻量工具使用，见 `client/BUILD.bazel`。

## 常见开发场景

1. **新增或调整对外 HTTP 行为**  
   - 在对应业务包下查找或新增 `*Controller`（如 `flowable`、`dcm`、`context`、`webhooks`、`work/order/statistics`）。  
   - 核对网关前缀 **`/flow/`** 与 `server_info`；契约同步更新 [external-api.md](./external-api.md)。

2. **新增业务表与 MyBatis 访问**  
   - 新增或修改 **`po` / `dao` / `mapper/*.xml`**（路径依域在 `form`、`flowable/**/infrastructure`、`context`、`webhooks` 等）。  
   - 数据库变更：在 **`server/resources/db/com.guhecloud.cube.base.flow/`** 增补 changelog SQL（或与团队约定流程）；Activiti 表勿手写变更。  
   - Liquibase 执行策略以环境配置为准（默认可能关闭自动执行）。

3. **扩展流程定义 / 实例 / 任务行为**  
   - 优先在 **`server/src/flowable/`** 下按 `definition`、`process`、`task`、`job`、`scene` 等子域修改；引擎底层行为涉及 **`server/src-activiti/`** 时需谨慎并回归流程用例。

## 注意事项

- **配置覆盖**：大量运行参数来自 **Nacos** `common-*.yaml`，本地 `application.yaml` 仅为基础壳；调试时若与预期不符，先确认配置中心与环境变量（如 `NACOS_*`、`common-env.*`）。
- **Activiti**：`spring.activiti.database-schema-update` 等会作用于引擎表；升级或切换环境前需评估对生产库的影响。
- **Liquibase**：默认 `spring.liquibase.enabled: false`，与 `liqui-base.execute` 等开关组合使用；勿假设启动即自动跑完所有业务脚本。
- **Bazel 与源码**：`server` 同时编译 **`src` 与 `src-activiti`**，修改引擎相关类时注意对多数据库方言（如 DM/MySQL）的既有分支。
- **生成物与契约**：若团队对 OpenAPI/Feign 有代码生成流程，**禁止**手工覆盖生成目录；具体路径 **待团队补充**。
