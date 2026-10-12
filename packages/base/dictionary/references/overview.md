# base/dictionary — 内部开发者视角

## 模块综述与边界

本模块提供 **数据字典服务**：系统字典、数据字典、字典项、级联字典、用户字典等的 **维护与查询**，并通过 Redis 等机制做 **字典缓存** 与 **变更通知**（见 `ChangeNoticeServiceImpl`、`DictionaryRedisServiceImpl`）；DCM 相关扩展在 `server/src-dcm/`。

**不负责**：其他业务域的语义校验与数据所有权、网关/注册中心的全局路由策略（见平台与部署文档）、与本模块无关的通用安全/数据源配置（以 Nacos 公共配置为准）。消费方 HTTP/Feign 契约见 `references/external-api.md`。

## 当前分层架构

服务端采用 **现状分层**（与部分 `packages/base` 模块一致，物理源码在 `server/src/` 而非 `src/main/java`，**现状，未重构**）：

- **接入层**：`controller/`（及 `src-dcm/controller/` 的 DCM HTTP）
- **应用层**：`service/` + `service/impl/`
- **持久层**：`dao/`（MyBatis Mapper 接口）+ `resources/mapper/**/*.xml`
- **模型**：`entity/`、`param/`、`vo/`，枚举在 `enumeration/`
- **横切**：`config/`、`event/`、`constant/`、`util/`

客户端：`client/` 下 **Feign 接口**（`client/`）、**适配器**（`adapter/` + `adapter/impl/`）、DTO/参数/降级等与调用侧相关代码。

## 目录与包结构

模块根：`packages/base/dictionary/`（展示主要 2 层及常用子路径）。

```text
packages/base/dictionary/
├── BUILD.bazel                 # 当前为空（无根级聚合 target）
├── variables.bzl               # VERSION、PACKAGE_GROUP
├── SKILL.md
├── references/
│   ├── external-api.md
│   └── overview.md
├── client/
│   ├── BUILD.bazel
│   └── src/
│       ├── adapter/            # Feign 适配层实现
│       ├── client/             # Feign 接口（*Client）
│       ├── dto/
│       ├── event/
│       ├── fallBack/
│       ├── param/
│       └── utils/
└── server/
    ├── BUILD.bazel
    ├── init/                   # 初始化 SQL 与 pkg_files
    ├── resources/              # application/bootstrap、mapper、db、DCM yaml
    ├── src/                    # 主业务（包根 com.guhecloud.cube.base.dictionary.server）
    │   ├── config/
    │   ├── constant/
    │   ├── controller/
    │   ├── dao/                # MyBatis Mapper 接口
    │   ├── entity/
    │   ├── enumeration/
    │   ├── event/
    │   ├── param/
    │   ├── service/            # 含 impl/
    │   ├── util/
    │   └── vo/
    ├── src-dcm/                # DCM 扩展（*.dcm.* 包）
    └── tests/                  # 测试与示例资源
```

说明：服务端 Java 源码位于 `server/src/`（非 `src/main/java`）；与 `platform/base` 等模块的物理布局可能不同，以 `server/BUILD.bazel` 中 `glob(["src/**/*.java", ...])` 为准。

**`server/src` 与 `client/src` 下展开两层**（目录名对应 Java 包路径片段；包根服务端为 `com.guhecloud.cube.base.dictionary.server`）：

| 路径（相对 `server/src` 或 `client/src`） | 用途 |
|------------------------------------------|------|
| `config/` | Spring 与组件配置 |
| `constant/` | 常量 |
| `controller/` | REST 控制器 |
| `dao/` | MyBatis Mapper 接口 |
| `entity/` | 持久化实体 |
| `enumeration/` | 枚举 |
| `event/` | 领域/集成事件相关 |
| `param/` | 入参模型 |
| `service/` | 服务接口 |
| `service/impl/` | 服务实现 |
| `util/` | 工具类（含 Redis key 等） |
| `vo/` | 视图/出参对象 |
| `adapter/`、`adapter/impl/`（client） | Feign 调用适配 |
| `client/`（client） | `*Client` Feign 声明 |
| `dto/`、`param/`、`event/`、`enumeration/`、`utils/`、`fallBack/`（client） | 客户端模型、工具与降级 |

**`server/src-dcm/`（两层）**：`controller/`、`param/`、`service/` + `service/impl/`、`utils/` — DCM 字典配置扩展。

**开发入口**：

- **Spring Boot 启动类**：`com.guhecloud.cube.base.dictionary.server.DictionaryServer`（`server/src/DictionaryServer.java`）。
- **业务阅读顺序**：`controller/` → `service/` → `dao/` + `resources/mapper/`；变更通知与缓存：`ChangeNoticeServiceImpl`、`DictionaryRedisServiceImpl`；DCM：`src-dcm/`。
- **客户端**：从 `client/src/client/*Client.java` 与各 `adapter` 入手。

## BUILD target 说明

| Target | 说明 |
|--------|------|
| `//packages/base/dictionary/server:deps` | 私有依赖聚合，供 `server` 使用 |
| `//packages/base/dictionary/server:server` | 服务端 `java_library`（含 `src`、`src-dcm`、`resources`） |
| `//packages/base/dictionary/server:server-bin` | `java_binary`，主类 `DictionaryServer`；**本地运行**入口 |
| `//packages/base/dictionary/server:server-exec` | `springboot` 宏生成的可执行 Boot 包；**构建 fat-jar** |
| `//packages/base/dictionary/server:package` | Maven 发布（服务端） |
| `//packages/base/dictionary/server:server-info` | `server_info`：库名、网关前缀、main class 等 |
| `//packages/base/dictionary/client:client` | 客户端库；**对外依赖**入口 |
| `//packages/base/dictionary/client:export*` | 客户端 Maven 发布（release/dev/test） |
| `//packages/base/dictionary/server/init:init-mysql` | 初始化 SQL 文件打包 |

## 本地运行

在仓库根目录执行（需已配置 Bazel 与运行期依赖：MySQL、Nacos/配置中心等，见 `application.yaml` / `bootstrap.yaml`）：

```bash
bazel run //packages/base/dictionary/server:server-bin
```

Spring Boot fat-jar 形态构建：

```bash
bazel build //packages/base/dictionary/server:server-exec
```

仅编译客户端库：

```bash
bazel build //packages/base/dictionary/client:client
```

**运行期前提（摘自 `server/resources/application.yaml`，实际值多由 Nacos 覆盖）**：

- **Nacos**：`NACOS_ADDR`、`NACOS_NAMESPACE` 等；配置通过 `spring.config.import` 拉取 `common-env`、`common-datasource`、`common-redis`、`common-mybatis-plus`、`common-rocketmq` 等多份公共配置。
- **MySQL**：JDBC URL 指向库名 **`cube-base-dictionary`**（与 `//packages/base/dictionary/server:server-info` 中 `database` 一致）。
- **端口**：`common-env.dictionary.port`，默认占位 **10203**。

更细的一键本地环境（账号、初始化顺序、是否与 `server-one` 联调）若未在仓库内固化，**待团队补充**。

## 数据层

- **数据库名**：`cube-base-dictionary`（与 `server_info.database`、JDBC URL 一致）。
- **Schema 变更**：仓库内含 `server/resources/db/com.guhecloud.cube.base.dictionary/*.mysql.sql`；`application.yaml` 中配置 `spring.liquibase.change-log` 指向 `db.com.guhecloud.cube.base.dictionary/changelog-master.xml`，**当前 `liquibase.enabled` 为 false**；是否在流水线/环境中启用 Liquibase **待团队补充**。根目录下未检出 `changelog-master.xml` 时，以团队实际发布物为准。
- **初始化 SQL 打包**：`server/init/init-db.mysql.sql`，Bazel 目标 `//packages/base/dictionary/server/init:init-mysql`（供 deploy database 等引用）。
- **核心表/实体（概述）**：与 `entity/` 及 Mapper 对应，包括用户字典、系统字典、数据字典、字典项、级联字典等（具体字段见实体与 `resources/mapper/*.xml`）。

## 直接依赖说明

以下为 **`server/BUILD.bazel` 中 `:deps` 聚合** 的典型直接依赖（能力说明；版本由 BOM/仓库约束）：

| Bazel label | 提供能力 |
|-------------|----------|
| `//packages/platform/base/model:pagination`、`...:paging`、`...:pair` | 分页与通用模型 |
| `//packages/platform/base/utils:*` | 集合、转换、日期、对象、字符串、JSON 等工具 |
| `//packages/platform/database` | MyBatis-Plus、租户/审计等数据访问基础设施 |
| `//packages/platform/dcm/server-support` | DCM 服务端支持 |
| `//packages/platform/liquibase` | Liquibase 集成 |
| `//packages/platform/rocketmq` | RocketMQ 客户端与集成 |
| `//packages/platform/security` | 安全与资源服务相关 |
| `//packages/platform/server` | 服务端通用基座 |
| `//third-party/java/google:guava` | Guava 工具库 |
| `//third-party/java/jackson` | Jackson JSON |
| `//third-party/java/spring:spring_boot_starter_jdbc` | Spring JDBC |

**客户端** `client/BUILD.bazel` 另依赖 `//packages/platform/client` 及若干 `platform/base` 工具包，见该文件。

## 常见开发场景

1. **新增或调整对外 HTTP 行为**  
   - 修改 `server/src/controller/*.java`（及 DCM 时 `server/src-dcm/controller/`）；网关前缀 **`/dictionary/`** 不变时仅改应用内路径。  
   - 同步更新 `references/external-api.md`、Feign：`client/src/client/*Client.java` 与 `adapter/`。

2. **新增实体与持久化访问**  
   - `server/src/entity/`、`dao/`、`resources/mapper/*.xml`；分页等沿用 `platform.database` 既有用法。  
   - 若涉及缓存键或失效：配合 `DictionaryRedisService`、`DictionaryRedisKeyUtil`、`ChangeNoticeServiceImpl`。

3. **排查字典缓存或变更通知异常**  
   - 从 `DictionaryRedisServiceImpl`、`ChangeNoticeServiceImpl` 入手；Redis 与 MQ 行为以 Nacos 公共配置为准。

## 注意事项

- **配置来源**：大量运行时项来自 **Nacos** `common-*` 导入，本地 `application.yaml` 仅为占位；勿假设仅改本地文件即可生效。
- **DCM 与主源码分离**：`src-dcm/` 与 `src/` 并列编译进 `:server`，修改 DCM 行为时注意包扫描与 Bean 冲突。
- **Liquibase**：默认关闭；是否在某环境执行以团队规范为准，避免与手工 SQL 或 init 包重复执行冲突。
- **代码生成**：若团队对 Mapper/XML 有生成流程，**禁止手改生成物** 的规则以团队文档为准；当前仓库内主要为手写 Mapper XML，**待团队补充** 是否引入生成器。
