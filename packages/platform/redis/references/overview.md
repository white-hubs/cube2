# platform/redis 综述

- `cloud` 等包曾依赖 `redis`；直连或传递依赖以各 `BUILD.bazel` 为准。

## 模块综述与边界

本模块为平台层 Redis 能力库：封装 **Redisson** 客户端与 Spring 配置，提供 **权限缓存**（`authority/`，实现 `PermissionCache` 等）、**Token 活跃状态与重放校验**（`token/`）、**Redis 发布/订阅监听注册**（`listener/` + `annotation/RedisSubscribe`）、**签名校验**（`signature/`）及通用工具（`util/`）。

**不负责**：业务领域模型与 HTTP/API；数据库访问与事务（见各业务模块与 `platform/database`）；安全策略定义本身（见 `platform/security`，本模块仅在其上提供 Redis 侧缓存与集成）；Redis 集群/实例的运维与部署（见基础设施与团队文档）。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

模块根目录：`packages/platform/redis/`；源码根：`src/`（Java 包前缀 `com.guhecloud.platform.redis.*`）。展开两层如下（每包一行说明）：

| 路径 | 用途 |
|------|------|
| `src/annotation/` | `@RedisSubscribe`：声明 Redis 频道订阅方法，供 `listener` 扫描注册。 |
| `src/authority/cache/` | `PrincipalPermissionCache`：主体权限写入/读取 Redis，对接 `platform.security` 的 `PermissionCache`。 |
| `src/authority/message/` | `AuthorityMessage`：权限相关消息结构（与订阅/广播配合）。 |
| `src/authority/` | `RedisAuthorityListener`：权限变更等 Redis 侧监听逻辑。 |
| `src/config/` | `RedissonConfiguration`、`RedisProperties`、`RedisConfigurer`、`RedisJwtActiveStoreProperties`：Redisson/JWT 活跃存储等自动配置与属性。 |
| `src/event/` | `TokenRemoveEvent`：Token 移除等事件类型。 |
| `src/listener/` | `RedisSubscribeListenerRegistry`：启动时注册 Redis `MessageListener` 与频道。 |
| `src/signature/` | `RedisSigner`：基于 Redis 的签名校验辅助。 |
| `src/token/` | `RedisJwtActiveValidator`、`RedisTokenReplayValidator`：JWT 活跃校验、Token 重放校验。 |
| `src/util/` | `RedisUtils`：通用 Redis 操作封装。 |

## BUILD target 说明

| Target（Bazel label） | 类型 | 用途 |
|----------------------|------|------|
| `//packages/platform/redis:redis` | `java_library` | 本模块主库：聚合 `src/**/*.java`，对外 `exports` Redisson；运行时依赖 Lettuce（`lettuce_core`）。 |
| `//packages/platform/redis:export` | `java_export` | Maven 坐标发布（`platform-redis` 构件），供外部或 Gradle/Maven 消费；`tags = ["no-javadocs"]`。 |
| `//packages/platform/redis:bom` | `maven_bom` | 与 `export` 配套的 BOM，统一版本对齐。 |

本模块无 `java_binary` / 可执行入口；消费方在其应用 `deps` 中引用 `:redis` 或 `:export` 即可。

## 本地运行

本模块为纯库，不独立运行。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

以下为 `BUILD.bazel` 中 `:redis` 的 `deps` / `runtime_deps` / `exports` 所涉能力摘要（具体以 `BUILD.bazel` 为准）：

| Bazel label | 说明 |
|-------------|------|
| `//packages/platform/base` | 平台基础能力（上下文、通用工具等）。 |
| `//packages/platform/base/model:requester` | 请求方/调用上下文相关模型。 |
| `//packages/platform/base/utils:collection` | 集合工具。 |
| `//packages/platform/base/utils:digest` | 摘要/哈希相关工具。 |
| `//packages/platform/base/utils:json` | JSON 序列化工具。 |
| `//packages/platform/base/utils:map` | Map 工具。 |
| `//packages/platform/base/utils:object` | 对象工具。 |
| `//packages/platform/base/utils:string` | 字符串工具。 |
| `//packages/platform/security` | 安全主体、权限模型与 `PermissionCache` 等契约。 |
| `//third-party/java:lombok` | Lombok 注解。 |
| `//third-party/java/alibaba:fastjson2` | Fastjson2。 |
| `//third-party/java/apache:tomcat` | Servlet/Tomcat API（部分类型引用）。 |
| `//third-party/java/jackson` | Jackson。 |
| `//third-party/java/jakarta:servlet_api` | Jakarta Servlet API。 |
| `//third-party/java/redis:redisson` | Redisson 客户端（本库 `exports` 同目标，供依赖方直接使用 `RedissonClient` 类型）。 |
| `//third-party/java/slf4j` | 日志门面。 |
| `//third-party/java/spring:oauth2_authorization_server` | OAuth2 授权服务器相关类型（与 Token 校验等集成）。 |
| `//third-party/java/spring:spring_boot` | Spring Boot 核心。 |
| `//third-party/java/spring:spring_boot_autoconfigure` | 自动配置支持。 |
| `//third-party/java/spring:spring_data` | Spring Data Redis 等（`RedisTemplate`、监听器等）。 |
| `//third-party/java/spring:spring_security` | Spring Security 集成。 |
| `//third-party/java/spring:springframework` | Spring Framework 核心。 |
| `//third-party/java/lettuce:lettuce_core` | **runtime**：Lettuce 驱动（与 Spring Data Redis 栈配合）。 |

## 常见开发场景

1. **调整 Redisson / Redis 连接与属性**  
   - 修改：`src/config/` 下 `RedissonConfiguration`、`RedisProperties`、`RedisConfigurer` 等；核对应用侧 `spring.redis.*` 与集群节点格式（集群地址需 `redis://` 前缀等，见类内注释）。

2. **扩展权限缓存或权限广播行为**  
   - 修改：`src/authority/cache/PrincipalPermissionCache.java`、`src/authority/RedisAuthorityListener.java`、`src/authority/message/AuthorityMessage.java`；注意与 `platform.security` 的 `PermissionCache` 契约一致。

3. **新增 Redis 频道订阅处理**  
   - 新增或修改：带 `@RedisSubscribe` 的方法（`src/annotation/RedisSubscribe.java`）；必要时调整 `src/listener/RedisSubscribeListenerRegistry.java` 的扫描与注册逻辑。

4. **调整 JWT 活跃或 Token 重放策略**  
   - 修改：`src/token/` 下校验类；配置相关见 `src/config/RedisJwtActiveStoreProperties.java` 等。

（其余场景待团队补充。）

## 注意事项

- 本库将 Redisson 置于 `exports`，依赖方 classpath 会直接暴露 Redisson API；升级 Redisson 时需联动验证所有引用模块。
- 集群模式下 Redis 节点地址格式错误会导致启动失败，详见 `RedissonConfiguration` 内注释。
- `java_export` 带 `no-javadocs`，发布物 Javadoc 行为以团队发布流程为准。
- 与 Nacos、多数据源或业务专属 Redis 实例的划分：若存在覆盖或双数据源约定，待团队补充至各应用配置说明。
