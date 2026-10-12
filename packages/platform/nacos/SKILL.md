---
name: cube2-platform-nacos
description: >
  Cube2 platform Nacos init package (packages/platform/nacos/init only). Use
  when wiring Nacos bootstrap resources, packages/platform/nacos.
---

# platform/nacos

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：`nacos/init` 下初始化相关 Bazel 目标（无根级 `BUILD`，仅子目录）。
- **不负责**：Nacos Server 运维与命名空间策略。

## 子模块分流

无；本体仅 `init/`。

## 快速定位清单

1. [nacos/init/BUILD.bazel](nacos/init/BUILD.bazel)。

## 构建与入口

- [nacos/init/BUILD.bazel](nacos/init/BUILD.bazel)：`pkg_files` 目标 `init-mysql` 等（资源型，非 `java_library`）。

## 对外契约

Nacos 初始化 SQL 等资源（见 `init/`）。

## 数据与配置

与应用启动配置配合。

## 深度阅读

- [references/overview.md](references/overview.md)
