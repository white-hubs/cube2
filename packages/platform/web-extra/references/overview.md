# platform/web-extra 综述

- 直连 BUILD 引用可能较少，多经 BOM 或上层聚合；需要时显式添加 `//packages/platform/web-extra`。

## 模块综述与边界

- **职责**：`platform/web-extra` 是平台侧 **Web 扩展库**，在 Spring Web 层提供横切能力；当前源码以全局异常处理为主（例如将数据库/MyBatis 相关异常转为统一 HTTP 响应），依赖 `//packages/platform/web` 与 `//packages/platform/database` 等。
- **不负责**：业务字段导出、业务权限规则、具体业务 Controller/Service 实现；这些由业务应用或其它平台模块承担。聚合与发布形态见上文「直连 BUILD」说明。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

```text
packages/platform/web-extra/
├── BUILD.bazel
├── SKILL.md
├── references/
│   └── overview.md          # 本开发手册
└── src/
    └── interceptor/         # Web 全局切面/拦截相关实现
        └── DatabaseExceptionInterceptor.java   # @RestControllerAdvice：唯一约束、MyBatis 系统异常等
```

- **Java 包（`src` 下 2 层）**：`com.guhecloud.platform.database.interceptor` — 数据库相关 Web 层异常处理与响应封装。

## BUILD target 说明

| Target | 说明 |
|--------|------|
| `//packages/platform/web-extra:web-extra` | `java_library`：本模块全部 `src/**/*.java` 源码，供其它 Bazel 目标 `deps` 引用。 |
| `//packages/platform/web-extra:export` | `java_export`：Maven 坐标发布（`platform-web-extra`），`runtime_deps` 指向 `:web-extra`。 |
| `//packages/platform/web-extra:bom` | `maven_bom`：聚合本模块导出坐标，供上层 BOM 或依赖管理引用。 |

本模块无 `bazel run` 可执行目标；消费方通过 `deps` 或 Maven 坐标引入。

## 本地运行

本模块为纯库，不独立运行。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

以下为 `BUILD.bazel` 中 `:web-extra` 的主要 Bazel 依赖及其作用（与仓库实际 `deps` 一致）：

| Bazel label | 能力说明 |
|-------------|----------|
| `//packages/platform/base` | 平台基础能力。 |
| `//packages/platform/base/model:error-response` | 统一错误响应模型（如 `ErrorResponse`）。 |
| `//packages/platform/base/utils:exception` | 异常工具（如 `ExceptionUtils`）。 |
| `//packages/platform/database` | 数据库访问层与异常类型（如 `UniqueViolationException`）。 |
| `//packages/platform/web` | 平台 Web 基础（与 Servlet/Spring Web 协同）。 |
| `//third-party/java:lombok` | Lombok 注解。 |
| `//third-party/java/apache:tomcat` | Servlet API（如 `HttpServletResponse`）。 |
| `//third-party/java/aspectj:aspectjweaver` | AspectJ（Spring AOP 生态）。 |
| `//third-party/java/google:guava` | Guava 工具库。 |
| `//third-party/java/jackson` | Jackson JSON。 |
| `//third-party/java/slf4j` | 日志门面。 |
| `//third-party/java/spring:spring_boot` | Spring Boot。 |
| `//third-party/java/spring:springframework` | Spring Framework（含 `@RestControllerAdvice`、`@ExceptionHandler` 等）。 |

## 常见开发场景

1. **新增或调整全局 Web 异常处理**  
   - 修改：`src/**/*.java`（例如 `src/interceptor/` 下 `DatabaseExceptionInterceptor` 或同类 `@RestControllerAdvice`）。  
   - 同步检查：`BUILD.bazel` 若新增对其它平台模块或三方库的引用，需在 `deps` 中显式声明。

2. **在业务服务中引入本库**  
   - Bazel：在应用或库的 `deps` 中加入 `//packages/platform/web-extra` 或按需 `:export` 发布的 Maven 坐标。  
   - 确认与 `//packages/platform/web`、数据库模块版本一致，避免类路径上重复或冲突。

3. **发布与 BOM 消费**  
   - 关注 `:export` 与 `:bom` 目标；多经上层聚合 BOM 引用时，直连 label 可能较少，需显式添加时见文件开头说明。

## 注意事项

- 修改全局异常处理会影响所有引入本库的 Web 应用的错误响应形态，变更前应评估对前端契约与监控告警的影响。
- `java_export` 带 `tags = ["no-javadocs"]`，发布行为以团队约定为准。
- 其余约束（如与 Nacos、多环境配置覆盖）若与本库无直接关联，**待团队补充**。
