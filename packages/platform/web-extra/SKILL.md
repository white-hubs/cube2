---
name: cube2-platform-web-extra
description: >
  Cube2 platform-web-extra: extended web helpers depending on web and database.
  Use for Excel/export style utilities, packages/platform/web-extra.
---

# platform/web-extra

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：`web-extra` 库及发布；依赖 `web`、`database`（见 [BUILD.bazel](BUILD.bazel)）。
- **不负责**：业务导出字段与权限。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. `src/**/*.java`。

## 构建与入口

- `//packages/platform/web-extra`、`//packages/platform/web-extra:export`。

## 对外契约

扩展 Web 能力（以源码为准）。

## 数据与配置

无。

## 深度阅读

- [references/overview.md](references/overview.md)
