# cloud-qr-code 对外契约速查

## 对外 Bazel 标签

| 用途 | Label |
|------|--------|
| 服务端 Spring Boot 可执行 | `//packages/base/cloud-qr-code/server:server-exec` |
| 服务端库（含 resources） | `//packages/base/cloud-qr-code/server:server` |
| 服务端 java_binary | `//packages/base/cloud-qr-code/server:server-bin` |
| 服务端 Maven 导出 | `//packages/base/cloud-qr-code/server:package` |
| 服务端元数据（server_info） | `//packages/base/cloud-qr-code/server:server-info` |
| SDK Java 库 | `//packages/base/cloud-qr-code/sdk:sdk` |
| SDK Maven 导出 | `//packages/base/cloud-qr-code/sdk:package` |
| DB 初始化脚本打包 | `//packages/base/cloud-qr-code/server/init:init-mysql` |

根目录 `//packages/base/cloud-qr-code/BUILD.bazel` 为空，无模块根 target。

## HTTP 接口路由前缀

- **网关前缀**（`server/BUILD.bazel` → `server_info.gw_prefix`）：`/cloudQrCode/`
- **应用内控制器路径**（需与网关前缀组合；直连本服务时通常从 context-path 根开始，**以实际部署为准**）：
  - 二维码：`/cloud-qr-code/...`、`/public/cloud-qr-code/short-code/{shortCode}` 等（`CloudQrCodeController`）
  - 类型：`/cloud-qr-code-type/...`（`CloudQrCodeTypeController`）
  - 项目：`/cloud-qr-code-projects/...`（`CloudQrCodeProjectController`）
  - 地址配置：`/cloud-address-config`（`CloudAddressConfigController`）
  - 短链/路由：`/qr/...`（`CloudQrRouteController`，`@RequestMapping("/qr")`）
- **公开路径模式**（元数据）：`/qr/**`

完整 OpenAPI/Swagger 若存在，**待团队补充**。

## SDK 使用方式（`sdk` 子目录）

- **Maven 坐标（Bazel `java_export`）**：`groupId` 来自 `//packages/base/alarm:variables.bzl` 的 `PACKAGE_GROUP`（当前为 `com.guhecloud.cube.base`），`artifactId` 为 `cloud-qr-code-sdk`，`version` 为该文件中 `VERSION`。**`sdk/README.md` 若仍写 `com.guhecloud` 为 groupId，以 BUILD 与发布产物为准，待团队核对文档。**
- **入口类**：`com.guhecloud.cube.base.cloud.qr.code.sdk.CloudQrCodeClient`
- **配置**：`com.guhecloud.cube.base.cloud.qr.code.sdk.config.CloudQrCodeSdkConfig`（`appId` / `appSecret` / `serverUrl`、可选 `tenantId`、连接与读超时）。
- **详细示例与 API 表**：见同模块 `sdk/README.md`。
