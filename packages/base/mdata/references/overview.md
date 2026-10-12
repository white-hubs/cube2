# base/mdata — 内部开发者视角

## 模块综述与边界

**职责（一句话）**：`mdata` 是 Cube2 中的**主数据域** Spring Boot 服务，提供区域/档位/商户/供应商/采购商/商品分类/可视化场景配置/网格/H5s 流媒体配置及抓拍截图/业务日志/统计/视频与设备等主数据相关的 REST 与消息协同能力，并通过 `mdata-client` 对外暴露 Feign 契约。

**不负责**：统一账号与用户中心的业务实现（依赖 `ouaa` 等客户端）；网关具体路由、strip 与发布拓扑（以部署与运维配置为准）；跨域业务流程编排若落在其他应用/平台模块，则不在本模块内实现。

## 当前分层架构

**现状模式**：`server/src` 下按**业务子域**划分顶层包（如 `area`、`merchant`、`grid` 等），各域内常见为 `controller` → `service` / `service/impl` → `dao` 或 `mapper`（MyBatis）→ `entity` / `vo` / `param` / `dto`，另含 `rocketmq`、横切 `job`、`excel` 等；**并非**统一的 `domain/` 单包或严格 DDD 分层目录。**现状，未主动重构**；改动时对齐既有子域划分与命名。

顶层按**业务子域**划分目录，常见分层名：

| 子目录模式 | 职责 |
|------------|------|
| `<域>/controller/` | REST 入口 |
| `<域>/service/`、`service/impl/` | 领域服务 |
| `<域>/dao/` 或 `bizlog/mapper/` | MyBatis Mapper 接口 |
| `<域>/entity/`、`vo/`、`param/`、`dto/` | 模型与入参出参 |
| `<域>/rocketmq/` | 消息体、消费者等 |
| `rocketmq/comsumer/` | 全局消费者（如业务日志，目录名拼写为 comsumer） |
| `excel/`、`constants/`、`job/`、`video/` 等 | 横切或专项能力 |

**子域示例**（`server/src` 下第一层目录）：`area`、`stall`、`merchant`、`supplier`、`purchaser`、`goods`、`scene`、`grid`、`config`、`bizlog`、`statistics`、`stat`、`video`、~~`device`（已完全弃用，勿关注、勿修改）~~、`job` 等。

`resources/mapper/**/*.xml` 对应 MyBatis XML；Liquibase changelog 路径见 `application.yaml`。

## 目录与包结构

**模块目录树（2 层）**：

```
packages/base/mdata/
├── variables.bzl
├── client/
│   └── src/                    # Feign、param、vo、dto、部分 rocketmq/message
├── server/
│   ├── BUILD.bazel
│   ├── init/                   # DB 初始化 SQL + pkg_files
│   ├── resources/              # application.yaml、bootstrap.yaml、mapper 等
│   └── src/                    # 主代码（未使用 src/main/java 分层）
```

模块根**无** `BUILD.bazel`，无根级 Bazel target。

**模块顶层**（`packages/base/mdata/`）：`client/`（Feign 与客户端模型）、`server/`（应用与资源）、`references/`（本手册等）、`SKILL.md`、`variables.bzl`；根目录存在占位 `BUILD.bazel`，**无**可引用的根级 Java target。

**服务端**：`server/src` 为扁平 Java 源码树（未采用 `src/main/java`）；`server/resources` 为 Spring 配置、MyBatis `mapper/**/*.xml`、Liquibase 目录 `resources/db/`；`server/init` 为初始化 SQL 与 Bazel `pkg_files`。

**客户端**：`client/src` 为 Feign、`param`/`vo`/`dto` 及部分 MQ 消息体等。

**开发入口**：
- **启动类**：`com.guhecloud.cube.biz.mdata.server.MDataServer`（`server/src/MDataServer.java`）
- **Feign 扫描**：`@EnableFeignClients(basePackages = "com.guhecloud")`
- **组件扫描**：`@SpringBootApplication(scanBasePackages = "com.guhecloud")`

## BUILD target 说明

**全量 target 列表**：

| 标签 | 说明 |
|------|------|
| `//packages/base/mdata/server:deps` | 私有依赖聚合 |
| `//packages/base/mdata/server:server` | 服务端 `java_library`，主源码 + resources |
| `//packages/base/mdata/server:server-bin` | `java_binary`，`main_class`：`com.guhecloud.cube.biz.mdata.server.MDataServer` |
| `//packages/base/mdata/server:server-exec` | `springboot` 宏，可执行 fat-jar |
| `//packages/base/mdata/server:package` | `export` 宏，Maven `mdata-server` |
| `//packages/base/mdata/server:server-info` | `server_info`：`gw_prefix`、`database`、`main_class`、`server_id` 等 |
| `//packages/base/mdata/client:deps` | 客户端私有依赖聚合 |
| `//packages/base/mdata/client:client` | 客户端库 |
| `//packages/base/mdata/client:package` | Maven `mdata-client` |
| `//packages/base/mdata/server/init:init-mysql` | `pkg_files`，初始化 SQL 重命名输出 |

**按用途归类**：

| 用途 | 标签 |
|------|------|
| 本地运行 | `//packages/base/mdata/server:server-bin`、`//packages/base/mdata/server:server-exec` |
| 编译/依赖消费 | `//packages/base/mdata/server:server`、`//packages/base/mdata/client:client` |
| 私有依赖聚合（一般不单引） | `:deps`（server/client 各自） |
| Maven 发布 | `//packages/base/mdata/server:package`、`//packages/base/mdata/client:package` |
| 部署元数据 | `//packages/base/mdata/server:server-info`（`gw_prefix`、`database`、`main_class`、`server_id` 等） |
| DB 初始化制品 | `//packages/base/mdata/server/init:init-mysql` |

## 本地运行

**命令**（仓库根目录执行）：

```bash
bazel run //packages/base/mdata/server:server-bin
```

需要 fat-jar 时：

```bash
bazel build //packages/base/mdata/server:server-exec
```

**前提**：`application.yaml` 通过 `spring.config.import` 从 Nacos 引入 `common-env`、`common-datasource`、`common-rocketmq`、安全、Redis、Job 等；需可用的 **Nacos**、**MySQL**（逻辑库名见下文「数据层」）及与 `common-datasource` 一致的账号权限。**最小本地 profile、必备环境变量表、是否启用本地 Nacos 替身**等待团队补充。

## 数据层

- **逻辑库名**：`cube-biz-mdata`（与 `server/BUILD.bazel` 中 `server_info` 的 `database`、以及 `server/resources/application.yaml` 内 JDBC URL 一致）。
- **Liquibase**：变更文件位于 `server/resources/db/com.guhecloud.cube.biz.mdata/`（如 `changelog-master.xml` 及若干 `changelog-*.mysql.sql`）。默认 `spring.liquibase.enabled: false`，是否执行以环境/Nacos 中 `common-env.liqui-base.execute` 等为准；**待团队补充**各环境启用策略与基线约定。
- **初始化 SQL**：`server/init/init-db.mysql.sql`，经 `//packages/base/mdata/server/init:init-mysql` 重命名发布为 deploy 数据库子包引用制品。
- **ORM**：MyBatis-Plus，`mapper-locations: classpath:mapper/**/*.xml`；Java Mapper 多在各域 `dao` 或 `bizlog/mapper`。
- **核心表概述**：覆盖区域、档位、商户、供应商、网格、商品分类、业务日志等多张业务表；完整清单与 ER **待团队补充**（可另建 `db-schema.md`）。

## 直接依赖说明

以下为 `//packages/base/mdata/server:deps` 中**直接**依赖的 Bazel 标签及能力摘要（节选；完整列表以 `server/BUILD.bazel` 为准）。

| Bazel label | 能力摘要 |
|-------------|----------|
| `//packages/platform/database` | 数据访问与 MyBatis 等平台封装 |
| `//packages/platform/liquibase` | 数据库版本与迁移集成 |
| `//packages/platform/redis` | Redis 客户端与配置 |
| `//packages/platform/rocketmq` | RocketMQ 生产/消费 |
| `//packages/platform/security` | 安全与资源服务相关 |
| `//packages/platform/server` | Spring Boot / 服务端共性 |
| `//packages/platform/job` | 定时与任务调度集成 |
| `//packages/base/ouaa/client`、`//packages/base/dictionary/client`、`//packages/base/work-center/client`、`//packages/base/todo/client`、`//packages/base/file/client` | 账号/字典/工作中心/待办/文件等 base 能力客户端 |
| `//packages/base/mdata/client` | 本模块自引用（类型共享等） |
| `//packages/base/device-center/client`、`//packages/apps/asset/client`、`//packages/apps/maintain/client` | 设备中心与指定应用侧客户端 |
| `//legacy:legacy_wallet_client` | 遗留钱包客户端 |
| `//packages/platform/base/model:*`、`//packages/platform/base/standard:*`、`//packages/platform/base/utils:*` | 通用模型、异常、HTTP、Jackson、工具类 |
| `//third-party/java/...` | EasyExcel、Fastjson2、HttpClient5、POI、Tomcat embed、Guava、校验、Spring JDBC 等 |

客户端 `//packages/base/mdata/client:deps` 主要含 `//packages/platform/client` 与若干 `base/model`，详见 `client/BUILD.bazel`。

## 常见开发场景

1. **新增或修改对外 HTTP 接口**
   - 在对应子域下找到或新增 `*Controller`（路径以方法级注解为准，网关前缀 `/mdata/`）。
   - 同步实现 `service` / `service/impl`、`dao` 或 Mapper 接口、`entity`/`vo`/`param`。
   - MyBatis XML：在 `server/resources/mapper/` 下按现有子路径增补。
   - 若需跨服务调用：在 `client` 增加或修改 `*Client`（`@FeignClient(name = "mdata")` 为服务端自身暴露时由消费方引用本 client）。

2. **新增库表或变更结构**
   - 在 `server/resources/db/com.guhecloud.cube.biz.mdata/` 按团队流程追加 Liquibase changelog；确认环境与 `liquibase.enabled` / `liqui-base.execute` 策略。
   - 大表迁移与回滚策略 **待团队补充**。

3. **新增或调整 MQ 消费/生产**
   - 消费：各域 `rocketmq` 包或全局 `rocketmq/comsumer/`（目录名历史拼写为 `comsumer`）。
   - 生产：查找既有 `RocketMQContextHolder.send` 与 Topic 常量用法对齐。
   - Topic/Group 与 `references/external-api.md` 对照，避免硬编码不一致。

## 注意事项

- **远程配置优先**：大量行为以 Nacos 中 `common-*` 为准，本地 `application.yaml` 仅为基线；调试时若与线上不一致，先核对 Nacos 与 profile。
- **Liquibase 默认关闭**：勿假设启动即执行迁移；启用条件与执行账号权限 **待团队补充**。
- **目录历史拼写**：`rocketmq/comsumer` 为既有路径，新增代码若需统一英文拼写须与团队约定，避免重复目录。
- **代码生成**：若团队对 Mapper/XML 有生成步骤，**禁止**手工覆盖生成产物；具体命令 **待团队补充**。
- **网关与 Feign 路径**：应用内路径与经网关的完整 URL 可能因 strip 规则不同，联调时以网关与 `server_info` 的 `gw_prefix` 为准。

**相关配置文件**：
- 配置：`server/resources/application.yaml`、`server/resources/bootstrap.yaml`
- 初始化 SQL：`server/init/init-db.mysql.sql`（经 `init-mysql` target 发布）
