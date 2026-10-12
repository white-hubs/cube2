---
name: cube2-platform-client
description: >
  Cube2 platform-client: Feign config, client aggregate library, Maven
  platform-client and BOM. Use for OpenFeign, FeignConfig, downstream client
  deps, or packages/platform/client.
---

# platform/client

总览见 [../SKILL.md](../SKILL.md)。原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。

## 模块边界与职责

- **负责**：客户端侧通用——`client-config`（Feign 配置与错误解码）、`client` exports、`export`/`bom`；依赖 `base` 的异常与 JSON 工具等。
- **不负责**：具体业务 Feign 接口定义（各业务 client 模块）；服务端安全策略。

## 子模块分流

无。

## 快速定位清单

1. [BUILD.bazel](BUILD.bazel)。
2. `CustomFeignErrorDecoder.java`、`FeignConfig.java` 等源码根文件。

## 构建与入口

- `//packages/platform/client`、`//packages/platform/client:export`。

## 对外契约

Feign 行为与错误解码约定（见源码与 Spring 配置）。

## 数据与配置

无。

## 深度阅读

- [references/overview.md](references/overview.md)
