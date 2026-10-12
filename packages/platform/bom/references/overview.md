# platform/bom 综述

- 无 `src`；仅 Starlark + Maven BOM。变更聚合列表时同步发布流程与 consumer。

## 模块综述与边界

本模块为 **顶层 platform Maven BOM**：通过 Bazel `maven_bom` 聚合多路 `//packages/platform/*:export`，对外发布统一坐标下的 BOM 工件，供应用或其它构建在非 Bazel 场景下对齐 platform 组件版本（与 [BUILD.bazel](../BUILD.bazel) 中 `artifactId = platform-bom`、`PACKAGE_GROUP` / `PLATFORM_VERSION` 一致，见 `//packages/platform:variables.bzl`）。

**不负责**：业务应用内的具体依赖冲突解析与排除、各子模块实现细节、非 platform 依赖的版本裁决；后者在各应用 `BUILD.bazel` / `MODULE.bazel` 或业务 BOM 中处理（见仓库级原则文档）。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

模块根目录：`packages/platform/bom/`。

无 `src/`。顶层与 `references/` 展开 2 层：

| 路径 | 用途 |
|------|------|
| `BUILD.bazel` | 定义 `maven_bom` 目标 `bom`，列出纳入 BOM 的 `java_exports` |
| `SKILL.md` | 模块级 Agent 导引与边界 |
| `references/overview.md` | 本文档：开发手册主体 |

## BUILD target 说明

| Target | 说明 |
|--------|------|
| `//packages/platform/bom:bom` | `maven_bom`：Maven 坐标为 `com.guhecloud.platform:platform-bom:<PLATFORM_VERSION>`（版本见 `//packages/platform:variables.bzl`）；`dependencies_maven_coordinates` 指向同组 `platform-bom-dependencies` 工件，用于与规则生成的依赖 BOM 对齐；`java_exports` 聚合下列 platform `export` 目标 |

本模块无 `bazel run` 型可执行目标；无可单独「运行」的服务进程。

## 本地运行

本模块为纯库，不独立运行。发布与验证流程以团队 Maven/Bazel 发布约定为准（待团队补充）。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

本目标不声明传统 `deps` 库链，而是通过 `maven_bom` 的 **`java_exports`** 将下列 Bazel 目标纳入 BOM（消费方通过 BOM 对齐这些构件的版本；各目标对应 Maven 坐标以各子包 `java_export` 为准）：

| Bazel label | 说明 |
|-------------|------|
| `//packages/platform/base:export` | platform base 能力，对外 Maven 导出 |
| `//packages/platform/cloud:export` | 云/Spring Cloud 相关能力导出 |
| `//packages/platform/database:export` | 数据库/MyBatis 等平台能力导出 |
| `//packages/platform/job:export` | 任务/调度相关导出 |
| `//packages/platform/liquibase:export` | Liquibase 相关导出 |
| `//packages/platform/redis:export` | Redis 相关导出 |
| `//packages/platform/rocketmq:export` | RocketMQ 相关导出 |
| `//packages/platform/security:export` | 安全相关导出 |
| `//packages/platform/web:export` | Web/MVC 相关导出 |
| `//packages/platform/web-extra:export` | Web 扩展能力导出 |
| `//packages/platform/subsystem-config:export` | 子系统配置相关导出 |
| `//packages/platform/subsystem-requester-sdk:export` | 子系统请求方 SDK 导出 |

## 常见开发场景

1. **将新的 platform 子模块纳入顶层 BOM**  
   在 [BUILD.bazel](../BUILD.bazel) 的 `maven_bom` → `java_exports` 中增加对应 `//packages/platform/<子模块>:export`；同步团队发布流程与消费方文档；确认 `PLATFORM_VERSION` 与 `java_export` 发布节奏一致（细节待团队补充）。

2. **调整 BOM 聚合列表或排查坐标**  
   查阅 `BUILD.bazel` 中 `artifactId`、`maven_coordinates`、`dependencies_maven_coordinates` 与 `variables.bzl`；用 `bazel query //packages/platform/bom:all` 等确认目标存在。

3. **消费方在非 Bazel 构建中引用**  
   在 Maven/Gradle 中导入 `com.guhecloud.platform:platform-bom`（版本与仓库以团队约定为准；待团队补充具体 `dependencyManagement` 示例）。

## 注意事项

- 变更 `java_exports` 会影响所有依赖该 BOM 的消费方版本对齐，需与发布与回归策略同步（见顶部综述要点）。
- `platform-bom-dependencies` 坐标由规则与 `BUILD` 字段共同约定，若与私服或文档不一致以构建产物与团队文档为准（待团队补充）。
- 本包无 Java 源码与生成物目录，勿在此添加 `src`；平台实现变更应在对应 `packages/platform/<模块>` 进行。
