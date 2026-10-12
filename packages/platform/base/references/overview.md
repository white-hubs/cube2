# platform/base — 开发手册

本文档回答：platform/base 提供了哪些能力、如何引用其子目标、以及使用时的常见场景与注意事项。

## 模块综述与边界

**负责**：横切协议与通用工具的聚合层——通用常量（`constants`）、分页/错误响应/树等通用模型（`model`）、统一异常族/Jackson序列化/HTTP标准（`standard`）、数十个细粒度工具类（`utils`）、以及 Spring 上下文/HTTP头常量/线程池/Jackson 自动配置（`src`）。

**不负责**：业务逻辑、持久层（见 `platform/database`）、安全/鉴权（见 `platform/security`）、Spring Cloud 接入（见 `platform/cloud`）。

## 目录与包结构

platform/base 是**纯库模块**，无 Spring Boot 服务，不独立运行。内部按能力域拆分为多个子包，每个子包有独立的 Bazel target（最小化依赖传递）。

```text
packages/platform/base/
├── BUILD.bazel            # :base（聚合）+ :export + :bom
├── constants/BUILD.bazel  # :date
├── model/BUILD.bazel      # :pagination :paging :error-response :tree :requester :period ...
├── standard/BUILD.bazel   # :exception :jackson :http
├── utils/BUILD.bazel      # 30+ 细粒度 target
└── src/                   # Spring 自动配置源码
    ├── config/            # ThreadPoolConfigurer、JacksonConfigurer、ObjectCopierConfigurer
    ├── context/           # SessionContextHolder、ApplicationContextHolder
    ├── http/              # HttpConstants、converter/
    └── interceptor/       # StringDateConverter、StringPeriodConverter
```

各子目录能力定位：

| 子目录 | 定位 |
|--------|------|
| `src/` | Spring 自动配置（线程池、Jackson、ObjectCopier）、`HttpConstants`、`SessionContextHolder` 等 |
| `constants/` | 日期格式等约定常量 |
| `model/` | 分页（`pagination`、`paging`）、错误响应（`error-response`）、树（`tree`）、请求者（`requester`）、时间段（`period`）等 |
| `standard/` | 统一异常族（`exception`）、Jackson 配置（`jackson`）、HTTP 标准（`http`） |
| `utils/` | 30+ 细粒度工具 target（`aes`、`json`、`date`、`string`、`collection`、`id`、`bean`、`barcode` 等） |

## BUILD target 说明

| Target | 用途 | 何时引用 |
|--------|------|---------|
| `//packages/platform/base` | 聚合：`src/` 中所有自动配置 + 常用 utils | 需要 Spring 上下文/线程池/HttpConstants/ObjectCopier 时 |
| `//packages/platform/base:export` | Maven 发布坐标 `platform-base` | 发布 Maven artifact 时（开发阶段不需要） |
| `//packages/platform/base:bom` | Maven BOM | 发布 BOM 时（开发阶段不需要） |
| `//packages/platform/base/constants:date` | 日期格式常量 | 仅需日期格式时 |
| `//packages/platform/base/model:error-response` | 统一错误响应体 | Controller 统一返回格式 |
| `//packages/platform/base/model:pagination` | 分页请求/结果模型 | 分页接口 |
| `//packages/platform/base/model:requester` | 当前请求者信息 | 需要传递 Requester 上下文时 |
| `//packages/platform/base/model:tree` | 树形结构 | 树形数据返回 |
| `//packages/platform/base/standard:exception` | 统一异常族（`BusinessException` 等） | 业务异常抛出/捕获 |
| `//packages/platform/base/standard:jackson` | Jackson 配置 Bean | 需要定制序列化时 |
| `//packages/platform/base/utils:<name>` | 细粒度工具（json/date/string/id/...） | **按需引入单个 target**，避免引入整条 base |

## 直接依赖说明

platform/base 本身依赖：
- `//packages/platform:generic-deps` — Spring Boot/Cloud 核心 BOM
- `//third-party/java/jackson:databind` — JSON 序列化
- `//third-party/java/glasnost:orika` — Bean 映射（`ObjectCopier`）
- `//third-party/java/spring:spring_web` — Spring MVC 基础

## 常见开发场景

**场景 1：业务服务需要统一异常与分页模型**
```python
# server/BUILD.bazel 的 deps 聚合目标中
exports = [
    "//packages/platform/base/model:error-response",
    "//packages/platform/base/model:pagination",
    "//packages/platform/base/standard:exception",
]
```

**场景 2：只需某个工具类（如 JSON 工具）**
```python
exports = [
    "//packages/platform/base/utils:json",  # 仅引入 JsonUtils，不拉入整条 base
]
```

**场景 3：需要 Spring 上下文支持（SessionContextHolder / ThreadPool）**
```python
exports = [
    "//packages/platform/base",  # 包含 src/ 下所有自动配置
]
```

## 注意事项

- **优先引用细粒度子目标**（`utils:<name>`、`model:<name>`），避免引入整条 `//packages/platform/base` 导致不必要的传递依赖
- `//packages/platform/server` 已包含 `//packages/platform/base`，若业务 `deps` 中已引 `server`，通常不需要再单独引 `base`
- `src/` 中的配置类通过 Spring Boot 自动装配生效，不需要手动 `@Import`
- `HttpConstants` 中定义了所有标准 HTTP 头名（租户、终端、签名等），引用时以此为准，不要自定义重复常量

## 当前分层架构

本模块为纯库，无业务分层。内部以**能力域**为维度划分子目录（而非 controller/service/mapper 分层），每个子目录对应一类横切能力：

| 能力域 | 子目录 | 说明 |
|--------|--------|------|
| Spring 自动配置 | `src/` | 线程池、Jackson、ObjectCopier、SessionContextHolder、HttpConstants |
| 约定常量 | `constants/` | 日期格式常量等 |
| 通用模型 | `model/` | 分页、错误响应、树、请求者、时间段等值对象 |
| 标准协议 | `standard/` | 统一异常、Jackson 配置、HTTP 标准 |
| 工具集 | `utils/` | 30+ 细粒度无状态工具类 |

各子目录内部无进一步分层约束，按 Bazel target 粒度对外暴露（现状，无需重构）。

## 本地运行

本模块为纯库，不独立运行。

## 数据层

本模块为纯库，无数据层。
