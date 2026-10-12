---
name: cube2-base-file
description: >
  Cube2 基础文件服务：元数据与文件关联、多存储后端（MinIO/OSS/FastDFS 等）、过期与场景策略，
  消费 RocketMQ 租户/字典消息。触发词：packages/base/file、file 服务、FileClient、
  FileOperateClient、Feign、gateway /file/、file upload、对象存储。
---

# base/file

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。术语见 [.agents/skills/cube2-repo/references/glossary.md](../../../.agents/skills/cube2-repo/references/glossary.md)。

## 模块边界与职责

- **负责**：
  - **文件域 REST API**（上传、查询、业务主键关联、公开下载入口、`FileOperate` 临时/持久化与 induce 流程等，见 `server/src/controller`）。
  - **文件与业务关联**（`FileRef`、批量/场景化关联与 induce）。
  - **清理与场景策略**（过期策略分页 CRUD、场景信息查询、定时任务入口）。
  - **存储路由与实现**（`service`/`minio`/`common` 下多实现：如 MinIO、单存储策略等；依赖含 Aliyun OSS、MinIO、FastDFS 等，具体启用以配置为准）。
  - **租户/字典驱动的初始化**（消费 RocketMQ，初始化租户文件配置、同步字典场景）。
  - **对外 Feign 契约**（`client`：`FileClient`、`FileOperateClient`，服务名 `file`）。
  - **数据库侧**：逻辑库名在 `server_info` 中为 `cube-base-file`；实际连接为 ShardingSphere + Nacos 配置（见 `application.yaml`）。
- **不负责**：统一认证实现本身（依赖 `platform/security` 等）；字典主数据写入（仅消费 `DICTIONARY` 主题消息）；网关路由规则细节（仅提供 `server_info` 中的 `gw_prefix` 元数据）。

## 子模块分流

| 子模块 | Bazel 标签（主要） | 职责 |
|--------|-------------------|------|
| **client** | `//packages/base/file/client:client`；发布 `//packages/base/file/client:export`（及 `export-dev` / `export-test`） | Feign 接口、客户端参数/VO、Excel 相关工具（见 client `BUILD.bazel`） |
| **server** | `//packages/base/file/server:server`；可执行 `//packages/base/file/server:server-bin`、`//packages/base/file/server:server-exec`（springboot）；发布 `//packages/base/file/server:package`；元数据 `//packages/base/file/server:server-info` | Spring Boot 应用、REST、领域服务、存储与 MQ 消费 |
| **server/init** | `//packages/base/file/server/init:init-mysql` | 初始化 SQL 制品（供 deploy 数据库子包引用） |

模块根目录 `packages/base/file/BUILD.bazel` 当前为空文件，**无**根级聚合 target；请直接引用 `client` 或 `server` 子包。

## 快速定位清单

1. **服务入口类**：`server/src/FileServer.java`（`FileServer`）。
2. **对外 HTTP 行为**：`server/src/controller/*Controller.java`（无类级 `@RequestMapping`，路径以方法上注解为准）。
3. **Feign 契约（消费方）**：`client/src/FileClient.java`、`client/src/FileOperateClient.java`（均为 `@FeignClient(name = "file")`）。
4. **MyBatis Mapper**：`server/src/dao/*.java` 与 `server/resources/mapper/*.xml`。
5. **存储与 MinIO 配置绑定**：`server/src/minio/domain/MinioConfig.java`（`@ConfigurationProperties(prefix = "file")`）。
6. **RocketMQ 消费者**：`server/src/event/FileInitialisationTenantEvent.java`、`FileDictionaryEvent.java`。
7. **网关/库名元数据**：`server/BUILD.bazel` 中 `server_info`（`gw_prefix`、`database`）。
8. **运行/打包**：本地 `bazel run //packages/base/file/server:server-bin`；fat-jar 见 `server-exec`。

## 构建与入口

- **服务端库**：`//packages/base/file/server:server`（依赖聚合 `:deps` 为私有）。
- **本地运行**：`//packages/base/file/server:server-bin`；Spring Boot 可执行：`//packages/base/file/server:server-exec`。
- **Maven 发布（服务端）**：`//packages/base/file/server:package`（`artifact_id`：`file-server`）。
- **客户端库**：`//packages/base/file/client:client`；**Maven 导出**：`//packages/base/file/client:export`（`artifact_id`：`file-client`，坐标组 `com.guhecloud.cube.base`，版本见 `variables.bzl`）。
- **数据库 init 文件打包**：`//packages/base/file/server/init:init-mysql`。

## 对外契约

- **HTTP**：应用内路径见 Controller 扫描结果；**部署侧网关前缀**（`server_info`）：`/file/`。网关是否 strip 前缀以部署配置为准，与 Feign 路径一并核对。
- **Feign Client**：`com.guhecloud.cube.base.file.client.FileClient`、`FileOperateClient`（`name = "file"`）。注意两接口路径风格不同（一组带 `/file/...` 前缀，一组为 `/upload/...` 等），调用时与网关规则保持一致。
- **RocketMQ**：
  - Topic `TENANT_INIT`，consumerGroup `TENANT_INIT_FILE`（租户初始化文件配置）。
  - Topic `DICTIONARY`，consumerGroup `DICTIONARY_FILE`，selector `FILE_SCENE_CODE`（字典变更驱动文件场景配置）。

## 数据与配置

- **spring.application.name**：`file`（`server/resources/bootstrap.yaml`）。
- **数据库名（元数据）**：`cube-base-file`（`server_info` `database`）。
- **数据源**：`application.yaml` 使用 ShardingSphere 驱动，JDBC URL 自 Nacos `sharding-db.yaml`；细粒度连接与分表待团队补充。
- **业务/存储配置**：Nacos 引入 `file.yaml`（`NACOS_CLOUD_FILE`）；本地 `MinioConfig` 绑定前缀 **`file.*`**（`type`、`endpoint`、`accessKeyId`、`accessKeySecret`、`accessPrefix` 等）。对象存储 bucket、多后端切换细节多在 Nacos，**待团队补充**。

## 深度阅读

- [references/overview.md](./references/overview.md)（目录与本地运行）
- [references/external-api.md](./references/external-api.md)（对外标签、路由、Feign、MQ、存储）
