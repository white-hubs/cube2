# base/file — 内部开发者视角

## 模块综述与边界

本模块为 **cube-base-file** 文件服务：提供文件元数据管理、上传/下载与多存储后端对接（如 **MinIO**、阿里云 **OSS**、**FastDFS** 等，具体策略见 `enumeration/FileStorageStrategyEnum`、`service/FileStore` 与各 `SingleFileStore`/`minio` 实现），并包含文件引用、过期策略、场景配置及租户初始化相关 MQ 消费等能力；HTTP 网关前缀以 `//packages/base/file/server:server-info` 中 `gw_prefix` 为准（`/file/`）。

**不负责**：统一认证与账号体系（由平台安全与其它服务提供）、业务单据全链路编排、非「文件元数据 + 二进制存储」类资源治理。与其它模块的 HTTP/Feign/MQ 契约以 `references/external-api.md` 为准（若已维护）。

## 当前分层架构

现状：服务端为 **Spring Boot 扁平 `server/src`**（未采用 `src/main/java` 物理分层），典型链路为 `controller` → `service`/`service/impl` → `dao`（MyBatis Mapper）+ `entity`/`dto`/`vo`/`param`；二进制存储经 `FileStore`/`FileStoreRouteStrategy` 路由到各存储实现（含 `minio/` 子树）；`event/` 承载 RocketMQ；`common/` 含上传封装与过期任务（`common/job`）。与仓库新建模块的「标准分层」目录形态可能不一致，属**现状，未计划由本文档驱动重构**。

实际顶层包为 `com.guhecloud.cube.base.file.server`，物理子目录与职责对应关系如下：

| 子目录 | 职责 |
|--------|------|
| `controller/` | REST 入口 |
| `service/`、`service/impl/` | 领域服务 |
| `dao/` | MyBatis Mapper 接口 |
| `entity/`、`dto/`、`vo/`、`param/` | 模型与入参出参 |
| `event/` | RocketMQ 监听与消息体 |
| `minio/` | MinIO 相关 domain/dao/service/tool（多存储之一） |
| `common/`、`common/job/` | 上传/操作封装、过期任务 |
| `client/` | 本服务对外 Feign（如租户列表） |
| `enumeration/`、`util/`、`spi/` | 枚举、工具、ShardingSphere URL 加载扩展 |

`resources/mapper/` 为 MyBatis XML；`resources/db/...` 为 Liquibase changelog（`liquibase.enabled` 默认 false，由配置覆盖）。

## 目录与包结构

```
packages/base/file/
├── variables.bzl
├── client/
│   └── src/                    # Feign、param、vo、util
├── server/
│   ├── BUILD.bazel
│   ├── init/                   # DB 初始化 SQL + pkg_files
│   ├── resources/              # application.yaml、bootstrap.yaml、mapper、db changelog
│   ├── src/                    # 主代码（本模块未使用 src/main/java 分层）
│   └── src-dm-database/        # 达梦等 ShardingSphere SPI 扩展
```

模块根 `BUILD.bazel` 当前为空，无根级 target。

**开发入口**：
- **启动类**：`com.guhecloud.cube.base.file.server.FileServer`（`server/src/FileServer.java`）
- **Feign 扫描**：`@EnableFeignClients(basePackages = "com.guhecloud")`
- **组件扫描**：`com.guhecloud` 与 FastDFS 相关包（见启动类注解）

## BUILD target 说明

| 标签 | 用途 |
|------|------|
| `//packages/base/file/server:deps` | 私有依赖聚合（`visibility` private） |
| `//packages/base/file/server:server` | 服务端 `java_library`，主源码 + resources |
| `//packages/base/file/server:server-bin` | `java_binary`，`main_class`：`com.guhecloud.cube.base.file.server.FileServer`；**本地运行入口** |
| `//packages/base/file/server:server-exec` | `springboot` 宏，可执行 fat-jar；**CI/部署用** |
| `//packages/base/file/server:package` | `export` 宏，Maven `file-server`；**发布用** |
| `//packages/base/file/server:server-info` | `server_info`：数据库名、`gw_prefix`、main class 等 |
| `//packages/base/file/client:client` | 客户端库；**对外依赖入口** |
| `//packages/base/file/client:export*` | `java_export`，`file-client` 多版本坐标；**客户端发布** |
| `//packages/base/file/server/init:init-mysql` | `pkg_files`，初始化 SQL 重命名输出 |

## 本地运行

在仓库根目录执行：

```bash
bazel run //packages/base/file/server:server-bin
```

Spring Boot 打包运行（与 CI/部署一致的可执行形态）：

```bash
bazel build //packages/base/file/server:server-exec
# 产物路径以 bazel-bin 为准；或用 java -jar 运行 springboot 输出
```

**说明**：默认连接 Nacos、ShardingSphere、Redis、RocketMQ 等（见 `server/resources/application.yaml` 的 `spring.config.import`）。本地需可用的环境变量/Nacos 地址，否则 **待团队补充** 最小本地 profile 或 mock 方案。

## 数据层

- **数据库名**：`cube-base-file`，与 `//packages/base/file/server:server-info` 的 `database` 一致。
- **初始化 SQL**：`server/init/init-db.mysql.sql`，经 `//packages/base/file/server/init:init-mysql` 打包重命名发布；内含 `file` 及分表 `file_*` 等结构（详见文件内 DDL）。
- **Liquibase**：`server/resources/application.yaml` 中 `spring.liquibase.change-log` 指向 `db.com.guhecloud.cube.base.file/changelog-master.xml`，默认 `liquibase.enabled: false`；增量 SQL 位于 `server/resources/db/com.guhecloud.cube.base.file/`（如 `changelog-2.*.mysql.sql`）。若制品树中未包含 `changelog-master.xml`，**待团队补充**或与发布物核对。
- **核心表（概述）**：`file`（及分表）、`file_ref`、`file_config`、`scene_info`、`file_expiration_policy`、MinIO 侧 `file_store_sign_up` 等；列级定义以 SQL/Liquibase 为准。

## 直接依赖说明

以下为 `//packages/base/file/server:deps` 中与能力相关的主依赖（完整列表以 `server/BUILD.bazel` 为准）：

| Bazel label（节选） | 提供能力 |
|---------------------|----------|
| `//packages/platform/database` | MyBatis 与数据访问栈 |
| `//packages/platform/server` | Spring Boot 服务端基座 |
| `//packages/platform/security` | 安全与资源服务衔接 |
| `//packages/platform/rocketmq`、`//packages/platform/event` | RocketMQ 消息 |
| `//packages/platform/job` | 定时任务（如过期扫描） |
| `//packages/base/dictionary/client` | 字典等基础数据 Feign |
| `//third-party/java/minio`、`//third-party/java/aliyun:oss` | MinIO / 阿里云 OSS |
| `//third-party/java/github:fastdfs_client` | FastDFS 客户端 |
| `//third-party/java/apache:sharding-jdbc` | ShardingSphere JDBC（与 Nacos 分片配置联动） |

**客户端** `//packages/base/file/client:client`：依赖 `//packages/platform/client` 及 EasyExcel/POI 等，用于 Feign 与表格相关 DTO（见 `client/BUILD.bazel`）。

## 常见开发场景

1. **新增或修改 REST 接口**
   - `server/src/**/controller/*Controller.java`
   - 对应 `service`/`service/impl`、`param`、`vo`
   - 涉及库表时：`dao`、`entity`、`resources/mapper/*.xml`，并按团队流程更新 Liquibase/SQL。

2. **调整多存储（MinIO/OSS/FastDFS 等）**
   - `enumeration/FileStoreType.java`、`enumeration/FileStorageStrategyEnum.java`、`service/FileStoreRouteStrategy.java`、各 `FileStore`/`SingleFileStore` 与 `minio/**`
   - 运行期配置以 Nacos（如 `file.yaml`、`sharding-db.yaml`）为准，见 `application.yaml` 的 `spring.config.import`。

3. **租户初始化 / 字典联动**
   - `event/` 下监听器与消息体；`service/impl/FileInitialisationTenantServiceImpl.java` 等。

## 注意事项

- **Nacos 优先**：多数行为依赖 `spring.config.import` 拉取的公共配置与 `file.yaml`，排障以远端配置为准；`liquibase.enabled` 与 `liqui-base.execute` 可被覆盖，勿默认本地会执行迁移。
- **历史 DDL**：`init-db.mysql.sql` 内注释说明部分对象曾未完全纳入 Liquibase；新增对象应遵循团队 migration 约定（**待团队补充**成文流程）。
- **分片**：数据源使用 ShardingSphere 驱动并从 Nacos 读取规则，避免在业务代码中硬编码分片键语义。
- **本模块未声明的代码生成产物**：当前无单独生成器目录约定；若后续引入，需在此补充「禁止手改」路径。
- **关键配置文件**：`server/resources/application.yaml`、`bootstrap.yaml`（启动配置入口）；`server/init/init-db.mysql.sql`（经 `init-mysql` target 发布的初始化 SQL）。
