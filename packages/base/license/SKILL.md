---
name: cube2-base-license
description: >
  Cube2 业务侧许可证签发服务：制 .lic、采集 LicenseCheckModel，CLI/可选 HTTP；非
  platform/license 运行时校验库。Triggers: base license, license-server, /license/,
  generateLicense, getServerInfos, 证书生成.
---

# base/license

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)；术语见 [.agents/skills/cube2-repo/references/glossary.md](../../../.agents/skills/cube2-repo/references/glossary.md)。

## 模块边界与职责

- **负责**：
  - **签发许可证文件**（`LicenseCreator` + TrueLicense `LicenseManager.store`，私钥与 keystore 参数由调用方提供）。
  - **采集服务端硬件信息**（HTTP `getServerInfos` → `ServerInfoUtils.getServerInfos`，返回 `LicenseCheckModel`，供写入证书 `extra` 字段）。
  - **命令行签发**（`LicenseClientMain` / `CreateLicenseCommand`，无 Web 容器）。
- **不负责**：
  - **运行时许可证安装、校验、续期逻辑** —— 这些由 **`packages/platform/license`**（`LicenseManagerHolder`、消费者侧校验等）与各业务进程集成完成；本模块是「制证工具/服务」，不是平台基础库。
  - **数据库持久化**：`LicenseServer` 显式 `exclude` `DataSourceAutoConfiguration`；`server_info` 标记 `no_datasource = True`。

## 子模块分流

| 路径 / Bazel 包 | 职责 |
|-----------------|------|
| `packages/base/license/`（根 `BUILD.bazel`） | 仅占位注释，无 `java_library` / 聚合目标；构建入口在 `server/`。 |
| `server/` | 唯一实现包：`deps`、`server`、`server-bin`、`server-exec`、`package`、`server-info`。 |

## 快速定位清单

1. **网关前缀**：`server/BUILD.bazel` 中 `server_info` → `gw_prefix = "/license/"`，`server_id = "license"`。
2. **HTTP 接口**：`server/src/controller/LicenseCreatorController.java` → `/getServerInfos`、`/generateLicense`（需以 **`LicenseServer`** 启动 Web；Bazel 默认可执行入口为 CLI，见下）。
3. **签发核心逻辑**：`server/src/creator/LicenseCreator.java`。
4. **CLI**：`server/src/cli/LicenseClientMain.java`、`cli/command/CreateLicenseCommand.java`。
5. **平台侧模型与工具类（依赖，非本模块实现）**：`com.guhecloud.platform.license.*`（如 `LicenseManagerHolder`、`LicenseCheckModel`、`ServerInfoUtils`）。

## 构建与入口

- **服务端库**：`//packages/base/license/server:server`。
- **可运行**：
  - `//packages/base/license/server:server-bin`（`java_binary`，`main_class` = `LicenseClientMain`，**CLI**）。
  - `//packages/base/license/server:server-exec`（`springboot` fat-jar，**主类同为 `LicenseClientMain`**）。
- **发布**：`//packages/base/license/server:package`（`artifactId = license-server`，`PACKAGE_GROUP = com.guhecloud.cube.base`）。
- **部署元数据**：`//packages/base/license/server:server-info`。
- **Web 主类（与 Bazel 默认可执行主类不同）**：`com.guhecloud.cube.base.license.server.LicenseServer` —— 用于暴露 REST；若生产仅以 CLI 制证，可不经由此类启动。

## 对外契约

- **HTTP**（经网关时前缀 **`/license/`**，应用内 Controller **无**类级 `@RequestMapping`，路径以方法为准）：
  - `GET /getServerInfos`：查询参数 `osName`（可选）。
  - `POST /generateLicense`：请求体 `LicenseCreatorParam`（JSON）。
- **MQ**：本模块源码中**未发现** RocketMQ / Topic / Tag 消费或生产；**无**（若存在侧car 或外层集成，**待团队补充**）。
- **License 相关 API 说明**：
  - **本模块提供**：机器信息采集（`getServerInfos`）与证书文件生成（`generateLicense`）；生成时把 `LicenseCheckModel` 序列化进证书 `extra`（见 `LicenseCreator.initLicenseContent`）。
  - **运行时「校验已安装 license」**：由引用 **`//packages/platform/license`** 的业务/平台进程完成，**不在**本模块暴露独立「校验 HTTP API」。

## 数据与配置

- **数据库**：无；未配置 JDBC（`no_datasource = True`）。
- **spring.application.name**：`license`（`server/resources/application.yml`）。
- **端口**：`server.port: 11003`（`application.yml`；多环境是否覆盖 **待团队补充**）。
- **业务配置**：`license.licensePath` —— 默认证书输出路径（`generateLicense` 可在请求体覆盖）。

## 深度阅读

- [references/external-api.md](./references/external-api.md)（对外 Bazel 标签、路由、License 相关 API、MQ）
- [references/overview.md](./references/overview.md)（目录树、BUILD target、本地运行）
