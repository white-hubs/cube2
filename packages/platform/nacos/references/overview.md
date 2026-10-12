# platform/nacos 综述

- 目录下仅有 `init/` 含 `BUILD.bazel`；无独立 `java_library` 于 `nacos/` 根名。

## 模块综述与边界

本模块为 **Nacos 初始化资源包**：在 `init/` 下通过 Bazel `pkg_files` 将 MySQL 初始化脚本打包为可被部署流水线引用的产物（供 `//packages/deploy/database` 子树消费），用于支撑 Nacos 元数据存储等初始化场景。

**不负责**：Nacos Server 的安装与运维、命名空间与鉴权策略、运行时配置中心业务（见运维与平台文档）；不负责替代 Liquibase 全量 schema 管理（SQL 文件内仍有历史 TODO 说明）。

## 当前分层架构

本模块为纯库，无业务分层。

## 目录与包结构

模块根目录：`packages/platform/nacos/`。

| 路径 | 说明 |
|------|------|
| `references/` | 模块开发手册（本文等） |
| `SKILL.md` | 模块级 Agent 导引入口 |
| `init/` | 初始化资源：`BUILD.bazel`（`pkg_files` 目标）、`init-db.mysql.sql`（打包时重命名为 `nacos_init.mysql.sql`） |
| `BUILD.bazel`（根） | 当前为空；可执行目标与资源目标均在 `init/` |

本模块 **无** `src/main/java` 或 Java 包树。

## BUILD target 说明

| Target | 说明 |
|--------|------|
| `//packages/platform/nacos/init:init-mysql` | `pkg_files`：将 `init-db.mysql.sql` 以 `nacos_init.mysql.sql` 名称纳入文件集，`visibility` 面向 `//packages/deploy/database:__subpackages__`，供数据库/部署侧引用 |

本模块无 `java_library`、`java_binary` 或 `bazel run` 型可执行目标；无 `export` 发布目标（待团队补充若后续新增）。

## 本地运行

本模块为纯库，不独立运行。

## 数据层

本模块为纯库，无数据层。

## 直接依赖说明

| 依赖 | 说明 |
|------|------|
| `@rules_pkg//pkg:mappings.bzl` → `pkg_files` | 将 SQL 脚本打包为可引用的文件集规则 |

消费方（如 `//packages/deploy/database` 相关包）如何挂载该产物、与具体数据库实例的关系，以部署与 DBA 侧文档为准（待团队补充）。

## 常见开发场景

1. **跟随上游 Nacos 调整 MySQL 初始化脚本**  
   修改 `init/init-db.mysql.sql`（内容源自 Nacos 官方 `mysql-schema.sql` 思路）；同步后确认与当前使用的 Nacos 大版本兼容，并评估是否需同步 Liquibase 策略（见 SQL 文件头部 TODO）。

2. **调整 Bazel 打包产物名或可见性**  
   修改 `init/BUILD.bazel` 中 `pkg_files` 的 `renames`、`visibility`；变更后需确认 `//packages/deploy/database` 侧引用仍解析通过。

## 注意事项

- `init-db.mysql.sql` 体量较大且含 Nacos 官方 schema 结构，手工合并上游变更时建议逐段 diff，避免误删约束或索引。
- 文件内注释标明部分表与数据初始化尚未纳入 Liquibase 管理；新增变更的规范以团队数据库迁移约定为准。
- 根目录 `BUILD.bazel` 为空属现状；若新增目标，应保持与 `init/` 职责边界清晰，避免重复打包同一 SQL。
