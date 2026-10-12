---
name: cube2-platform-repository
description: >
  Cube2 platform-repository: repository interfaces and implementations split
  under repository/interfaces and repository/impl. Use for generic repository
  abstractions, packages/platform/repository.
---

# platform/repository

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：聚合 `repository/interfaces` 与 `repository/impl` 的 Bazel 目标（见 [BUILD.bazel](BUILD.bazel) 与子目录 BUILD）。
- **不负责**：业务实体的仓储实现（各业务服务）。

## 子模块分流

接口与实现分目录；仍由本 SKILL 覆盖。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. [repository/interfaces/BUILD.bazel](repository/interfaces/BUILD.bazel)。
3. [repository/impl/BUILD.bazel](repository/impl/BUILD.bazel)。

## 构建与入口

- `//packages/platform/repository` 及子包目标（以 BUILD 为准）。

## 对外契约

通用仓储接口与基类（源码为准）。

## 数据与配置

无。

## 深度阅读

- [references/overview.md](references/overview.md)
