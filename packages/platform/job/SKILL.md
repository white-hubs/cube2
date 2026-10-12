---
name: cube2-platform-job
description: >
  Cube2 platform-job: scheduling/task support library, job/init, Maven export.
  Use for platform jobs, Quartz/spring scheduling integration,
  packages/platform/job.
---

# platform/job

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：平台任务相关库 [BUILD.bazel](BUILD.bazel)；初始化见 [job/init/BUILD.bazel](job/init/BUILD.bazel)。
- **不负责**：各业务定时任务的注册与业务逻辑实现。

## 子模块分流

`init/` 为初始化子包；仍归本 SKILL 描述。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. [job/init/BUILD.bazel](job/init/BUILD.bazel)。

## 构建与入口

- `//packages/platform/job`、`//packages/platform/job:export`；init 包见 `//packages/platform/job/init:...`。

## 对外契约

任务基础设施 API（以源码为准）。

## 数据与配置

按业务需要。

## 深度阅读

- [references/overview.md](references/overview.md)
