# base/work-center 内部导览

## 模块综述与边界

**职责**：工作中心（work-center）面向业务侧聚合「工作任务、标签、统计、交接」等工作台相关接口与 MQ 消费逻辑；对外通过网关前缀 `/workCenter/`（见 `server:server-info`）暴露 HTTP，并通过 `client` 模块提供 Feign 与模型供其他服务调用。

**不负责**（请将域外需求路由到对应模块或平台能力）：通用组织架构/账号体系（见 `ouaa` 等）、通用文件存储实现（见 `file` 客户端与平台文件能力）、消息投递基础设施（见 `message/sdk` 与平台 MQ 配置）、跨业务域的审批/流程引擎本体（本模块仅承载任务视图与状态，与具体业务流编排的边界待团队补充）。

## 当前分层架构

**现状**（未单独使用 `domain` / `mapper` 包名）：接口层为 `controller/`、`bim/controller/` 与 `src-dcm/` 下 DCM 控制器；应用层为 `service/` + `service/impl/`；持久化为 `dao/`（MyBatis 接口）+ `resources/mapper/**/*.xml`；模型为 `entity/`、`param/`、`vo/`；横切为 `config/`、`filter/`、`rocketmq/`、`enumeration/`、`coverts/`。与仓库「最佳实践」示例命名不完全一致时，以**当前目录与包为准**，不在未立项重构前提下主动改造。

## 目录与包结构

说明：本服务源码在 `server/src/` 与 `server/src-dcm/`（非 `server/src/main/java`）。以下为 `packages/base/work-center` 下主要结构（约 2 层及关键子层）。

```
packages/base/work-center/
├── variables.bzl          # VERSION、PACKAGE_GROUP
├── BUILD.bazel            # 当前无 Java 目标
├── client/
│   └── src/               # WorkTaskClient、param、vo、message、enumeration
├── server/
│   ├── BUILD.bazel        # server / server-exec / server-bin / package / server-info
│   ├── resources/         # application.yaml、bootstrap.yaml、dcm、mapper
│   ├── src/               # 业务源码
│   ├── src-dcm/           # DCM 控制器与支撑
│   └── init/
│       ├── BUILD.bazel    # init-mysql pkg_files
│       └── init-db.mysql.sql
```

`server/src/` 下主要子包（内部视角，约 2 层）：

| 子目录 | 角色 |
|--------|------|
| `controller/` | REST：工作任务、标签、统计、工作中心、交接等 |
| `bim/controller/` | BIM 工作统计 |
| `service/`、`service/impl/` | 应用服务 |
| `dao/` | MyBatis Mapper 接口 |
| `entity/`、`param/`、`vo/` | 实体与入参/出参 |
| `config/` | Spring 配置 |
| `rocketmq/consumer`、`rocketmq/message` | MQ 消费与消息体 |
| `filter/` | 过滤器 |
| `enumeration/`、`coverts/` | 枚举与转换 |

**说明**：本模块未使用名为 `domain` / `mapper` 的包目录；持久化接口在 `dao/`，XML 在 `resources/mapper/`。

**开发入口**：

- **Spring Boot 主类**：`com.guhecloud.cube.biz.work.center.server.WorkCenterServer`（`server/BUILD.bazel`）。
- **HTTP**：`server/src/controller/`、`server/src/bim/controller/`、`server/src-dcm/controller/WorkDcmController.java`。
- **MQ**：`server/src/rocketmq/consumer/WorkTaskFlowConsumer.java`。
- **MyBatis XML**：`server/resources/mapper/**/*.xml`。
- **库表迁移**：`application.yaml` 中引用 Liquibase changelog；本地是否执行见 `liquibase.enabled` 与 `liqui-base.execute`。

## BUILD target 说明

| 目标 | 类型 | 说明 |
|------|------|------|
| `//packages/base/work-center/client:deps` | `java_library` | 私有依赖聚合 |
| `//packages/base/work-center/client:client` | `java_library` | Feign 与模型（**对外依赖**） |
| `//packages/base/work-center/client:package` | `export` | Maven `workcenter-client`（**发布**） |
| `//packages/base/work-center/server:deps` | `java_library` | 服务端依赖聚合 |
| `//packages/base/work-center/server:server` | `java_library` | `src`、`src-dcm`、`resources` |
| `//packages/base/work-center/server:server-bin` | `java_binary` | `main_class = WorkCenterServer`（**本地运行**） |
| `//packages/base/work-center/server:server-exec` | `springboot` | 可执行 Spring Boot fat-jar（**本地运行 / 部署**） |
| `//packages/base/work-center/server:package` | `export` | Maven `workcenter-server`（**发布**） |
| `//packages/base/work-center/server:server-info` | `server_info` | 网关前缀、数据库名、主类、`server_id` |
| `//packages/base/work-center/server/init:init-mysql` | `pkg_files` | 初始化 SQL 重命名打包 |

## 本地运行

在仓库根目录执行（需已配置 Bazel、JDK，且 **Nacos 等外部配置可访问**，否则仅验证编译/启动阶段）：

```bash
bazel run //packages/base/work-center/server:server-bin
```

或使用 Spring Boot 打包目标（产出 fat-jar，便于与部署一致）：

```bash
bazel build //packages/base/work-center/server:server-exec
```

默认 HTTP 端口来自 `application.yaml`：`${common-env.work-center.port:10220}`，**以 Nacos `common-env` 为准**；数据库 URL 指向 `cube_biz_work_center`。**若本机无 Nacos/MySQL，完整联调步骤待团队补充**（可用 profile、本地 YAML 覆盖等）。

**环境与中间件前提（摘要）**：需可访问 Nacos（`application.yaml` 中默认 `NACOS_ADDR` 等，实际以团队环境为准）以拉取 `common-env`、`common-datasource`、安全、Redis、RocketMQ、MyBatis 等公共配置；本地数据库需存在 `cube_biz_work_center`（可先通过 `server/init/init-db.mysql.sql` 建库及部分初始化，**完整基线以团队流程为准**）。关键环境变量示例：`NACOS_ADDR`、`NACOS_NAMESPACE`、`NACOS_CONFIG_GROUP`；Liquibase 是否执行由 `liquibase.enabled` 与自定义项 `liqui-base.execute`（引用 `common-env.liqui-base.execute`）等共同决定，**细则待团队补充**。

## 数据层

- **数据库名**：`cube_biz_work_center`（与 `//packages/base/work-center/server:server-info` 的 `database` 字段及 `application.yaml` 中 JDBC URL 一致）。
- **Liquibase**：`application.yaml` 中 `spring.liquibase.change-log` 指向 `db.com.guhecloud.cube.biz.work.center/changelog-master.xml`（classpath）；仓库内可见的变更脚本位于 `server/resources/db/com.guhecloud.cube.biz.work.center/*.mysql.sql`。若运行期缺少 `changelog-master.xml` 或需与平台 Liquibase 包合并加载，**待团队补充**。
- **一次性初始化 SQL**：`server/init/init-db.mysql.sql`（由 `//packages/base/work-center/server/init:init-mysql` 打包分发）；其中注释说明部分历史表/数据未纳入 Liquibase，新表与初始化应逐步迁移至 Liquibase（见该文件头部 TODO）。
- **核心表（概述）**：至少包含工作任务相关表（如 `work_task`、`work_person`）、交接引用（如 `handover_task_ref`）等；**完整 ER/表清单待团队补充**（可维护专文或引用 DBA 文档）。

## 直接依赖说明

以下基于 `server/BUILD.bazel` 中 `deps` 聚合目标的 **Bazel 依赖**（能力说明为工程视角摘要）：

| Bazel label | 提供能力（摘要） |
|-------------|------------------|
| `//packages/base/file/client` | 文件相关 Feign/模型，供业务附件等场景 |
| `//packages/base/message/sdk` | 站内/消息侧 SDK 与模型 |
| `//packages/base/ouaa/client` | 组织与用户等 OUAA Feign 调用 |
| `//packages/platform/base/model:*` | 分页、周期等通用模型（pagination / paging / period） |
| `//packages/platform/base/utils:*` | 集合、日期、对象、字符串、JSON 等工具 |
| `//packages/platform/database` | MyBatis-Plus 与数据访问栈（与 `common-mybatis-plus` 配置配合） |
| `//packages/platform/dcm/server-support` | DCM 声明与控制器支撑（`src-dcm`） |
| `//packages/platform/liquibase` | Liquibase 集成 |
| `//packages/platform/rocketmq` | RocketMQ 客户端与监听基础设施 |
| `//packages/platform/security` | 安全资源服务侧集成 |
| `//packages/platform/server` | Cube 服务端通用启动与 Web 栈 |
| `//third-party/java/alibaba:fastjson2`、`//third-party/java/google:guava`、`//third-party/java/jackson`、`//third-party/java/spring:spring_boot_starter_jdbc` | 第三方 JSON/工具/JDBC |

**客户端库** `client/BUILD.bazel` 的 `deps` 另含 `//packages/platform/client` 等，供 `workcenter-client` 发布物使用；**消费方选型以 Maven 坐标与模块边界为准**。

## 常见开发场景

1. **新增或调整 HTTP 接口**：在 `server/src/controller/`（或 `bim/controller/`、`server/src-dcm/controller/`）增加/修改 Controller；在对应 `service`/`impl` 实现业务；需要持久化时在 `dao/`、`entity/` 与 `resources/mapper/` 增补；对外 DTO 放在 `param/`/`vo/`。网关路由前缀以 `server-info` 的 `gw_prefix` 为准。
2. **新增实体与 Mapper**：新建 `entity` 与 `dao` 接口，补充 `resources/mapper/<Xxx>Mapper.xml`；在 `application.yaml` 已配置 `mapper-locations: classpath:mapper/**/*.xml`，无需改扫描路径除非拆分目录。**表结构变更**优先走 Liquibase（`server/resources/db/com.guhecloud.cube.biz.work.center/`），并与 `init` SQL 策略对齐（见 `init-db.mysql.sql` 注释）。
3. **新增 MQ 消费**：在 `server/src/rocketmq/consumer/` 增加消费者，消息体放在 `rocketmq/message/`；RocketMQ 公共配置来自 Nacos `common-rocketmq.yaml`。**Topic 与消费组命名规范见团队约定或 `references/external-api.md`（若已维护）**。

## 注意事项

- **配置以 Nacos 为准**：`application.yaml` 大量依赖 `spring.config.import` 拉取公共配置，本地仅默认值；**禁止**在未理解覆盖链的情况下仅凭本地片段推断生产行为。
- **Liquibase 与历史 init SQL**：`liquibase.enabled` 默认 `false`，实际是否执行以环境及 `liqui-base.execute` 为准；`init-db.mysql.sql` 含历史基线，与 Liquibase 并存时注意**不要重复执行或冲突**，变更流程待团队补充。
- **DCM**：`server/resources/dcm/` 与 `src-dcm` 为声明式能力相关代码，修改需符合平台 DCM 规范。
- **代码生成物**：若后续引入 OpenAPI/MyBatis 生成目录，**勿手工覆盖生成产物**（当前仓库状态待团队补充是否启用）。
