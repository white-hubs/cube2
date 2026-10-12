---
name: cube2-platform-server
description: >
  Cube2 platform-server aggregate library: re-exports base, cloud, web, and
  common Spring/Nacos deps for service modules. Use when the user adds
  //packages/platform/server to deps, platform-server BOM, or 服务端平台聚合依赖.
---

# platform/server

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：`java_library` `server` 通过 `exports` 聚合 `//packages/platform/base`、`cloud`、`web` 及 Spring Boot/Cloud/Nacos 等，便于业务 `deps` **一行引用平台服务端栈**；含 `export`、`bom`（与 `platform-client` 坐标存在关联时应以 [BUILD.bazel](BUILD.bazel) 为准）。
- **不负责**：业务接口与领域逻辑；具体 MyBatis/Security 实现仍分别在 `database`/`security` 等包（此处多为传递暴露）。

## 子模块分流

无；见 [submodule-index](../references/submodule-index.md)。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)：`server`、`export`、`bom`。
2. 若缺依赖：检查是否应改引用 `//packages/platform/database` 等直连目标，而非盲目加码。

## 构建与入口

- `//packages/platform/server`、`//packages/platform/server:export`。

## 对外契约

无独立进程；**库的聚合导出面**。

## 数据与配置

无。

## 深度阅读

- [references/overview.md](references/overview.md)
