---
name: cube2-base-mdata
description: >
  Cube2 base/mdata 主数据域 Spring Boot 服务：区域/档位/商户/供应商/可视化场景配置/网格等业务 REST，
  对外通过 Feign（服务名 mdata）与网关前缀 /mdata/ 暴露。触发词：packages/base/mdata、mdata、
  主数据、AreaClient、MerchantClient、gw_prefix、cube-biz-mdata、主数据服务。
---

# base/mdata

原则见 [.agents/skills/cube2-repo/references/principles.md](../../../.agents/skills/cube2-repo/references/principles.md)。术语见 [.agents/skills/cube2-repo/references/glossary.md](../../../.agents/skills/cube2-repo/references/glossary.md)（**mdata**：主数据业务域）。

## 模块边界与职责

- **负责**：
  - **主数据域 REST API**（区域 `area`、档位 `stall`、商户 `merchant`、供应商/采购商 `supplier`/`purchaser`、商品分类 `goods`、可视化场景配置 `scene`、网格 `grid`、H5s 流媒体配置及抓拍截图 `config`/`video`、统计 `stat`/`statistics`、业务日志 `bizlog`、设备 `device` 等；Controller 多在 `server/src/<域>/controller`，多数**无类级** `@RequestMapping`，路径以方法注解为准）。
  - **对外 Feign 契约**（`client` 下 `*Client`，`@FeignClient(name = "mdata")`）。
  - **数据持久化**：MyBatis-Plus（`mapper-locations: classpath:mapper/**/*.xml`）；部分子域使用 `dao` 包命名 Mapper 接口，`bizlog` 使用 `mapper` 子包。
  - **消息**：消费用户变更、业务日志、系统操作日志等；部分子域向 RocketMQ **发送**区域变更、摊位同步、供应商同步、加铺等消息（见 `RocketMQContextHolder.send` 与消息类 `TOPIC` 常量）。
  - **运行元数据**：`server/BUILD.bazel` 中 `server_info` 声明网关前缀 **`/mdata/`**、逻辑库 **`cube-biz-mdata`**、服务标识 `mdata`。
- **不负责**：统一账号实现（依赖 `ouaa` 等客户端）；网关具体路由与 strip 规则（以部署配置为准）。

## 子模块分流

| 子模块 | Bazel 标签（主要） | 职责 |
|--------|-------------------|------|
| **client** | `//packages/base/mdata/client:client`；Maven 发布 `//packages/base/mdata/client:package` | Feign 接口、客户端 DTO/param/vo、部分 MQ 消息体（如 `AddMallMessage`） |
| **server** | `//packages/base/mdata/server:server`；可执行 `//packages/base/mdata/server:server-bin`、`//packages/base/mdata/server:server-exec`；发布 `//packages/base/mdata/server:package`；元数据 `//packages/base/mdata/server:server-info` | Spring Boot 应用 `MDataServer`、REST、领域服务、DAO、MQ 生产/消费 |
| **server/init** | `//packages/base/mdata/server/init:init-mysql` | 初始化 SQL 制品（`pkg_files`，供 deploy 数据库子包引用） |

模块根目录**无** `BUILD.bazel`，**无**根级聚合 target；请直接引用 `client` 或 `server` 子包。

## 快速定位清单

1. **启动类**：`server/src/MDataServer.java`（`com.guhecloud.cube.biz.mdata.server.MDataServer`）。
2. **对外 HTTP**：各 `*Controller.java`（路径多为 `/area/...`、`/merchant/...`、`/grid/...` 等；经网关时叠加 **`/mdata/`** 前缀，与 `server_info` 一致）。
3. **Feign**：`client/src/*Client.java`，统一 **`name = "mdata"`**。
4. **MyBatis**：Java Mapper 在 `*/dao` 或 `bizlog/mapper`；XML 在 `server/resources/mapper/**/*.xml`。
5. **MQ 消费**：`server/src/**/rocketmq/consumer`、`bizlog/event/OperationLogEvent`、`rocketmq/comsumer/BizLogRecordConsumer`。
6. **MQ 生产**：`RocketMQContextHolder.send`（如区域变更 `AREA_CHANGE`、摊位 `STALL-SYNC` 等）。
7. **库名/网关**：`server/BUILD.bazel` → `server_info`（`database`、`gw_prefix`）。

## 构建与入口

- **服务端库**：`//packages/base/mdata/server:server`（私有依赖聚合 `:deps`）。
- **本地运行**：`//packages/base/mdata/server:server-bin`；Spring Boot fat-jar：`//packages/base/mdata/server:server-exec`。
- **Maven 发布**：`//packages/base/mdata/server:package`（`artifact_id`：`mdata-server`）；客户端 `//packages/base/mdata/client:package`（`mdata-client`）；坐标组见 `variables.bzl` 中 `PACKAGE_GROUP`（`com.guhecloud.cube.biz`）。
- **DB init 制品**：`//packages/base/mdata/server/init:init-mysql`。

## 对外契约

- **HTTP**：应用内路径以 Controller 方法为准；**部署侧网关前缀**（`server_info`）：`/mdata/`。是否与 Feign 路径一致需结合网关 strip 规则核对。
- **Feign Client**：`com.guhecloud.cube.biz.mdata.client` 包下 `*Client` 接口（服务名 **`mdata`**）。完整列表见 [references/external-api.md](./references/external-api.md)。
- **RocketMQ**：
  - **消费**：Topic `user-update`（`UserUpdateMessage`）、`BIZ-LOG-RECORD`、`SYSTEM_OPERATION_LOG`；ConsumerGroup 见代码拼接（如 `supplier_user-update`、`BIZ-LOG-RECORD_MDATA` 等）。
  - **生产**（节选）：`AREA_CHANGE`、`STALL-SYNC`、`SUPPLIER_SYNCHRONOUS`、`add-mall` 等；**Tag** 若未在注解中配置 `selectorExpression`，则按平台默认或全量消费，细粒度 Tag 策略 **待团队补充**。

## 数据与配置

- **spring.application.name**：`mdata`（`server/resources/bootstrap.yaml`）。
- **数据库名（连接与元数据）**：`cube-biz-mdata`（`application.yaml` JDBC URL 与 `server_info` `database` 一致）。
- **端口**：默认 `${common-env.mdata.port:10333}`（`application.yaml`）。
- **远程配置**：`application.yaml` 通过 Nacos 引入 common-env、Redis、Security、MyBatis、RocketMQ、数据源、Job 等；本地/联调所需环境变量与 profile **待团队补充**。
- **Liquibase**：`liquibase.enabled: false`（默认），`change-log` 指向 `db.com.guhecloud.cube.biz.mdata/changelog-master.xml`；是否启用以环境配置为准。

## 深度阅读

- [references/overview.md](./references/overview.md)（目录树、target、本地运行）
- [references/external-api.md](./references/external-api.md)（Bazel 对外标签、路由、Feign、MQ 汇总）
