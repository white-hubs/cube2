# base/alarm 对外契约（消费者视角）

## 对外 Bazel 标签

| 用途 | 标签 |
|------|------|
| 引用 Feign 与 DTO | `//packages/base/alarm/client:client` |
| 发布 `alarm-client` | `//packages/base/alarm/client:package` |
| 服务端实现（通常不直接依赖） | `//packages/base/alarm/server:server` |
| 发布 `alarm-server` | `//packages/base/alarm/server:package` |

模块根 `packages/base/alarm/BUILD.bazel` 无聚合 `java_library` / `export` 目标。

## HTTP 接口路由前缀

**网关前缀**（`server_info`）：`/alarm/`  
应用内 Controller **未**统一使用类级 `@RequestMapping`；以下按代码中的 `@RequestMapping` 或路径**族**归纳（经扫描 `@RequestMapping` / `*Mapping` 推断）。经网关访问时一般为 **`/alarm/` + 下列路径**（以实际网关 strip 规则为准）。

| 路径族 / 前缀 | 说明 |
|---------------|------|
| `/alarm-record`、`/alarm-records`、`/alarm-standard-records` | 告警记录主流程 |
| `/alarm-msg` | 未确认消息等 |
| `/alarm-reason` | 报警原因配置 |
| `/alarm-level/play-config` | 等级播放配置 |
| `/alarm-statistics`、`/public/alarm-statistics` | 统计与公开统计 |
| `/alarm-bim-statistics`、`/alarm-bim`、`/alarm-bim-result`、`/alarm-bim-device` | BIM 相关统计 |
| `/sat-alarm-msg`、`/sat-alarm-type`、`/sat-alarm-level`、`/sat-alarm-business-url`、`/sat-alarm` | SAT 报警与消息 |
| `/safety/analysis` + 子路径 | `SafetyAnalysisController` 类级前缀 |
| `/alarm/dcm/config` | DCM 报警配置（`src-dcm`） |
| `/risk-index` | 市场风险指数 |
| `/alarm-record/follow-up` | 跟进记录 |
| `/alarm-records/ref/devices` | 关联设备 |

部分 `AlarmStatisticsController` 方法路径**缺少前导 `/`**（如 `cold-water/...`、`alarm/record/...`）；若经网关拼接异常，**待团队补充**统一规范或网关 rewrite 说明。

## Feign Client 接口名

均配置 **`@FeignClient(name = "alarm")`**（服务发现名与 `bootstrap.yaml` 中 `spring.application.name` 一致）。

| Java 接口 |
|-----------|
| `AlarmRecordClient` |
| `AlarmStatisticsClient` |
| `AlarmBimStatisticsClient` |
| `SatAlarmTypeClient` |
| `SatAlarmLevelClient` |
| `SatAlarmMsgSendClient` |

路径以各接口方法上的 `@GetMapping` / `@PostMapping` 等为准（与上表 HTTP 族一致）。

## MQ 消息（Topic / Tag）

### 本模块消费（`RocketMQMessageListener`）

| Topic | ConsumerGroup | Tag / selector | 说明 |
|-------|---------------|----------------|------|
| `WORK_TASK_TRANSFER` | `WORK_TASK_TRANSFER_ALARM` | `selectorExpression = "ALARM_TASK"` | 报警任务交接 |
| `LINKAGE_MESSAGE_NOTIFY` | `LINKAGE_MESSAGE` | `SelectorType.TAG`（注解未写 `selectorExpression`） | 联动消息通知 |
| `LINKAGE_DEVICE_CTL_NOTIFY` | `LINKAGE_DEVICE` | `SelectorType.TAG`（注解未写 `selectorExpression`） | 联动设备控制 |
| `TENANT_INIT` | `alarm_reason_init` | `SelectorType.TAG`（注解未写 `selectorExpression`） | 租户初始化时初始化报警原因 |

跨模块消息类定义位置（供核对常量）：`WorkTaskTransferMessage`（work-center client）、`LinkageMessageNotifyMessage` / `LinkageDeviceCtlMessage`（本服务 message 包）、`TenantInitMessage`（ouaa client）。

### 本模块发送（节选）

| Topic | 说明 |
|-------|------|
| `TODO_TASK_FLOW` | 待办任务流程（`TodoTaskFlowMessage`） |
| `WORK_TASK_FLOW` | 工作任务流程（`WorkTaskInitMessage` / `WorkTaskFlowCirculationMessage`） |
| `DEVICE_LIFE_EVENT` | 设备生命周期事件 |

完整发送点见 `flow/infrastructure/mq/rocketmq/todo/*.java` 与 `AlarmMessageSender.java`。
