---
name: cube2-base-alarm
description: >
  Cube2 base-alarm: alarm record lifecycle (create, assign, handle, transfer),
  SAT alarm types/levels/messaging, statistics/BIM analytics, safety analysis,
  DCM alarm config, RocketMQ integration with todo/work-center/linkage, and
  Feign clients registered as service name alarm. Triggers for packages/base/alarm,
  alarm 微服务, 报警中心, 告警记录与待办/工作任务联动, gateway prefix /alarm/.
---

# base/alarm

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)；术语见 [.agents/skills/cube2-repo/references/glossary.md](../../../.agents/skills/cube2-repo/references/glossary.md)。

## 模块边界与职责

- **负责**：
  - **告警记录主流程**（`flow`：聚合根、状态机、命令/查询网关、REST 接口、MyBatis 仓储与 Mapper）。
  - **SAT 报警配置与消息**（`sat`：报警类型/等级/业务 URL、消息发送与指派初始化）。
  - **统计与 BIM 看板**（`statistics`：多维统计、公开统计、BIM 专项接口）。
  - **安全分析看数**（`analysis`：`/safety/analysis` 下大屏类接口）。
  - **风险指数展示**（`riskscore`：市场风险指数查询）。
  - **与待办、工作中心、联动、设备生命周期等** 的 RocketMQ 生产/消费协作。
  - **对外 Feign**：`client` 模块提供 `@FeignClient(name = "alarm")` 的 HTTP 契约。
- **不负责**：统一认证实现（依赖 `ouaa` client）；其他业务域核心逻辑（通过 client 与 MQ 协作）；网关路由与租户级配置细节（见 Nacos）。

## 子模块分流

| Bazel 标签 | 职责 |
|------------|------|
| `//packages/base/alarm/client:client` | Feign 接口、查询/命令参数与 VO/DTO，供其他服务引用。 |
| `//packages/base/alarm/client:package` | `alarm-client` Maven 发布（`export`）。 |
| `//packages/base/alarm/server:server` | Spring Boot 应用源码与 `resources`（主库）。 |
| `//packages/base/alarm/server:server-exec` | `springboot` 可执行 fat-jar（生产/集成常用）。 |
| `//packages/base/alarm/server:server-bin` | `java_binary`，便于 `bazel run` 本地起进程。 |
| `//packages/base/alarm/server:package` | `alarm-server` Maven 发布（`export`）。 |
| `//packages/base/alarm/server:server-info` | `server_info`：库名、网关前缀、主类、`server_id` 等部署元数据。 |
| `//packages/base/alarm/server/init:init-mysql` | 数据库初始化 SQL 的 `pkg_files`（供 deploy/database 消费）。 |

模块根目录 `BUILD.bazel` 当前无 `java_library` / `export` 目标；构建入口在 `client/` 与 `server/` 子包。

## 快速定位清单

1. **网关前缀与库名**：`server/BUILD.bazel` 中 `server_info` → `gw_prefix = "/alarm/"`，`database = "cube-base-alarm"`。
2. **告警记录 CRUD 与状态流转**：`server/src/flow/interfaces/AlarmRecordController.java` 及 `flow/application`、`flow/domain`。
3. **Feign 调用本服务**：`client/src/*Client.java`，统一 `@FeignClient(name = "alarm")`。
4. **RocketMQ 消费入口**：`server/src/flow/infrastructure/mq/rocketmq/consumer/*.java`。
5. **统计与导出**：`server/src/statistics/interfaces/*Controller.java`。
6. **SAT 报警类型/等级/消息发送**：`server/src/sat/controller/*.java`。
7. **DCM 相关 HTTP**：`server/src-dcm/controller/AlarmDcmController.java`（`/alarm/dcm/config`）。
8. **Liquibase / Mapper XML**：`server/resources/db/`、`server/resources/mapper/`。

## 构建与入口

- **客户端库**：`//packages/base/alarm/client:client`；发布：`//packages/base/alarm/client:package`。
- **服务端**：`//packages/base/alarm/server:server`；可运行：`//packages/base/alarm/server:server-bin`、`//packages/base/alarm/server:server-exec`；发布：`//packages/base/alarm/server:package`。
- **部署元数据**：`//packages/base/alarm/server:server-info`。
- **主类**：`com.guhecloud.cube.base.alarm.server.AlarmServer`（见 `server/BUILD.bazel`）。

## 对外契约

- **HTTP**：网关层前缀 **`/alarm/`**（`server_info`）；应用内路径以 `/alarm-*`、`/sat-*`、`/safety/analysis`、`/public/`、`/risk-index` 等为主（无统一类级 `@RequestMapping` 前缀，以各 Controller 方法路径为准；详见 [references/external-api.md](./references/external-api.md)）。
- **Feign Client**：`name = "alarm"`，接口类见 client 模块（同 external-api 清单）。
- **RocketMQ**：
  - **消费**：`WORK_TASK_TRANSFER`（selector `ALARM_TASK`）、`LINKAGE_MESSAGE_NOTIFY`、`LINKAGE_DEVICE_CTL_NOTIFY`、`TENANT_INIT`（租户初始化联动报警原因）等。
  - **发送**：`TODO_TASK_FLOW`、`WORK_TASK_FLOW`、`DEVICE_LIFE_EVENT` 等（与待办/工作中心/设备生命周期协作）。
  - 完整 Topic/Tag 见 [references/external-api.md](./references/external-api.md)。

## 数据与配置

- **数据库名**：`cube-base-alarm`（`server_info` 与 `application.yaml` 中 JDBC URL 一致）。
- **spring.application.name**：`alarm`（见 `server/resources/bootstrap.yaml`）。
- **端口**：`server/resources/application.yaml` 中 `server.port` 引用 Nacos `common-env.alarm.port`，默认占位 `10106`（以环境配置为准）。

## 深度阅读

- [references/external-api.md](./references/external-api.md)（对外 Bazel 标签、路由族、Feign、MQ）
- [references/overview.md](./references/overview.md)（目录树、目标说明、本地运行）
