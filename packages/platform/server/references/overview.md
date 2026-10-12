# platform/server — 开发手册

本文档回答：platform/server 提供了哪些能力、如何在业务服务中引用、以及何时需要额外补充其他 platform 依赖。

## 模块综述与边界

**负责**：业务服务的**一站式依赖聚合入口**——一次引用 `//packages/platform/server` 即可拉入 Spring Boot/Cloud、Nacos、Web、Security、Base 等通用平台能力的 exports。

**不负责**：持久层（需额外引 `platform/database`）、Liquibase 迁移（需额外引 `platform/liquibase`）、RocketMQ（需额外引 `platform/rocketmq`）、Redis（需额外引 `platform/redis`）、Feign 客户端（需额外引 `platform/client`）。

## 库内部结构

platform/server 是**纯聚合库**，无源码（`srcs = []`），仅通过 `exports` 将其他 platform 子模块暴露出来。目录中只有 `BUILD.bazel` 和 Maven POM 模板文件。

## 目录与包结构

```text
packages/platform/server/
├── BUILD.bazel   # :server（聚合 exports）+ :export + :bom
├── pom.tpml      # Maven POM 模板（发布用）
├── references/
│   └── overview.md
└── SKILL.md
```

## BUILD target 说明

| Target | 用途 | 何时引用 |
|--------|------|---------|
| `//packages/platform/server` | 核心聚合：exports Spring Boot/Cloud/Nacos/Web/Security/Base | 业务服务 `deps` 聚合目标中的首选入口 |
| `//packages/platform/server:export` | Maven 发布坐标 `platform-server` | 发布时（开发阶段不需要） |
| `//packages/platform/server:bom` | Maven BOM | 发布 BOM 时（开发阶段不需要） |

**`:server` 实际 exports（来自 BUILD.bazel）：**

| 传递导入 | 说明 |
|---------|------|
| `//packages/platform:generic-deps` | Spring Boot/Cloud 核心 BOM |
| `//packages/platform/base` | 通用工具/模型/自动配置 |
| `//packages/platform/base/model:error-response` | 统一错误响应 |
| `//packages/platform/base/standard:exception` | 统一异常族 |
| `//packages/platform/cloud` | Nacos 服务发现/配置 |
| `//packages/platform/web` | Web MVC 通用配置 |
| `//third-party/java/spring:spring_security` | Spring Security |
| `//third-party/java/nacos:nacos_core` | Nacos 核心 |

## 直接依赖说明

platform/server 本身不引入新代码，所有能力均来自其 `exports` 列表（见上表）。

## 常见使用场景

**场景 1：标准业务服务引入平台基础能力（最常见用法）**
```python
# server/BUILD.bazel
java_library(
    name = "deps",
    visibility = ["//visibility:private"],
    exports = [
        "//packages/platform/server",      # 一次引入 Spring/Nacos/Web/Security/Base
        "//packages/platform/database",    # 持久层（需单独加）
        "//packages/platform/security",    # 安全增强（server 已含 spring_security，此处补业务安全逻辑）
    ],
)
```

**场景 2：确认某能力是否已通过 server 传递（避免重复依赖）**

先查 `platform/server` 的 `exports` 列表。若目标已在其中，不需要在业务 BUILD 中重复声明。

## 注意事项

- `platform/server` **不包含** database、liquibase、rocketmq、redis——这是最容易踩的坑，持久层必须额外加 `//packages/platform/database`
- 若业务服务不需要某些传递依赖（如 security），仍建议使用 `platform/server`，而不是自己逐个列举，否则难以维护
- `export` 与 `bom` target 用于 Maven 发布，**开发阶段不需要，不要主动在 BUILD 中引入**
