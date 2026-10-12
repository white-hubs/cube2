---
name: cube2-platform-license
description: >
  Cube2 platform-license: license-related platform library and Maven export. Use
  for license checks or UI support code under packages/platform/license.
---

# platform/license

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：`license` 库及 `export`/`bom`（[BUILD.bazel](BUILD.bazel)）。
- **不负责**：产品授权商业策略与法务结论。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. `src/**/*.java`。

## 构建与入口

- `//packages/platform/license`、`//packages/platform/license:export`。

## 对外契约

许可证校验/展示相关 API（源码为准）。

## 数据与配置

无。

## 深度阅读

- [references/overview.md](references/overview.md)
