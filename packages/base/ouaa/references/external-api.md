# ouaa — 外部消费者视角

## 对外 Bazel 标签

| 用途 | Label |
|------|--------|
| Feign / 客户端 JVM 库 | `//packages/base/ouaa/client:client` |
| 服务端 JVM 库 | `//packages/base/ouaa/server:server` |
| Maven 发布（client） | `//packages/base/ouaa/client:package`（`export` 宏，`artifact_id = ouaa-client`） |
| Maven 发布（server） | `//packages/base/ouaa/server:package`（`export` 宏，`artifact_id = ouaa-server`） |

`PACKAGE_GROUP` 与版本见 [`packages/base/ouaa/variables.bzl`](../variables.bzl)（当前版本号与 `groupId` 以此为准）。

根目录 [`packages/base/ouaa/BUILD.bazel`](../BUILD.bazel) 为空，**无** `:client` / `:server` 根级别名。

## HTTP 接口路由前缀

- **网关层**：`server_info`（`server/BUILD.bazel`）声明 **`gw_prefix = "/ouaa/"`**。外部经网关访问时，路径一般为 **`/ouaa` + 应用内路径**（以实际网关配置为准）。
- **应用内（Controller）**：多数类**无**类级 `@RequestMapping`，方法上映射为**自根路径**的 REST 路径，例如：
  - 管理端：`/tenant`、`/user`、`/menu`、`/application`、…
  - 租户域：`/user-under-tenant`、`/organization`、`/role`、…
  - 上下文：`/context/user`、`/tenant/terminals`、…
  - 认证相关示例：`/oauth2/jwt.key`、`/oauth2/wechat/jscode2session`、`/public/auth/login/code/generate`
  - 验证码：`captcha/...`（类级 `@RequestMapping("captcha")`）
- **OAuth2 资源路径**：Spring Authorization Server / Resource Server 的过滤器链路径（如 `/oauth2/**`）以 **`server/src/security`** 与全局 Security 配置为准；**待团队补充**完整列表若需与 OpenAPI 对齐。

## Feign Client 接口与 Label

所有接口均为 **`@FeignClient(name = "ouaa")`**，定义于 **`//packages/base/ouaa/client:client`**：

| 接口名 |
|--------|
| `ApplicationClient` |
| `AppointmentClient` |
| `OrganizationClient` |
| `PositionClient` |
| `RoleClient` |
| `TenantClient` |
| `TerminalClient` |
| `UserClient` |
| `UserTenantClient` |
| `UserWechatClient` |

路径以各接口方法上的 `@GetMapping` / `@PostMapping` 等为准（与上文 HTTP 应用内路径一致，不经网关时需自行加端口与上下文）。

## RocketMQ

### Topic 常量（`server/src/rocketmq/message`）

| Topic 常量 | 说明 |
|------------|------|
| `user-update` | `UserUpdateMessage` |
| `TENANT_USER_INCREASE` | `IncreaseTenantUserMessage` |
| `user-register` | `UserRegisterMessage`（类存在；仓库内未见发送引用，**是否启用待团队补充**） |
| `USER_PERMISSION_REFRESH` | `UserPermissionRefreshMessage` |
| `TENANT-USER-WORK-STATUS-UPDATE` | `TenantUserWorkStatusMessage` |
| `TENANT_INIT` | `TenantInitMessage`（事务消息） |
| `TENANT_ADD_APPLICATION` | `TenantAddApplicationMessage`（事务消息） |
| `OPERATION_ORGANIZATION` | `OperationOrganizationMessage` |
| `tenant-user-del` | `DelTenantUserMessage` |
| `APP_VERSION_UPGRADE` | `AppVersionUpgradeMessage` |

### 订阅（`@RocketMQMessageListener`）

| Topic | ConsumerGroup | 类 |
|-------|----------------|-----|
| `USER_PERMISSION_REFRESH` | 与 Topic 相同 | `UserPermissionRefreshConsumer` |
| `APP_VERSION_UPGRADE` | `APP_VERSION_GROUP` | `AppVersionUpgradeConsumer` |

### Tag

本模块内 **`@RocketMQMessageListener` 未使用 `selectorExpression`（Tag）**；业务日志发送使用 **`BizLogRecordMessage.TOPIC + ":" + OuaaCode.OUAA`** 形式（Topic `BIZ-LOG-RECORD`，后缀作 Tag/选择器语义以平台 RocketMQ 封装为准），见 `UserAuthenticationProvider`、`SystemUserAuthServiceImpl`。

### 事务监听

`TenantInitTransactionChecker`、`TenantAddApplicationTransactionChecker` 实现平台 `RocketMQTransactionChecker`，与事务消息 `TENANT_INIT`、`TENANT_ADD_APPLICATION` 配合；**非** `@RocketMQTransactionListener` 注解风格。

## Maven Export 坐标

由 `export` 宏与 [`variables.bzl`](../variables.bzl) 推导：

- **GroupId**：`com.guhecloud.cube.biz`（`PACKAGE_GROUP`）
- **ArtifactId**：`ouaa-client`（client）、`ouaa-server`（server）
- **Version**：见 `VERSION` 字段（当前 `variables.bzl` 中版本号）

发布目标名为各包 `:package`，非 `:export`。
