---
name: cube2-platform-bom
description: >
  Cube2 platform aggregate Maven BOM re-exporting multiple platform libraries.
  Use for platform-bom coordinates, BOM-only package under packages/platform/bom.
---

# platform/bom

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：`maven_bom` 规则 [BUILD.bazel](BUILD.bazel)，聚合 `base`、`cloud`、`database`、`job`、`liquibase`、`redis`、`rocketmq`、`security`、`web`、`web-extra`、`subsystem-*` 等 `export`。
- **不负责**：业务版本裁决；具体依赖冲突在各服务 `BUILD`/`MODULE.bazel` 治理。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)（`artifactId = platform-bom`）。

## 构建与入口

- `//packages/platform/bom:bom`。

## 对外契约

Maven BOM 坐标与传递 `java_exports` 列表。

## 数据与配置

无。

## 深度阅读

- [references/overview.md](references/overview.md)
