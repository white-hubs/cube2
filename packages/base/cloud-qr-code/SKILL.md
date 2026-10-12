---
name: cube2-base-cloud-qr-code
description: >
  面向「云二维码 / cloud QR code / 动态路由 / 短码解析」场景：说明 Cube2 base 侧云二维码模块的职责边界、SDK 与 Spring Boot 服务的 Bazel 标签、网关前缀与本地运行入口。
  Use when integrating or debugging QR management, route resolution, or the cloud-qr-code server/SDK in this monorepo.
---

## 模块边界与职责

本模块在 Cube2 中承担**云二维码全生命周期管理**（类型、实例、项目、地址配置）及**短链/扫码路由解析**相关能力；对外提供 **HTTP API（经网关前缀暴露）** 与 **可发布的 Java SDK**（OAuth2 client_credentials、二维码与类型 CRUD、分页查询）。业务代码采用 **CQRS 风格**：`commandgateway` 写路径、`querygateway` 读路径，持久化与外部客户端在 `infrastructure`。

## 子模块分流

| 子目录 | 职责 | 主要 Bazel label（`//packages/base/cloud-qr-code/...`） |
|--------|------|----------------------------------------------------------|
| **sdk** | 纯 Java 客户端与 DTO，供外部系统依赖；通过 `java_export` 发布 Maven 构件 | `:sdk`（库）、`:package`（导出 `cloud-qr-code-sdk`） |
| **server** | Spring Boot 应用、REST 控制器、领域与仓储、Liquibase/MyBatis 资源 | `:server`、`:server-bin`、`:server-exec`（Spring Boot 可执行）、`:package`（导出 `cloud-qr-code-server`）、`:server-info`（元数据：库名、网关前缀等） |
| **server/init** | 数据库初始化 SQL 的打包映射 | `:init-mysql` |

根目录 `BUILD.bazel` 当前为空文件，**无根级 target**；版本与 `PACKAGE_GROUP` 见同目录 `variables.bzl`（与部分子包 BUILD 中引用的路径一致）。

## 快速定位清单

| 需求 | 位置 |
|------|------|
| 主类 / 进程入口 | `com.guhecloud.cube.base.cloud.qr.code.server.CloudQrCodeServer`（`server/BUILD.bazel` 中 `_MAIN_CLASS`） |
| REST 控制器 | `server/src/interfaces/controller/`（如 `CloudQrCodeController`、`CloudQrRouteController`） |
| 应用服务（命令/查询） | `server/src/application/commandgateway/`、`server/src/application/querygateway/`（如 `*CommandService`、`*QueryService`） |
| SDK 入口 | `sdk/src/CloudQrCodeClient.java`、`sdk/src/config/CloudQrCodeSdkConfig.java` |
| 网关与公开路径元数据 | `server/BUILD.bazel` 中 `server_info`：`gw_prefix`、`public_path_patterns` |
| 本地端口与 Nacos | `server/resources/application.yaml`（端口占位符见下文） |
| 应用名 | `server/resources/bootstrap.yaml` → `spring.application.name: cloudQrCode` |

## 构建与入口

- **编译服务端库**：`bazel build //packages/base/cloud-qr-code/server:server`
- **运行 Spring Boot（推荐）**：`bazel run //packages/base/cloud-qr-code/server:server-exec`
- **普通 java_binary**：`bazel run //packages/base/cloud-qr-code/server:server-bin`
- **SDK 库**：`bazel build //packages/base/cloud-qr-code/sdk:sdk`
- **发布用 export**：`//packages/base/cloud-qr-code/sdk:package`、`//packages/base/cloud-qr-code/server:package`

## 对外契约

### HTTP

- **网关路由前缀**（来自 `server_info`）：`/cloudQrCode/`（具体是否带尾斜杠以网关与部署约定为准）。
- **控制器上的相对路径**（需与网关前缀拼接后访问，除非直连应用根路径）示例：
  - 二维码：`/cloud-qr-code/...`（见 `CloudQrCodeController`）
  - 二维码类型：`/cloud-qr-code-type/...`
  - 项目：`/cloud-qr-code-projects/...`
  - 地址配置：`/cloud-address-config`
  - 扫码/短链路由：`/qr/{shortCode}`（`CloudQrRouteController` 上为 `@RequestMapping("/qr")`）
- **公开路径模式**（元数据）：`/qr/**`（`server_info.public_path_patterns`）。

### SDK

- Maven 坐标（版本以发布与 `variables.bzl` 为准）：`com.guhecloud:cloud-qr-code-sdk`
- 初始化 `CloudQrCodeClient`、OAuth2、超时等见 **`sdk/README.md`**。

## 数据与配置

- **数据源库名**（`server_info`）：`cube_base_cloud_qr_code`；JDBC URL 模板见 `server/resources/application.yaml`。
- **Spring 应用名**：`cloudQrCode`（`bootstrap.yaml`）。
- **配置前缀 / 占位符**：
  - 服务端口：`${common-env.cloud-qr-code.port:10221}`（默认 **10221**）。
  - 大量行为依赖 **Nacos** 导入的 `common-env.yaml`、`common-datasource.yaml`、`common-security-*.yaml` 等；仓库内仅保留占位与本地默认，**完整键表待团队补充**。
- **模块特有 `@ConfigurationProperties`**：当前源码中未检索到典型前缀 Bean；若存在仅通过 Nacos 扁平键注入，**待团队补充**。

## 深度阅读

- `packages/base/cloud-qr-code/references/overview.md`：目录树、target、运行命令摘要。
- `packages/base/cloud-qr-code/references/external-api.md`：Bazel 标签、路由与 SDK 速查。
- `sdk/README.md`：SDK API 与示例代码。
- `server/BUILD.bazel`：`server_info`（库名、网关前缀、公开路径）。
- Liquibase：`server/resources/db/` 下 changelog。
