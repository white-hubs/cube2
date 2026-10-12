---
name: cube2-base-process-engine
description: >
  Cube2 `packages/base/process-engine` 流程引擎（Activiti）：表单流、流程定义与实例、
  任务与统计、Webhook、DCM；服务注册名 processEngine，网关 `/flow/`。中英触发：流程引擎、
  process engine、Activiti、FlowServer、Feign processEngine、工单、审批、BPMN。
---

# base/process-engine

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：
  - **流程运行时**：基于 Activiti（`RepositoryService` / `RuntimeService` / `TaskService` / `HistoryService`）的流程定义部署与查询、流程实例启停与历史查询、激活任务（同意/退回/转办等）与历史任务。
  - **表单与业务流**：表单定义/实例、表单流（`form-flow` 等）配置与提交、与业务类型绑定；表单数据向业务侧投递（MQ）。
  - **集成**：Feign 对外契约（`client`）；消费/生产 RocketMQ 与待办、工作台等协同；Webhook 回调；DCM 声明式配置（`/flow/dcm/config`）。
  - **统计**：流程实例与工单维度统计接口。
- **不负责**：统一认证与网关实现（依赖平台 `security` 与 Nacos）；**待办/工作台等 Topic 的权威定义**以对应 `base/todo`、`base/work-center` 客户端消息类为准；跨模块拓扑与 Nacos 中 `common-env` 的最终值以环境为准。

## 子模块分流

| 路径 | 职责 | Bazel 标签（常用） |
|------|------|-------------------|
| `client/` | Feign 接口 `FlowClient`、`ProcessInstanceClient`、`FormClient`；与调用方共享的 command/DTO/枚举；`java_export` 发布 `flow-client`。 | `//packages/base/process-engine/client:client`、`//packages/base/process-engine/client:package` |
| `server/` | Spring Boot 应用 `FlowServer`；`src` 下 context / flowable / form / dcm / webhooks / work 等；`src-activiti`；`resources`（bootstrap、application、DCM、mapper 等）；RocketMQ 消费者与生产者。 | `//packages/base/process-engine/server:server`、`//packages/base/process-engine/server:server-bin`、`//packages/base/process-engine/server:server-exec`、`//packages/base/process-engine/server:package`、`//packages/base/process-engine/server:server-info` |
| `server/init/` | MySQL 初始化 SQL 的 `pkg_files`（`init-mysql` → `process_engine_server_init.mysql.sql`），供 deploy/database 聚合。 | `//packages/base/process-engine/server/init:init-mysql` |

模块根目录 `BUILD.bazel` 当前无聚合的 `java_library` / `export` 目标；**构建与发布入口在 `client/` 与 `server/`**（与 `variables.bzl` 同级）。

## 快速定位清单

1. **网关前缀、库名、注册名**：`server/BUILD.bazel` 中 `server_info` → `gw_prefix = "/flow/"`，`database = "cube-base-flow"`，`server_id = "processEngine"`；`server/resources/bootstrap.yaml` → `spring.application.name = processEngine`。
2. **HTTP 接口**：`server/src/**/interfaces/*Controller.java`、`server/src/**/controller/*Controller.java`（含 `flowable`、`context`、`form`、`dcm`、`webhooks`、`work/order/statistics` 等）；部分 Controller 无类级 `@RequestMapping`，路径以方法上 `@*Mapping` 为准。
3. **Feign 调用本服务**：`client/src/.../FlowClient.java`、`ProcessInstanceClient.java`、`FormClient.java`（均 `@FeignClient(name = "processEngine")`，常量见 `ServiceRoute.TAG`）。
4. **RocketMQ**：本模块内 Topic 常量见 `server/src/**/rocketmq/**/*.java`、`flowable/**/event/**/*.java`；消费类见 `server/src/rocketmq/*.java`、`webhooks/rocketmq/`、`flowable/task/.../DeleteAutoPassMessageEvent.java` 等（详情见 [references/external-api.md](./references/external-api.md)）。
5. **Activiti 与扩展表**：引擎配置见 `server/resources/application.yaml` 中 `spring.activiti.*`；业务扩展与 MyBatis 见 `flowable`、`context`、`form` 下 `dao`/`mapper`/`po`。
6. **DCM**：`server/resources/dcm/dcm-flow-form.yml` 与 `FlowDcmController`（`/flow/dcm/config`）。

## 构建与入口

- **客户端库**：`//packages/base/process-engine/client:client`；发布：`//packages/base/process-engine/client:package`（artifactId `flow-client`，`variables.bzl` 中 `PACKAGE_GROUP = com.guhecloud.cube.base`）。
- **服务端**：`//packages/base/process-engine/server:server`；可运行：`//packages/base/process-engine/server:server-bin`、`//packages/base/process-engine/server:server-exec`；发布：`//packages/base/process-engine/server:package`（artifactId `flow-server`）。
- **部署元数据**：`//packages/base/process-engine/server:server-info`（网关前缀、库名、主类、server_id）。
- **DB 初始化制品**：`//packages/base/process-engine/server/init:init-mysql`。
- **主类**：`com.guhecloud.cube.base.flow.FlowServer`（`server/BUILD.bazel` 中 `_MAIN_CLASS`）。

## 对外契约

- **HTTP**：网关层前缀 **`/flow/`**（`server_info.gw_prefix`）；应用内路径族包括 `/form-flow*`、`/form/instance`、`/process/*`、`/instance/*`、`/statistics/*`、`/flow/dcm/config` 等。**经网关的完整路径** = `/flow/` + 应用内路径（无 `server.servlet.context-path` 时）。方法级与 Feign 对齐说明见 [references/external-api.md](./references/external-api.md)。
- **Feign Client**：`name = "processEngine"`（`ServiceRoute.TAG`），接口类：`FlowClient`、`ProcessInstanceClient`、`FormClient`。
- **RocketMQ**：
  - **本模块定义或直接使用常量的 Topic**：如 `FLOW_EVENT_START`、`FLOW_COMPLETED`、`FORM_DATA_TO_BUSINESS`、`PROCESS_TASK_BACKLOG`、`WEB_HOOK_CALL_BACK`、`AUTO_PASS_TOPIC`；与 **待办** `TODO_TASK_FLOW`、**工作台** `WORK_TASK_FLOW` / `WORK_TASK_TRANSFER` 的协同见代码引用与 [references/external-api.md](./references/external-api.md)。
  - **Tag/选择器**：例如 `FlowProcessTransferConsumer` 使用 `selectorExpression = "APPROVE_TASK"`（绑定 `WORK_TASK_TRANSFER`）；其余消费者是否带 Tag 以各 `@RocketMQMessageListener` 为准。
- **流程引擎 API**：非单独 REST 包装层，而是 **Spring MVC Controller** + **Activiti Java API**。流程定义入口见 `ProcessDefinitionController`（如 `/process/definition/*`）；流程实例见 `ProcessInstanceController`、`HistoricalProcessInstanceController`；任务见 `ActivatedTaskController`、`HistoricalTaskController` 等。

## 数据与配置

- **数据库名**：`cube-base-flow`（`server_info`、`application.yaml` 中 JDBC URL 片段一致）。
- **spring.application.name**：`processEngine`（`server/resources/bootstrap.yaml`）。
- **端口**：`server/resources/application.yaml` 中 `server.port` 引用 `common-env.process-engine.port`，占位默认 `10110`（以 Nacos `common-env` 为准）。
- **Activiti**：`application.yaml` 中 `spring.activiti.*`（如 `database-schema-update`、`history-level` 等）。
- **其它**：数据源、Redis、安全、Feign、RocketMQ、Job 等通过 Nacos `common-*.yaml` 引入（见 `application.yaml` 的 `spring.config.import` 列表）。Liquibase 默认 `enabled: false`，是否在生产执行以环境为准。

## 深度阅读

- [references/external-api.md](./references/external-api.md)（对外 Bazel 标签、路由族、Feign、MQ、流程 API 索引）
- [references/overview.md](./references/overview.md)（目录树、BUILD 目标、本地运行）
