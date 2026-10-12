# ouaa — 内部开发者视角

## 模块综述与边界

- **职责（一句话）**：`ouaa` 为统一账号认证与用户中心服务，提供多租户下的用户/组织/角色/终端/应用与上下文等 API，集成 OAuth2/JWT、权限与 RocketMQ 消息，并通过 `client` 对外暴露 Feign 契约。
- **不负责**：
  - **通用协议与工具**（分页模型、通用异常、JsonUtils 等）：见 `packages/platform/base`。
  - **网关路由与 rewrite 的最终落地**：以部署侧 `server_info` 与网关配置为准；本模块仅在 `server_info` 中声明 `gw_prefix`、`gw_path_rewrite` 等元数据。
  - **其他业务域**（非账号/租户/认证相关）：对应其他 `packages/base/*` 或 `packages/apps/*` 模块。
- 更完整的「负责 / 不负责」列表见模块根目录 [`SKILL.md`](../SKILL.md) 中「模块边界与职责」。

## 当前分层架构

- **现状（未要求重构前保持）**：HTTP 层 → 业务层 → 持久化层，辅以安全与消息。
  - **Controller**：`controller/**`（含 `admin`、`authenticated`、`context`、`tenant`、`login` 等子包），对外 HTTP 入口。
  - **Service**：`service/**` 与 `service/**/impl/**`，业务编排与领域逻辑；部分能力拆在 `service/login`、`service/organization`、`service/user` 等子包。
  - **实体与持久化**：`entity/**` 与表映射；数据访问为 **`dao/*Mapper.java`（MyBatis）**，XML 位于 `resources/mapper/**`（本仓库**无**独立 `mapper` Java 包）。
  - **安全与 OAuth**：`security/**`（含 `oauth2`、`authentication`、`redis` 等）。
  - **权限**：`permission/**`（含 `common` 下 service/controller/dto/po 等）。
  - **消息**：`rocketmq/message`、`rocketmq/consumer`、`rocketmq/producer`。
  - **其他**：`captcha/**`（验证码）、`third/**`（第三方对接）、`behavior/**`、`converters/**`、`vo/**`、`param/**`、`dto/**` 等支撑包。
- 本模块**未**单独使用名为 `domain` 的包。

## 目录与包结构

- **模块顶层**（与 [`SKILL.md`](../SKILL.md)「子模块分流」一致）：
  - `client/`：Feign、`DTO`、`param`、`message` 等客户端契约。
  - `metadata/`：常量、枚举、共享参数模型。
  - `server/`：Spring Boot 服务、`resources/`、`init/`（初始化 SQL）、`src/` 与 `src-dcm/`（DCM 相关扩展，见 `server/BUILD.bazel` 的 `glob`）。
  - `references/`：本手册与 `external-api.md` 等。
- **`server/src` 下约两层包结构（用途摘要）**：

| 包 / 区域 | 用途 |
|-----------|------|
| `controller/*` | 各域 REST 入口 |
| `service`、`service/impl` 及子包 | 业务接口与实现 |
| `entity` | 实体与表映射 |
| `dao` | `*Mapper` 接口（MyBatis） |
| `security` | OAuth2、认证、Token 等 |
| `permission` | 权限查询与接口 |
| `rocketmq/*` | MQ 消息与消费者 |
| `captcha` | 图形/行为验证码 |
| `third` | 第三方用户仓储等 |
| `param`、`dto`、`vo`、`converters` | 入参、传输对象、转换 |

> 说明：本仓库服务端源码为 **`server/src` 扁平布局**，而非 `src/main/java`。

**关键入口文件**：

- **启动类**：`com.guhecloud.cube.biz.ouaa.server.OUAAServer`（[`server/src/OUAAServer.java`](../server/src/OUAAServer.java)）
- **主业务 Controller（示例）**：`…controller.context.ContextController`、`…controller.admin.TenantController`、`…controller.tenant.UserUnderTenantController`（按域拆分）
- **Service 实现**：`com.guhecloud.cube.biz.ouaa.server.service.impl` 包下 `*ServiceImpl`

## BUILD target 说明

| Label | 用途 |
|-------|------|
| `//packages/base/ouaa/client:deps` | 客户端私有依赖聚合（`exports`） |
| `//packages/base/ouaa/client:client` | Feign 与客户端 API 的 `java_library` |
| `//packages/base/ouaa/client:package` | Maven `export`（`ouaa-client`） |
| `//packages/base/ouaa/metadata:metadata` | 共享元数据库 |
| `//packages/base/ouaa/server:deps` | 服务端依赖聚合（见下文「直接依赖说明」） |
| `//packages/base/ouaa/server:server` | 服务端主库（含 `resources/**`） |
| `//packages/base/ouaa/server:server-bin` | **本地运行**：`java_binary`，`main_class` = `OUAAServer` |
| `//packages/base/ouaa/server:server-exec` | **本地运行**：`springboot` 可执行 fat-jar |
| `//packages/base/ouaa/server:package` | Maven `export`（`ouaa-server`） |
| `//packages/base/ouaa/server:server-info` | 部署元数据（库名、网关前缀、`gw_path_rewrite` 等） |
| `//packages/base/ouaa/server/init:init-mysql` | 初始化 SQL 的 `pkg_files` 打包 |

根目录 `packages/base/ouaa/BUILD.bazel` 当前为空，无根级聚合 target。

## 本地运行

- **命令**：

```bash
bazel run //packages/base/ouaa/server:server-bin
# 或
bazel run //packages/base/ouaa/server:server-exec
```

- **运行前前提**：`application.yaml` 通过 `spring.config.import` 依赖 **Nacos** 中的 `common-env.yaml`、`common-datasource.yaml`、`common-redis.yaml`、`common-rocketmq.yaml` 等；本地需可用的 **MySQL**（库名 `cube-biz-ouaa`）、**Redis**、**RocketMQ** 与 Nacos，且与环境变量一致（如 `NACOS_ADDR`、`NACOS_NAMESPACE` 等，见 `server/resources/application.yaml`）。
- **最小本地 profile / 一键启动**：**待团队补充**（若团队有标准 docker-compose 或文档，应链接至此）。

**关键配置前缀**（来自仓库 `application.yaml`，大量键由 Nacos 覆盖）：

| 前缀 / 键 | 来源 | 含义 |
|-----------|------|------|
| `common-env.ouaa.port` | `application.yaml` | HTTP 端口（默认 `10211`） |
| `common-env.ouaa.access-token-uri` | `application.yaml` → `third-part.auth-server.uri` | 第三方认证服务器地址 |
| `third-part.auth-server` | `application.yaml` | 第三方 OAuth 相关 URI 配置 |
| `mybatis-plus.mapper-locations` | `application.yaml` | `classpath:mapper/**/*.xml` |

> 大量环境相关配置通过 **Nacos** `spring.config.import` 引入（`common-env.yaml`、`common-datasource.yaml` 等），完整键表**待团队补充**。

## 数据层

- **数据库名**：`cube-biz-ouaa`，与 `//packages/base/ouaa/server:server-info` 的 `database` 及 `application.yaml` 中 JDBC URL 一致。
- **初始化 SQL**：`server/init/init-db.mysql.sql`，由 `//packages/base/ouaa/server/init:init-mysql` 打包供部署侧使用。
- **Liquibase**：`application.yaml` 中 `spring.liquibase.enabled: false`；存在 `change-log` 与 `liqui-base.execute` 等占位，是否由流水线或其它方式执行 **待团队补充**。平台依赖含 `//packages/platform/liquibase`。
- **核心表 / ER 概述**：**待团队补充**（或见团队库表文档）。

## 直接依赖说明

以下仅列 `//packages/base/ouaa/server:deps` 中 `exports` 的 Bazel label 及能力摘要（与 `server/BUILD.bazel` 一致）。

| Label | 能力摘要 |
|-------|----------|
| `//packages/base/dictionary/client` | 字典服务 Feign 客户端 |
| `//packages/base/file/client` | 文件服务 Feign 客户端 |
| `//packages/base/message/sdk` | 站内消息等 SDK |
| `//packages/base/ouaa/metadata` | 本模块共享常量、枚举、参数模型 |
| `//packages/platform/base/model:models` | 平台通用模型 |
| `//packages/platform/base/standard:exception` | 标准业务异常 |
| `//packages/platform/base/standard:jackson` | Jackson 约定配置 |
| `//packages/platform/base/utils:*` | AES/Base64/集合/日期/脱敏/摘要/JSON/树等工具库（见 BUILD 中各 utils target） |
| `//packages/platform/database` | 数据访问基础设施（如 MyBatis-Plus 等与仓内约定） |
| `//packages/platform/dcm/server-support` | DCM 服务端扩展（与 `src-dcm` 配合） |
| `//packages/platform/event` | 领域事件相关 |
| `//packages/platform/job` | 定时任务 / 调度集成 |
| `//packages/platform/liquibase` | Liquibase 集成 |
| `//packages/platform/redis` | Redis 访问 |
| `//packages/platform/rocketmq` | RocketMQ 生产/消费封装 |
| `//packages/platform/security` | 安全框架集成 |
| `//packages/platform/server` | 服务端通用能力 |
| `//third-party/java/alibaba:easyexcel_core` | Excel 导入导出 |
| `//third-party/java/alibaba:fastjson2` | JSON 处理 |
| `//third-party/java/apache:hc5` | HTTP 客户端 |
| `//third-party/java/aspectj:aspectjweaver` | AOP |
| `//third-party/java/github:easy_captcha` | 验证码组件 |
| `//third-party/java/github:mouzt_bizlog_sdk` | 业务日志 SDK |
| `//third-party/java/google:guava` | Guava 工具 |
| `//third-party/java/hibernate:validator` | Bean Validation |
| `//third-party/java/hutool:http` | Hutool HTTP |
| `//third-party/java/jackson` | Jackson 核心 |
| `//third-party/java/jakarta:annotation_api` | Jakarta 注解 |
| `//third-party/java/jakarta:servlet_api` | Servlet API |
| `//third-party/java/nimbusds:jose_jwt` | JWT/JWE 等 |
| `//third-party/java/projectreactor:reactor_core` | Reactor |
| `//third-party/java/spring:spring_boot_starter_jdbc` | JDBC |
| `//third-party/java/spring:spring_cloud_starter_bootstrap` | Spring Cloud Bootstrap |
| `//third-party/java/spring:spring_security_config` | Spring Security 配置 |
| `//third-party/java/spring:spring_security_oauth2_authorization_server` | OAuth2 授权服务器 |
| `//third-party/java/spring:spring_security_web` | Spring Security Web |
| `//third-party/java/yaml:snakeyaml` | YAML 解析 |
| `//third-party/java/apache:poi` | Office 文档（POI） |

## 常见开发场景

1. **新增对外 HTTP 接口**  
   - 在 `server/src/controller/**` 新增或扩展 `*Controller`（按域选 `admin` / `authenticated` / `tenant` 等子包）。  
   - 在 `service` 定义接口、`service/impl` 实现业务；复杂查询参数放 `param/**`，返回聚合用 `vo/**`。  
   - 若需 Feign 暴露给其他服务：在 `client/src` 增加方法及 DTO（并跑通 `//packages/base/ouaa/client:client` 编译）。

2. **新增实体与数据访问**  
   - 在 `entity/**` 增加实体；在 `dao/**` 增加 `*Mapper.java`；在 `server/resources/mapper/**` 增加对应 XML。  
   - 在 `service/impl` 注入 Mapper 并编排事务与校验。  
   - 数据库变更：与团队约定一致（Liquibase / 手工 SQL / `init` 脚本）；本仓 Liquibase 默认关闭，**待团队补充** 标准流程。

3. **新增或调整 RocketMQ 消费**  
   - 在 `rocketmq/message` 定义消息体或常量；在 `rocketmq/consumer` 增加 `*Consumer`；Topic 与订阅关系见 [`external-api.md`](./external-api.md)。  
   - 变更后确认 Nacos 中 `common-rocketmq.yaml` 等与本地一致。

## 注意事项

- **配置以 Nacos 为准**：仓库内 `application.yaml` 仅一部分键；`spring.application.name`、数据源细节、安全与 OAuth 大量项来自 `common-env.yaml`、`common-datasource.yaml`、`common-security-*.yaml` 等，**禁止**仅凭本地片段推断生产行为。
- **网关与对外路径**：HTTP 对外前缀、OAuth 路径改写以 `server_info`（`gw_prefix`、`gw_path_rewrite`）及网关部署为准；与 Controller 上路径拼接关系见 [`SKILL.md`](../SKILL.md)。
- **Liquibase**：默认 `spring.liquibase.enabled: false`；是否在其他环境或流水线执行 **待团队补充**。
- **DCM**：`src-dcm` 与 `//packages/platform/dcm/server-support` 相关，修改时注意与主 `src` 的边界，避免破坏非 DCM 构建。
- **代码生成物**：若团队对 Mapper/XML 有生成步骤，**禁止**手改生成区（若有约定）；当前仓库未见统一生成脚本说明，**待团队补充**。
