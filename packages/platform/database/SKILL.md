---
name: cube2-platform-database
description: >
  Cube2 platform-database: MyBatis-Plus, JDBC drivers, datasource helpers, and
  Maven platform-database. Use for persistence layer deps, pagination with DB,
  or packages/platform/database.
---

# platform/database

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：数据访问封装——MyBatis / MyBatis-Plus、多厂商 JDBC、`java_library` `database` 及 `export`/`bom`；与 `//packages/platform/base` 模型协作（分页等）。
- **不负责**：业务表含义与各服务 SQL/Mapper 业务规则（归属各业务服务）。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. 源码：`src/**/*.java`。

## 构建与入口

- `//packages/platform/database`、`//packages/platform/database:export`。

## 对外契约

库 API + 传递的 MyBatis-Plus / JDBC 依赖。

## 数据与配置

迁移执行常配合 `//packages/platform/liquibase`。

## 深度阅读

- [references/overview.md](references/overview.md)
