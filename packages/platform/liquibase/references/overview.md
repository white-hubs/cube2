# platform/liquibase 综述

- 在 apps/base 的 `BUILD` 中引用频率高；新增迁移能力优先参考邻包写法。

## 模块综述与边界

本模块提供 **Spring Boot 与 Liquibase 的集成能力**：绑定动态数据源配置（`spring.datasource.dynamic`）、按数据源名扫描 `classpath*:db/<数据源名>/` 下的变更脚本并装配为 `SpringLiquibase`，结合库表 `databasechangelog` 与若干环境规则控制是否自动执行增量 SQL。业务应用在启动类或 `@Configuration` 中调用 `LiquiBaseDataSourceConfig#getSpringLiquibase` 接入。

**不负责**：各业务库的具体 DDL/DML 内容与版本策略；应用运行时数据源、Nacos 等部署配置；MyBatis/JPA 等 ORM 映射与业务表设计（由业务模块与数据相关模块承担）。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

```text
packages/platform/liquibase/
├── BUILD.bazel          # java_library / java_export / maven_bom
├── references/
│   └── overview.md      # 本文档
└── src/                 # Java 源码（单包扁平布局）
    ├── LiquiBaseDataSourceConfig.java   # Liquibase 扫描、排序、执行条件与 SpringLiquibase 装配
    ├── DataSourceProperties.java       # @ConfigurationProperties 绑定动态数据源 Map
    ├── DataSourceProperty.java         # 单数据源 URL/账号等
    └── DataSourceConfig.java           # 简单数据源 DTO
```

- `com.guhecloud.platform.liquibase`：上述类均在 `src/` 根目录，职责见上表注释。

## BUILD target 说明

| Target（Bazel label） | 用途 |
|------------------------|------|
| `//packages/platform/liquibase:liquibase` | 主代码 `java_library`，供业务 `server` 等依赖。 |
| `//packages/platform/liquibase:export` | `java_export`，对外 Maven 坐标 `platform-liquibase`（版本见 `PLATFORM_VERSION`），供聚合 BOM 等消费。 |
| `//packages/platform/liquibase:bom` | `maven_bom`，发布 BOM 坐标 `platform-liquibase-bom`，聚合本模块 export。 |

本模块 **无** `java_binary` / 可执行入口；本地运行目标为 **无**（见下节）。

## 本地运行

本模块为纯库，不独立运行。

## 数据层

本模块为纯库，无数据层。

业务侧迁移脚本需出现在运行时的 classpath 中，并由本模块按 **`classpath*:db/<数据源名>/*.sql`**（及代码中的 `classpath:db/<数据源名>/…`）解析；数据源名常与应用里传入 `getSpringLiquibase(environment, datasourceName)` 的名称一致。瀚高等场景下若 `db-type` 为 `hg`，会在路径上追加 `/hg` 子目录。各业务模块将变更 SQL 放在 `init/`、`server` 资源目录等位置的约定以模块为准，**待团队补充**统一说明。

## 直接依赖说明

| Bazel label | 说明 |
|-------------|------|
| `//packages/platform/base` | 平台基础能力（与本模块同属 platform）。 |
| `//packages/platform/base/utils:object` | `ObjectUtils` 等空值/集合工具。 |
| `//packages/platform/base/utils:string` | `StringUtils` 等字符串工具。 |
| `//third-party/java:lombok` | Lombok 注解（`@Data`、`@Slf4j` 等）。 |
| `//third-party/java/alibaba:fastjson2` | JSON 序列化（日志与调试输出）。 |
| `//third-party/java/google:guava` | Guava 工具类。 |
| `//third-party/java/liquibase` | Liquibase 核心与 Spring 集成（`SpringLiquibase` 等）。 |
| `//third-party/java/slf4j` | 日志门面。 |
| `//third-party/java/spring:spring_boot` | Spring Boot 自动配置、属性绑定等。 |
| `//third-party/java/spring:springframework` | Spring Framework（`Environment`、`Resource`、JDBC 等）。 |

`exports`：`//third-party/java/liquibase`，使依赖本库的目标能直接使用 Liquibase API（以 BUILD 与依赖解析为准）。

## 常见开发场景

1. **新业务模块接入 Liquibase 自动迁移**  
   - 在业务 `server/BUILD.bazel` 的 `deps` 中增加 `//packages/platform/liquibase`。  
   - 在应用 `@Configuration` 中注入 `LiquiBaseDataSourceConfig`，暴露 `List<SpringLiquibase>`（或等价 Bean），调用 `getSpringLiquibase(environment, "<数据源名>")`。可参考 `packages/base/ouaa/server`、`packages/apps/onecard/server` 等模块中的 `*Configuration.java`。  
   - 将增量 SQL 放入运行时可见的 `db/<数据源名>/` 路径下，命名与排序规则需符合本类中的数字序逻辑。

2. **调试「是否执行 / 执行顺序」**  
   - 关注配置项 `liqui-base.execute`（默认 `true`）、`mybatis-plus.global-config.db-config.db-type`（如瀚高 `hg`）、以及 `isNeedAutoExecute` 中对 `changelog-master.xml`、特定 IP、`databasechangelog` 已执行记录的判断。需要改行为时在 `LiquiBaseDataSourceConfig.java` 中修改并评估对各环境的影响。

3. **对外发布与 BOM**  
   - 若需将本模块纳入对外 Maven BOM，使用 `:export` / `:bom` 目标及 `packages/platform/bom` 等既有聚合方式；细节 **待团队补充**。

## 注意事项

- 已记录在 `databasechangelog` 中的文件不会再次自动执行；修改已发布过的 SQL 文件通常不会重跑，需按团队流程做补丁或新文件。  
- 特定环境（如代码中硬编码的 dev 库地址特征）会跳过自动执行，避免误跑；变更前确认环境与 URL 约定。  
- `LiquiBaseDataSourceConfig` 直接查询 `databasechangelog` 表，依赖目标库已具备 Liquibase 标准表结构。  
- 本模块 **不** 单独可运行；验证请在依赖本库的某个 `server` 应用中完成。
