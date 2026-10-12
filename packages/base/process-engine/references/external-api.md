# base/process-engine 对外契约

## 对外 Bazel 标签

| 用途 | 标签 |
|------|------|
| 客户端库 | `//packages/base/process-engine/client:client` |
| 客户端发布（`flow-client`） | `//packages/base/process-engine/client:package` |
| 服务端库 | `//packages/base/process-engine/server:server` |
| 可执行（`java_binary`） | `//packages/base/process-engine/server:server-bin` |
| Spring Boot 可执行包 | `//packages/base/process-engine/server:server-exec` |
| 服务端发布（`flow-server`） | `//packages/base/process-engine/server:package` |
| 部署元数据（网关前缀、库名、主类等） | `//packages/base/process-engine/server:server-info` |
| DB 初始化文件打包 | `//packages/base/process-engine/server/init:init-mysql` |

模块根 `//packages/base/process-engine:BUILD.bazel` 当前无 Java 目标。

## HTTP 接口路由前缀

- **网关前缀**（部署侧）：`/flow/`（`server/BUILD.bazel` → `server_info.gw_prefix`）。
- **应用内路径族**（无全局 `context-path` 时与 Controller 声明一致；经网关为 `/flow/` + 下列路径）：

| 路径前缀或族 | 说明 |
|--------------|------|
| `/form-flow`、`/form-flow-group`、`/custom-form-flow` | 表单流配置（`context/interfaces`） |
| `/form/instance`、`/form-definition` | 表单实例与定义（`form/interfaces`） |
| `/process/definition`、`/process/instance`、`/process/task`、`/process/start`、`/process/run`、`/process/historical`、`/process/print` 等 | 流程定义、实例、任务、统计相关（`flowable/**/interfaces` 等） |
| `/instance` | 流程实例操作（如 `/instance/start`） |
| `/form-flows`、`/form/no`、`/form/instance` | 与 `FlowClient` 对齐的表单流与工单号 |
| `/statistics/*` | 实例数量、工单按业务类型统计等 |
| `/flow/dcm/config` | DCM 配置（类级 `@RequestMapping`） |
| `/task/feedback/log` | 任务反馈日志 |
| `/flow/{processInstanceId}/first/task/active` 等 | 激活任务相关 |

完整方法级清单以各 `*Controller.java` 为准；**完整 OpenAPI 导出**待团队补充。

## Feign Client 接口名

| 接口 | `@FeignClient` name | 包路径 |
|------|---------------------|--------|
| `FlowClient` | `processEngine`（`ServiceRoute.TAG`） | `com.guhecloud.cube.base.flow.client` |
| `ProcessInstanceClient` | `processEngine` | 同上 |
| `FormClient` | `processEngine` | 同上 |

`ProcessInstanceClient` 中声明的 `GET /flow/historical/instance/list` 在 **当前** `server/src` 内**未检索到**同名 `@GetMapping` 实现，**Feign 与 HTTP 实现是否一致待团队补充/核对**。

## MQ 消息（Topic / Tag）

### 本模块源码中直接出现的 Topic 常量

| Topic | 角色 | 备注 |
|-------|------|------|
| `FLOW_EVENT_START` | 消费 | `EventConsumer`，`ProcessEventStartMessage.TOPIC` |
| `FLOW_COMPLETED` | 作为消息体反序列化类型参与处理 | `ProcessEventStartMessage` 负载解析 |
| `FORM_DATA_TO_BUSINESS` | 生产 | `FormEventProduce` → `FormDataToBusinessMessage.TOPIC` |
| `PROCESS_TASK_BACKLOG` | 生产 | `BacklogMessage.TOPIC` |
| `FLOW_COMPLETED` | 生产 | `ProcessCompletedMessage.TOPIC` |
| `WEB_HOOK_CALL_BACK` | 消费 | `WebHooksConsumer`，`WebHookMessage` |
| `AUTO_PASS_TOPIC` | 消费/生产 | `DeleteAutoPassMessageEvent`；`AutoPassMessageTopic`；业务中 `asyncSend` |

### 与外部模块消息类绑定的 Topic（定义在依赖的 client 包中）

| Topic | 说明 |
|-------|------|
| `TODO_TASK_FLOW` | `com.guhecloud.cube.biz.todo.client.message.TodoTaskFlowMessage` — 本服务大量 `syncSendOrderly` |
| `WORK_TASK_FLOW` | `com.guhecloud.cube.biz.work.center.client.message.WorkTaskFlowMessage` |
| `WORK_TASK_TRANSFER` | `WorkTaskTransferMessage.TOPIC`；消费者 `FlowProcessTransferConsumer`，**Tag 选择器 `APPROVE_TASK`** |

### ConsumerGroup（摘录）

- `ProcessEventStartMessage.TOPIC + "_SYNC_ALL"`
- `WorkTaskTransferMessage.TOPIC + "_APPROVE_TASK"`
- `WebHookMessage.GROUP`（`WEB_HOOK_GROUP`）
- `DELETE_AUTO_PASS_MESSAGE`（监听 `AUTO_PASS_TOPIC`）

**RocketMQ 全局命名空间、生产集群与完整订阅关系**待团队补充（以部署与 Nacos 为准）。

## 流程定义 / 实例 API

实现方式为 **Spring MVC + Activiti Engine**，非独立「引擎 HTTP 网关」。

| 能力 | 主要入口（应用内路径） |
|------|------------------------|
| ~~流程定义~~ | ~~`ProcessDefinitionController`：`/process/definition/xml`、部署相关 POST 等~~ **【暂时无实际作用，已废弃，勿调用】** |
| 流程实例 | `ProcessInstanceController`：`/instance/start` 等；`HistoricalProcessInstanceController`：`/process/instance/historical/*` |
| 运行任务 | `ActivatedTaskController`：`/process/task/*`、`/flow/{processInstanceId}/first/task/active` 等 |
| 历史任务 | `HistoricalTaskController`：`/process/task/historical/*` |
| Feign 聚合 | `FlowClient`、`ProcessInstanceClient`、`FormClient` 中声明的路径（与上表配合使用） |

Activiti REST 官方端点是否暴露：**待团队补充**（当前以业务 Controller 为准）。
