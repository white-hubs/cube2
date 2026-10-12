# fusion-communicate 概览

## 模块综述与边界

**职责**：`fusion-communicate` 为融合通信领域服务，覆盖会议房间、融合通讯账号与文件、GPS 轨迹上报、以及腾讯实时音视频（TRTC）与迅一等第三方对接与同步任务。

**本模块不负责**（仅消费依赖或应由其他域承接）：统一认证与用户主数据（见 `packages/base/ouaa` 等账号域）、通用字典与消息能力（`dictionary` / `message` 客户端）、平台级安全/数据源/任务调度等横切能力（见 `packages/platform/*`）、网关路由与 StripPrefix 的最终落地（以部署与网关配置为准）。对外 HTTP/MQ 契约细节见同目录 `external-api.md`。

## 当前分层架构

**现状（未重构标注）**：本模块内存在多种组织方式并存，修改时对齐所在子包惯例即可。

- **`conference/`**：偏 DDD 分层——`interfaces`（HTTP）→ `application`（查询/命令、事件）→ `domain`（聚合、实体、工厂）→ `infrastructure`（仓储、消息发送等）。
- **`gps/`**：经典 Web 分层——`controller` / `service` / `mapper` / `po` / `param` / `vo`。
- **`third/account/`**：融合账号子域——含 `application`、`domain`、`infrastructure`、`interfaces`、`facade`（按对接拆分 Tencent 等），与 `third/tencent`、`third/xunyi` 的 API、Job、配置协同。
- **根包 `controller` / `service`**：融合通讯账号与文件的入口控制器与聚合服务；**`third/base`**：账号 VO/配置刷新/公共抽象等横切支撑。
- **`config`、`param`、`vo`**：横切配置与入参/出参模型。

## 目录与包结构

```text
fusion-communicate/
├── BUILD.bazel                 # 当前无 java 目标（仅注释）
├── variables.bzl               # VERSION、PACKAGE_GROUP
├── SKILL.md
├── references/
│   ├── external-api.md
│   └── overview.md
└── server/
    ├── BUILD.bazel             # server / server-bin / server-exec / package / server-info
    ├── init/
    │   ├── BUILD.bazel         # init-mysql（pkg_files）
    │   └── init-db.mysql.sql
    ├── resources/
    │   ├── application.yaml
    │   └── bootstrap.yaml
    └── src/
        ├── FusionCommunicateServer.java
        ├── conference/         # 会议房间（DDD 分层）
        ├── config/
        ├── controller/         # 融合通讯账号、文件
        ├── gps/
        ├── param/
        ├── service/
        ├── third/              # 腾讯 / 迅一 / account / base
        └── vo/
```

`server/src` 主要子包（再向下 1 层）：

- `conference/`：`application`、`domain`、`infrastructure`、`interfaces`
- `gps/`：`controller`、`mapper`、`param`、`po`、`service`、`vo`
- `third/`：`account`、`base`、`tencent`、`xunyi`

**顶层目录说明：**

- **`server/`**：唯一 Bazel Java 包与服务资源根。
- **`server/init/`**：数据库初始化 SQL 的 Bazel 打包目标（`init-mysql`），供部署流水线引用。
- **`references/`**：模块手册与对外契约（本文件、`external-api.md`）。

**开发入口：**

- **Spring Boot 主类**：`server/src/FusionCommunicateServer.java`
  - `@SpringBootApplication(scanBasePackages = "com.guhecloud")`
  - `@EnableFeignClients(basePackages = "com.guhecloud")`
- **业务入口（按域）**：
  - HTTP：`server/src/controller`、`conference/interfaces`、`gps/controller`、`third/*/controller`
  - 账号策略与第三方：`server/src/third/account`、`third/base`、`third/tencent`、`third/xunyi`
  - 定时任务：`third/tencent/job`、`third/xunyi/job`、`third/base/config/job` 等（`@JobAnnotation`）

## BUILD target 说明

| Target | 说明 |
|--------|------|
| `:deps` | 私有依赖聚合（dictionary/file/message/sdk、platform、third-party 腾讯 TRTC 等）。 |
| `:server` | 主 `java_library`，源码 `src/**/*.java`，资源 `resources/**`。**对外依赖**目标。 |
| `:server-bin` | `java_binary`，入口 `com.guhecloud.cube.biz.fusion.communicate.server.FusionCommunicateServer`。**本地运行**目标。 |
| `:server-exec` | `springboot` 可执行 JAR。**打包运行**目标（常用于与生产相近的方式）。 |
| `:package` | `export`，发布 `fusioncommunicate-server`。**发布**目标。 |
| `:server-info` | 部署元数据：数据库 `cube_biz_fusioncommunicate`、网关前缀 `/fusion-communicate/`、`server_id` `fusionCommunicate` 等。 |

`server/init`：

| Target | 说明 |
|--------|------|
| `:init-mysql` | 将 `init-db.mysql.sql` 打包为 `fusion_communicate_server_init.mysql.sql` 供数据库初始化流水线使用。 |

**补充**：模块根 `//packages/base/fusion-communicate` 的 `BUILD.bazel` 当前无 Java 目标（仅注释），实际构建入口均在 `server/BUILD.bazel`。

## 本地运行

在仓库根目录执行：

```bash
# 编译服务端库
bazel build //packages/base/fusion-communicate/server:server

# 以 java_binary 直接运行（需本地已配置 Nacos/数据库等，否则可能启动失败）
bazel run //packages/base/fusion-communicate/server:server-bin

# springboot fat-jar 目标（常用于与生产相近的打包运行方式）
bazel run //packages/base/fusion-communicate/server:server-exec
```

默认 **HTTP 端口**见 `server/resources/application.yaml`：`${common-env.fusion-communicate.port:10222}`（实际以 Nacos `common-env` 为准）。

**运行前提（无法从仓库单独推断的细节请见团队文档）**：

- **Nacos**：`application.yaml` 通过 `spring.config.import` 拉取多份 `common-*.yaml`（环境、Redis、安全、MyBatis、RocketMQ、数据源、Job 等）；需可用的 `NACOS_ADDR` / 命名空间及对应配置集。
- **数据源**：默认 JDBC URL 指向库名 `cube_biz_fusioncommunicate`（与 `server_info.database` 一致），主机与端口来自 Nacos `common-env`。
- **服务发现**：`spring.cloud.nacos.discovery.enabled: true`（与配置中心 namespace 对齐）。

**待团队补充**：推荐的最小本地依赖清单、是否与聚合调试应用联调的固定命令、以及本地关闭 Nacos 的 profile（若存在）。

## 数据层

- **数据库名**：`cube_biz_fusioncommunicate`（与 `server/BUILD.bazel` 中 `server_info` → `database` 一致）。
- **Liquibase**：`application.yaml` 中 `spring.liquibase.change-log` 指向 `db.com.guhecloud.cube.biz.fusion.communicate/changelog-master.xml`；**默认** `spring.liquibase.enabled: false`，是否由环境打开以 Nacos / `common-env` 为准。增量 SQL 位于 `server/resources/db/com.guhecloud.cube.biz.fusion.communicate/`（如 `changelog-1.0.0.mysql.sql` 等）。**当前仓库** `server/resources/db/` 下未检出 `changelog-master.xml`（仅有分版本 `.mysql.sql`），若启用 Liquibase 时装配方式 **待团队补充**。
- **初始化 SQL**：`server/init/init-db.mysql.sql` 经 `//packages/base/fusion-communicate/server/init:init-mysql` 打包，供数据库初始化流水线使用。
- **ORM**：MyBatis-Plus，`mapper-locations: classpath:mapper/**/*.xml`（见 `server/resources/mapper/`）。
- **核心表/领域对象概述**：会议房间与用户、融合第三方账号与分配、GPS 历史等——**待团队补充**与 ER/表清单专文（若有）。

## 直接依赖说明

`server/BUILD.bazel` 中 `:deps` 聚合的主要 Bazel 依赖（节选，能力说明）：

| Bazel label | 能力 |
|-------------|------|
| `//packages/base/dictionary/client` | 字典客户端 |
| `//packages/base/file/client` | 文件客户端 |
| `//packages/base/message/sdk` | 消息 SDK |
| `//packages/base/ouaa/client` | 账号/用户相关客户端 |
| `//packages/platform/base/model:*` | 分页、周期等通用模型 |
| `//packages/platform/base/standard:*` | 异常、HTTP 标准封装 |
| `//packages/platform/base/utils:*` | Base64、集合、日期、JSON 等工具 |
| `//packages/platform/database` | 数据访问与 MyBatis-Plus 等栈 |
| `//packages/platform/job` | 平台任务调度注解与基础设施 |
| `//packages/platform/liquibase` | 数据库版本迁移 |
| `//packages/platform/redis` | Redis 支持 |
| `//packages/platform/security` | 安全相关 |
| `//packages/platform/server` | 服务端公共能力 |
| `//third-party/java/apache:hc5` 等 | HTTP 客户端 |
| `//third-party/java/jackson` | JSON |
| `//third-party/java/tencent:tencentcloud_trtc` | 腾讯实时音视频相关 SDK |

## 常见开发场景

1. **新增或调整 HTTP 接口**
   - 会议域：改 `server/src/conference/interfaces/*Controller.java` 及 `application`/`domain`/`infrastructure` 中对应服务、仓储、工厂。
   - 融合账号/文件（根包）：`server/src/controller/FusionCommunicate*Controller.java`、`server/src/service/*`。
   - GPS：`server/src/gps/controller`、`service`、`mapper`、`resources/mapper/`。
   - 腾讯/迅一：`server/src/third/tencent` 或 `third/xunyi` 下 `controller`、`job`、`api` 等。
   - 路由前缀与对外契约请同步维护 `references/external-api.md`。

2. **数据库结构变更**
   - 在 `server/resources/db/com.guhecloud.cube.biz.fusion.communicate/` 增加/调整 changelog SQL，并确认 `changelog-master.xml` 引用链（若仓库中 master 由生成或他处维护则 **待团队补充**）。
   - 部署初始化路径关注 `server/init/init-db.mysql.sql` 与 `init:init-mysql` 目标。

3. **第三方对接与定时同步**
   - 腾讯：`third/tencent`（含 `TencentCommunicateRoomProvider`、`job`、配置 `TencentConfig` 等）。
   - 迅一：`third/xunyi`（`controller`、`job`、事件处理等）。
   - 账号聚合与 Facade：`third/account` 下 `facade` / `application`。

## 注意事项

- **配置覆盖**：大量运行参数以 Nacos `common-env` 及多份 `common-*.yaml` 为准，本地仅 `application.yaml`/`bootstrap.yaml` 不一定反映生产行为。
- **Liquibase 开关**：默认关闭，环境误配可能导致迁移未执行或重复执行，以团队约定为准。
- **模块根 BUILD**：`packages/base/fusion-communicate/BUILD.bazel` 无 Java 目标，勿在根目录寻找 `java_library`；以 `server/BUILD.bazel` 为准。
- **网关前缀**：`server_info` 中 `gw_prefix` 为 `/fusion-communicate/`，与 Controller 应用内路径拼接规则见 `external-api.md`。
- **代码生成**：若后续引入生成类（Mapper/PO 等），**待团队补充**禁止手改的路径约定。
