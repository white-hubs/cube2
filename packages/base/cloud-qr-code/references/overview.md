# cloud-qr-code 概览

## 模块综述与边界

本模块提供 **云二维码** 相关能力：二维码类型/实例/项目/地址配置的管理，短链与扫码路由解析，以及供外部集成的 **HTTP API** 与 **Java SDK**。

**不负责**：统一身份认证与账号体系（见 `//packages/base/ouaa` 等）、通用对象存储与文件托管、跨业务消息编排（RocketMQ 等仅按平台配置接入，本模块不定义业务 Topic 契约）、以及其它 base 模块中的领域逻辑。网关路由与部署拓扑以运维/平台约定为准。

## 当前分层架构

服务端采用 **CQRS 风格**（现状，未单独抽象名为 `service` 的包）：

- **`interfaces`**：`controller`、`vo`，对外 HTTP。
- **`application`**：`commandgateway`（写路径：命令、校验器）、`querygateway`（读路径：Query、`*QueryService`）。
- **`domain`**：实体、值对象、枚举、持久化对象（PO）。
- **`infrastructure`**：仓储与 Mapper、DAO、与外部 HTTP 客户端、拦截器、短码生成等。
- **`config`**：Spring / MyBatis 等配置类。
- **根包**（`server/src` 下）：`CloudQrCodeServer`、`CloudQrCodeConfiguration` 等启动与装配。

SDK 为薄客户端分层：`CloudQrCodeClient` → `config` / `http` / `model` / `exception`。

## 目录与包结构

**模块顶层目录**：

```
packages/base/cloud-qr-code/
├── BUILD.bazel                 # 空文件，无 target
├── variables.bzl               # VERSION、PACKAGE_GROUP
├── sdk/
│   ├── BUILD.bazel
│   ├── README.md
│   └── src/                    # Java SDK 源码
├── server/
│   ├── BUILD.bazel
│   ├── init/                   # DB 初始化 SQL 与 BUILD
│   ├── resources/              # bootstrap.yaml、application.yaml、db changelog
│   └── src/                    # 服务端源码
```

| 路径 | 用途 |
|------|------|
| `variables.bzl` | 部分子包引用的 `VERSION`、`PACKAGE_GROUP`（与 `sdk/BUILD.bazel` 等一致时需与发布坐标核对） |
| `sdk/` | Java SDK 源码与 `BUILD.bazel`（`:sdk`、`:package`） |
| `server/` | Spring Boot 服务、`resources/`、`init/` |
| `references/` | `overview.md`、`external-api.md` 等手册分册 |
| 根 `BUILD.bazel` | 当前为空，无根级 target |

**服务端 `server/src` 下主要子包**：

| 目录 | 说明 |
|------|------|
| `application/commandgateway` | 命令侧服务、命令对象、校验器 |
| `application/querygateway` | 查询侧服务、Query 对象 |
| `config` | Spring 等配置类 |
| `domain` | 实体、值对象、枚举、PO |
| `infrastructure` | 仓储、Mapper、DAO、客户端、拦截器等 |
| `interfaces` | `controller`、`vo` |

向下展开两层：`application/commandgateway`、`application/querygateway`、`config`、`domain/entity|enumeration|po|valueobject`、`infrastructure/client|converters|dao|generator|interceptor|repository|utils`、`interfaces/controller|vo`；根目录另有 `CloudQrCodeServer.java`、`CloudQrCodeConfiguration.java`。

**SDK `sdk/src` 一层结构**（包前缀 `com.guhecloud.cube.base.cloud.qr.code.sdk`）：

| 目录/文件 | 说明 |
|-----------|------|
| `CloudQrCodeClient.java` | SDK 客户端入口 |
| `config/` | `CloudQrCodeSdkConfig` |
| `http/` | HTTP 封装 |
| `model/` | 请求/响应 DTO |
| `exception/` | `CloudQrCodeSdkException` |

**开发入口与关键路径**：

- **服务端主类**：`com.guhecloud.cube.base.cloud.qr.code.server.CloudQrCodeServer`
- **REST**：`server/src/interfaces/controller/*.java`
- **读写应用服务**：`server/src/application/commandgateway/`、`server/src/application/querygateway/`

## BUILD target 说明

| Target | 用途 |
|--------|------|
| `//packages/base/cloud-qr-code/sdk:deps` | SDK 私有导出依赖 |
| `//packages/base/cloud-qr-code/sdk:sdk` | SDK 库 |
| `//packages/base/cloud-qr-code/sdk:package` | Maven 发布 `cloud-qr-code-sdk` |
| `//packages/base/cloud-qr-code/server:deps` | 服务端传递依赖聚合 |
| `//packages/base/cloud-qr-code/server:server` | 服务端主库 + resources |
| `//packages/base/cloud-qr-code/server:server-bin` | `java_binary`，`main_class` → `CloudQrCodeServer` |
| `//packages/base/cloud-qr-code/server:server-exec` | **本地运行推荐**：Spring Boot 可执行 |
| `//packages/base/cloud-qr-code/server:package` | Maven 发布 `cloud-qr-code-server` |
| `//packages/base/cloud-qr-code/server:server-info` | 元数据：库名、网关前缀、`public_path_patterns` 等 |
| `//packages/base/cloud-qr-code/server/init:init-mysql` | 初始化 SQL 打包映射 |

## 本地运行

**命令**（仓库根目录）：

```bash
bazel run //packages/base/cloud-qr-code/server:server-exec
```

**端口**：默认 **10221**（`${common-env.cloud-qr-code.port:10221}`，见 `server/resources/application.yaml`）。

**配置与中间件前提**：`application.yaml` 通过 `spring.config.import` 依赖 **Nacos**（如 `NACOS_ADDR`、`NACOS_NAMESPACE`、`NACOS_CLOUD_CONFIG` 等占位）及多份 `common-*.yaml`；数据源主机/端口等来自 `common-env`。**完整环境变量清单与本地 profile 示例见团队文档**；无 Nacos 时能否仅依赖仓库内默认值启动 **待团队补充**。

## 数据层

- **数据库名**（与 `server_info.database` 一致）：`cube_base_cloud_qr_code`。
- **Liquibase**：`application.yaml` 中 `spring.liquibase.change-log` 指向 `db.com.guhecloud.cube.base.cloud.qr.code/changelog-master.xml`；仓库内可见变更 SQL 位于 `server/resources/db/com.guhecloud.cube.base.cloud.qr.code/`（如 `changelog-1.0.0.mysql.sql`）。**若 `changelog-master.xml` 未纳入本仓库或需与运行环境对齐，待团队补充**。
- **初始化 SQL**：`server/init/init-db.mysql.sql`，经 `//packages/base/cloud-qr-code/server/init:init-mysql` 映射为部署侧使用的文件名；文件内注明部分表曾未完全由 Liquibase 管理，**历史与迁移策略待团队补充**。
- **核心表概述**：二维码、类型、项目、路由/短链、地址配置等 PO 见 `domain/po/` 与 Mapper；**业务字段说明可另立 db 文档，待团队补充**。

## 直接依赖说明

以下摘自 `server/BUILD.bazel` 的 `:deps`（及 `server` 目标额外依赖），说明「通过该 label 得到什么能力」：

| Bazel label | 能力摘要 |
|-------------|----------|
| `//packages/base/file/client` | 文件相关客户端能力 |
| `//packages/base/message/sdk` | 消息 SDK |
| `//packages/base/ouaa/client` | 统一认证与账户相关客户端 |
| `//packages/platform/base/model:pagination`、`//packages/platform/base/model:paging` | 分页模型 |
| `//packages/platform/base/utils:*` | Base64、集合、日期、JSON、对象、字符串等工具 |
| `//packages/platform/database` | MyBatis-Plus、多租户/数据源上下文、分页工具等 |
| `//packages/platform/liquibase` | Liquibase 与数据源集成（参见 `CloudQrCodeConfiguration`） |
| `//packages/platform/security` | 安全与资源服务相关 |
| `//packages/platform/server` | 平台服务端公共能力 |
| `//third-party/java/github:jsqlparser`、`//third-party/java/mybatis-plus:jsqlparser` | SQL 解析（MyBatis 生态） |
| `//third-party/java/hutool:extra` | Hutool 扩展 |
| `//third-party/java/jackson` | Jackson |
| `//third-party/java/spring:spring_boot_starter_jdbc` | JDBC 起步依赖 |
| `@maven//:com_aliyun_alibabacloud_dysmsapi20170525` | 阿里云短信 API（挂在 `:server` 上，非 `:deps` 导出聚合） |

**SDK**（`sdk/BUILD.bazel` → `:deps`）：`//packages/platform/base/standard:jackson`、`//third-party/java:lombok`、`//third-party/java/jackson`。

## 常见开发场景

1. **新增或调整 HTTP 接口**
   - 修改 `server/src/interfaces/controller/` 下对应 `*Controller.java`。
   - 在 `application/commandgateway` 或 `querygateway` 增加/调整 `*CommandService` / `*QueryService`、命令与 Query 对象。
   - VO 放在 `interfaces/vo/`；对外契约摘要可同步 `references/external-api.md`。

2. **新增持久化实体与访问**
   - `domain/po/`、`domain/entity/`（及枚举、值对象如需）。
   - `infrastructure/repository/mapper/`、`infrastructure/repository/*Repository.java`；必要时 `infrastructure/dao/`、`infrastructure/converters/`。
   - 新增 Liquibase 变更：`server/resources/db/...` 下 SQL，并维护 master changelog（**具体流程待团队补充**）。

3. **消费方集成 SDK**
   - 依赖 `//packages/base/cloud-qr-code/sdk:sdk` 或 Maven 坐标发布的 `cloud-qr-code-sdk`；入口 `CloudQrCodeClient`，配置见 `sdk/src/config/` 与 `sdk/README.md`。

## 注意事项

- **Nacos 覆盖**：大量有效配置来自 Nacos 导入的 `common-env.yaml`、`common-datasource.yaml`、`common-security-*.yaml` 等，仓库内 YAML 仅为占位；**切勿假设仅本地文件即可复现生产行为**。
- **Liquibase 开关**：`application.yaml` 中 `spring.liquibase.enabled` 可为 `false`，实际是否执行以环境与 `common-env.liqui-base.execute` 等为准；变更库结构前确认环境与流水线约定。
- **日志**：`mybatis-plus` 配置了 `StdOutImpl`，生产是否保留 **待团队补充**。
- **代码生成**：未发现典型生成物目录；若后续引入生成 Mapper/XML，**禁止手改生成区**（若有则按团队生成规范执行）。
- **模块根 `BUILD.bazel`** 为空，勿在根目录新增 target 除非团队统一约定。
