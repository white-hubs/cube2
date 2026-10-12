---
name: cube2-platform-event
description: >
  Cube2 platform-event: event infrastructure library and Maven export. Use for
  domain event helpers, Guava event bus patterns, packages/platform/event.
---

# platform/event

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：事件基础能力（[BUILD.bazel](BUILD.bazel) 中 `event`）；导出的 Guava 等依赖见 BUILD。
- **不负责**：业务事件类型与订阅方语义（业务服务）。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. `src/**/*.java`。

## 构建与入口

- `//packages/platform/event`、`//packages/platform/event:export`。

## 对外契约

事件发布/订阅侧的公共类型与配置（以源码为准）。

## 数据与配置

无。

## 深度阅读

- [references/overview.md](references/overview.md)
