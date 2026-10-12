# platform/cloud 综述

- 与 `server` 聚合关系密切；很多服务通过 `//packages/platform/server` 间接获得 `cloud`，而非直连本包。

## 模块综述与边界

本模块为纯库，无业务分层。提供 Spring Cloud / Nacos（服务发现与动态实例列表）、OpenFeign/Ribbon 相关拦截与配置，以及与 `client`、`redis`、`rocketmq`、`security`、`web` 等平台的集成装配；另含请求上下文、HTTP 客户端、操作日志（MQ/注解扫描）等横切能力。**不负责**：业务侧服务命名、Nacos 配置键与命名空间约定、各应用自身的注册中心/配置中心运维策略（见各业务模块与团队基础设施文档）。与 `//packages/platform/server` 聚合关系密切时，消费方常经 `server` 间接依赖本库。

## 当前分层架构

本模块为纯库，无 `controller/service/domain` 等业务分层；源码按技术职责分包，例如 `config`（含 Nacos 实时列表、`Feign`/HTTP 客户端与安全装配）、`interceptor`（Feign 与 Web 请求链）、`filter`、`listener`、`logger`（业务日志采集与配置）、`mq`（操作日志消息）、`util`。现状即如此，未按领域模型拆分。

## 目录与包结构

模块根目录主要包含 `src/`（Java 源码）、`BUILD.bazel`、`SKILL.md`、`references/`。

`src/main/java` 等价路径为 `src/`，根包 `com.guhecloud.platform.cloud`，展开两层如下：

| 路径 | 说明 |
|------|------|
| `config/` | Spring 配置、Feign/HTTP 客户端与安全、`AopConfig`、`RedisSecurityConfigurer` 等 |
| `config/nacos/realtime/` | Nacos 动态 `ServerList` 更新与监听（与负载均衡实例列表联动） |
| `filter/` | Servlet 过滤器（如可重复读请求体、渠道过滤） |
| `interceptor/` | Feign 与 MVC 拦截器（OAuth2 头、异常与响应处理、请求日志与上下文） |
| `listener/` | Web 请求上下文监听器 |
| `logger/` | 操作日志注解扫描、采集实现、`BizLogRecordConfiguration` 与消息 DTO |
| `mq/` | 系统操作日志等 MQ 消息定义 |
| `util/` | Excel 等工具类 |

## BUILD target 说明

| Target | 用途 |
|--------|------|
| `//packages/platform/cloud:cloud` | `java_library`：本模块全部源码，供其他 Bazel 目标依赖 |
| `//packages/platform/cloud:export` | `java_export`：发布为 Maven 构件 `platform-cloud`（版本见 `PLATFORM_VERSION`） |
| `//packages/platform/cloud:bom` | `maven_bom`：同名 BOM，统一对齐本模块导出坐标 |

本模块无独立可执行 `bazel run` 应用目标；对外以库 / Maven 形式被引用。

## 本地运行

本模块为纯库，不独立运行。联调时在依赖本库的应用中启动服务，并确保运行环境已按团队标准接入 Nacos、Redis、RocketMQ 等（以应用 `application*.yml` 为准）。具体命令与 profile 待团队补充。

## 数据层

本模块为纯库，无数据层；无本模块专属数据库或 Liquibase 目录。

## 直接依赖说明

以下为 `BUILD.bazel` 中 `deps` 内 **Bazel label** 及所支撑能力（摘要）：

| Label | 说明 |
|-------|------|
| `//packages/platform/base` | 平台基础能力 |
| `//packages/platform/base/model:requester` | 请求方/调用上下文等模型 |
| `//packages/platform/base/standard:exception` | 标准异常类型 |
| `//packages/platform/base/standard:http` | HTTP 相关标准封装 |
| `//packages/platform/base/utils:aes` | AES 工具 |
| `//packages/platform/base/utils:json` | JSON 工具 |
| `//packages/platform/base/utils:list` | 集合工具 |
| `//packages/platform/base/utils:object` | 对象工具 |
| `//packages/platform/base/utils:string` | 字符串工具 |
| `//packages/platform/client:client-config` | 客户端配置抽象 |
| `//packages/platform/redis` | Redis 集成 |
| `//packages/platform/rocketmq` | RocketMQ 集成 |
| `//packages/platform/security` | 安全与认证相关 |
| `//packages/platform/web` | Web 层通用能力 |
| `//third-party/java/...` | Lombok、EasyExcel、Fastjson2、HttpClient5、Tomcat、BizLog SDK、Hutool POI、Jackson、Servlet API、**Nacos 与 Spring Cloud Alibaba**、OpenFeign、LoadBalancer、SLF4J、Spring Boot/Cloud/Data/Security 等 |

## 常见开发场景

1. **调整 Feign / HTTP 客户端或安全装配**：优先查看 `config/FeignConfigurer.java`、`FeignSecurityConfigurer.java`、`HttpClientConfigurer.java` 及 `OAuth2FeignRequestInterceptor` 等；改完后跑依赖本库的应用集成测试或本地启动验证。
2. **调整 Nacos 与负载均衡实例列表行为**：查看 `config/nacos/realtime/` 下 `NacosDynamicServerList*` 与相关 `Configuration`；确认与 `NacosDiscoveryProperties`、命名服务地址一致。
3. **扩展操作日志或 MQ 消息**：查看 `logger/` 与 `mq/SysOperationLog*.java`，并核对是否需同步修改依赖方的 topic 与消费者（待团队补充契约）。

## 注意事项

- 修改 `BUILD.bazel` 依赖后，按仓库流程执行 Maven repin / Bazel 同步（见仓库级 cube2-repo 说明）。
- Nacos、Redis、RocketMQ 等连接与开关以**引用本库的应用配置**为准，本库不单独约定环境变量名；与动态服务列表相关的 Bean 行为受 Spring Cloud Nacos 版本与启动顺序影响，升级栈时需回归联调。
- `export` 目标带 `no-javadocs` 标签；发布与 BOM 坐标以 `BUILD.bazel` 与 `variables.bzl` 为准。
- 与 `//packages/platform/server` 的聚合关系变更时，应同步更新各消费方文档，避免误以为必须直连 `//packages/platform/cloud`。
