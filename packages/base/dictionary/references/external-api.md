# base/dictionary — 对外契约（消费者视角）

## 对外 Bazel 标签

其他包在 `deps` 中可引用的典型目标（完整 label 前缀为 `//packages/base/dictionary/`）：

| 用途 | 标签 |
|------|------|
| 客户端库（Feign + DTO 等） | `//packages/base/dictionary/client:client` |
| 客户端 Maven 发布 | `//packages/base/dictionary/client:export`、`:export-dev`、`:export-test` |
| 服务端实现库 | `//packages/base/dictionary/server:server` |
| 服务端 Maven 发布 | `//packages/base/dictionary/server:package` |
| 部署侧服务元数据 | `//packages/base/dictionary/server:server-info` |
| DB 初始化脚本打包 | `//packages/base/dictionary/server/init:init-mysql` |

根目录 `BUILD.bazel` 无聚合目标；若需统一别名，待团队补充。

## HTTP 路由前缀

- **网关前缀**（`server_info` / `server/BUILD.bazel`）：`/dictionary/`  
  下文路径均为 **应用 Controller 上的相对路径**；经网关的完整路径一般为 **`/dictionary` + 下列路径**（是否含尾部斜杠以网关规则为准）。

### 用户字典（类级前缀）

- 前缀：`/user-dictionary`  
- 方法示例：`/get/by-code`、`/list`、`/page`、`/add`、`/add/or/update/batch`、`/update`、`/delete/{id}`  
- 来源：`server/src/controller/UserDictionaryController.java`

### 系统字典、数据字典、字典项、级联字典（多为根路径）

无类级 `@RequestMapping` 的控制器，路径即方法上的映射（与 Feign 一致），包括但不限于：

| 分组 | 示例路径（节选） |
|------|------------------|
| 系统字典 | `/getSysDictionaryById/{id}`、`/getSysDictionaryByIds`、`/getSysDictionaryByCode`、`/getSysDictionaryByItem`、`/addSysDictionary`、`/updateSysDictionary`、`/deleteSysDictionary/{id}`、`public/sys-ability-config` |
| 数据字典 | `/getDataDictionaryById/{id}`、`/getDataDictionaryByItem`、`/admin/getDataDictionaryByItem`、`/addDataDictionary`、… |
| 字典项 | `/getDictionaryItemByCode/{code}`、`/getDictionaryItemAll`、`/addDictionaryItem`、`/public/refresh-dictionary-item-cache` |
| 级联字典 | `/getCascadeDictionaryById/{id}`、`/getCascadeDictionary`、`/getCascadeDictionaryItem`、`/addCascadeDictionary`、… |
| 公开代理 | `/public/getDictionaryItemByCode/{code}`、`/public/getDataDictionaryByItem` |

完整列表以 `server/src/controller/*.java` 为准。

### DCM

- 前缀：`/dict/dcm/config`（`server/src-dcm/controller/DictionaryDcmController.java`）

## Feign Client 接口名

服务名均为 **`dictionary`**（`@FeignClient("dictionary")` 或 `value = "dictionary"`）：

| 接口 | 说明 |
|------|------|
| `com.guhecloud.cube.base.dictionary.client.UserDictionaryClient` | 用户字典 |
| `com.guhecloud.cube.base.dictionary.client.SysDictionaryClient` | 系统字典 |
| `com.guhecloud.cube.base.dictionary.client.DataDictionaryClient` | 数据字典（含 `fallback = DataDictionaryClientFallBack`） |
| `com.guhecloud.cube.base.dictionary.client.CascadeDictionaryClient` | 树型/级联字典 |

路径与 Controller/Feign 方法上的 `@GetMapping` 等一致；**未在 client 中声明的 HTTP 接口**（例如部分 `admin/` 路径）需直连 HTTP 或待团队补充 Feign。

## MQ 消息（Topic / Tag）

本模块 **未发现** `@RocketMQMessageListener` **消费者**；以下为**发送侧**常量（`RocketMQContextHolder.send`）：

| 常量 / 形式 | 含义 |
|-------------|------|
| `DictionaryMessage.TOPIC` + `":"` + `dictionaryItem.getCode()` | 即 **`DICTIONARY:<字典项编码>`**，作 Tag/子主题使用；消息体为 `DictionaryMessage` |
| `DictionaryDeleteMessage.TOPIC` | **`DICTIONARY_DELETE`**；删除通知 |

具体 Tag 语义与订阅方约定以平台 RocketMQ 配置及消费服务为准；不足处待团队补充。
