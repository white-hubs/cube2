---
name: cube2-platform-security
description: >
  Cube2 platform-security: Spring Security, OAuth2 authorization server helpers,
  and Maven platform-security. Use for authn/authz, OAuth, security filters,
  or packages/platform/security.
---

# platform/security

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：安全栈封装——`java_library` `security`、`export`/`bom`；与 Spring Security、OAuth2 等集成（以 [BUILD.bazel](BUILD.bazel) 为准）。
- **不负责**：各业务系统的权限模型与 URL 授权规则细节（在业务服务内）；账号体系业务（如 `ouaa` 领域）。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. `src/**/*.java`。

## 构建与入口

- `//packages/platform/security`、`//packages/platform/security:export`。

## 对外契约

过滤器链、配置类、与安全相关的可复用组件（具体类名以源码为准）。

## 数据与配置

无独立 DB；可能与业务数据源解用。

## 深度阅读

- [references/overview.md](references/overview.md)
