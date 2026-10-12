# device-center — 内部开发者视角

## 模块综述与边界

**职责**：IoT **设备中心**域能力聚合——设备接入与管理、物模型（含旧版 `mgt` 与 v3 `product-model`）、北向协议与消息（`north` 的 Feign / Rabbit 消费侧）、主服务上的开放接口与场景/统计/可视化，以及台账与健康监控子服务（`supervise`）。对外通过 Feign client、网关路由及 MQ 与平台其它服务协作。

**不负责**：跨行业的业务流程编排、通用身份与权限模型实现（由 `ouaa` 等模块提供）、非设备域的数据中台/报表平台能力；具体 HTTP 前缀与 Topic 以 `references/external-api.md` 与部署元数据为准。更细的「不负责」范围待团队补充。

## 当前分层架构

**现状，未主动重构**：各子树惯例不完全一致。

- **`server/`（主服务）**：以 `core/controller`、`core/service`、`core/mapper`、`core/model` 为主链路；另有 `open/`、`scene/`、`statistics/`、`visualization/`、`proxy/` 等按业务能力分包。与 Spring MVC + MyBatis-Plus 常见分层一致。
- **`mgt/`（旧版业务库，随主服务类路径加载）**：`base/`、`things/`、`device/`、`north/`、`acc/`、`transfer/` 等按领域分包；`core/innerbus` 等与南向消息常量等相关。非独立 DDD 分层命名。
- **`supervise/server/`**：部分能力采用 `interfaces` + `commandgateway` / `querygateway`，以及 `analysis/` 下 `application` / `domain` / `infrastructure` 等更接近分层的结构（与健康/台账子域相关）。
- **`product-model/`**：`src/v3/` 为物模型 v3 实现；与 `server` 依赖库 `:product-model` 并存于同包 BUILD 中。

### `server/src` 子包（设备中心主服务）

| 子包 | 角色 |
|------|------|
| `core/controller` / `core/service` / `core/mapper` / `core/model` | 设备实例、标签、规则等主链路 |
| `core/rule` | 标签规则与 RocketMQ 消费 |
| `open/` | 开放接口适配（如 `DeviceCenterClientAdapter`） |
| `scene/` | 设备场景与变更监听（含 `scene/rocketmq`） |
| `statistics/` | 统计报表 API |
| `visualization/` | 可视化相关 API |
| `proxy/iot` | IoT 相关代理 |

根入口：`server/src/DeviceCenterServer.java`。

### `mgt/src` 子包（旧版；随主服务加载）

| 子包 | 角色 |
|------|------|
| `base/` | 字典、公共 Controller/Service |
| `things/` | 物模型（产品/属性/事件/方法）旧版 |
| `device/` | 设备业务、Controller、与内部数据代理 |
| `north/` | 北向服务端实现、订阅 WebSocket 等 |
| `acc/` | 接入相关 Controller |
| `core/innerbus` | 南向运行时消息发送（RocketMQ 常量） |
| `transfer/` | 迁移/传输相关 |

独立启动入口见 `mgt/server/IotApplication.java`（仅 `mgt/server` 目标）。

### `north/consumer/src` 子包

| 子包 | 角色 |
|------|------|
| `cfg` / `auto` | 订阅配置与自动配置 |
| `handler` / `vo` | 消息处理与载荷 |
| `filter` | 过滤器与测试 |

### `supervise/server/src` 子包

| 子包 | 角色 |
|------|------|
| `account/interfaces` | HTTP API（台账等） |
| `account/commandgateway` / `querygateway` | 读写分离网关 |
| `account/event/rocketmq` | 生命周期消息消费 |
| `analysis/` | 健康值等领域（application/domain/infrastructure/interfaces） |
| `notify/rule` | 通知规则相关持久化 |

入口：`DeviceSuperviseServer`（与 `BUILD` 中 `main_class` 一致）。

## 目录与包结构

```
device-center/
├── access/                 # 接入：根 BUILD 为空；init 仅打包 MySQL init SQL 供 deploy/database 引用
├── client/                 # 对外 Feign：其它服务调用设备中心的接口定义（依赖 metadata）
├── metadata/               # 共享库：Const、DTO、事件类型、注解（无 Spring Boot）
├── mgt/                    # 旧版物模型/设备/north 适配等业务大库；被 server 依赖
│   ├── doc/                # 领域文档（如下行命令）
│   ├── openspec/           # OpenSpec 规范与变更
│   ├── resources/          # MyBatis XML、Liquibase 等
│   ├── server/             # 独立 IotApplication 入口（仅测试/本地）
│   ├── src/                # 主源码（acc/base/core/device/things/north/transfer…）
│   └── tests/              # 单测
├── north/                  # 北向：client=IoT Feign；consumer=Rabbit 订阅 SDK
├── product-model/          # 物模型 v3 独立服务 + v3 子库；init 为 DB 资源
├── server/                 # 设备中心主服务（DeviceCenterServer）
│   ├── init/               # DB 初始化资源（deploy）
│   ├── resources/          # application.yaml、mapper、liquibase
│   ├── src/                # core/open/scene/statistics/visualization…
│   └── tests/
└── supervise/              # 台账与健康监控独立服务 + 对外 Feign client
    ├── client/
    └── server/
```

## BUILD target 说明

| 路径 | 目标 | 说明 |
|------|------|------|
| `server/BUILD.bazel` | `:deps` | 私有依赖聚合 |
| | `:server` | 主应用 `java_library` |
| | `:server-bin` | `java_binary`，`main_class` = `DeviceCenterServer`（**本地运行入口**） |
| | `:server-exec` | `springboot` fat jar（**发布用**） |
| | `:package` | Maven `java_export` |
| | `:server-info` | 网关前缀、库名、数据源包名等元数据 |
| `mgt/BUILD.bazel` | `:mgt` | 物模型旧版大库（**对外依赖**） |
| | `:serializer-tests` | 测试目标 |
| `mgt/server/BUILD.bazel` | `:server` / `:server-bin` | 仅 `IotApplication` + 资源（调试用） |
| `client/BUILD.bazel` | `:client` | 对外 Feign（**其它服务依赖此标签**） |
| | `:package` | Maven 发布 |
| `metadata/BUILD.bazel` | `:metadata` | 共享模型（**对外依赖**） |
| | `:package` | Maven 发布 |
| `north/client` | `:client` | IoT Feign；含 `serializer-tests` |
| `north/consumer` | `:consumer` | Rabbit 消费者库；`filter-tests` |
| `product-model` | `:server` / `:product-model` / `:server-bin` / `:server-info` | 物模型 v3 服务（**无** `springboot`/`export` 目标） |
| `supervise/server` | `:server-exec`、`:package`、`:server-info` 等 | 同主服务模式 |
| `supervise/client` | `:client` / `:package` | Feign |
| `access/init` | `:init-mysql` | SQL 文件映射 |

## 本地运行

在仓库根目录执行（需本地 JDK/Bazel 与 Nacos 等配置按团队环境就绪）：

```bash
# 设备中心主服务
bazel run //packages/base/device-center/server:server-bin

# 物模型 v3（java_binary）
bazel run //packages/base/device-center/product-model:server-bin

# 设备监控/台账
bazel run //packages/base/device-center/supervise/server:server-bin

# 仅调试旧版 mgt 独立进程
bazel run //packages/base/device-center/mgt/server:server-bin
```

**说明**：主服务与 `mgt` 在运行时关系为「同一进程内依赖库」；与 `product-model`、`supervise` 为多进程，需各自配置端口与数据源。

**环境前提**（无法从仓库单独推断完整清单时以团队文档为准）：需可用的 **Nacos**（配置中心/发现，见各 `application.yaml` 中 `spring.config.import`）、**MySQL**（与各服务 `datasource` URL 中库名一致）、以及按配置启用的 **Redis / RocketMQ / Rabbit（IoT）** 等。端口占位如 `common-env.device-center.port` 等由 Nacos 中 `common-env` 解析。

## 数据层

| 服务/库 | `server_info.database`（与部署元数据一致） | Liquibase / changelog 位置（资源路径相对各包 `resources/`） | 备注 |
|--------|---------------------------------------------|------------------------------------------------------------|------|
| 设备中心主服务 + 同进程 `mgt` | `cube_base_iot_center` | `application.yaml` 中 `spring.liquibase.change-log`：`db.com.guhecloud.cube.base.device.center/changelog-master.xml`；仓库内可见版本化增量 SQL：`server/resources/db/com.guhecloud.cube.base.device.center/*.mysql.sql` | `server_info` 含 `legacy_datasource_packages`：`com.guhecloud.cube.iot.mgt`，与历史库表共存；主配置中 `spring.liquibase.enabled` 常为 `false`，实际是否执行以环境/Nacos 为准 |
| 物模型 v3 `product-model` | `cube_base_iot_product_model` | `com.guhecloud.cube.base.iot.product.model/changelog-master.xml` | 独立进程；BUILD 中无 `springboot`/`export` 目标 |
| `supervise` 台账/监控 | `cube-srv-gen-device-supervise` | `com.guhecloud.cube.srv.gen.device.supervise/changelog-master.xml` | 独立进程 |
| 部署用初始化 SQL | — | `server/init/`、`supervise/server/init/`、`access/init/`、`product-model/init/` 等 | 供数据库部署/流水线引用，与运行时 migration 分工待团队补充 |

核心表清单与 ER 说明待团队补充（可落在专文如 `db-schema.md`）。

## 直接依赖说明

以下以主服务 `server/BUILD.bazel` 的 `:deps` 为纲（其它子包见各自 `BUILD.bazel`）：

| Bazel label | 能力说明 |
|-------------|----------|
| `//packages/base/device-center/metadata` | 设备中心共享常量、DTO、事件类型、注解等 |
| `//packages/base/device-center/mgt` | 旧版物模型/设备/北向等大库，主服务运行时依赖 |
| `//packages/base/device-center/north/client` | 北向 IoT Feign 接口与序列化相关 |
| `//packages/base/device-center/north/consumer` | 北向 Rabbit 消费侧 SDK |
| `//packages/base/alarm/client`、`//packages/base/dictionary/client`、`//packages/base/file/client`、`//packages/base/mdata/client`、`//packages/base/ouaa/client`、`//packages/apps/maintain/client` | 各域客户端（告警、字典、文件、主数据、认证、运维等），按业务调用 |
| `//packages/platform/database` | 平台数据访问封装（与 MyBatis-Plus 等配合） |
| `//packages/platform/dcm/server-support` | 设备连接/DCM 相关服务端支持 |
| `//packages/platform/event`、`//packages/platform/rocketmq` | 事件与消息 |
| `//packages/platform/redis` | 缓存 |
| `//packages/platform/liquibase` | 数据库版本管理 |
| `//packages/platform/security`、`//packages/platform/server` | 安全与 Web 服务栈 |
| `//packages/platform/base/model:models` 及多类 `//packages/platform/base/utils:*` | 通用模型与工具库 |

## 常见开发场景

1. **新增或修改设备中心主服务 HTTP 接口**
   - 在 `server/src` 对应分包增加或调整 `*Controller`、`*Service`、`*Mapper` 与 `resources/mapper/**/*.xml`；若涉及对外 Feign，同步 `client/` 与 `metadata/`（如有 DTO/常量）。
   - 运行/调试：`bazel run //packages/base/device-center/server:server-bin`。

2. **修改旧版物模型/设备/北向逻辑（`mgt` 库）**
   - 改 `mgt/src` 与 `mgt/resources/mapper/` 等；注意与主服务同进程，启动入口仍为 `DeviceCenterServer`。
   - 领域说明可查阅 `mgt/doc/`、`mgt/openspec/`。

3. **北向消息或序列化变更**
   - 客户端契约：`north/client`；消费逻辑：`north/consumer/src`。
   - 与主服务的集成方式见现有 `handler`/`cfg` 分包及团队 MQ 规范。

## 注意事项

- **Nacos 覆盖**：业务端口、数据源、Liquibase 开关（如 `liqui-base.execute`）等多来自 Nacos 的 `common-env` 等配置，**勿仅以本地 `application.yaml` 默认值为准**。
- **多进程与多库**：`product-model`、`supervise` 与主服务使用**不同数据库**；联调时需分别启动并保证注册发现与路由正确。
- **历史遗留**：`mgt` 体量大、与主服务耦合深；大改动前建议先读 `mgt` 下文档与现有接口，避免破坏北向/南向约定。
- **代码生成 / 模板产物**：若仓库中存在生成代码目录，**禁止手改** 以生成物为准；具体路径待团队补充。
- **BUILD 差异**：`product-model` 无 `:server-exec`/`export` 等目标，发布形态与主服务不一致，部署时需按团队约定选用 `java_binary` 或打包方式。
