---
name: cube2-platform-redis
description: >
  Cube2 platform-redis: Redis integration library and Maven export. Use for
  cache, RedisTemplate, packages/platform/redis.
---

# platform/redis

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：Redis 访问封装与 Spring 集成（见 [BUILD.bazel](BUILD.bazel)）。
- **不负责**：业务缓存键策略与缓存一致性方案（业务服务）。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. `src/**/*.java`。

## 构建与入口

- `//packages/platform/redis`、`//packages/platform/redis:export`。

## 对外契约

可复用的 Redis 配置与 Bean。

## 数据与配置

连接配置由各服务 `application` 提供。

## 深度阅读

- [references/overview.md](references/overview.md)
