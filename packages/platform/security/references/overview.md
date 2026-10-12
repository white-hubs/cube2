# platform/security — 开发手册

本文档回答：platform/security 提供了哪些安全能力、如何在业务服务中使用 JWT/Token 验证与数据权限、以及常见扩展点。

## 模块综述与边界

**负责**：JWT Token 解析与多维度校验（租户、有效期、重放）、`SecurityContextHolder`（当前用户上下文）、接口级数据权限注解（`@DataPermissionSwitch`）、API 签名验证（`@Signable`/`SignatureController`）、Spring Security 配置（`WebSecurityConfigurer`）、Feign 请求安全传播（`FeignSecurityProperties`）。

**不负责**：Token 颁发/OAuth2 授权服务器（见 `packages/base/ouaa`）、用户/租户数据管理（见 `base/ouaa` 或 `base/mdata`）、业务侧的 `@PreAuthorize` 权限表达式规则（由各业务模块自定义）。

## 库内部结构

platform/security 是**纯库模块**，无独立运行入口，通过 Spring Boot 自动装配生效。

| 包路径 | 内容 |
|--------|------|
| `src/token/` | `JwtPrincipalValidator`、`JwtTenantValidator`、`JwtActiveValidator`、`TokenReplayValidator` |
| `src/context/` | `SecurityContextHolder`（获取当前 `Principal`） |
| `src/model/` | `Principal`（当前用户/租户信息载体） |
| `src/config/` | `WebSecurityConfigurer`、`MethodSecurityConfigurer`、`SecurityProperties`、`FeignSecurityProperties` |
| `src/signature/` | `Signer`、`SignatureController`、`@Signable` 注解 |
| `src/permission/` | `@DataPermissionSwitch`、`PermissionCache` |
| `src/annotation/` | `@Repeatable`、`@Signable` |
| `src/util/` | `TokenUtils`（Token 提取/解析工具） |

## 目录与包结构

```text
packages/platform/security/
├── BUILD.bazel    # :security + :export + :bom
├── references/
│   └── overview.md
├── SKILL.md
└── src/
    ├── annotation/    # @Repeatable、@Signable
    ├── config/        # WebSecurityConfigurer、MethodSecurityConfigurer、SecurityProperties
    ├── context/       # SecurityContextHolder
    ├── model/         # Principal
    ├── permission/    # @DataPermissionSwitch、PermissionCache
    ├── signature/     # Signer、SignatureController、@Signable
    ├── token/         # JwtPrincipalValidator、JwtTenantValidator 等校验器
    └── util/          # TokenUtils
```

## BUILD target 说明

| Target | 用途 | 何时引用 |
|--------|------|---------|
| `//packages/platform/security` | 完整安全库：JWT 校验 + Spring Security 配置 + 数据权限 | 业务服务需要 Token 解析/权限控制时 |
| `//packages/platform/security:export` | Maven 发布坐标 `platform-security` | 发布时（开发阶段不需要） |
| `//packages/platform/security:bom` | Maven BOM | 发布 BOM 时（开发阶段不需要） |

## 直接依赖说明

| 依赖 | 提供能力 |
|------|---------|
| `//packages/platform/base` | 通用工具（AES 加解密、String/Collection/JSON utils 等） |
| `//packages/platform/web` | Web MVC 配置基础 |
| `//packages/platform/license` | License 校验集成 |
| `//third-party/java/spring:spring_security` | Spring Security 核心 |
| `//third-party/java/spring:spring_security_oauth2_authorization_server` | OAuth2 授权服务器支持 |
| `//third-party/java/aspectj:aspectjweaver` | AOP 支持（数据权限切面） |

## 常见开发场景

**场景 1：在 Service 中获取当前登录用户**
```java
// 获取当前 Principal（租户ID、用户ID、角色等）
Principal principal = SecurityContextHolder.getContext().getPrincipal();
String tenantId = principal.getTenantId();
String userId = principal.getUserId();
```

**场景 2：Controller 接口加签名验证**
```java
@GetMapping("/sensitive-data")
@Signable  // 触发 platform/security 的签名校验拦截器
public Result<Data> getSensitiveData() { ... }
```

**场景 3：关闭某接口的数据权限过滤**
```java
@GetMapping("/admin/all-data")
@DataPermissionSwitch(enabled = false)  // 跳过多租户数据权限
public Result<List<Data>> getAllData() { ... }
```

## 注意事项

- `WebSecurityConfigurer` 已通过自动装配生效，业务服务**不需要**再自定义 `WebSecurityConfigurerAdapter`（已弃用），如需扩展放行路径，通过配置文件中 `security.permit-urls` 配置
- `SecurityContextHolder` 是 platform 提供的，不是 Spring 框架自带的同名类，注意 import 路径
- Feign 调用默认会通过 `FeignSecurityProperties` 传播 Token 头，跨服务调用**不需要**手动设置 Authorization 头
- `platform/server` 已包含 `spring_security`，但**不包含** `platform/security` 库本身——业务服务需要 JWT 解析时须显式引入 `//packages/platform/security`
