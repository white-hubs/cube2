---
name: cube2-platform-web
description: >
  Cube2 platform-web: servlet/web utilities, error handling helpers, Maven
  platform-web. Use when resolving //packages/platform/web, often transitively
  via platform-server, or Web layer utilities.
---

# platform/web

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：Web/Servlet 侧通用能力，`java_library` `web` 及发布。
- **不负责**：业务 Controller 与 API 设计。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. `src/**/*.java`。

## 构建与入口

- `//packages/platform/web`、`//packages/platform/web:export`。
- 许多服务**不直连** `web`，而通过 `//packages/platform/server` 传递。

## 对外契约

Web 层工具与异常响应相关类型（源码为准）。

## 数据与配置

无。

## 深度阅读

- [references/overview.md](references/overview.md)
