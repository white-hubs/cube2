# base/work-center 对外契约

## 对外 Bazel 标签

| 用途 | 标签 |
|------|------|
| 依赖客户端（Feign 与模型） | `//packages/base/work-center/client:client` |
| Maven 发布 client | `//packages/base/work-center/client:package`（`artifactId`: `workcenter-client`） |
| 依赖服务端库（含 resources） | `//packages/base/work-center/server:server` |
| Maven 发布 server | `//packages/base/work-center/server:package`（`artifactId`: `workcenter-server`） |
| 可执行 `java_binary` | `//packages/base/work-center/server:server-bin` |
| Spring Boot fat-jar | `//packages/base/work-center/server:server-exec` |
| 部署元数据（库名、网关前缀、主类） | `//packages/base/work-center/server:server-info` |
| DB 初始化 SQL 打包 | `//packages/base/work-center/server/init:init-mysql` |

模块根 `//packages/base/work-center` 当前无 `BUILD.bazel` 中声明的可引用 Java 目标。

## HTTP 接口路由前缀

- **网关前缀**（`server_info`）：`/workCenter/`  
  对外完整路径通常为 **`/workCenter` + 应用内路径**（是否带尾部斜杠以网关规则为准）。

应用内路径（**不含**网关前缀；摘自 Controller 源码）：

| 前缀或根路径 | 说明 |
|--------------|------|
| `/work/center` | 首页任务数、任务查询（`WorkCenterController`） |
| `/handover/record` | 交接记录（`HandoverRecordController`） |
| `/work/dcm/config` | DCM 配置（`WorkDcmController`，`src-dcm`） |
| `/work-tasks` | 工作任务分页（GET） |
| `/work-tasks-done` | 已完成工作任务分页（GET） |
| `/work-task/business/batch` | 按业务主键批量删除（DELETE） |
| `/work-tags`、`/work-tags/{id}`、`/work-tags/{workTagCode}/refer/business-type` | 工作标签 |
| `/work-business-url`、`/work-business-url/{id}` | 业务跳转 URL |
| `/work-task-statistics` 及子路径 | 工作统计 |
| `/work-task-tag-statistics`、`/work-task-tags`、`/work-done-task-tags` | 标签统计 |
| `/work-task-bim-statistics` 及子路径 | BIM 统计 |

## Feign Client 接口名

| 接口 | `@FeignClient` | 说明 |
|------|----------------|------|
| `com.guhecloud.cube.biz.work.center.client.WorkTaskClient` | `name = "workCenter"` | 删除批量、标签关联、工作中心任务查询与统计（方法级路径见该接口） |

**待人工补充 / 注意**：`WorkTaskClient` 中 `DELETE /work-tasks/business/batch` 与服务端 `WorkTaskController` 的 `DELETE /work-task/business/batch` 路径不一致，是否经网关重写或需修正契约，请团队确认。

## MQ 消息（Topic / Tag）

| 方向 | Topic | Tag / 选择器 | 说明 |
|------|--------|----------------|------|
| 消费 | `WORK_TASK_FLOW` | 注解未声明 `selectorExpression`（全量消费该 topic） | `WorkTaskFlowConsumer`，`consumerGroup = work_task_msg`，顺序消费 |
| 发送 | `WORK_TASK_TRANSFER` | **`Topic:Tag`**，Tag 为 **工作标签 code**（如交接逻辑中 `entry.getKey()`） | `HandoverRecordServiceImpl` 经 `RocketMQContextHolder.send` 发送 |

其他 MQ 常量类（如 `WorkTaskTransferMessage`）中 Topic 字符串与业务发送点以代码为准；若存在平台侧统一 Tag 命名规范，待团队补充。
