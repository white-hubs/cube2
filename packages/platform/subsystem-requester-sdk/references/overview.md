# subsystem-requester-sdk 综述

- 典型传递依赖：`client-config`、`rocketmq`、`subsystem-config`、MyBatis-Plus 等；以 [BUILD.bazel](../BUILD.bazel) 为单一事实源。

## 模块综述与边界

本模块为 **子系统请求侧 SDK**（Feign 客户端、请求参数/返回值模型、与负载均衡/动态配置等编排相关类型），以 Bazel `java_library` 形式供其他模块依赖；Maven 坐标通过 `:export` / `:bom` 发布（版本见 `PLATFORM_VERSION`）。

- **负责**：封装跨子系统调用的客户端与配套 DTO/枚举、配置辅助类（源码与 [BUILD.bazel](../BUILD.bazel) 为事实源）。
- **不负责**：具体业务子系统的路由策略、鉴权与安全策略落地（见各业务应用与平台安全/网关相关模块）；不负责子系统服务端的实现与部署。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

模块根目录无 `src/main/java` 拆分，源码位于 `src/` 下（Java 包与目录一致），顶层与向下约一层的职责如下（再下层多为按业务域划分的细分类）。

| 路径 | 用途 |
|------|------|
| `src/client/` | 各子域 Feign 客户端（如门禁、停车、视频、一卡通、广播等）。 |
| `src/config/` | 与 Feign/负载均衡相关的动态配置与客户端工厂类。 |
| `src/param/` | 各域请求参数、查询 DTO（子目录如 `accesscontrol`、`parking`、`video` 等）。 |
| `src/vo/` | 各域响应/展示用 VO（子目录与 `param/` 域划分对应）。 |
| `src/enumeration/` | 少量枚举（如停车订阅类型、视频云台命令等）。 |
| `references/` | 模块开发手册（本文件等）。 |

## BUILD target 说明

| Target（Bazel label） | 说明 |
|------------------------|------|
| `//packages/platform/subsystem-requester-sdk:subsystem-requester-sdk` | 主 `java_library`，聚合 `src/**/*.java`，对外可见性为 `//packages:__subpackages__`。 |
| `//packages/platform/subsystem-requester-sdk:export` | `java_export`，发布 Maven 构件 `subsystem-requester-sdk`（`tags` 含 `no-javadocs`）。 |
| `//packages/platform/subsystem-requester-sdk:bom` | `maven_bom`，发布 `subsystem-requester-sdk-bom`，聚合本模块 `export`。 |

本地无可 `bazel run` 的应用入口；依赖方通过 `deps` 引用 `:subsystem-requester-sdk` 或通过 Maven BOM 对齐版本。

## 本地运行

本模块为纯库，不独立运行。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

以下为 [BUILD.bazel](../BUILD.bazel) 中 `deps` / `exports` 的直接 Bazel 依赖（能力说明为概要；传递依赖见构建图或 IDE）。

| 依赖 label | 能力概要 |
|------------|----------|
| `//packages/platform/base` | 平台基础能力（与本库共用的通用类型与工具）。 |
| `//packages/platform/base/model:pagination`、`//packages/platform/base/model:paging` | 分页相关模型。 |
| `//packages/platform/base/utils:string` | 字符串工具。 |
| `//packages/platform/client:client-config` | 客户端侧配置支持。 |
| `//packages/platform/rocketmq` | 平台 RocketMQ 封装。 |
| `//packages/platform/subsystem-config` | 子系统配置（本库 **`exports`** 该项，依赖方将传递可见其 API）。 |
| `//third-party/java:lombok` | Lombok 注解处理。 |
| `//third-party/java/apache:rocketmq` | RocketMQ 客户端 API。 |
| `//third-party/java/apache:tomcat` | Servlet/Tomcat 相关（传递用途以实际引用为准）。 |
| `//third-party/java/aspectj:aspectjweaver` | AspectJ 织入（与 Spring/AOP 链配合）。 |
| `//third-party/java/google:guava` | Guava 工具库。 |
| `//third-party/java/jackson` | JSON 序列化。 |
| `//third-party/java/jakarta:validation_api` | Bean Validation API。 |
| `//third-party/java/mybatis-plus:extension` | MyBatis-Plus 扩展（与持久化辅助用法相关类）。 |
| `//third-party/java/openfeign` | OpenFeign 声明式 HTTP 客户端。 |
| `//third-party/java/slf4j` | 日志门面。 |
| `//third-party/java/spring:spring_boot` | Spring Boot 核心。 |
| `//third-party/java/spring:spring_cloud` | Spring Cloud 通用组件。 |
| `//third-party/java/spring:spring_cloud_loadbalancer` | Spring Cloud LoadBalancer。 |
| `//third-party/java/spring:spring_data` | Spring Data 抽象。 |
| `//third-party/java/spring:springframework` | Spring Framework 核心。 |

## 常见开发场景

1. **新增或调整某子系统的 Feign 调用**  
   - 修改或新增 `src/client/` 下对应 `*Client.java`。  
   - 在 `src/param/`、`src/vo/` 对应子域目录补充或调整入参/出参类型。  
   - 若涉及公共配置或负载均衡行为，检查 `src/config/`。  
   - 构建验证：`bazel build //packages/platform/subsystem-requester-sdk:subsystem-requester-sdk`。

2. **新增跨子系统请求用的枚举或常量**  
   - 在 `src/enumeration/<域>/` 新增枚举类，并与 `param`/`vo` 引用关系保持一致。

3. **发布与版本对齐**  
   - 版本与坐标以 `PLATFORM_VERSION`、`PACKAGE_GROUP` 及 `:export` / `:bom` 为准；消费方通过 BOM 或直连 `java_library` 标签对齐。具体发布流水线待团队补充。

## 注意事项

- `exports` 包含 `//packages/platform/subsystem-config`：升级本库可能同时影响依赖方对 `subsystem-config` 传递 API 的可见性与版本解析，升级前建议在消费模块做编译与集成验证。  
- `:export` 使用 `no-javadocs`：Javadoc 发布策略与是否另有文档站点待团队补充。  
- 本库聚合多业务域客户端，变更接口契约时需评估所有引用该 Feign/DTO 的上游服务。  
- 模块内若存在历史命名拼写（例如源码中的 `Subsytem` 等），修改涉及公共 API 时需与调用方协调；非必要不批量重命名。
