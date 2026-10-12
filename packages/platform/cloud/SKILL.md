---
name: cube2-platform-cloud
description: >
  Cube2 platform-cloud: Spring Cloud, Nacos client, Feign/loadbalancer
  integrations. Use for microservice integration library packages/platform/cloud,
  often with redis and rocketmq.
---

# platform/cloud

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：`cloud` 库：整合 `client`、`redis`、`rocketmq`、`security`、`web` 及 Nacos/Spring Cloud 栈（见 [BUILD.bazel](BUILD.bazel)）。
- **不负责**：业务服务发现命名规则与配置中心键值设计。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。

## 构建与入口

- `//packages/platform/cloud`、`//packages/platform/cloud:export`。

## 对外契约

云原生集成组件与配置（源码为准）。

## 数据与配置

无。

## 深度阅读

- [references/overview.md](references/overview.md)
