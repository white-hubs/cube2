# base/todo 对外契约速查

## 对外 Bazel 标签（供依赖与构建）

| 标签 | 说明 |
|------|------|
| `//packages/base/todo/client:client` | 客户端 `java_library`（Feign、共享消息与参数类型）。 |
| `//packages/base/todo/client:package` | `todo-client` Maven 发布（`export`）。 |
| `//packages/base/todo/server:server` | 服务端源码与 resources。 |
| `//packages/base/todo/server:server-bin` | `java_binary`，主类 `TodoServer`。 |
| `//packages/base/todo/server:server-exec` | `springboot` 可执行 fat-jar。 |
| `//packages/base/todo/server:package` | `todo-server` Maven 发布（`export`）。 |
| `//packages/base/todo/server:server-info` | `server_info`：库名、网关前缀、`server_id`、主类等。 |
| `//packages/base/todo/server/init:init-mysql` | DB 初始化 SQL 打包（`todo_server_init.mysql.sql`）。 |

模块根 `//packages/base/todo:...` 无独立 java 目标（根 `BUILD.bazel` 为空）。

## HTTP 接口路由前缀

- **网关前缀**（`server_info`）：`/todo/`
- **经网关访问时**：完整路径 ≈ `/todo/` + 下列「应用内路径」（以网关与 StripPrefix 规则为准；若网关另有拼接规则，**待团队补充**）。

应用内路径（摘自 Controller 方法映射）：

| 前缀/路径模式 | 说明 |
|---------------|------|
| `/todo-tasks`、`/todo-tasks-done` | 待办分页、已办分页 |
| `DELETE /todo-task/business/batch` | 按业务主键批量删除 |
| `/todo-tag`、`PUT /todo-tag/{id}`、`GET /todo-tags`、`DELETE /todo-tag/{id}` | 标签 |
| `POST /todo-tag/{todoTagCode}/refer/business-type` | 标签关联业务类型 |
| `/todo-business-url`、`PUT /todo-business-url/{id}` | 业务跳转 URL |
| `/todo-task-statistics`、`/todo-task-statistics/...` | 任务统计 |
| `/todo-task-tag-statistics`、`/todo-task-tags`、`/todo-done-task-tags`、`POST /todo-task-tag/number-statistics` | 标签统计 |
| ~~`/todo-task-bim-statistics`、`/type`、`/tag`~~ | ~~BIM 统计~~（**已废弃**） |
| `/todo/dcm/config` 及其子路径 | DCM 配置（继承平台 `AbstractBaseDcmController` 的通用路由，**子路径待团队补充**若需逐项列出） |

## Feign Client 接口名

| 接口类（全限定名） | `@FeignClient` | 备注 |
|-------------------|----------------|------|
| `com.guhecloud.cube.biz.todo.client.TodoTaskClient` | `name = "todoCenter"` | 路径示例：`/todo-task/business/batch`、`/todo-tag/{todoTagCode}/refer/business-type`、`DELETE /todo-tag/{id}`、`POST /todo-task-tag/number-statistics` |

## MQ 消息（Topic / Tag）

| 角色 | Topic | ConsumerGroup / 说明 | Tag |
|------|--------|------------------------|-----|
| 消费 | `TODO_TASK_FLOW`（常量见 `TodoTaskFlowMessage.TOPIC`） | `todo_task_msg` | **未在 Listener 配置 selector**；业务分支依赖消息体 `operationType`（INSERT/CHANGE/DELETE） |
| 生产 | 同上（常量被 client/server 消息类引用） | — | 本模块内无 `RocketMQTemplate` 发送实现；**生产者所在服务/模块待团队补充** |
