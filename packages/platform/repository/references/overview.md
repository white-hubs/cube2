# platform/repository 综述

- 直连引用计数可能较低；若服务需特定接口目标，在 `BUILD` 中引用 `//packages/platform/repository/interfaces:...` 等。

## 模块综述与边界

本模块提供通用仓储抽象（`Repository`、分页 `Paging`、业务/缓存/统计向扩展接口）以及基于 MyBatis-Plus 的仓储基类实现，供应用按 Bazel label 选择性依赖。**不负责**：各业务域具体实体、Mapper、表结构及业务仓储实现；这些由各应用或领域模块自行维护。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

- **`BUILD.bazel`**：模块根；声明 `java_library(name = "repository")`，当前无 `srcs`（占位/聚合语义待团队补充）。
- **`interfaces/`**：仓储接口与 Bazel 子目标；Java 包主要为 `com.guhecloud.platform.standard.interfaces`（如 `Repository`、`BussinessRepository`、`CacheRepository`、`StatisitcsRepository`）及 `com.guhecloud.platform.repository.interfaces`（`Paging`）。
- **`impl/`**：MyBatis-Plus 侧抽象基类（如 `MybatisRepository`、`MybatisStatisitcsRepository`）及包内示例实现。
- **`references/`**：模块开发手册与引用文档（含本文件）。

## BUILD target 说明

| Bazel target | 说明 |
|--------------|------|
| `//packages/platform/repository:repository` | 根库目标，当前无源码；消费方一般依赖子目录目标。 |
| `//packages/platform/repository/interfaces:repository` | `Repository.java` 基础接口。 |
| `//packages/platform/repository/interfaces:cache-repository` | `CacheRepository` 接口，依赖 `:repository`。 |
| `//packages/platform/repository/interfaces:paging` | `Paging` 分页值对象。 |
| `//packages/platform/repository/interfaces:business-repository` | `BussinessRepository` 抽象基类，依赖 `:paging`、`:repository`。 |
| `//packages/platform/repository/interfaces:statisitcs-repository` | `StatisitcsRepository` 抽象基类，依赖 `:repository`。 |
| `//packages/platform/repository/interfaces:interfaces` | 聚合导出上述接口相关目标（`exports`）。 |
| `//packages/platform/repository/impl:mybatis-repository` | `MybatisRepository` 基类。 |
| `//packages/platform/repository/impl:mybatis-stat-repository` | `MybatisStatisitcsRepository` 基类。 |
| `//packages/platform/repository/impl:mybatis-repository-example` | 示例实现，`visibility` 为 private，不对外部包开放。 |

本地可运行目标、发布专用目标：本模块为纯库，无；对外依赖方在自身 `BUILD.bazel` 中 `deps` 引用上表 label。

## 本地运行

本模块为纯库，不独立运行。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

- `//third-party/java/mybatis-plus:extension`：`impl` 包使用 MyBatis-Plus `IRepository` 等能力。
- `//packages/platform/repository/interfaces:...`：`impl` 包依赖接口子目标；上层可按需依赖 `:interfaces` 聚合或细粒度 target（见上表）。
- 其他 Bazel 依赖：以 `interfaces/BUILD.bazel`、`impl/BUILD.bazel` 中 `deps` 为准；若有遗漏说明，待团队补充。

## 常见开发场景

1. **扩展或修改仓储接口**：编辑 `interfaces/` 下 Java 源文件，并同步更新 `interfaces/BUILD.bazel` 中对应 `java_library` 的 `srcs` / `deps`；若需对外一次性导出，检查 `:interfaces` 的 `exports`。
2. **调整 MyBatis 基类行为**：修改 `impl/MybatisRepository.java`、`impl/MybatisStatisitcsRepository.java` 等，并维护 `impl/BUILD.bazel` 依赖。
3. **业务服务引用本模块**：在服务的 `BUILD.bazel` 的 `deps` 中增加所需 label（例如 `//packages/platform/repository/interfaces:business-repository`、`//packages/platform/repository/impl:mybatis-repository`），按需子集依赖以降低传递依赖面。

## 注意事项

- 类名与 Bazel target 中存在历史拼写（如 `Bussiness`、`Statisitcs`）；重命名需全仓引用与 BUILD 同步，成本待评估。
- 根目标 `//packages/platform/repository:repository` 无源码，实际集成应使用 `interfaces` 或 `impl` 下具体 target（与篇首说明一致）。
- `impl:mybatis-repository-example` 为包内私有目标，勿在应用侧直接依赖；若需对外示例，待团队补充策略。
