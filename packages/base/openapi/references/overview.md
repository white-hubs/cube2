# base/openapi — 概览

## 模块综述与边界

- **职责（一句话）**：对外开放 API 网关：按应用与路径将请求代理至内部服务（含服务发现），并可在响应后对响应体执行可选函数链；`metadata` 子包提供 HTTP 方法、响应类型、函数引擎类型等枚举供服务端与扩展复用。
- **不负责**：不托管 OpenAPI/Swagger 规范文档本身；**动态 API 注册持久化**（`APIServiceImpl` 中注册表仍以占位/TODO 为主，待团队补充）；不承担独立业务域 CRUD；**不声明独立业务数据库**（与 `server_info` 的 `no_datasource` 一致）。认证/鉴权细则以 Nacos 与平台安全模块约定为准，见 `SKILL.md` 与 `references/external-api.md`。

## 当前分层架构

**现状，未重构**：本模块采用 `controller` → `service`（含 `service/impl`）→ `entity` / `vo` 与并列的 `function`（含 `function/engine` 下 Java/JavaScript 执行引擎）结构；**无** `domain/`、`mapper/` 或 MyBatis 持久化分层。若后续引入数据库与映射层，以仓库实际结构为准。

## 目录与包结构

```text
packages/base/openapi/
├── BUILD.bazel                 # 当前为空文件
├── SKILL.md
├── references/
│   ├── external-api.md
│   └── overview.md
├── metadata/
│   ├── BUILD.bazel
│   └── src/
│       └── enumeration/        # HTTPMethod, ResponseType, FCEngineType
└── server/
    ├── BUILD.bazel
    ├── resources/
    │   ├── application.yaml
    │   └── bootstrap.yaml
    └── src/
        ├── OpenAPIServer.java
        ├── controller/
        ├── entity/
        ├── function/           # FCEngine、java/js 执行引擎等
        ├── service/            # 含 impl/
        └── vo/
```

**说明**：`server/src` 下**无**名为 `domain/`、`mapper/` 的包；持久化与 MyBatis 映射不在本模块当前结构中。若后续增加，以仓库为准。

| 包路径（相对 `server/src` 或 `metadata/src`） | 用途 |
|-----------------------------------------------|------|
| `com.guhecloud.cube.base.openapi.server` | Spring Boot 启动类 `OpenAPIServer`。 |
| `...server.controller` | HTTP 入口（如 `OpenAPIController`）。 |
| `...server.service` / `...server.service.impl` | 接口与实现：`ProxyService`、`APIService` 及代理、路由匹配逻辑。 |
| `...server.entity` | 领域模型类（如 `RegisteredAPI`、`FunctionDefinition`）。 |
| `...server.vo` | 对外/内部传参 VO。 |
| `...server.function` | 函数链引擎入口（如 `FCEngine`/`FCEngineImpl`）。 |
| `...server.function.engine` / `...engine.java` / `...engine.js` | 执行引擎抽象与各语言实现（含 Java sandbox 等）。 |
| `com.guhecloud.cube.base.openapi.metadata.enumeration` | 元数据枚举：`HTTPMethod`、`ResponseType`、`FCEngineType` 等。 |

**开发入口（关键类）**：
- **Spring Boot 启动类**：`com.guhecloud.cube.base.openapi.server.OpenAPIServer`
- **对外 HTTP 入口**：`com.guhecloud.cube.base.openapi.server.controller.OpenAPIController`
- **代理核心**：`com.guhecloud.cube.base.openapi.server.service.impl.ProxyServiceImpl`
- **API 匹配与（未来）注册表**：`com.guhecloud.cube.base.openapi.server.service.impl.APIServiceImpl`

## BUILD target 说明

| Target | 说明 |
|--------|------|
| `//packages/base/openapi/metadata:metadata` | 元数据枚举等，供 server 编译依赖。 |
| `//packages/base/openapi/server:deps` | 私有依赖聚合，`visibility` 私有。 |
| `//packages/base/openapi/server:server` | 主 `java_library`，**对外依赖**时引用此标签。 |
| `//packages/base/openapi/server:server-bin` | `java_binary`，`main_class` = `...OpenAPIServer`，**本地运行**时使用。 |
| `//packages/base/openapi/server:server-exec` | Spring Boot 可执行 jar（`springboot` 规则），**发布**时构建。 |
| `//packages/base/openapi/server:server-info` | 网关前缀 `/open-api/`、`server_id = openGateway`、`no_datasource = True` 等。 |

`server` 的 `runtime_deps` 中显式包含 `//packages/platform/base/utils:string`（供 Java 函数链等运行时路径使用）。

## 本地运行

在仓库根目录执行（需已配置 Bazel 与 JDK）：

```bash
bazel run //packages/base/openapi/server:server-bin
```

打包 Spring Boot 可执行 jar：

```bash
bazel build //packages/base/openapi/server:server-exec
```

默认监听端口来自 Nacos `common-env.open-gateway.port`，占位默认 **10000**（见 `server/resources/application.yaml`）；本地若未连 Nacos，需**待团队补充**最小本地 profile 或环境变量覆盖方式。

运行前提与变量补充：

- **Nacos**：`application.yaml` 通过 `spring.config.import` 拉取多份 `common-*.yaml`；默认 `NACOS_ADDR` 等见 `server/resources/application.yaml`。无 Nacos 时本地启动方式待团队补充。
- **服务发现**：`spring.cloud.nacos.discovery.enabled: true`，`spring.cloud.loadbalancer` 启用；本地单机调试是否 Mock 负载均衡待团队补充。
- **端口**：`server.port` 绑定 `${common-env.open-gateway.port:10000}`。

## 数据层

- **数据库名**：`server_info`（`server/BUILD.bazel`）中 `no_datasource = True`，**本模块不绑定独立业务数据源**；与 `server_info.database` 一致地视为无库（若部署元数据另有约定以 CI/发布配置为准）。
- **Liquibase / migration**：本模块根下**无** `init/` 等 migration 目录；无本模块专属 Liquibase 路径可列。
- **核心表**：不适用（无业务库）；API 注册表若未来落库，**待团队补充** 表设计与 migration 归属模块。

## 直接依赖说明

`//packages/base/openapi/server:deps` 聚合导出依赖（`server/BUILD.bazel`），含义如下：

| Bazel label | 能力说明 |
|-------------|----------|
| `//packages/base/openapi/metadata` | 本模块 `metadata` 库：枚举与元数据类型。 |
| `//packages/platform/base/utils:ant-path-matcher` | Ant 风格路径匹配（API 路径匹配等）。 |
| `//packages/platform/base/utils:string` | 字符串工具（`server` 的 `runtime_deps` 亦引用，供函数链等使用）。 |
| `//packages/platform/security` | 平台安全能力（与 Spring Security / OAuth2 Resource Server 等集成，具体以配置为准）。 |
| `//packages/platform/server` | Cube 平台 Spring Boot 服务端公共基座与约定。 |
| `//third-party/java/apache:hc5` / `:hc5-client` | Apache HttpClient 5：对外发起 HTTP 代理请求。 |
| `//third-party/java/apache:tomcat_embed_core` | 嵌入式 Tomcat 相关依赖（Servlet 容器侧）。 |

## 常见开发场景

1. **调整对外 HTTP 路径或转发行为**：修改 `server/src/controller/OpenAPIController.java`；代理与响应头、`afterFC` 逻辑见 `server/src/service/impl/ProxyServiceImpl.java`。
2. **调整 API 匹配与注册数据**：`server/src/service/impl/APIServiceImpl.java`（当前注册加载仍为占位/TODO，与产品方案同步后再改）。
3. **扩展或修改函数链执行（Java/JS）**：`server/src/function/FCEngineImpl.java`、`function/engine/java/**`、`function/engine/js/**`。
4. **新增或修改 API 元数据枚举**：`metadata/src/enumeration/*.java`，并确保 `server` 依赖 `//packages/base/openapi/metadata:metadata`（已由 `:deps` 聚合）。

## 注意事项

- **无数据源**：不要在未引入 `server_info`/部署约定变更的情况下假设本模块具备 JDBC；业务持久化应落在负责数据域的模块。
- **注册表与 TODO**：`APIServiceImpl` 中动态 API 注册数据来源仍以占位/待实现为主，改动前与产品/平台同事对齐。
- **网关前缀与 Controller 路径**：部署侧 `gw_prefix`（如 `/open-api/`）与应用内 `/openapi/...` 的拼接与 strip/rewrite 规则以网关与 `server_info` 实际配置为准（见 `SKILL.md`「对外契约」）。
- **Nacos 覆盖项**：端口、安全、Redis、RocketMQ、Feign、HttpClient 等均以 Nacos 中 `common-*.yaml` 为准；本地 `application.yaml` 仅为占位与 import 列表。
- **代码生成产物**：本模块当前未见统一代码生成目录约定；若后续引入生成代码，**待团队补充** 禁止手改路径说明。
