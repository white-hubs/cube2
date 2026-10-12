---
name: cube2-platform-dcm
description: >
  Cube2 platform DCM: common, server-support, test-support, and CLI modules
  under packages/platform/dcm. Use for DCM integration, dcm server-support,
  or dcm CLI.
---

# platform/dcm

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：DCM 公共代码 `dcm/common`、服务端支撑 `dcm/server-support`、测试支撑 `dcm/test-support`、命令行 `dcm/cli`（说明见 [cli/README.md](cli/README.md)）；各子包独立 `BUILD.bazel`。
- **不负责**：各领域业务中「如何使用 DCM」的流程与数据契约（业务团队）。

## 子模块分流

本子树 **不另建** 每个子目录的 SKILL；以本文件 + 子包 BUILD 为准。

## 快速定位清单

1. [dcm/common/BUILD.bazel](dcm/common/BUILD.bazel)。
2. [dcm/server-support/BUILD.bazel](dcm/server-support/BUILD.bazel)。
3. [dcm/cli/BUILD.bazel](dcm/cli/BUILD.bazel) 与 [cli/README.md](cli/README.md)。

## 构建与入口

- 例：`//packages/platform/dcm/common:common`、`//packages/platform/dcm/server-support:...`、`//packages/platform/dcm/cli:...`（以各 BUILD 为准）。

## 对外契约

库与 CLI 行为以源码与 CLI README 为准。

## 数据与配置

依集成方而定。

## 深度阅读

- [references/overview.md](references/overview.md)
