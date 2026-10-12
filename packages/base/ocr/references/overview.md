# base/ocr — 概览（overview）

## 模块综述与边界

- **职责**：提供 **车牌 OCR 识别** 能力；当前实现线为 `vehicle/server` Spring Boot 服务，HTTP 入口在 `controller`，业务编排与参数处理在 `service`，具体识别委托第三方厂家实现（`third`，当前含阿里云 `ali`）。
- **不负责**：通用对象存储与文件生命周期（通过 `file` client 消费平台能力）、字典主数据治理（通过 `dictionary` client 查询）、非本模块网关路由与鉴权策略制定（遵循 `common-security-*` / 平台安全约定）、其它类型 OCR（如票据/通用文字）若未在本模块扩展则 **不负责**。

## 当前分层架构

- **实际分层**（现状，未重构）：`controller`（HTTP）→ `service`（校验、编排、字典查询）→ `third`（厂家抽象 `VehicleOcr` / `VehicleOcrManufacture` 与实现包 `ali`）+ `context`（厂家 Bean 注册与查找）+ `enums`（来源类型、厂家枚举）。
- **无**：独立 `domain` 包、MyBatis `mapper`、仓储层；与 `server_info` 中 `no_datasource=True` 一致。

| 路径 | 职责 |
|------|------|
| `controller/` | HTTP 入口（车牌 OCR） |
| `service/` | 参数校验、字典查询、委托厂家实现 |
| `context/` | 厂家 `VehicleOcr` Bean 注册与查找 |
| `enums/` | 文件来源类型、厂家枚举 |
| `third/` | 厂家抽象 `VehicleOcr` / `VehicleOcrManufacture`、DTO、`ali/` 阿里云实现 |

## 目录与包结构

```text
packages/base/ocr/
├── SKILL.md
├── references/
│   ├── external-api.md
│   └── overview.md
└── vehicle/
    └── server/
        ├── BUILD.bazel
        ├── resources/
        │   ├── application.yaml
        │   └── bootstrap.yaml
        └── src/
            ├── OcrVehicleServer.java
            ├── context/
            ├── controller/
            ├── enums/
            ├── service/
            └── third/
```

**说明**：`ocr` 根下当前仅有 **`vehicle/server`** 一条实现线；根目录另有 **空** `BUILD.bazel`（占位、无 target），**可构建入口**在 `vehicle/server/BUILD.bazel`。

- **模块顶层**：`references/`（手册与契约）、`vehicle/server/`（唯一服务端包）；根目录 `BUILD.bazel` 为空占位（无 target）。
- **Java 包（`com.guhecloud.base.ocr.vehicle.server` 下展开）**：
  - `controller`：HTTP API。
  - `service`：应用服务。
  - `context`：厂家 OCR Bean 上下文持有与查找。
  - `enums`：文件来源、厂家等枚举。
  - `third`：厂家 SPI 与 DTO；`third.ali` 阿里云实现；`third.dto` / `third.dto.req` / `third.dto.res` 请求响应模型。
  - 根包：`OcrVehicleServer` 启动类。
- **开发入口**：主类 `com.guhecloud.base.ocr.vehicle.server.OcrVehicleServer`；首读代码：`controller/VehicleOcrController.java` → `service/VehicleOcrService.java` → `third/ali/AliVehicleOcr.java`。

## BUILD target 说明

**定义位置**：`vehicle/server/BUILD.bazel`。

| Target | 说明 |
|--------|------|
| `:deps` | 私有 `java_library`，聚合三方与平台依赖 |
| `:server` | 主代码与 `resources/**`，依赖 `:deps` |
| `:server-bin` | `java_binary`，主类 `com.guhecloud.base.ocr.vehicle.server.OcrVehicleServer`（**本地运行入口**） |
| `:server-exec` | `springboot()` 包装的可执行产物（**本地运行备选**） |
| `:server-info` | `server_info`：`gw_prefix="/ocr-vehicle/"`，`server_id="ocrVehicle"`，`no_datasource=True` |

- **对外/发布**：以团队 deploy 流程为准，**待团队补充**是否另有 `export` 聚合目标。

## 本地运行

在仓库根目录执行（Bazel 版本与 `.bazelversion` 一致）：

```bash
bazel run //packages/base/ocr/vehicle/server:server-bin
```

或使用 Spring Boot 规则产物：

```bash
bazel run //packages/base/ocr/vehicle/server:server-exec
```

- **依赖环境**：默认需可连 Nacos（地址见 `application.yaml` 占位）及内网公共配置；离线/纯本地 profile **待团队补充**。默认监听端口来自 `common-env.ocr-vehicle.port`，未配置时回退 **`11111`**。
- **配置与中间件前提**：`vehicle/server/resources/application.yaml` 通过 `spring.config.import` 拉取 Nacos 上 `common-env.yaml`、`common-jackson.yaml`、`common-security-*.yaml`、`common-platform-*.yaml`、`common-rocketmq.yaml`、`common-redis.yaml` 等；需可用的 **Nacos**（`NACOS_ADDR`、`NACOS_NAMESPACE` 等，见配置文件占位默认值）及内网可访问的配置中心内容。
- **常用环境变量（示例）**：`NACOS_ADDR`、`NACOS_NAMESPACE`、`NACOS_CLOUD_CONFIG*`、`NACOS_CONFIG_GROUP`；完整清单与离线启动方式 **待团队补充**。

## 数据层

- **应用数据源**：`server_info` 中 **`no_datasource=True`**，本服务 **无** 模块自有 JDBC 数据源与 ORM 实体表。
- **Liquibase / `init/`**：模块内 **无** `init/` 目录与 migration；数据库名与表结构 **不适用**（若未来接入库表，与 `server_info.database` 对齐方式 **待团队补充**）。
- **外部状态**：识别所需的文件、字典等通过 **Feign/Client**（`file`、`dictionary`）与平台配置获取，核心表归属各依赖模块。

## 直接依赖说明

以下 Bazel 依赖来自 `vehicle/server/BUILD.bazel` 中 `:deps` 的 `exports`（「提供什么能力」为概要说明）：

| Bazel label | 能力概要 |
|---------------|----------|
| `//packages/base/dictionary/client` | 字典数据查询（服务内字典类逻辑依赖）。 |
| `//packages/base/file/client` | 文件访问/下载等，供 OCR 输入文件处理。 |
| `//packages/platform/base/utils:json` | JSON 工具。 |
| `//packages/platform/base/utils:list` | 集合/List 工具。 |
| `//packages/platform/base/utils:map` | Map 工具。 |
| `//packages/platform/base/utils:string` | 字符串工具。 |
| `//packages/platform/security` | 安全相关集成（与 `common-security-*` 配置配合）。 |
| `//packages/platform/server` | 平台服务端基座（Spring Cloud 等）。 |
| `//third-party/java/aliyun:aliyun_darabonba` | 阿里云 Darabonba 运行时。 |
| `//third-party/java/aliyun:aliyun_java_auth` | 阿里云鉴权。 |
| `//third-party/java/aliyun:ocr-vehicle` | 阿里云车牌 OCR SDK。 |
| `//third-party/java/apache:hc5` / `:hc5-client` | HTTP 客户端（Apache HttpClient 5）。 |
| `//third-party/java/apache:tomcat_annotations_api_legacy` / `:tomcat_embed_core` | 嵌入式 Tomcat/注解 API（传递依赖用途以三方库为准）。 |

## 常见开发场景

1. **调整或新增 HTTP 行为**：改 `controller/VehicleOcrController.java`、`service/VehicleOcrService.java`；对外契约同步 `references/external-api.md`（若团队有约定）。
2. **新增或切换识别厂家**：在 `third` 实现 `VehicleOcr` / `VehicleOcrManufacture`（可参考 `third/ali`）；在 `context` 完成 Bean 注册与查找；在 `enums` 增加厂家枚举；在 `BUILD.bazel` 的 `:deps` 中增加对应三方 `//third-party/...`（若需要）。
3. **排查配置与安全**：查 `vehicle/server/resources/application.yaml`、`bootstrap.yaml` 及 Nacos 公共配置；安全与 Feign 行为与 `common-security-*`、`common-platform-*` 相关条目 **待团队补充**具体键名清单。

## 注意事项

- **禁止或慎改**：`server_info`、网关前缀 `gw_prefix` 与 `server_id` 变更需与部署/注册中心约定一致；阿里云密钥与 endpoint **勿** 硬编码入库，应走配置中心或团队密钥方案（**待团队补充**）。
- **Nacos 覆盖**：大量运行时行为由 Nacos 导入的 `common-*.yaml` 决定，本地 `application.yaml` 仅为基础端口与 import 列表；排障时需对照配置中心与环境变量。
- **无数据库**：业务代码中不应引入本模块专属 Mapper/实体 expecting 本地库表，除非团队明确变更 `no_datasource` 与数据归属设计。
- **根目录 BUILD**：`packages/base/ocr/BUILD.bazel` 当前为空、未声明 target；勿在此添加业务 target 而绕过 `vehicle/server` 约定（若团队统一调整目录策略，**待团队补充**）。
