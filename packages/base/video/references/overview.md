# base/video — 结构与入口

## adapter（视频适配器）

### 子目录结构（`adapter/src` 下 2 层）

```text
adapter/src/
├── application/         # 按厂商/平台分子包
│   ├── dahua/
│   ├── guheyun/
│   ├── h5s/
│   ├── hikvision/
│   ├── uniview/
│   └── util/
└── dto/
    ├── request/
    └── vo/
```

`adapter/resources/` 为 Spring 配置与静态资源。

### 主要 BUILD 目标

| 目标 | 含义 |
|------|------|
| `:server` | `java_library`，源码 + resources |
| `:server-bin` | `java_binary`，主类 `AdapterVideoServer` |
| `:server-exec` | Spring Boot 可执行 |
| `:package` | Maven `adapter-video-server` |
| `:server-info` | `gw_prefix=/video-adapter/`，无数据源 |

### 开发入口（建议优先阅读）

- **Controller**：`application/hikvision/isc/controller/HikvisionIscVideoController.java`、`application/dahua/icc/controller/DaHuaVideoController.java`、`application/guheyun/controller/GhyVideoController.java`、`application/h5s/controller/H5sVideoController.java`（及 `uniview` 等）。
- **Service**：与各 Controller 同厂商包下的 `*VideoService.java`。

### 本地运行

```bash
cd /path/to/cube2
bazel run //packages/base/video/adapter:server-exec
```

默认端口见 `adapter/resources/application.yaml`（`common-env.video-adapter.port`，默认 **10221**）。需可用 Nacos 等依赖。

---

## gateway（视频网关 + client）

### 子目录结构（`gateway/server/src` 下 2 层）

```text
gateway/server/src/
├── common/
│   ├── application/     # command/query 应用服务
│   ├── domain/          # 含 VideoProtocolEnum 等
│   ├── infrastructure/
│   └── interfaces/      # VideoController、VideoFileController
├── config/
└── facade/
    ├── device/
    ├── file/
    └── video/           # SubsystemVideoSdkService
```

`gateway/client/src` 为供其他服务引用的 API 客户端类型（与 `client/BUILD.bazel` 一致）。

### 主要 BUILD 目标

**server**

| 目标 | 含义 |
|------|------|
| `:server` | 服务端库 |
| `:server-bin` / `:server-exec` | 可运行入口，主类 `VideogwServer` |
| `:package` | `videogw-server` |
| `:server-info` | `gw_prefix=/videogw/`，库 `cube_base_videogw` |
| `init:init-mysql` | 初始化 SQL 打包 |

**client**

| 目标 | 含义 |
|------|------|
| `:client` | 类库 |
| `:export` / `:export-dev` / `:export-test` | Maven `cube-base-videogw-client` |

### 开发入口

- **Controller**：`common/interfaces/VideoController.java`、`VideoFileController.java`。
- **应用服务**：`common/application/commandgateway/`、`common/application/querygateway/`（如 `VideoQueryService`、`VideoCommandService`）。
- **对接适配器**：`facade/video/service/SubsystemVideoSdkService.java`（使用 `VideoClient`）。

### 本地运行

```bash
bazel run //packages/base/video/gateway/server:server-exec
```

默认端口 `common-env.video-gateway.port`（**10225**）。

---

## monitor（视频监控）

### 子目录结构（`monitor/server/src` 下 2 层）

```text
monitor/server/src/
├── auth/                # 授权：controller、dao、service、rocketmq
├── common/
├── controller/         # 含 statistics 子包
├── dao/
├── entity/
├── enumeration/
├── param/
├── service/
│   └── impl/
├── util/
└── vo/
```

### 主要 BUILD 目标

| 目标 | 含义 |
|------|------|
| `:server` | 服务端库 |
| `:server-bin` / `:server-exec` | 主类 `VMonitorServer` |
| `:package` | `vmonitor-server` |
| `:server-info` | `gw_prefix=/vmonitor/`，库 `cube-biz-vmonitor` |
| `init:init-mysql` | 初始化 SQL 打包 |

### 开发入口

- **业务 Controller**：`controller/MultipleVideoController.java`、`MultipleVideoCollect*Controller.java`、`CameraPatrol*.java`、`CameraPtzController.java`、`SecurityDeviceAuthController.java` 等。
- **授权**：`auth/controller/CameraAuthController.java` 与 `auth/service/`。
- **MQ**：`auth/rocketmq/consumer/`。

### 本地运行

```bash
bazel run //packages/base/video/monitor/server:server-exec
```

默认端口 `common-env.video-monitor.port`（**10208**）。

---

## 模块综述与边界

**职责**：`packages/base/video` 聚合视频域能力：**adapter** 对接各厂商/平台（海康 ISC、大华 ICC、宇视、H5S、谷和云等）的预览/云台/抓图等 HTTP/SDK 适配；**gateway**（videogw）提供统一适配网关服务接口，包含实时预览、抓拍、云台控制能力，并支持第三方厂商视频抓拍截图本地转存；网关本身不关注协议细节，协议转换由 adapter 层承担；对其他模块导出 **gateway/client**；**monitor** 提供安防监控应用相关能力，包含视频巡逻、监控收藏、设备收藏分组等。

**不负责**：设备主数据与元数据模型以 **device-center** 为权威；账号组织权限以 **ouaa** 等身份模块为准；通用文件服务以 **file** 为准；monitor 侧安防设备查询通过 facade 调用 **security-management**。adapter 不实现完整行业平台（如国标平台侧全链路）——**GB28181** 等在代码中多为注释或厂商请求说明，是否作为对外产品能力见 `references/external-api.md`，**待团队补充**。

## 当前分层架构

- **adapter**：按厂商分子包 `application/<vendor>/`，常见形态为 `controller` → `service` → `facade`（同步 HTTP、事件 handler）+ `convert` / `dto`；无本模块独立持久化层（`server_info` 为无数据源）。现状如此，未与仓库「最佳实践新建模块」模板逐条对齐时，**不主动重构**。
- **gateway/server**：`common/interfaces`（Controller）→ `common/application`（command/query 应用服务）→ `common/domain` + `common/infrastructure` → `facade`（对接子系统/SDK）；含 MyBatis 与 Liquibase。
- **monitor/server**：经典 **controller / service(impl) / dao / entity**；`auth/` 下授权与 RocketMQ consumer；`common/` 下跨切面与外部 facade。

## 目录与包结构

**顶层目录树**（`packages/base/video/`，约 2 层）：

```text
packages/base/video/
├── BUILD.bazel          # 当前为空
├── SKILL.md
├── references/
├── adapter/
│   ├── BUILD.bazel
│   ├── resources/       # application.yaml, bootstrap.yaml
│   └── src/
├── gateway/
│   ├── client/          # Java 客户端导出
│   └── server/
│       ├── BUILD.bazel
│       ├── init/
│       ├── resources/
│       └── src/
└── monitor/
    └── server/
        ├── BUILD.bazel
        ├── init/
        ├── resources/
        └── src/
```

本仓库 adapter 使用 `adapter/src/...`，非 `src/main/java` 路径。各子模块更细的两层包展开见 **adapter / gateway / monitor** 各节。

## BUILD target 说明

以下为 **Bazel 标签索引**（各 target 语义表格见上文各子模块「主要 BUILD 目标」）。

| Label | 用途 |
|-------|------|
| `//packages/base/video/adapter:server` | 适配器 Java 库 + resources |
| `//packages/base/video/adapter:server-bin` | `java_binary`，主类 `AdapterVideoServer` |
| `//packages/base/video/adapter:server-exec` | **本地 Spring Boot 可运行** |
| `//packages/base/video/adapter:package` | Maven `adapter-video-server` |
| `//packages/base/video/adapter:server-info` | 部署元数据（`gw_prefix=/video-adapter/`，无数据源） |
| `//packages/base/video/gateway/server:server` / `:server-bin` / `:server-exec` | 网关库 / 可运行（主类 `VideogwServer`） |
| `//packages/base/video/gateway/server:package` | Maven `videogw-server` |
| `//packages/base/video/gateway/server:server-info` | `gw_prefix=/videogw/`，库 `cube_base_videogw` |
| `//packages/base/video/gateway/server/init:init-mysql` | 网关初始化 SQL 打包（供 deploy/database） |
| `//packages/base/video/gateway/client:client` | 对外 Java API 客户端库 |
| `//packages/base/video/gateway/client:export` / `:export-dev` / `:export-test` | Maven `cube-base-videogw-client` |
| `//packages/base/video/monitor/server:server` / `:server-bin` / `:server-exec` | 监控服务（主类 `VMonitorServer`） |
| `//packages/base/video/monitor/server:package` | Maven `vmonitor-server` |
| `//packages/base/video/monitor/server:server-info` | `gw_prefix=/vmonitor/`，库 `cube-biz-vmonitor` |
| `//packages/base/video/monitor/server/init:init-mysql` | 监控初始化 SQL 打包 |

## 本地运行

| 服务 | 命令 | 端口（配置键） |
|------|------|------------------|
| adapter | `bazel run //packages/base/video/adapter:server-exec` | `common-env.video-adapter.port`（默认 **10221**） |
| gateway | `bazel run //packages/base/video/gateway/server:server-exec` | `common-env.video-gateway.port`（**10225**） |
| monitor | `bazel run //packages/base/video/monitor/server:server-exec` | `common-env.video-monitor.port`（**10208**） |

**前提**：与平台其他 Spring Boot 服务一致，通常需可用 **Nacos**；gateway、monitor 需 **MySQL** 及对应库已初始化。完整环境变量清单、profile、联调顺序 **待团队补充**（或见团队部署文档）。

## 数据层

| 子服务 | 数据库名（与 `server_info` 一致） | Migration / 初始化 |
|--------|----------------------------------|--------------------|
| adapter | **无**（`no_datasource = True`） | 无 |
| gateway | `cube_base_videogw` | Liquibase：`gateway/server/resources/db/com.guhecloud.cube.base.videogw/`；另见 `gateway/server/init/init-db.mysql.sql`（目标 `//packages/base/video/gateway/server/init:init-mysql`） |
| monitor | `cube-biz-vmonitor` | Liquibase：`monitor/server/resources/db/com.guhecloud.cube.biz.vmonitor/`；另见 `monitor/server/init/init-db.mysql.sql`（目标 `//packages/base/video/monitor/server/init:init-mysql`） |

核心表清单与领域模型 **待团队补充**（可从 Mapper XML 与 changelog 追溯）。

## 直接依赖说明

以下摘自各包 `BUILD.bazel` 中 `:deps` 的 **`exports`**（表示本服务构建时聚合引入的能力域；具体传递闭包以 Bazel/Maven 解析为准）。

| Bazel label（节选） | 在本模块中的作用 |
|---------------------|------------------|
| `//packages/platform/server` | Spring Boot 服务端基座 |
| `//packages/platform/redis` | Redis 客户端与缓存能力 |
| `//packages/platform/job` | 定时任务（adapter 等） |
| `//packages/platform/subsystem-config` | 子系统配置 |
| `//packages/platform/database` | MyBatis-Plus 等数据访问（gateway、monitor） |
| `//packages/platform/liquibase` | 数据库版本迁移 |
| `//packages/platform/event` | 领域事件（gateway） |
| `//packages/platform/rocketmq` | MQ 生产/消费（monitor） |
| `//packages/platform/security` | 安全相关（monitor） |
| `//packages/base/device-center/client`、`//packages/base/device-center/metadata` | 设备中心客户端与元数据 |
| `//packages/base/file/client` | 文件服务客户端 |
| `//packages/base/video/gateway/client` | videogw 对外类型（monitor 依赖） |
| `//packages/apps/security-management/client`、`//packages/base/alarm/client`、`//packages/base/mdata/client`、`//packages/base/ouaa/client` | 各业务域客户端（以 monitor 为主） |
| `//third-party/java/hikvision:ga_sdk` | 海康 GA SDK（adapter） |

## 常见开发场景

1. **新增或调整某厂商适配（adapter）**：在 `adapter/src/application/<厂商>/` 扩展 `controller`、`service`、`facade`、`*Convert`；配置与静态资源在 `adapter/resources/`。入口文件清单见上文 **adapter「开发入口」**。
2. **网关侧新增/修改视频或文件能力（gateway）**：优先改 `gateway/server/src/common/interfaces/`、`common/application/`、`facade/`；协议与持久化对象见 `common/domain`。入口见上文 **gateway「开发入口」**。
3. **监控侧业务或授权（monitor）**：改 `controller/`、`service/`、`auth/`、`dao/` 与 `resources/mapper/`。入口见上文 **monitor「开发入口」**。

## 注意事项

- 模块根 **`BUILD.bazel` 为空**，不要在根目录假设可运行目标；以 `adapter`、`gateway/server`、`gateway/client`、`monitor/server` 下 BUILD 为准。
- **Liquibase changelog** 与 `init-db.mysql.sql` 的维护职责、是否双写 **待团队补充**；变更库结构前确认与 DBA/发布流程一致。
- **Nacos** 与本地 `application.yaml` 并存时，以运行环境实际生效配置为准；敏感项勿写入文档。
- **厂商 SDK / 第三方 JAR**（如海康 GA SDK）受许可与版本约束，升级需 **待团队补充** 流程。
- 与 **GB28181、FLV** 等与 `VideoProtocolEnum` 不完全一致的概念，勿在对外契约中默认等同实现；见 `SKILL.md`、`references/external-api.md`。
