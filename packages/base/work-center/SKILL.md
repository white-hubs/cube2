---
name: cube2-base-work-center
description: >
  Cube2 base work-center: work tasks, tags, statistics, handover records, BIM
  stats, and DCM declarative config (WORK_CENTER). Feign name workCenter,
  gateway /workCenter/, MQ WORK_TASK_FLOW / WORK_TASK_TRANSFER. Triggers for
  packages/base/work-center, 工作中心, 待办任务, 工作任务流转, BIM 工作统计.
---

# base/work-center

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)；术语见 [.agents/skills/cube2-repo/references/glossary.md](../../../.agents/skills/cube2-repo/references/glossary.md)。

## 模块边界与职责

- **负责**：
  - **工作任务**：分页查询（待办/已办）、按业务主键批量删除、与业务 URL 跳转信息扩展。
  - **工作标签与业务类型关联**：标签 CRUD、标签关联业务类型维护。
  - **统计与 BIM**：工作数量/业务类型/趋势、标签维度统计；BIM 专项统计接口。
  - **工作中心首页**：任务数量、任务查询（`WorkCenterController` 下 `/work/center`）。
  - **交接记录**：交接单新增、接管、列表与详情（`/handover/record`）。
  - **RocketMQ**：消费工作任务流程 topic，驱动插入/流转/删除；交接场景向 `WORK_TASK_TRANSFER` 发送（按工作标签 code 作为 Tag）。
  - **DCM**：声明式配置提供方 `WORK_CENTER`（工作标签、标签-业务类型、业务 URL），HTTP 入口 `/work/dcm/config`（见 `src-dcm`）。
  - **对外 Feign**：`client` 提供 `@FeignClient(name = "workCenter")` 的 `WorkTaskClient`。
- **不负责**：统一认证与权限框架实现（依赖平台 `security` 等）；网关路由与租户级细节（见 Nacos）；其他业务域核心逻辑（通过 Feign/MQ 协作）。

## 子模块分流

| Bazel 标签 | 职责 |
|------------|------|
| `//packages/base/work-center/client:client` | Feign `WorkTaskClient`、param/vo/message/enumeration，供上游依赖。 |
| `//packages/base/work-center/client:package` | `workcenter-client` Maven 发布（`export`）。 |
| `//packages/base/work-center/server:server` | Spring Boot 应用源码（含 `src-dcm`）与 `resources`。 |
| `//packages/base/work-center/server:server-exec` | `springboot` 可执行 fat-jar。 |
| `//packages/base/work-center/server:server-bin` | `java_binary`，便于 `bazel run` 本地起进程。 |
| `//packages/base/work-center/server:package` | `workcenter-server` Maven 发布（`export`）。 |
| `//packages/base/work-center/server:server-info` | `server_info`：库名、网关前缀、主类、`server_id` 等部署元数据。 |
| `//packages/base/work-center/server/init:init-mysql` | 数据库初始化 SQL 的 `pkg_files`（供 deploy/database 消费）。 |

模块根目录 `BUILD.bazel` 当前无 Java 目标；构建入口在 `client/` 与 `server/` 子包。

## 快速定位清单

1. **网关前缀与库名**：`server/BUILD.bazel` 中 `server_info` → `gw_prefix = "/workCenter/"`，`database = "cube_biz_work_center"`。
2. **工作任务与删除**：`server/src/controller/WorkTaskController.java`；Feign 契约见 `client/src/WorkTaskClient.java`（删除路径与 Controller 声明需对照 [references/external-api.md](./references/external-api.md)）。
3. **工作中心聚合接口**：`server/src/controller/WorkCenterController.java`（`/work/center`）。
4. **标签与业务 URL**：`WorkTagController.java`、`WorkBusinessUrlController.java`。
5. **统计**：`WorkTaskStatisticsController.java`、`WorkTaskTagStatisticsController.java`；BIM：`server/src/bim/controller/WorkTaskBimController.java`。
6. **交接**：`HandoverRecordController.java`。
7. **MQ 消费**：`server/src/rocketmq/consumer/WorkTaskFlowConsumer.java`。
8. **DCM**：`server/src-dcm/controller/WorkDcmController.java`；声明文件 `server/resources/dcm/dcm-declaration-work.yml`。
9. **持久化**：`server/src/dao/`（MyBatis Mapper 接口）；XML：`server/resources/mapper/**/*.xml`（见 `application.yaml`）。

## 构建与入口

- **客户端库**：`//packages/base/work-center/client:client`；发布：`//packages/base/work-center/client:package`。
- **服务端**：`//packages/base/work-center/server:server`；可运行：`//packages/base/work-center/server:server-bin`、`//packages/base/work-center/server:server-exec`；发布：`//packages/base/work-center/server:package`。
- **部署元数据**：`//packages/base/work-center/server:server-info`。
- **主类**：`com.guhecloud.cube.biz.work.center.server.WorkCenterServer`（`server/BUILD.bazel` 中 `_MAIN_CLASS`）。

## 对外契约

- **HTTP**：网关层前缀 **`/workCenter/`**（`server_info`）；应用内路径包括 `/work/center`、`/handover/record`、`/work/dcm/config` 及根路径上的 `/work-tasks`、`/work-tags`、`/work-task-statistics` 等（无单一全局 `@RequestMapping` 统一前缀，以各 Controller 为准；完整族见 [references/external-api.md](./references/external-api.md)）。
- **Feign Client**：`name = "workCenter"`，接口类 `WorkTaskClient`（`client/src/WorkTaskClient.java`）。
- **RocketMQ**：
  - **消费**：`WORK_TASK_FLOW`（`consumerGroup = work_task_msg`，顺序消费，未在注解中声明 Tag 过滤）。
  - **发送**：`WORK_TASK_TRANSFER`，发送时使用 **`Topic:Tag`** 形式，`Tag` 为工作标签 code（如 `ALARM_TASK` 等，以运行时 `workTagCodeMap` 为准）。
  - 详见 [references/external-api.md](./references/external-api.md)。

## 数据与配置

- **数据库名**：`cube_biz_work_center`（`server_info` 与 `server/resources/application.yaml` 中 JDBC URL 一致）。
- **spring.application.name**：`workCenter`（`server/resources/bootstrap.yaml`）。
- **端口**：`server/resources/application.yaml` 中 `server.port` 引用 `common-env.work-center.port`，默认占位 **`10220`**（以 Nacos `common-env` 为准）。
- **配置导入**：`application.yaml` 从 Nacos 拉取 `common-env`、`common-datasource`、`common-rocketmq`、`common-platform-fegin` 等；本地无 Nacos 时完整运行方式待团队补充。

## 深度阅读

- [references/external-api.md](./references/external-api.md)（对外 Bazel 标签、路由族、Feign、MQ）
- [references/overview.md](./references/overview.md)（目录树、目标说明、本地运行）
