# platform/client — 开发手册

本文档回答：platform/client 提供了哪些 Feign 客户端能力、如何在业务服务中引用、以及常见错误处理配置。

## 模块综述与边界

**负责**：OpenFeign 客户端公共配置（`FeignConfig`）、统一错误解码器（`CustomFeignErrorDecoder`，将远程错误响应反序列化为 `BusinessException`）、Spring Cloud OpenFeign 依赖聚合。

**不负责**：具体业务 Feign 接口定义（由各模块的 `client/` 子目录维护）、服务注册/发现（见 `platform/cloud`）、Token 透传（见 `platform/security` 的 `FeignSecurityProperties`）。

## 库内部结构

platform/client 是**纯库模块**，无独立运行入口。仅有两个 Java 文件（直接位于模块根）和 Maven POM 模板：

| 文件 | 内容 |
|------|------|
| `FeignConfig.java` | Feign 全局配置 Bean，注册 `CustomFeignErrorDecoder` |
| `CustomFeignErrorDecoder.java` | 将远程服务的错误响应体解析为 `BusinessException`，保留原始错误码与消息 |

## 目录与包结构

```text
packages/platform/client/
├── BUILD.bazel                   # :client-config + :client + :export + :bom
├── CustomFeignErrorDecoder.java
├── FeignConfig.java
├── pom.tpml                      # Maven POM 模板（发布用）
├── references/
│   └── overview.md
└── SKILL.md
```

## BUILD target 说明

| Target | 用途 | 何时引用 |
|--------|------|---------|
| `//packages/platform/client` | 完整 Feign 客户端库：`client-config` + Spring Cloud OpenFeign + Base | 业务服务需要调用其他服务的 Feign 接口时 |
| `//packages/platform/client:client-config` | 仅 `FeignConfig` + `CustomFeignErrorDecoder` | 极少单独使用，通常引整条 `:client` |
| `//packages/platform/client:export` | Maven 发布坐标 `platform-client` | 发布时（开发阶段不需要） |
| `//packages/platform/client:bom` | Maven BOM | 发布 BOM 时（开发阶段不需要） |

**`:client` 实际 exports：**

| 传递导入 | 说明 |
|---------|------|
| `:client-config` | FeignConfig + CustomFeignErrorDecoder |
| `//packages/platform:generic-deps` | Spring Boot/Cloud 核心 BOM |
| `//packages/platform/base` | 通用工具/模型 |
| `//third-party/java/spring:spring_cloud` | Spring Cloud（含 OpenFeign） |

## 直接依赖说明

| 依赖 | 提供能力 |
|------|---------|
| `//packages/platform/base/model:error-response` | 错误响应体反序列化（用于错误解码器） |
| `//packages/platform/base/standard:exception` | `BusinessException` 定义（错误解码器抛出） |
| `//packages/platform/base/utils:json` | JSON 解析工具（用于解码器） |
| `//third-party/java/openfeign:openfeign_core` | OpenFeign 核心接口（`ErrorDecoder`） |

## 常见使用场景

**场景 1：业务服务的 client 子模块定义 Feign 接口**
```python
# packages/base/<name>/client/BUILD.bazel
java_library(
    name = "client",
    srcs = glob(["src/**/*.java"]),
    deps = [
        "//packages/platform/client",       # Feign 配置 + Spring Cloud
        "//packages/platform/base",          # 通用 DTO
        "//packages/platform/base/model:pagination",
    ],
)
```

**场景 2：Feign 接口定义**
```java
@FeignClient(name = "target-service", configuration = FeignConfig.class)
public interface TargetServiceClient {
    @GetMapping("/api/resource/{id}")
    Result<ResourceDTO> getById(@PathVariable String id);
}
```

**场景 3：调用远程服务发生错误时的处理**

`CustomFeignErrorDecoder` 会自动将远程服务返回的错误响应（含 code + message）转为 `BusinessException`，调用方无需额外处理，直接 try-catch `BusinessException` 即可。

## 注意事项

- `platform/client` 与 `platform/server` **可以同时引用**：`server` 聚合了服务端通用能力，`client` 提供 Feign 调用能力，两者职责不重叠
- 业务 Feign 接口的 `@FeignClient` 中需指定 `configuration = FeignConfig.class` 才能激活统一错误解码器
- Token 透传由 `platform/security` 的 `FeignSecurityProperties` 负责，`platform/client` 本身不处理认证头——二者需同时引入
