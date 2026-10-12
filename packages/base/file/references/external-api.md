# base/file — 外部消费者视角

## 对外 Bazel 标签

其他模块在 `deps` 中常引用：

| 用途 | 标签 |
|------|------|
| 客户端（Feign + DTO） | `//packages/base/file/client:client` |
| Maven 发布（客户端 JAR） | `//packages/base/file/client:export`（及 `export-dev`、`export-test`） |

服务端 JAR 发布：`//packages/base/file/server:package`（`file-server`）。  
运行用二进制：`//packages/base/file/server:server-bin`、`//packages/base/file/server:server-exec`。

Maven 坐标组：`com.guhecloud.cube.base`；版本见 `packages/base/file/variables.bzl` 中 `VERSION`。

## HTTP 接口与网关前缀

- **部署元数据**（`server_info`）：网关前缀 **`/file/`**（见 `packages/base/file/server/BUILD.bazel`）。
- **应用内路径**由 `@GetMapping` / `@PostMapping` 等定义；Controller **无**类级 `@RequestMapping`，以下为扫描摘要（相对应用 context，**不含**网关 strip 规则）。

### `FileController`（`/file/...` 为主）

| 方法 | 路径 |
|------|------|
| GET | `/file/upload/details`（deprecated） |
| POST | `/file`、`/file/upload`、`/public/file/upload` |
| DELETE | `/file/deleteByIds`、`/file/{id}` |
| GET | `/file/{id}`、`/public/file/{id}`、`/file/ids` |
| POST | `/file/list`、`/file/business-keys` |
| GET | `/file/business-key`、`/file/business-key/tag` |
| GET | `/file/getByte/{id}`、`/file/getFileBase64/{id}` |
| GET | `/file/resourceBundle` |

### `FileOperateController`（无前缀 `/file`）

| 方法 | 路径 |
|------|------|
| GET | `/upload/details` |
| POST | `/upload/temporary`、`/upload/sustain` |
| POST | `/induce/single`、`/induce/single/body`、`/induce/batch` |
| DELETE | `/deleteByIds` |

### `FileRefController`

| 方法 | 路径 |
|------|------|
| GET | `/file-ref` |
| POST | `/file-ref/batch`、`/file-ref/businessKey/batch`、`/file-ref/businessKey/single/induce`、`/file-ref/businessKey/induce`、`/file-ref/businessKey/batch/induce` |
| DELETE | `/file-ref/{id}`、`/file-ref/business-key`、`/remove/file-ref/business-key/ids`、`/file-ref/business-key/scene-code` |

### `FileExpirationPolicyController`

| 方法 | 路径 |
|------|------|
| GET | `/get/expiration/policy/page` |
| POST | `/add/expiration/policy` |
| PUT | `/update/expiration/policy` |
| DELETE | `/delete/expiration/policy/{id}` |

### `SceneInfoController`

| 方法 | 路径 |
|------|------|
| GET | `/get/scene/info/all` |

对外 Feign `FileTenantClient`（`/authenticated/tenant/...`）为**本服务对外调用其他服务**的客户端，非 file 服务对外暴露路由。

**待团队补充**：网关聚合后对外统一 URL 形态（是否 `/file` 重复前缀、是否 strip）。

## Feign Client 接口名

| 接口 | `@FeignClient` | 说明 |
|------|----------------|------|
| `com.guhecloud.cube.base.file.client.FileClient` | `name = "file"` | 路径多为 `/file/...` |
| `com.guhecloud.cube.base.file.client.FileOperateClient` | `name = "file"` | 面向上传/induce 的路径 `/upload/...`、`/induce/...`、`/deleteByIds` |

## 文件存储配置（OSS / MinIO / 本地等）

- 代码侧 **`@ConfigurationProperties(prefix = "file")`**：`MinioConfig`（`server/src/minio/domain/MinioConfig.java`），字段包括 `type`、`endpoint`、`accessKeyId`、`accessKeySecret`、`accessPrefix`。
- 运行时大量配置来自 **Nacos**：`application.yaml` 中 `spring.config.import` 包含 `file.yaml`（`NACOS_CLOUD_FILE`）。具体 MinIO/OSS/FastDFS bucket、区域、是否启用多后端等 **待团队补充**（以 Nacos 与环境为准）。
- 依赖层面可见 Aliyun OSS、MinIO、FastDFS 等（`server/BUILD.bazel` `:deps`），**实际启用策略**由配置与实现类选择决定。

## MQ 消息（Topic / Tag）

| 监听器 | Topic | ConsumerGroup | 备注 |
|--------|-------|---------------|------|
| `FileInitialisationTenantEvent` | `TENANT_INIT` | `TENANT_INIT_FILE` | 常量见 `FileTenantInitMessage.TOPIC` |
| `FileDictionaryEvent` | `DICTIONARY` | `DICTIONARY_FILE` | `DictionaryMessage.TOPIC`；`selectorExpression = "FILE_SCENE_CODE"` |
