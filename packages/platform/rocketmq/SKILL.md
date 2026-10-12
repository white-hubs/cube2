---
name: cube2-platform-rocketmq
description: >
  Cube2 platform-rocketmq: RocketMQ Spring integration and Maven export. Use
  for MQ producers/consumers setup, packages/platform/rocketmq.
---

# platform/rocketmq

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：RocketMQ 与 Spring Boot 集成封装，主目标 [BUILD.bazel](BUILD.bazel) 中 `rocketmq`。
- **不负责**：业务 topic/消费组命名与业务消息体契约（业务服务）。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. `src/`、`resources/`（若有）。

## 构建与入口

- `//packages/platform/rocketmq`、`//packages/platform/rocketmq:export`。

## 对外契约

模板配置与公共监听器/工具类（以源码为准）。

## 数据与配置

无。

## 深度阅读

- [references/overview.md](references/overview.md)
