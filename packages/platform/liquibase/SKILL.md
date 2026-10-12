---
name: cube2-platform-liquibase
description: >
  Cube2 platform-liquibase: Liquibase integration and Maven export. Use for
  database migrations, changelog support, packages/platform/liquibase.
---

# platform/liquibase

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：Liquibase 与 Spring 集成封装，`java_library` `liquibase` 及发布。
- **不负责**：各服务的 changelog 文件内容与发布节奏（业务服务仓库/目录）。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. `src/**/*.java`。

## 构建与入口

- `//packages/platform/liquibase`、`//packages/platform/liquibase:export`。

## 对外契约

迁移运行期的 Spring/Liquibase 扩展点（以源码为准）。

## 数据与配置

与 `database` 配合。

## 深度阅读

- [references/overview.md](references/overview.md)
