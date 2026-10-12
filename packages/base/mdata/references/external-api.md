# base/mdata — 对外契约速查

## 对外 Bazel 标签（供其他包 `deps` 引用）

| 用途 | 标签 |
|------|------|
| 客户端库（Feign 与 DTO 等） | `//packages/base/mdata/client:client` |
| 服务端库（编译依赖） | `//packages/base/mdata/server:server` |
| 服务端元数据（server-one / 部署消费） | `//packages/base/mdata/server:server-info` |
| DB 初始化 SQL 制品 | `//packages/base/mdata/server/init:init-mysql` |

Maven 发布制品（`java_export` / `export` 宏）：`//packages/base/mdata/client:package`、`//packages/base/mdata/server:package`（版本与 `PACKAGE_GROUP` 见模块 `variables.bzl`）。

## HTTP 接口路由前缀

- **网关侧前缀**（`server_info` `gw_prefix`）：`/mdata/`  
- **应用内路径**：Controller 多为方法级映射（如 `/area/...`、`/merchant/...`、`/grid/...`），**无统一类级前缀**；完整清单以代码与网关配置为准。

## Feign Client 接口名

均使用 `@FeignClient(name = "mdata")`，包名 `com.guhecloud.cube.biz.mdata.client`：

| 接口 |
|------|
| `AreaClient` |
| `AreaSceneConfigClient` |
| `BizLogClient` |
| `GoodsCategoryClient` |
| `GridAreaConfigClient` |
| `MerchantClient` |
| `RiskLevelClient` |
| `SceneTypeClient` |
| `StallClient` |
| `StreamMediaConfigClient` |
| `SupplierClient` |
| `VideoPtzClient` |

## MQ 消息（Topic / ConsumerGroup / Tag）

### 消费（本服务订阅）

| Topic | ConsumerGroup（代码表达式求值后） | 说明 |
|-------|-----------------------------------|------|
| `user-update` | `supplier_user-update` | 供应商侧用户姓名同步 |
| `user-update` | `purchaser_user-update` | 采购商侧用户姓名同步 |
| `BIZ-LOG-RECORD` | `BIZ-LOG-RECORD_MDATA` | 业务操作日志落库 |
| `SYSTEM_OPERATION_LOG` | `SYSTEM_OPERATION_LOG` | 系统操作日志（与 Topic 同名常量） |

**Tag**：上述 `@RocketMQMessageListener` **未**配置 `selectorExpression` 时，Tag 过滤规则以平台与 Broker 默认为准；若需精确 Tag 列表，**待团队补充**。

### 生产（本服务发送，节选）

| Topic 常量 | 典型用途 |
|------------|----------|
| `AREA_CHANGE` | 区域变更通知 |
| `STALL-SYNC` | 档位同步 |
| `SUPPLIER_SYNCHRONOUS` | 供应商同步 |
| ~~`add-mall`~~ | ~~加铺（`AddMallMessage`）~~（**已废弃**） |

其他生产者或异步链路若未在本文档列出，**待团队补充**。
