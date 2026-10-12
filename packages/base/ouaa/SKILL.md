---
name: cube2-base-ouaa
description: >
  Cube2 `ouaa`（统一账号认证 / OAuth2 用户中心）：租户、用户、组织、角色、终端、应用与上下文 API；
  Feign 名 `ouaa`，网关前缀 `/ouaa/`。Use for packages/base/ouaa, OUAA, 认证中心, 用户中心,
  tenant/user/role Feign, or Spring Authorization Server 集成.
---

# base/ouaa

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。术语见 [.agents/skills/cube2-repo/references/glossary.md](../../../.agents/skills/cube2-repo/references/glossary.md)（`ouaa` = 统一账号认证）。

## 模块边界与职责

- **负责**：
  - **统一账号与多租户**：用户、租户、租户下用户、组织、岗位、任职、终端、应用与菜单/权限等业务 API（`server/src/controller`、`server/src/permission`、`server/src/security`）。
  - **OAuth2 / JWT 相关端点**（如 `JwtController`、`WechatController` 等与 `/oauth2/**` 路径相关的控制器；具体以代码与 Spring Security 配置为准）。
  - **对外 Feign 契约**：`client/src` 下各 `*Client` 接口（`@FeignClient(name = "ouaa")`），供其他服务调用 OUAA。
  - **跨服务消息**：RocketMQ 发布/订阅（用户更新、租户初始化、权限刷新等），见 `server/src/rocketmq`。
  - **共享 DTO/常量**：`metadata` 与 `client` 中可被其他模块依赖的类型。
- **不负责**：
  - **通用协议与工具**（分页模型、通用异常、JsonUtils 等）：`packages/platform/base`。
  - **网关路由与 rewrite 的最终落地**：以部署侧 `server_info` 与网关配置为准（本模块仅提供 `server_info` 元数据中的 `gw_prefix` 等声明）。
  - **其他业务域**（设备、流程等）：对应 `packages/base/*` 或 `packages/apps/*` 模块。

## 子模块分流

| 子目录 | Bazel label（主要） | 职责 |
|--------|---------------------|------|
| `client/` | `//packages/base/ouaa/client:client` | Feign API、DTO/查询参数；依赖 `metadata`；Maven `export` 目标名为 `:package`（`artifact_id = ouaa-client`）。 |
| `server/` | `//packages/base/ouaa/server:server`、`//packages/base/ouaa/server:server-exec`、`//packages/base/ouaa/server:server-bin` | Spring Boot 服务实现、资源与 MyBatis XML；Maven `export` 目标名为 `:package`（`artifact_id = ouaa-server`）。 |
| `metadata/` | `//packages/base/ouaa/metadata:metadata` | 共享元数据（常量、枚举、参数模型等），被 client/server 引用。 |
| `server/init/` | `//packages/base/ouaa/server/init:init-mysql` | 数据库初始化 SQL 的 `pkg_files`（供 deploy 数据库包使用），非 Java 库。 |

根目录 `packages/base/ouaa/BUILD.bazel` 当前为空，**无**根级聚合 `java_library`；请按上表子包引用。

## 快速定位清单

1. **Feign 从哪调、服务名是什么**：`client/src/*Client.java`（均为 `@FeignClient(name = "ouaa")`）；Bazel `//packages/base/ouaa/client:client`。
2. **网关对外前缀与数据库名**：`server/BUILD.bazel` 中 `server_info`（`gw_prefix`、`database`）；数据库名亦为 `application.yaml` 中 JDBC URL 的库名。
3. **HTTP 业务接口从哪找**：`server/src/controller/**`（多数为根路径映射，如 `/tenant`、`/context/user`；少数类级前缀如 `captcha`、`/public/auth/login/code`）；Bazel `//packages/base/ouaa/server:server`。
4. **权限与数据权限**：`server/src/permission/**`、`RolePermissionRefreshAspect`、`UserAuthorityRefreshAspect`。
5. **OAuth2 / JWT 公钥等**：`server/src/controller/JwtController.java`、`server/src/security/oauth2/**`。
6. **RocketMQ Topic 常量**：`server/src/rocketmq/message/*Message.java`；消费者 `server/src/rocketmq/consumer/*Consumer.java`。
7. **MyBatis Mapper 与 XML**：`server/src/dao/*Mapper.java`、`server/resources/mapper/**/*.xml`。
8. **本地可执行入口类**：`com.guhecloud.cube.biz.ouaa.server.OUAAServer`（`server/BUILD.bazel` 中 `_MAIN_CLASS`）。

## 构建与入口

- **客户端库**：`//packages/base/ouaa/client:client`
- **服务端库**：`//packages/base/ouaa/server:server`
- **本地运行（java_binary）**：`//packages/base/ouaa/server:server-bin`
- **Spring Boot 可执行 JAR（springboot 宏）**：`//packages/base/ouaa/server:server-exec`
- **Maven 发布（export 宏，artifact 名见各包 BUILD）**：`//packages/base/ouaa/client:package`、`//packages/base/ouaa/server:package`
- **部署元数据**：`//packages/base/ouaa/server:server-info`
- **元数据库**：`//packages/base/ouaa/metadata:metadata`
- **DB 初始化文件包**：`//packages/base/ouaa/server/init:init-mysql`

## 对外契约

- **HTTP**：经网关时前缀为 **`/ouaa/`**（见 `server_info` 的 `gw_prefix`）；应用内 Controller 多为 **`/` 起头的绝对路径**（如 `/tenant`、`/oauth2/jwt.key`、`/authenticated/user/page`），与网关前缀拼接后对外访问。OAuth 路径改写规则见 `server_info` 的 `gw_path_rewrite`。
- **Feign**：所有 Feign 接口使用 **`name = "ouaa"`**（与注册中心服务 ID 一致）；接口类名见「子模块分流」与 [references/external-api.md](./references/external-api.md)。
- **MQ**：Topic 与发布/订阅关系见 [references/external-api.md](./references/external-api.md)；业务日志类消息使用平台 `BizLogRecordMessage.TOPIC` + 模块枚举后缀（见该文档）。

## 数据与配置

- **数据库名**：`cube-biz-ouaa`（`server_info` 与 `server/resources/application.yaml` 中 JDBC URL 一致）。
- **spring.application.name**：仓库内 `application.yaml` **未声明**；运行时可能来自 Nacos `common-env` 等，**待团队补充**。
- **配置前缀**：本仓可见片段包括 **`common-env.ouaa.*`**（端口、第三方 token URI 等，见 `application.yaml`）、**`third-part.auth-server.uri`**；其余以 Nacos 导入的 `common-*.yaml` 为准。

## 深度阅读

- [references/external-api.md](./references/external-api.md)（对外：HTTP、Feign、MQ、Maven 坐标）
- [references/overview.md](./references/overview.md)（对内：目录树、入口类、本地运行命令）
