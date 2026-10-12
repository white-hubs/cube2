# platform/web 综述

- BUILD 引用计数在 apps/base 中可能为 0，仍可能被 `server`/`cloud` 等传递引入；排查依赖时用 `bazel query` / deps-server。

## 模块综述与边界

本模块为 **Spring Web MVC 通用能力库**：提供全局异常与校验错误处理、TraceId 过滤器、请求日志与 `Requester` 方法参数解析、扩展 HTTP 消息转换器（如 YAML）、以及 Spring 6 下 GET 请求对象绑定的兼容配置等；供 `//packages/platform/server`、`cloud`、`security` 等组合进应用 classpath。

**不负责**：具体业务 HTTP 接口与路由、领域业务逻辑、认证授权策略实现（见 `packages/platform/security` 等）、独立可运行的 HTTP 服务进程。CORS 等若未在本包代码中出现，以应用侧或其它平台模块配置为准（待团队补充）。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

模块根目录：`packages/platform/web/`。

`src/` 下（展开 2 层；Java 包名为 `com.guhecloud.platform.base.*`，与物理模块路径不一致，见「注意事项」）：

| 目录 | 用途 |
|------|------|
| `src/config/` | WebMvc 相关配置：`WebMvcControllerConfig`（Spring 6 GET 对象绑定兼容）、`ExtraHttpMessageConverter`（扩展 YAML 等消息转换器） |
| `src/filter/` | Servlet `Filter`，如 `TraceIdFilter`（`X-Trace-Id` 透传/生成） |
| `src/interceptor/` | 拦截器与参数解析：`GlobalExceptionInterceptor`、`RequestLoggingInterceptor`、`RequesterArgumentResolver` |
| `src/util/` | HTTP/Servlet/TraceId 辅助：`HttpUtils`、`ServletUtils`、`TraceIdUtils` |

## BUILD target 说明

| Target | 说明 |
|--------|------|
| `//packages/platform/web:web` | 主 `java_library`，聚合 `src/**/*.java`，供其它 Bazel 包 `deps` 引用 |
| `//packages/platform/web:export` | `java_export`，对外 Maven 坐标 `platform-web`（版本见 `PLATFORM_VERSION`），供 BOM 或非 Bazel 消费方 |
| `//packages/platform/web:bom` | `maven_bom`，聚合本模块 export，用于依赖版本对齐 |

本模块无 `bazel run` 型可执行目标；本地运行见下节。

## 本地运行

本模块为纯库，不独立运行。接入方通过依赖 `:web` 或 `:export` 在 Spring Boot 应用中随进程启动。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

以下为 `BUILD.bazel` 中 `java_library` `deps` 的主要能力（细粒度以 BUILD 为准）：

| Bazel label（节选） | 说明 |
|---------------------|------|
| `//packages/platform/base` | 平台 base 能力聚合 |
| `//packages/platform/base/standard:exception` | 标准异常类型，供全局异常拦截器映射 |
| `//packages/platform/base/model:requester`、`//packages/platform/base/model:error-response` | 请求人模型与统一错误响应体 |
| `//packages/platform/base/utils:*`（`host`、`string`、`object`、`collection`、`map`、`json` 等） | 字符串、集合、JSON 等工具 |
| `//packages/platform:generic-deps` | 平台层通用依赖集合 |
| `//third-party/java/spring:{springframework,spring_boot}` | Spring MVC / Boot Web |
| `//third-party/java/jakarta:servlet_api` | Jakarta Servlet API |
| `//third-party/java/apache:tomcat` | 与 Servlet 栈相关的第三方依赖 |
| `//third-party/java/jackson` | JSON 序列化 |
| `//third-party/java/google:guava` | Guava 工具 |
| `//third-party/java/slf4j` | 日志门面 |
| `//third-party/java/aspectj:aspectjweaver` | AspectJ 运行时（与 Spring/AOP 链配合） |

## 常见开发场景

1. **调整全局异常或参数校验错误响应**  
   修改 `src/interceptor/GlobalExceptionInterceptor.java`；异常类型与 `ErrorResponse` 契约需与 `//packages/platform/base` 对齐。

2. **调整 TraceId 生成/响应头/日志关联**  
   修改 `src/filter/TraceIdFilter.java`、`src/util/TraceIdUtils.java`；注意与网关或上游 `X-Trace-Id` 约定一致（细节待团队补充）。

3. **调整请求日志或 `Requester` 注入行为**  
   修改 `src/interceptor/RequestLoggingInterceptor.java`、`src/interceptor/RequesterArgumentResolver.java`。

4. **扩展 HTTP 消息转换或 Spring MVC 绑定行为**  
   修改 `src/config/ExtraHttpMessageConverter.java`、`src/config/WebMvcControllerConfig.java`。

## 注意事项

- **包名与目录**：源码包前缀为 `com.guhecloud.platform.base.*`，模块物理路径为 `packages/platform/web`，导航与 import 时勿混淆。
- **传递依赖**：见文首 bullet——引用计数为 0 仍可能经 `server`/`cloud` 等传递引入；排查用 `bazel query` / 团队约定的 `deps-server`（待团队补充具体命令与文档链接）。
- **发布与文档**：`export`/`bom` 带 `no-javadocs` 标签；若需对外 JavaDoc 策略，待团队补充。
- **与 `web-extra` 等平台包的分工**：若能力拆在 `packages/platform/web-extra` 等模块，改动前确认归属，避免重复注册同一 Web 组件导致冲突（待团队补充清单）。
