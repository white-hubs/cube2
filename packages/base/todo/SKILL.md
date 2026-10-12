---
name: cube2-base-todo
description: >
  Cube2 `packages/base/todo` 待办中心 todoCenter：任务与标签、统计/BIM、DCM，消费 Topic
  TODO_TASK_FLOW。中英触发：待办、todos、todoCenter、Feign、gateway `/todo/`。
---

# base/todo

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)；术语见 [.agents/skills/cube2-repo/references/glossary.md](../../../.agents/skills/cube2-repo/references/glossary.md)。

## 模块边界与职责

- **负责**：
  - **待办任务**：分页查询、办结列表、按业务主键批量删除；任务流转由 MQ 驱动（插入/变更/删除）。
  - **待办标签与业务 URL**：标签 CRUD、标签与业务类型关联、业务跳转地址维护。
  - **统计**：任务数量/类型/趋势、标签维度统计；~~BIM 专项统计接口（已废弃）~~。
  - **DCM**：待办侧声明式配置（`dcm-declaration-todo.yml`）及 `/todo/dcm/config` 下能力注册。
  - **对外 Feign**：`client` 提供 `@FeignClient(name = "todoCenter")` 的 HTTP 契约（当前为 `TodoTaskClient`）。
- **不负责**：统一认证与资源服务器实现（依赖平台 `security` 与 Nacos 公共配置）；**向 `TODO_TASK_FLOW` 生产消息**的实现若在其他业务服务，本模块仅消费；网关路由细节以部署与 `server_info` 为准。

## 子模块分流

| 路径 | 职责 |
|------|------|
| `client/` | Feign 接口、与调用方共享的消息体/枚举/参数类型；`java_export` 发布 `todo-client`。 |
| `server/` | Spring Boot 应用：`TodoServer`、Controller/Service/DAO、RocketMQ 消费者、MyBatis XML、`resources`（含 Liquibase changelog 路径配置）。 |
| `server/init/` | 数据库初始化 SQL 的 `pkg_files`（`init-mysql` → `todo_server_init.mysql.sql`），供 deploy/database 聚合使用。 |

模块根目录 `BUILD.bazel` 当前无 `java_library` / `export` 目标；**构建入口在 `client/` 与 `server/`**（与 `variables.bzl` 同级的聚合文件为空属正常）。

## 快速定位清单

1. **网关前缀、库名、注册名**：`server/BUILD.bazel` 中 `server_info` → `gw_prefix = "/todo/"`，`database = "cube-srv-gen-todo"`；`bootstrap.yaml` → `spring.application.name = todoCenter`。
2. **HTTP 业务接口**：`server/src/controller/*Controller.java`、`server/src/bim/controller/TodoTaskBimController.java`。
3. **DCM HTTP**：`server/src-dcm/controller/TodoDcmController.java`（类级 `/todo/dcm/config`）。
4. **Feign 调用本服务**：`client/src/TodoTaskClient.java`（`name = "todoCenter"`）。
5. **RocketMQ 消费**：`server/src/rocketmq/consumer/TodoTaskFlowConsumer.java`（Topic `TODO_TASK_FLOW`，ConsumerGroup `todo_task_msg`）。
6. **数据访问**：`server/src/dao/*.java` 与 `server/resources/mapper/*.xml`；实体在 `server/src/entity/`。
7. **DCM 声明与 Liquibase**：`server/resources/dcm/dcm-declaration-todo.yml`、`server/resources/db/com.guhecloud.cube.biz.todo/`（`application.yaml` 中 `liquibase.enabled` 默认 false，以环境为准）。

## 构建与入口

- **客户端库**：`//packages/base/todo/client:client`；发布：`//packages/base/todo/client:package`（artifact `todo-client`）。
- **服务端**：`//packages/base/todo/server:server`；可运行：`//packages/base/todo/server:server-bin`、`//packages/base/todo/server:server-exec`；发布：`//packages/base/todo/server:package`（artifact `todo-server`）。
- **部署元数据**：`//packages/base/todo/server:server-info`（含 `server_id = todoCenter`、网关前缀、库名、主类）。
- **DB 初始化制品**：`//packages/base/todo/server/init:init-mysql`。
- **主类**：`com.guhecloud.cube.biz.todo.server.TodoServer`（见 `server/BUILD.bazel`）。

## 对外契约

- **HTTP**：网关层前缀 **`/todo/`**（`server_info` 中 `gw_prefix`）；应用内路径以 `/todo-*`、`/todo/dcm/config` 等为主（无统一类级前缀覆盖全部 Controller，以各方法 `@*Mapping` 为准；**经网关的完整路径** = `/todo/` + 应用内路径）。方法级清单见 [references/external-api.md](./references/external-api.md)。
- **Feign Client**：`name = "todoCenter"`，当前接口类为 `TodoTaskClient`（见 client 模块）。
- **RocketMQ**：
  - **消费**：Topic **`TODO_TASK_FLOW`**，ConsumerGroup **`todo_task_msg`**，顺序消费；消息多态由 JSON 字段 `operationType`（INSERT/CHANGE/DELETE）区分，**未在 `@RocketMQMessageListener` 上配置 Tag 选择器**（全 Topic 消费）。
  - **生产**：本仓库 `packages/base/todo` 内**未发现**向该 Topic 发送消息的代码；生产者可能在其他模块或服务，**待团队补充**完整生产拓扑。

## 数据与配置

- **数据库名**：`cube-srv-gen-todo`（`server_info` 与 `application.yaml` 中 JDBC URL 一致）。
- **spring.application.name**：`todoCenter`（`server/resources/bootstrap.yaml`）。
- **端口**：`server/resources/application.yaml` 中 `server.port` 引用 Nacos `common-env.todo.port`，占位默认 `10220`（以实际 Nacos `common-env` 为准）。
- **其它**：数据源、Redis、安全、Feign、RocketMQ 等通过 Nacos 的 `common-*.yaml` 引入（见 `application.yaml` 的 `spring.config.import` 列表）。

## 深度阅读

- [references/external-api.md](./references/external-api.md)（对外 Bazel 标签、路由族、Feign、MQ）
- [references/overview.md](./references/overview.md)（目录树、目标说明、本地运行）
