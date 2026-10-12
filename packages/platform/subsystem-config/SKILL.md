---
name: cube2-platform-subsystem-config
description: >
  Cube2 subsystem-config library: multi-subsystem configuration support with
  RocketMQ integration. Use for subsystem config, packages/platform/subsystem-config.
---

# platform/subsystem-config

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：`subsystem-config` 库及 `export`/`bom`；见 [BUILD.bazel](BUILD.bazel)。
- **不负责**：各业务子系统的具体配置项语义与存储。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. `src/**/*.java`。

## 构建与入口

- `//packages/platform/subsystem-config`、`//packages/platform/subsystem-config:export`。

## 对外契约

配置模型与 Spring Bean（源码为准）。

## 数据与配置

无独立迁移；由使用者服务承载。

## 深度阅读

- [references/overview.md](references/overview.md)
