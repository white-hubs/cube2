# base/openapi — 对外契约与集成

## 对外 Bazel 标签

- **`//packages/base/openapi/metadata:metadata`**  
  可被 `//packages/...` 子包引用的共享元数据库（枚举等）。仓库内当前除本模块 `server` 外**未见其它 BUILD 依赖**，后续若有 Feign client 或其它模块依赖，以 `BUILD.bazel` 为准。

- **`//packages/base/openapi/server:server`**  
  服务端 `java_library`（源码 + 资源）。

- **`//packages/base/openapi/server:server-exec` / `server-bin`**  
  可执行产物（`springboot` fat-jar / `java_binary`）。

- **`//packages/base/openapi/server:server-info`**  
  供部署/网关生成使用的 `ServerInfo`（含 `gw_prefix`、`server_id`、`no_datasource` 等）。

本模块 `server` **未**在 `BUILD.bazel` 中使用 `export()` 发布 Maven 坐标；若需「对外 Maven 构件名」，待团队补充。

## HTTP 接口路由前缀

| 层级 | 前缀 / 模式 | 来源 |
|------|----------------|------|
| 网关（部署元数据） | `/open-api/` | `server/BUILD.bazel` → `server_info(gw_prefix = ...)` |
| Spring Controller | `/openapi/{application}/**` | `OpenAPIController`：`@RequestMapping("/openapi")` + `/{application}/**` |

网关在转发到本服务时是否 **strip** `/open-api/`、是否与 `/openapi` 叠加，**待团队补充**（依赖实际网关与 `server_info` 的 strip/rewrite 配置）。

## 认证 / 鉴权方式

- 本地 `application.yaml` 通过 Nacos 引入 **OAuth2 Resource Server** 与 **平台安全** 相关公共配置（`common-security-resource-server.yaml`、`common-platform-security.yaml` 等）。
- 推断：请求需携带平台认可的 **Bearer Token**（或网关注入的身份头），具体 **匿名白名单、scope、路径级规则** 不在本模块仓库内硬编码，**待团队补充**（以 Nacos 与 `packages/platform/security` 约定为准）。

## MQ 消息

- **代码扫描**：`packages/base/openapi` 下**未发现** RocketMQ / Kafka / `@RabbitListener` 等消费者或生产者实现。
- **配置**：`application.yaml` 的 `spring.config.import` 包含 Nacos 的 `common-rocketmq.yaml`，属平台级共享配置；**本服务是否启用 MQ 客户端**取决于远端配置与本服务 Bean，**待团队补充**。
