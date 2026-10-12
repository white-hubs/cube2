# base/event — 对外契约摘录

> 以 `packages/base/event/server/BUILD.bazel` 与 `server/src` 源码为准；未在仓库中出现的信息标为待团队补充。

## 对外 Bazel 标签

| 标签 | 说明 |
|------|------|
| `//packages/base/event/server:server` | 应用 `java_library`（源码 + `resources`）。 |
| `//packages/base/event/server:server-bin` | `java_binary`，主类 `com.guhecloud.cube.base.event.server.EventServer`。 |
| `//packages/base/event/server:server-exec` | `springboot` 可执行产物。 |
| `//packages/base/event/server:package` | Maven `export`（`artifactId = cube-biz-event`）。 |
| `//packages/base/event/server:server-info` | `server_info`：库名、网关前缀、`server_id`、`main_class` 等。 |
| `//packages/base/event/server:deps` | `visibility private`，聚合平台依赖，一般不直接对外引用。 |

模块根目录 `//packages/base/event:BUILD.bazel` 当前无目标。

## HTTP 接口路由前缀

- **网关前缀**：`/event/`（`server/BUILD.bazel` 中 `server_info` 的 `gw_prefix`）。
- **应用内自研 Controller**：当前源码中未发现；完整对外路径清单 **待团队补充**。

## MQ 消息（Topic / Tag）

| 方向 | Topic | ConsumerGroup / 说明 | Tag / 选择器 |
|------|--------|----------------------|----------------|
| 消费（订阅） | `BUSINESS_DATA_EVENT_TRIGGER`（`BusinessEventMessage.TOPIC`） | `BUSINESS_EVENT` | `selectorType = TAG`；**未在注解中声明 `selectorExpression`**，具体 Tag 过滤规则 **待团队补充** |
| 生产（再投递） | **动态**（`BusinessEventRecord.getTopic()`，来自表 `event_record`） | — | 非固定 Tag；载荷类型为 `EventMessage`；与下游系统的字段/Topic 白名单 **待团队补充** |
