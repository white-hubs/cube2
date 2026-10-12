# platform/database — 开发手册

本文档回答：platform/database 提供了哪些持久层能力、如何使用其基础实体类与分页工具、以及多数据库支持情况。

## 模块综述与边界

**负责**：MyBatis-Plus 完整集成（含分页、数据权限、多租户隔离拦截器）、多数据库驱动（MySQL、达梦 DM、瀚高 Highgo）支持、通用 `BaseEntity` 体系、分页工具类（`PagingUtils`）、数据库上下文持有者（`DatabaseContextHolder`）。

**不负责**：具体业务表的 Mapper 与 XML（由各业务模块维护）、Liquibase 数据库迁移（见 `platform/liquibase`）、连接池配置（通过 Nacos `common-datasource.yaml` 注入）。

## 库内部结构

platform/database 是**纯库模块**，无独立运行入口。内部源码在 `src/` 下：

| 包路径 | 内容 |
|--------|------|
| `src/annotation/` | `@DataPermission`、`@DataPermissionField`、`@DataPermissionMoreField`、`@Unique`、`@Confidential` |
| `src/audit/` | `EntityAuditor`（审计字段自动填充） |
| `src/config/` | `MybatisPlusConfigurer`（分页/多租户插件）、`DatabaseSecurityProperties`、`TransactionConfig` |
| `src/context/` | `DatabaseContextHolder`（动态数据源上下文） |
| `src/entity/` | 旧版基础实体体系（不推荐新代码使用，见"常见开发场景"） |
| `src/entity/enhanced/` | **推荐**增强版实体体系（含完整审计字段，`BasicEnhancedEntity` 系列） |
| `src/enumeration/` | `ConditionEnum`、`OpConditionEnum`（数据权限条件枚举） |
| `src/exception/` | `UniqueViolationException`（唯一约束冲突异常） |
| `src/expression/` | `SpecialLikeExpression`（特殊字符 LIKE 表达式） |
| `src/interceptor/` | `PropertyUpdateInterceptor`（审计字段注入）、`UniqueValidateInterceptor`、`DbDataPermissionInterceptor`、`CustomTenantLineInnerInterceptor`、各类 TypeHandler |
| `src/interceptor/special/` | 特殊字符处理（`SpecialCharacterInterceptor`、`DmSpecialCharacterHandler` 等） |
| `src/mapper/` | `BatchSoftDeleteMapper`（批量软删除基础 Mapper） |
| `src/permission/` | 数据权限核心（`DataPermissionAnnotationReader`、`SelectStatementProcessor`、`DataPermissionExpression`） |
| `src/util/` | `PagingUtils`（Paging ↔ MyBatis-Plus Page ↔ Pagination 转换）、`EntityUtils` |

## 目录与包结构

```text
packages/platform/database/
├── BUILD.bazel    # :database + :export + :bom
├── references/
│   └── overview.md
├── SKILL.md
└── src/
    ├── annotation/      # @DataPermission、@Unique、@Confidential 等
    ├── audit/           # EntityAuditor
    ├── config/          # MybatisPlusConfigurer、DatabaseSecurityProperties、TransactionConfig
    ├── context/         # DatabaseContextHolder
    ├── entity/          # 旧版基类（不推荐）：BasicEntity、BasicTenantEntity 等接口与抽象类
    ├── entity/enhanced/ # 推荐基类：BasicEnhancedEntity、BasicEnhancedTenantEntity、BasicEnhancedTreeEntity、BasicEnhancedTenantTreeEntity
    ├── enumeration/     # ConditionEnum、OpConditionEnum
    ├── exception/       # UniqueViolationException
    ├── expression/      # SpecialLikeExpression
    ├── interceptor/     # PropertyUpdateInterceptor、UniqueValidateInterceptor、DbDataPermissionInterceptor 等
    ├── interceptor/special/  # 特殊字符处理器（DM 适配等）
    ├── mapper/          # BatchSoftDeleteMapper
    ├── permission/      # 数据权限：注解读取、SQL 改写、字段权限实体
    └── util/            # PagingUtils、EntityUtils
```

## BUILD target 说明

| Target | 用途 | 何时引用 |
|--------|------|---------|
| `//packages/platform/database` | 完整持久层库：MyBatis-Plus + 多数据库驱动 + 实体体系 + 配置 | 业务服务需要持久层时 |
| `//packages/platform/database:export` | Maven 发布坐标 `platform-database` | 发布时（开发阶段不需要） |
| `//packages/platform/database:bom` | Maven BOM | 发布 BOM 时（开发阶段不需要） |

## 直接依赖说明

platform/database 对外 exports：

| 依赖 | 提供能力 |
|------|---------|
| `//third-party/java/mybatis-plus:boot_starter` | MyBatis-Plus Spring Boot 自动配置 |
| `//third-party/java/mybatis-plus:core` | MyBatis-Plus 核心（BaseMapper、IPage 等） |
| `//third-party/java/mybatis-plus:extension` | 分页插件、乐观锁、逻辑删除等扩展 |
| `//third-party/java/mysql:mysql_connector_java` | MySQL JDBC 驱动 |
| `//third-party/java/dameng:dmjdbc` | 达梦 DM 数据库驱动 |
| `//third-party/java/highgo:hgjdbc` | 瀚高 Highgo 数据库驱动 |
| `//packages/platform/base/model:actor-type` | 操作者类型枚举 |

## 常见开发场景

**场景 1：新建业务 Mapper 接口**
- 文件：`server/src/mapper/<EntityName>Mapper.java`
- 继承 `BaseMapper<T>`（来自 `mybatis-plus:core`，通过 `platform/database` 传递）
- XML 放 `server/src/resources/mapper/<EntityName>Mapper.xml`
- 无需额外 BUILD 声明，`resources = glob(["src/**/*.xml"])` 已覆盖

**场景 2：新建实体类**

> **推荐**：新建实体类统一继承 `enhanced/` 系列（带完整审计字段），旧版基类（无 `Enhanced` 前缀）已不推荐用于新代码。

| 场景 | 推荐基类（enhanced） | 旧版基类（不推荐） |
|------|---------------------|-------------------|
| 普通实体 | `BasicEnhancedEntity` | ~~`BasicEntity`~~ |
| 需要租户隔离 | `BasicEnhancedTenantEntity` | ~~`BasicTenantEntity`~~ |
| 需要树形结构 | `BasicEnhancedTreeEntity` | ~~`BasicTreeEntity`~~ |
| 需要租户 + 树形 | `BasicEnhancedTenantTreeEntity` | ~~`BasicTenantTreeEntity`~~ |

`BasicEnhancedEntity` 在 `BasicEntity`（含 id、逻辑删除、createBy/updateBy）基础上额外携带 `creatorId`、`creatorType`、`updaterId`、`updaterType` 审计字段，由 `PropertyUpdateInterceptor` 自动填充。

**场景 3：分页查询结果转换**
```java
// 将 Paging 请求参数转为 MyBatis-Plus Page
Page<T> mpPage = PagingUtils.convert(paging);
// 执行查询后，将 Page 转为业务分页响应 Pagination
Pagination<T> result = PagingUtils.convert(mpPage);
// 若需类型转换（DTO 映射），传入目标类型
Pagination<DtoType> result = PagingUtils.convert(mpPage, DtoType.class);
```

## 注意事项

- `platform/database` **未包含** Liquibase——数据库迁移需要额外引 `//packages/platform/liquibase`（在 `init/BUILD.bazel` 中）
- `MybatisPlusConfigurer` 已配置**多租户插件**，业务实体继承 `BasicTenantEntity` 后自动生效，无需手动设置
- 多数据库驱动（DM、Highgo）已包含在 exports 中，切换数据库类型通过 Nacos 配置 JDBC URL 即可，不需要改 BUILD
- **禁止引入 JPA / Spring Data JPA**（见 ADR-002），全仓统一 MyBatis-Plus
