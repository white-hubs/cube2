---
name: cube2-platform-base
description: >
  Cube2 platform-base: shared protocols and utilities—DateFormat constants,
  Pagination/Paging/ErrorResponse models, standard exceptions and Jackson
  (desensitize, decimal), HTTP header constants (tenant, authorization),
  JsonUtils/StringUtils/DateUtils/AESUtils and many granular //base/utils:* Bazel
  targets, Spring context holders and HTTP/YAML message converters. Use for
  packages/platform/base, platform-base, API error types, pagination DTO, or
  平台通用库/工具类/分页/错误码/Jackson脱敏.
---

# platform/base

总览见 [上层 platform SKILL](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：
  - **约定常量**（[`constants`](constants)：日期/时间格式字符串、时区等）。
  - **通用协议模型**（[`model`](model)：`Pagination`/`Paging`、`Period`、`ErrorResponse`、`Tree`、`Requester`、枚举等；细粒度 Bazel 目标 + `:models` 聚合）。
  - **异常与序列化协议**（[`standard`](standard)：`exception` 下 REST/业务异常族、`ErrorTypeEnum`/`ErrorSpec`；`jackson` 模块与脱敏/小数注解；`http` 下 HttpClient 工厂）。
  - **工具类矩阵**（[`utils`](utils)：加解密、字符串/集合/树、日期与 ID、JSON、反射与 Bean、条码、拼音、Ant 路径匹配、POI 导出等；**每个大类多为独立 `java_library` 目标**）。
  - **运行时横切**（[`src`](src)：`ApplicationContextHolder`、会话/执行器上下文、**`HttpConstants`（authorization、tenant、terminal、version、signature、permission 等请求头）**、Jackson/线程池/ObjectCopier 配置、HTTP 消息转换含 YAML、日期/周期请求参数转换器）。
- **不负责**：各业务域实体、表结构、业务流程；**具体认证实现**在 `security`/`ouaa` 等模块（此处仅 **协议常量与异常类型** 层面）。

## 子模块分流

**不拆第三层 SKILL**；按子目录阅读：

| 子目录 | 内容侧重 |
|--------|----------|
| `constants/` | 时间与格式「协议数字符串」 |
| `model/` | 分页、错误响应、树、时间段等 **API 通用载体** |
| `standard/exception` | **统一异常类型与错误规范** |
| `standard/jackson` | **JSON 表现层**（脱敏、小数、LocalDate 序列化等） |
| `standard/http` | HTTP 客户端构造 |
| `utils/` | **按 Bazel 目标按需引用** 的工具实现（见 catalog） |
| `src/` | Spring 与 **HTTP 协议常量**、Web 层转换与配置 |

详表：[references/catalog.md](./references/catalog.md)。

## 快速定位清单

1. **错误怎么抛、响应长什么样**：`standard/exception/*.java`、`model/ErrorResponse.java`；Bazel 常引 `//packages/platform/base/standard:exception`、`//packages/platform/base/model:error-response`。
2. **分页/排序入参出参**：`model/Pagination.java`、`Paging.java`。
3. **日期字符串格式与解析**：`constants/DateFormat.java`、`utils/DateUtils.java`、`standard/jackson`；Bazel `constants:date`、`utils:date`。
4. **JSON 读写**：`utils/JsonUtils.java` → `//packages/platform/base/utils:json`。
5. **网关/权限常用的路径匹配**：`utils/AntPathMatcher.java` → `utils:ant-path-matcher`。
6. **请求头名字不要硬编码**：`src/http/HttpConstants.java`（tenant、authorization 等）。
7. **我要最小依赖但要用某个工具**：在 [catalog.md](./references/catalog.md) 查 **Bazel 目标名**，只加对应 `//packages/platform/base/utils:xxx`。
8. **Maven 对外**：根 [BUILD.bazel](BUILD.bazel) 的 `export` / `bom`。

## 构建与入口

- 聚合库：`//packages/platform/base`（`:base`）。
- 发布：`//packages/platform/base:export`、`//packages/platform/base:bom`。
- 细粒度：`//packages/platform/base/utils:*`、`//packages/platform/base/model:*`、`//packages/platform/base/standard:*`、`//packages/platform/base/constants:date`。

## 对外契约

- **Java API**：工具与模型类（包名 `com.guhecloud.platform.base.*`）。
- **隐式协议**：错误类型枚举、错误体结构、日期格式常量、常用 HTTP 头名、Jackson 注解语义（**非 REST 文档**，以代码为准）。

## 数据与配置

无独立数据源；配置类在 `src/config`（线程池、Jackson 等）随 Spring Boot 启用。

## 深度阅读

- [references/catalog.md](./references/catalog.md)（**逐项扫描，必读**）
- [references/overview.md](./references/overview.md)
