---
name: cube2-platform-subsystem-requester-sdk
description: >
  Cube2 subsystem-requester-sdk: Feign + MQ + persistence helpers for subsystem
  requests. Use for subsystem-requester-sdk, cross-service request SDK,
  packages/platform/subsystem-requester-sdk.
---

# platform/subsystem-requester-sdk

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：子系统请求侧 SDK（`subsystem-requester-sdk` 库、`export`/`bom`）。
- **不负责**：具体业务子系统路由与鉴权策略。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)（注意 `exports` 含 `subsystem-config`）。

## 构建与入口

- `//packages/platform/subsystem-requester-sdk`、`//packages/platform/subsystem-requester-sdk:export`。

## 对外契约

SDK 入口类与 Feign/MQ 编排（源码为准）。

## 数据与配置

无。

## 深度阅读

- [references/overview.md](references/overview.md)
