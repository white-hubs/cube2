# base/alarm 内部导览

## 模块综述与边界

**职责**：`base/alarm` 提供告警域能力，包括告警规则与类型配置、告警记录全生命周期与状态流转、告警统计与 BIM/态势类汇总、以及与待办/消息/工作协同相关的推送与集成。

**不负责**（由其他模块或平台承担，本模块仅通过 Feign/MQ 协同）：设备接入与北向采集细节（见 `device-center` 等）、应急指挥与专项业务应用（见 `emergency` 等应用模块）、统一认证授权策略本身（见 `ouaa` / 平台 security）、通用消息通道基础设施（见 `message` 平台包）。跨域编排与数据权威源以各域文档为准。

## 当前分层架构

本模块 **现状** 为「多子域并存」，未做统一重构；不同子目录采用不同风格，修改时随所在包惯例即可。

| 子域 / 路径 | 分层模式（现状） |
|-------------|------------------|
| `flow/` | 偏 DDD：`interfaces`（REST）→ `application`（命令/查询网关、流程 process、事件 handler）→ `domain`（聚合、PO、工厂）→ `infrastructure`（仓储、MQ、外部 client） |
| `sat/` | 经典 Spring：`controller` → `service` → `dao` / `entity`，`param` / `vo` |
| `statistics/` | `interfaces`（Controller）→ `service/impl` → `param` / `vo` |
| `analysis/` | `controller` → `service` → `mapper`（及 `param` / `vo`） |
| `riskscore/` | `controller`、`service`、`dao`、`job`、DTO/PO 等并列 |
| `config/`、`facade/`、`common/`、`state/`、`util/` | 横切：配置、对外门面、状态机封装、工具 |

另：`server/src-dcm/` 为 DCM HTTP 控制器（与 `//packages/platform/dcm/server-support` 配合），与 `src/` 主业务并列。

## 目录与包结构

说明：本服务源码在 `server/src/`（非 `server/src/main/java`）。以下为 `packages/base/alarm` 下主要结构（2 层及关键子层）。

```
packages/base/alarm/
├── variables.bzl          # VERSION、PACKAGE_GROUP
├── BUILD.bazel            # 当前无 Java 目标
├── client/
│   └── src/               # Feign 接口、param/vo/dto/enumeration
└── server/
    ├── BUILD.bazel        # server / server-exec / server-bin / package / server-info
    ├── resources/         # application.yaml、bootstrap.yaml、mapper、liquibase
    ├── src/               # 业务源码（按域分子目录）
    ├── src-dcm/           # DCM HTTP 控制器
    └── init/
        ├── BUILD.bazel    # init-mysql pkg_files
        └── init-db.mysql.sql
```

`server/src/` 下主要子包（2 层包路径）：

| 包路径（`server/src/` 下） | 用途 |
|---------------------------|------|
| `flow/application` | 应用服务、告警流程步骤与命令/查询网关 |
| `flow/domain` | 领域模型、聚合、值对象、领域事件 |
| `flow/infrastructure` | 仓储实现、RocketMQ、外部服务适配 |
| `flow/interfaces` | REST 接口与 VO |
| `sat/controller` ~ `sat/vo` | SAT 告警类型、消息发送等 |
| `statistics/interfaces` ~ `statistics/service` | 告警统计 HTTP 与实现 |
| `analysis/controller` ~ `analysis/vo` | 安全分析相关接口与模型 |
| `riskscore/*` | 风险指数计算、任务、持久化 |
| `config/` | Spring 等配置类 |
| `facade/emergency` | 应急等门面调用封装 |
| `common/facade` | 通用门面 |
| `state/`、`state/operate` | 告警状态与操作封装 |
| `util/` | 工具类 |

**客户端**：`client/src/` 为 Feign 与对外 DTO/枚举等，供上游依赖（目标见 `client/BUILD.bazel`）。

**关键入口**：
- **Spring Boot 主类**：`com.guhecloud.cube.base.alarm.server.AlarmServer`（`server/BUILD.bazel` 中 `_MAIN_CLASS`）。
- **告警领域入口**：`server/src/flow/interfaces/`（REST）、`flow/application`（应用服务）、`flow/domain`（领域模型）、`flow/infrastructure`（持久化、MQ）。
- **MyBatis XML**：`server/resources/mapper/*.xml`。
- **库表迁移**：`server/resources/db/com.guhecloud.cube.base.alarm/`（Liquibase changelog；是否执行见 `application.yaml` 与 `liqui-base.execute`）。

## BUILD target 说明

| 类别 | Bazel target | 说明 |
|------|----------------|------|
| 本地运行 | `//packages/base/alarm/server:server-bin`、`//packages/base/alarm/server:server-exec` | `java_binary` 直接跑主类；`springboot` 产出可执行 jar |
| 服务端库 | `//packages/base/alarm/server:server`、`//packages/base/alarm/server:deps` | 业务代码 + 资源；依赖聚合（private） |
| 发布 / 元数据 | `//packages/base/alarm/server:package`、`//packages/base/alarm/server:server-info` | Maven `alarm-server`；网关前缀、库名、主类（`database = cube-base-alarm`） |
| 客户端 | `//packages/base/alarm/client:client`、`//packages/base/alarm/client:package` | Feign 库；Maven `alarm-client` |
| DB 初始化包 | `//packages/base/alarm/server/init:init-mysql` | `pkg_files`，供部署侧数据库初始化引用 |

根目录 `packages/base/alarm/BUILD.bazel` 当前无 Java 目标。

## 本地运行

在仓库根目录执行（需已配置 Bazel、JDK，且 **Nacos 等外部配置可访问**，否则仅验证编译/启动阶段）：

```bash
bazel run //packages/base/alarm/server:server-bin
```

或使用 Spring Boot 打包目标（产出 fat-jar，便于与部署一致）：

```bash
bazel build //packages/base/alarm/server:server-exec
```

默认 HTTP 端口来自 `application.yaml`：`${common-env.alarm.port:10106}`，**以 Nacos `common-env` 为准**；数据库 URL 指向 `cube-base-alarm`。**若本机无 Nacos/MySQL，完整联调步骤待团队补充**（可用 profile、本地 YAML 覆盖等）。

**环境与中间件前提**：

- **Nacos**：`application.yaml` 中 `spring.config.import` 引入 `common-env`、`common-datasource`、`common-mybatis-plus`、`common-rocketmq` 等；需可访问的 Nacos 地址与命名空间。
- **MySQL**：库名 **`cube-base-alarm`**（与 `server_info` / `application.yaml` 一致）。
- **其他**：Redis、RocketMQ、平台安全配置等随 `common-*` 配置启用；完整本地 profile **待团队补充**。

## 数据层

- **数据库名**：`cube-base-alarm`（见 `server/BUILD.bazel` 中 `server_info` 与 `server/resources/application.yaml`）。
- **Liquibase**：配置项 `spring.liquibase.change-log` 指向 `db.com.guhecloud.cube.base.alarm/changelog-master.xml`；增量 SQL 位于 `server/resources/db/com.guhecloud.cube.base.alarm/*.mysql.sql`。默认 `spring.liquibase.enabled: false`，实际是否执行以 `common-env` / `liqui-base.execute` 为准。
- **初始化 SQL 包**：`server/init/init-db.mysql.sql`，经 `//packages/base/alarm/server/init:init-mysql` 重命名为 `alarm_server_init.mysql.sql` 供部署数据库流水线使用。
- **ORM**：MyBatis-Plus，`mapper-locations: classpath:mapper/**/*.xml`，XML 在 `server/resources/mapper/`。
- **核心表概述**：告警记录及关联处理人、通知渠道、原因配置等以 `flow/domain/po` 与 Liquibase 脚本为准；SAT/统计/风险子域各有独立 PO/表，**详细表清单待团队补充**（可维护专文 `db-schema.md`）。

## 直接依赖说明

以下依据 `//packages/base/alarm/server:deps` 归纳；细粒度以 `server/BUILD.bazel` 为准。

| Bazel 依赖 | 能力说明 |
|------------|----------|
| `//packages/platform/database` | MyBatis-Plus / 数据访问基础 |
| `//packages/platform/liquibase` | 库表版本迁移 |
| `//packages/platform/redis` | 缓存 |
| `//packages/platform/rocketmq` | 消息队列 |
| `//packages/platform/security` | 安全资源服务与集成 |
| `//packages/platform/server` | 平台服务端通用能力 |
| `//packages/platform/event` | 领域事件基础设施 |
| `//packages/platform/job` | 定时任务 |
| `//packages/platform/dcm/server-support` | DCM 控制器支持（`src-dcm`） |
| `//packages/base/message/sdk`、`//packages/base/message/bracing/client` | 消息推送与支撑 |
| `//packages/base/todo/client` | 待办协同 |
| `//packages/base/ouaa/client` | 账户组织等统一引用 |
| `//packages/base/file/client`、`//packages/base/dictionary/client`、`//packages/base/mdata/client` | 文件、字典、主数据 |
| `//packages/base/device-center/client`、`//packages/base/device-center/north/client` | 设备侧能力 |
| `//packages/base/work-center/client` | 工作中心 |
| `//packages/apps/emergency/client`、`//packages/apps/linkage/client` 等 | 应急、联动等应用侧 API |
| `//packages/apps/risk-source/client`、`//packages/apps/risk-source/risk-score/sdk` | 风险源与风险分 SDK |
| `//third-party/java/...`（Jackson、Guava、EasyExcel、HttpClient5、Tomcat/JDBC 等） | 序列化、工具、HTTP、嵌入式 Web 依赖 |

**客户端库** `//packages/base/alarm/client:deps` 另含分页/周期等基础模型与 `//packages/platform/client`，见 `client/BUILD.bazel`。

## 常见开发场景

1. **新增或调整告警主域 HTTP 行为**
   - 接口：`server/src/flow/interfaces/`
   - 应用逻辑：`flow/application/`（命令/查询服务、`process/step`、事件 `handler`）
   - 领域与持久化：`flow/domain/`、`flow/infrastructure/`
   - 若涉及库表：同步 `server/resources/db/com.guhecloud.cube.base.alarm/` 与 `mapper/*.xml`。

2. **新增 SAT 告警类型或消息发送相关能力**
   - `server/src/sat/controller`、`sat/service`、`sat/dao`、`sat/entity`，参数与返回：`sat/param`、`sat/vo`。

3. **新增告警统计或 BIM 类查询接口**
   - `server/src/statistics/interfaces`、`statistics/service/impl`、`statistics/param`、`statistics/vo`。

4. **DCM 暴露的 HTTP 入口**
   - 在 `server/src-dcm/` 增加或修改控制器（与 DCM 规范一致），并确认 `deps` 中 `dcm/server-support` 已覆盖所需能力。

## 注意事项

- **配置以 Nacos 为准**：端口、数据源、Liquibase 是否执行、MQ/Redis/任务等大量来自 `common-env` 与各 `common-*.yaml`，勿仅依赖本地 `application.yaml` 默认值判断生产行为。
- **多子域风格不一致**：同一需求需先确认改 `flow` 还是 `sat`/`statistics`/`analysis`/`riskscore`，避免跨风格复制粘贴导致边界混乱。
- **代码生成物**：若团队对 Mapper/XML 有生成流程，**禁止手改生成区**（具体路径待团队补充）；当前仓库以手写 XML 为主时以评审约定为准。
- **网关前缀**：对外 HTTP 前缀以 `server_info` 的 `gw_prefix`（`/alarm/`）及网关配置为准，与下游 Feign 命名区分。
