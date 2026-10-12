# platform/job 综述

- 与 `event`、`rocketmq` 等可同时出现在服务 `deps`；新增任务相关依赖应对齐同服务现有列表。

## 模块综述与边界

本模块为 **平台定时任务库**：封装 **XXL-Job** 与 **Spring Task** 两套调度实现，提供 `@JobAnnotation` 标记的可调度方法、启动阶段 **任务扫描与注册**（`bootstrap/`）、**调度器抽象**（`JobScheduler`）、以及 **YAML/配置属性**（`config/`、`xxl/config/`、`spring/config/`）等；并与 **platform/event** 集成，在应用就绪事件后触发任务注册（见 `JobInitializeListener`）。

**不负责**：业务领域任务的具体实现（由各应用/业务模块编写 Bean 与方法）；XXL-Job 调度中心与执行器进程的部署运维；消息投递与业务事件建模（见 `//packages/platform/event` 及 MQ 相关模块）；仅提供库与自动配置，**不承载独立 HTTP 服务**。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

源码根目录为 `src/`（本仓 Bazel 约定，无 `src/main/java` 拆分）。Java 包前缀为 `com.guhecloud.platform.job`，物理目录与用途如下（列至约两层）：

- `jobtask/annotation/` — `@JobAnnotation`：标记可调度的任务方法。
- `jobtask/bootstrap/` — `JobScanner` / `JobRegistrar` / `JobInitializeListener`：扫描注解、向调度器注册任务；监听 `SysReadEvent` 触发初始化。
- `jobtask/config/` — `JobAutoConfiguration`、`JobConfig`、调度与停止相关配置类。
- `jobtask/core/` — `JobScheduler` 接口与 `JobInfo` 等核心模型。
- `jobtask/cron/`、`jobtask/cron/dto/` — Cron 解析与 DTO（`CronExpression`、`CronModelDTO`）。
- `jobtask/dto/` — 任务注册信息 `JobAddInfo` 等。
- `jobtask/enums/` — 任务状态、参数、循环类型等枚举。
- `jobtask/listener/`、`jobtask/listener/nacos/` — 任务生命周期监听；Nacos 侧停止监听。
- `jobtask/service/` — `JobService`：对上层暴露增删改查、启停、触发一次等 API，屏蔽具体调度实现。
- `spring/config/`、`spring/impl/` — Spring `TaskScheduler` 与 `SpringScheduler` 实现相关自动配置。
- `xxl/config/` — XXL-Job 执行器与 `job.impl.xxl-job` 配置属性、`xxlJobAutoConfiguration`。
- `xxl/constant/`、`xxl/dto/`、`xxl/handler/`、`xxl/impl/`、`xxl/operator/` — 与调度中心交互的常量、DTO、`SimpleJobHandler`、`XxlJobScheduler`、API 操作封装等。

## BUILD target 说明

| Target（`//packages/platform/job:`） | 类型 | 用途 |
|--------------------------------------|------|------|
| `job` | `java_library` | 本模块全部源码与依赖；业务服务通过 `deps` 引用此目标即可使用任务能力。 |
| `export` | `java_export` | Maven 坐标发布（`runtime_deps` 指向 `:job`）；供外部或非 Bazel 消费方对齐版本。 |
| `bom` | `maven_bom` | 聚合本模块 Maven 坐标的 BOM，便于依赖版本对齐。 |

本模块 **无** `java_binary` / `java_test` 等可执行目标；本地运行见下节。

## 本地运行

本模块为纯库，不独立运行。验证方式：在依赖本库的应用服务中配置 `job.scheduler.type` 等并启动该服务；或对该包执行 Bazel 编译/测试（若有测试目标，待团队补充）。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

以下为 `BUILD.bazel` 中 `:job` 的 `deps` / `exports` 要点（Bazel label → 能力说明）：

- `//packages/platform/base` — 平台基础能力（与本库协同的公共类型等）。
- `//packages/platform/base/standard:exception` — 统一断言与参数校验（如 `JobService` 使用 `Assert`）。
- `//packages/platform/base/utils:collection`、`date`、`object`、`object-id`、`string` — 通用工具类。
- `//packages/platform/event` — 事件与 EventBus 集成（如 `SysReadEvent`、`@EventBusListen`），用于任务初始化时机。
- `//third-party/java:lombok` — 样板代码简化。
- `//third-party/java/alibaba:fastjson2` — JSON 处理。
- `//third-party/java/jackson:core`、`databind`、`dataformat_yaml` — JSON/YAML 与数据绑定。
- `//third-party/java/jakarta:annotation_api` — `@PreDestroy` 等注解。
- `//third-party/java/nacos:nacos_core`、`spring_alibaba_nacos_config` — 与 Nacos 配置/监听相关的任务停止等能力。
- `//third-party/java/slf4j` — 日志门面。
- `//third-party/java/spring:spring_boot`、`spring_boot_autoconfigure`、`springframework` — Spring Boot 自动配置与调度抽象。
- `//third-party/java/xuxueli:xxl_job_core` — XXL-Job 客户端核心（**同时经 `exports` 透出，供依赖方直接使用 XXL 类型时 classpath 一致**）。

## 常见开发场景

1. **为业务服务新增一个基于注解的定时任务**  
   - 在业务 Bean 方法上增加 `@JobAnnotation`（`jobtask/annotation/JobAnnotation.java`）。  
   - 确保应用已依赖 `//packages/platform/job` 且配置了 `job.scheduler.type`（`spring-task` 或 `xxl-job`）及对应实现配置（如 `job.impl.xxl-job.*`）。  
   - 若需自定义注册时机，了解 `JobRegistrar.register()` 与 `JobInitializeListener` 的触发链。

2. **在代码中动态增删任务或立即执行一次**  
   - 注入 `JobService`（`jobtask/service/JobService.java`），调用 `addJob` / `startJob` / `stopJob` / `runOnceJob` 等。  
   - 底层实际调度由当前激活的 `JobScheduler` 实现（`spring/impl/SpringScheduler` 或 `xxl/impl/XxlJobScheduler`）完成。

3. **调整调度实现或 XXL 与调度中心对接**  
   - 修改或扩展 `xxl/config/`、`spring/config/` 下自动配置与属性类；执行器 Bean 见 `xxlJobAutoConfiguration`。  
   - 调度中心 REST 封装见 `xxl/operator/XxlJobApiOperator.java`（细节待团队补充）。

## 注意事项

- 与 `event`、`rocketmq` 等同时出现在服务 `deps` 时，**新增依赖应对齐该服务既有依赖列表**，避免重复或版本冲突（见文首列表说明）。
- `job.scheduler.type` 决定使用 Spring Task 还是 XXL-Job；两套自动配置均依赖该属性存在，具体默认值与条件以源码 `@ConditionalOnProperty` 为准，部署前需与运维/配置规范对齐。
- `export` 目标带有 `no-javadocs` 标签；对外 Maven 坐标与版本以 `variables.bzl` 中 `PACKAGE_GROUP`、`PLATFORM_VERSION` 为准。
- XXL-Job 调度中心地址、执行器端口、accessToken 等**敏感或环境相关配置**应以环境/Nacos 管理，勿硬编码入库；历史约束与禁止改动的生成物（若有）待团队补充。
