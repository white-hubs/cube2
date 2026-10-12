# base/license 概览

## 模块综述与边界

本模块是 **Cube2 业务侧许可证管理服务**：负责 **`.lic` 签发**（TrueLicense `store`）、**采集硬件指纹**（`LicenseCheckModel`）、以及 **CLI 制证**；可选以 **`LicenseServer`** 暴露 REST。

**不负责**：**运行时安装/校验/续期** 由各业务进程依赖 **`//packages/platform/license`**（基础库与 `LicenseManagerHolder` 等）完成；本模块 **不是** 平台侧校验库。**不负责** 业务数据持久化：显式排除数据源自动配置，`server_info` 标记 `no_datasource = True`（见 `server/BUILD.bazel`）。

## 当前分层架构

**现状**（未单独抽象 `service` 层）：`controller`（REST）→ `creator`（制证与 `param`）→ 依赖 **`com.guhecloud.platform.license.*`**（平台 license 库）；CLI 经 `cli`（`LicenseClientMain`、`command`）直接驱动制证。与「controller/service/impl/domain/mapper」经典分层不完全一致，**属现状，未重构**；改动时对齐既有包划分即可。

## 目录与包结构

```
packages/base/license/
├── BUILD.bazel                 # 占位注释，无构建目标
├── variables.bzl               # VERSION、PACKAGE_GROUP
└── server/
    ├── BUILD.bazel             # server、可执行、export、server_info
    ├── resources/
    │   └── application.yml
    └── src/
        ├── LicenseServer.java          # Web 模式 @SpringBootApplication
        ├── controller/                 # REST
        ├── creator/                    # 制证逻辑 + param
        └── cli/                        # CLI 入口与命令
```

`server/src` 下一层包：**`cli`**（含 `command`）、**`controller`**、**`creator`**（含 `param`）。无独立 `service` 子包；业务逻辑在 `creator`，接口在 `controller`，CLI 在 `cli`。

`server/resources/` 含 `application.yml`、示例脚本 `cli/example-cli.sh` 及制证相关静态资源（如 keystore/cer，**勿提交敏感生产私钥至仓库**，部署方式**待团队补充**）。

**开发入口**：

| 场景 | 入口类 / 位置 |
|------|----------------|
| REST（采集指纹、生成证书） | `com.guhecloud.cube.base.license.server.LicenseServer` |
| CLI 制证 | `com.guhecloud.cube.base.license.server.cli.LicenseClientMain` |
| HTTP 映射 | `com.guhecloud.cube.base.license.server.controller.LicenseCreatorController` |
| 制证实现 | `com.guhecloud.cube.base.license.server.creator.LicenseCreator` |

## BUILD target 说明

| Target | 说明 |
|--------|------|
| `//packages/base/license/server:deps` | 私有 `java_library`，聚合三方与 `//packages/platform/license` 等依赖 |
| `//packages/base/license/server:server` | 主源码与 `resources` |
| `//packages/base/license/server:server-bin` | `java_binary`，主类 `LicenseClientMain`（CLI） |
| `//packages/base/license/server:server-exec` | `springboot` 可执行 jar，主类 `LicenseClientMain`（CLI） |
| `//packages/base/license/server:package` | Maven `license-server` 发布 |
| `//packages/base/license/server:server-info` | 网关前缀 `/license/`、`server_id`=`license`、`no_datasource` 等 |

- **本地/日常运行**：`server-bin`（CLI）、`server-exec`（Spring Boot fat-jar，主类均为 `LicenseClientMain`）。
- **对外依赖方**：通常依赖 `//packages/base/license/server:server` 或发布物 `//packages/base/license/server:package`（`artifactId = license-server`）。
- **网关与部署登记**：`server-info`（`gw_prefix`、`server_id`、`no_datasource` 等）。

## 本地运行

**CLI（与 Bazel 声明的主类一致）**

```bash
bazel run //packages/base/license/server:server-bin -- --help
```

具体子命令与参数以 `CommandOptionParser`、`CreateLicenseCommand` 为准（**待团队补充**完整示例与必选参数说明）。

**Web（需直接运行 `LicenseServer`）**

Bazel 默认可执行目标未指向 `LicenseServer`。本地可在 IDE 中运行 `LicenseServer.main`，或使用 `bazel build //packages/base/license/server:server` 后自行指定主类启动；默认端口见 `server/resources/application.yml`（`11003`）。

**集成测试 / 生产启动方式**是否与上述一致，**待团队补充**。

**环境变量 / Nacos / 多环境覆盖**：本仓库 `application.yml` 未体现远程配置依赖；是否与全域 Nacos 一致，**待团队补充**。中间件前提：REST 场景仅需 JVM；无数据库与消息队列依赖。

## 数据层

- **数据库**：**无**。`server_info` 为 `no_datasource = True`；`LicenseServer` 侧排除数据源自动配置（见模块 `SKILL.md`）。**无** 与业务表对应的 JDBC/MyBatis 使用。
- **Liquibase / `init/`**：模块下 **无** `init/` migration 目录；**无** Liquibase 变更集由本模块维护。
- **持久化形态**：制证输出为 **文件系统上的 `.lic`**（路径由请求体或 `license.licensePath` 等配置决定，见 `application.yml`）。

## 直接依赖说明

`//packages/base/license/server:deps` 聚合依赖（节选，完整见 `server/BUILD.bazel`）：

| Bazel label | 能力说明 |
|-------------|----------|
| `//packages/platform:generic-deps` | 平台通用三方与基础约定 |
| `//packages/platform/base` | 平台基础能力 |
| `//packages/platform/base/utils:{date,object,string}` | 日期/对象/字符串工具 |
| `//packages/platform/license` | **TrueLicense 封装、`LicenseManagerHolder`、`LicenseCheckModel`、`ServerInfoUtils` 等**（与本模块边界：本模块制证，该平台包供运行时校验与消费者集成） |
| `//third-party/java/alibaba:fastjson2` | JSON 序列化 |
| `//third-party/java/commons:cli` | CLI 解析 |
| `//third-party/java/jackson` | Jackson |
| `//third-party/java/spring:spring_*` | Spring Boot / Web / Beans |
| `//third-party/java/truelicense` | TrueLicense 签发 |
| `//third-party/java/typesafe:config` | Typesafe Config |

运行时日志：`server` 依赖 `//third-party/java/logback:logback_classic`（`runtime_deps`）。

## 常见开发场景

1. **调整或新增 HTTP 接口（指纹采集 / 制证）**
   - 修改：`server/src/controller/LicenseCreatorController.java`
   - 若改请求体或签发参数：`server/src/creator/param/LicenseCreatorParam.java`、`server/src/creator/LicenseCreator.java`
   - 对外契约摘要：`references/external-api.md`

2. **调整 CLI 子命令或参数**
   - 修改：`server/src/cli/LicenseClientMain.java`、`server/src/cli/CommandOptionParser.java`、`server/src/cli/command/CreateLicenseCommand.java`
   - 示例：`server/resources/cli/example-cli.sh`

3. **变更网关前缀、主类、`server_id` 等部署元数据**
   - 修改：`server/BUILD.bazel` 中 `server_info(...)`（及必要时 `export`/`springboot`）

## 注意事项

- **主类双入口**：Bazel `server-bin` / `server-exec` 主类为 **`LicenseClientMain`（CLI）**；暴露 **`/getServerInfos`、`/generateLicense`** 需启动 **`LicenseServer`**，与默认可执行目标不一致，避免混用。
- **敏感资源**：`server/resources/` 下 keystore/cer 等仅作开发或示例；生产密钥管理与轮换 **待团队补充**。
- **配置覆盖**：`application.yml` 中示例含本机路径（如 `license.licensePath`）；多环境是否由 Nacos 覆盖 **待团队补充**。
- **无代码生成物**：本模块未发现需「禁止手改」的生成目录；若后续引入 OpenAPI/模板生成，**待团队补充** 并在此更新。
- **与 `platform/license` 区分**：业务问题「校验失败、Holder 初始化」优先查消费方与 **`//packages/platform/license`**；本模块聚焦 **制证服务与 CLI**。
