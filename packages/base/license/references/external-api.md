# base/license 对外契约（消费者视角）

## 对外 Bazel 标签

| 用途 | 标签 |
|------|------|
| 服务端实现（源码 + resources） | `//packages/base/license/server:server` |
| 可执行（CLI，`LicenseClientMain`） | `//packages/base/license/server:server-bin` |
| Spring Boot 打包可执行（CLI 主类） | `//packages/base/license/server:server-exec` |
| 发布 `license-server`（`export`） | `//packages/base/license/server:package` |
| 部署元数据（网关前缀、主类、`server_id` 等） | `//packages/base/license/server:server-info` |
| 内部依赖聚合（不对外） | `//packages/base/license/server:deps` |

`packages/base/license/BUILD.bazel` **无** `java_library` / `export` 目标。

## HTTP 接口路由前缀

**网关前缀**（`server_info`）：`/license/`  

应用内 `LicenseCreatorController` **无**类级 `@RequestMapping`。经网关访问时一般为 **`/license/` + 下列路径**（以实际网关 strip 规则为准）。

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/getServerInfos` | 采集服务器硬件信息，返回 `LicenseCheckModel`（`osName` 可选 query） |
| `POST` | `/generateLicense` | 根据 `LicenseCreatorParam` 生成 `.lic` 文件 |

**前置条件**：需以 **`LicenseServer`** 启动 Spring Web（见 [overview.md](./overview.md)）；Bazel 默认 `server-bin` / `server-exec` 入口为 **非 Web** CLI。

## License 校验 / 查询 API

| 说明 |
|------|
| **本模块不提供**「安装后在线校验 license 是否有效」的独立 REST API。 |
| **机器指纹采集**：`GET /getServerInfos` 使用 `com.guhecloud.platform.license.util.ServerInfoUtils`，返回 **`com.guhecloud.platform.license.model.LicenseCheckModel`**，用于制证时写入证书扩展字段。 |
| **制证**：`POST /generateLicense` 调用本模块 `LicenseCreator`，内部使用 **`com.guhecloud.platform.license.LicenseManagerHolder`**（来自 **`//packages/platform/license`**）完成 `store`。 |
| **运行时校验**：由各服务依赖 **`packages/platform/license`** 完成（与 **base/license** 服务边界不同）。 |

## MQ 消息（Topic / Tag）

本模块源码中**未发现** RocketMQ 生产/消费或 Topic/Tag 常量；标注为 **无**。若通过外部进程或 sidecar 集成消息，**待团队补充**。
