---
name: cube2-base-openapi
description: >
  Cube2 对外开放 API 网关（非 Swagger 文档）：按应用与路径将请求代理至内部服务（含服务发现），
  并可在响应后执行函数链。触发 openapi、open-api、open gateway、开放平台网关、API 代理。
---

# base/openapi

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)；术语见 [.agents/skills/cube2-repo/references/glossary.md](../../../.agents/skills/cube2-repo/references/glossary.md)（其中 `openapi` 条目明确本服务为**对外开放 API 网关**，而非 OpenAPI 规范文件）。

## 模块边界与职责

- **负责**：
  - **HTTP 入口与反向代理**：`OpenAPIController` 暴露 `/openapi/{application}/**`，将请求转交给 `ProxyService`，按 `APIService` 匹配的 `RegisteredAPI` 构造目标 URL 并转发（Apache HttpClient 5）。
  - **内部服务解析**：目标 URL 支持 `internal://<serviceName>/...`，通过 `LoadBalancerClient` 解析为 `http://host:port/...`。
  - **可选响应后处理**：对配置的 `afterFC` 在 2xx 响应体上执行函数链（见 `FCEngine` / JavaScript / Java 执行引擎）。
  - **API 元数据与枚举**：`metadata` 子包提供 `HTTPMethod`、`ResponseType`、`FCEngineType` 等供 server 与后续扩展复用。
- **不负责**：本仓库 glossary 所指的「OpenAPI 规范文档」托管；**动态 API 注册持久化**（`APIServiceImpl` 中注册表加载仍为 TODO/占位数据）；业务域 CRUD；独立业务数据库（见下「数据与配置」）。

## 子模块分流

| Bazel 标签 | 职责 |
|------------|------|
| `//packages/base/openapi/metadata:metadata` | 纯 `java_library`：枚举与元数据类型，供 `server` 依赖；`visibility` 为 `//packages:__subpackages__`。 |
| `//packages/base/openapi/server:deps` | 私有聚合依赖（metadata、platform server/security、HttpClient 等）。 |
| `//packages/base/openapi/server:server` | 应用源码与 `resources`（主库）。 |
| `//packages/base/openapi/server:server-exec` | `springboot` 可执行 fat-jar。 |
| `//packages/base/openapi/server:server-bin` | `java_binary`，便于 `bazel run` 本地启动。 |
| `//packages/base/openapi/server:server-info` | `server_info`：网关前缀、主类、`server_id`、**无数据源**等部署元数据。 |

模块根目录 `BUILD.bazel` 当前为空文件，无聚合目标；构建入口在 `metadata/` 与 `server/`。

## 快速定位清单

1. **网关前缀与注册 ID**：`server/BUILD.bazel` 中 `server_info` → `gw_prefix = "/open-api/"`，`server_id = "openGateway"`，`no_datasource = True`。
2. **对外 HTTP 形状**：`server/src/controller/OpenAPIController.java` → 类级 `/openapi`，方法 `/{application}/**`。
3. **代理与上游**：`server/src/service/impl/ProxyServiceImpl.java`（转发、响应头、`afterFC`）。
4. **路由匹配与注册表**：`server/src/service/impl/APIServiceImpl.java`（Ant 风格路径匹配；注册数据加载 TODO）。
5. **函数链引擎**：`server/src/function/FCEngineImpl.java` 及 `function/engine/**`。
6. **应用名与端口**：`server/resources/bootstrap.yaml`（`spring.application.name`）；`server/resources/application.yaml`（端口来自 Nacos `common-env.open-gateway.port`，默认占位 `10000`）。

## 构建与入口

- **元数据库**：`//packages/base/openapi/metadata:metadata`。
- **服务端**：`//packages/base/openapi/server:server`；可运行：`//packages/base/openapi/server:server-bin`、`//packages/base/openapi/server:server-exec`。
- **部署元数据**：`//packages/base/openapi/server:server-info`。
- **主类**：`com.guhecloud.cube.base.openapi.server.OpenAPIServer`（见 `server/BUILD.bazel`）。

## 对外契约

- **HTTP**：
  - **网关层**（部署元数据）：前缀 **`/open-api/`**（`server_info`）。
  - **应用内 Controller**：`/openapi/{application}/**`（`OpenAPIController`）；完整对外 URL 是否写作 `/open-api/` + `/openapi/...` 取决于网关 **strip/rewrite** 规则，**待团队补充**（以网关与 `server_info` 的 `strip_prefix`/`gw_path_rewrite` 实际配置为准）。
- **认证 / 鉴权**：`application.yaml` 通过 Nacos 引入 `common-security-resource-server`、`common-platform-security` 等；推断为 **Spring Security OAuth2 Resource Server** 体系。**具体放行路径、JWT/OAuth2 客户端与 scope 规则以 Nacos 与平台安全约定为准**；若需精确清单，待团队补充。

## 数据与配置

- **数据库名**：`server_info` 设置 `no_datasource = True`，**本模块不声明独立业务库**；无 JDBC URL 于本模块 `application.yaml` 中体现。
- **spring.application.name**：`openapi`（`server/resources/bootstrap.yaml`）。
- **端口**：`server/resources/application.yaml` → `server.port` 引用 `common-env.open-gateway.port`，默认占位 **10000**（以环境为准）。
- **配置中心**：Nacos 多份 `common-*.yaml`（含 Redis、RocketMQ 等）；本服务**是否实际使用** Redis/MQ 需结合运行配置判断，见 [references/external-api.md](./references/external-api.md)。

## 深度阅读

- [references/external-api.md](./references/external-api.md)（对外 Bazel 标签、路由、认证、MQ）
- [references/overview.md](./references/overview.md)（目录树、目标说明、本地运行命令）
