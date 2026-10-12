# platform/base 能力与 Bazel 目标目录

本文档基于对 [`packages/platform/base`](../) 下源码与 `BUILD.bazel` 的扫描归纳。**Java 包前缀**：`com.guhecloud.platform.base.*`（`util`、`constants`、`model`、`standard`、`context`、`config`、`http`、`interceptor` 等）。

## 1. 顶层聚合

| 目标 | 说明 |
|------|------|
| `//packages/platform/base` | `java_library` **`base`**：`src/**/*.java` 与一组细粒度 `deps`（Jackson、常用 utils、Tomcat embed 等），是多数业务引入 platform-base 的**主入口库**。 |
| `//packages/platform/base:export` / `:bom` | Maven `platform-base` / `platform-base-bom` 发布。 |

## 2. constants（约定与格式协议）

路径：[constants/](../constants)；Bazel：`//packages/platform/base/constants:date`。

| 源码 | 用途 |
|------|------|
| `DateFormat.java` | 全仓通用日期/时间 **格式字符串常量**（如 `yyyy-MM-dd HH:mm:ss`、UTC ISO、`yyyyMMddHHmmss` 等）。 |
| `TimeZone.java` | 时区约定（如 **北京时间 `GMT+8`**）。 |

被 `utils:date`、`standard:jackson`、`utils:json` 等 **exports/deps** 引用，属于跨服务的**时间序列化/展示协议基础**。

## 3. model（通用载体与协议模型）

路径：[model/](../model)；Bazel 见 [`model/BUILD.bazel`](../model/BUILD.bazel)。

| 目标 | 典型类型 | 用途摘要 |
|------|----------|----------|
| `:pagination` | `Pagination` | 分页请求/元数据载体。 |
| `:paging` | `Paging` | 分页结果封装（配合 assert/collection/validation）。 |
| `:period` / `:period-type` | `Period`, `PeriodTypeEnum` | 时间段与周期枚举。 |
| `:requester` | `Requester` | 调用方/请求者上下文模型。 |
| `:tree` | `Tree` | 树形结构模型。 |
| `:pair` | `Pair` | 二元组。 |
| `:error-response` | `ErrorResponse` | **错误响应体**（与异常体系配合）。 |
| `:legacy-response` | `LegacyResponse` | 兼容旧版响应形态。 |
| `:actor-type` | `ActorTypeEnum` | 参与者/主体类型枚举。 |
| `:models` | *聚合 exports* | 一次导出多 model 目标（不含 `error-response` 时见 BUILD 列表）。 |

## 4. standard（异常协议、HTTP、Jackson）

路径：[standard/](../standard)。

### 4.1 `//packages/platform/base/standard:exception`

| 类 | 语义（协议层） |
|----|----------------|
| `ServiceException` | 业务服务异常基类场景 |
| `ClientException` | 客户端/调用方错误 |
| `InvalidArgumentException` | 参数非法 |
| `UnauthenticatedException` / `PermissionDeniedException` | 认证/授权 |
| `ResourceNotFoundException` / `ResourceConflictException` / `ResourceNotAcceptableException` / `ResourcePreconditionFailedException` | REST 资源语义 |
| `OutOfRangeException` / `TooManyRequestException` | 限流与范围 |
| `OuaaException` | 与认证中心相关 |
| `UnimplementedException` | 未实现 |
| `ErrorTypeEnum` / `ErrorSpec` | **错误类型与规范** |
| `Assert` | 断言辅助（与 utils 配合） |

构成平台统一的 **HTTP/API 错误语义** 与跨服务异常类型。

### 4.2 `//packages/platform/base/standard:http`

| 类 | 用途 |
|----|------|
| `HttpClientFactory` | HTTP 客户端工厂（HC5 等）。 |

### 4.3 `//packages/platform/base/standard:jackson`

| 路径 | 用途 |
|------|------|
| `jackson/ObjectMapperConfig.java` | ObjectMapper 默认行为 |
| `jackson/Jackson*Serializer*.java` / `JacksonNullSerializerModifier` | 序列化策略 |
| `jackson/annotation/Desensitize.java` | **脱敏**注解协议 |
| `jackson/annotation/Decimal.java` | 小数序列化约定 |

与 `utils:desensitize`、`utils:regex`、`constants:date`、`:model` 等协作，形成 **JSON 与 API 字段表现层协议**。

## 5. utils（工具类矩阵）

路径：[utils/](../utils)；各目标见 [`utils/BUILD.bazel`](../utils/BUILD.bazel)。**包名**：`com.guhecloud.platform.base.util`。

### 5.1 断言、集合与基础结构

| 目标 | 类 | 说明 |
|------|-----|------|
| `:assert` | `AssertUtils` | 前置校验 |
| `:collection` | `CollectionUtils` | 集合操作 |
| `:list` / `:map` / `:set` | `ListUtils` / `MapUtils` / `SetUtils` | 结构工具 |
| `:tree` | `TreeUtils` | 树操作（依赖 `model:tree`） |

### 5.2 字符串、正则、路径匹配

| 目标 | 类 | 说明 |
|------|-----|------|
| `:string` | `StringUtils` | 字符串（Lang3 + text） |
| `:regex` | `RegexUtils` | 正则 |
| `:desensitize` | `DesensitizeUtils` | 脱敏字符串处理 |
| `:ant-path-matcher` | `AntPathMatcher` | **Ant 风格路径匹配**（网关/权限常用） |
| `:pinyin` | `PinYinUtils` | 拼音 |

### 5.3 时间与 ID

| 目标 | 类 | 说明 |
|------|-----|------|
| `:date` | `DateUtils` | 日期解析与计算（**导出** `constants:date`） |
| `:id` | `IdUtils`, `Snowflake` | ID 与雪花（含 hutool 迁移注记） |
| `:object-id` | `ObjectIdUtils` | ObjectId |
| `:random` | `RandomUtils` | 随机 |

### 5.4 编解码与安全

| 目标 | 类 | 说明 |
|------|-----|------|
| `:base64` | `Base64Utils` | Base64 |
| `:digest` | `DigestUtils` | 摘要 |
| `:aes` | `AESUtils` | AES |
| `:secure` | `SecureUtils` | 加密工具（hutool crypto） |

### 5.5 对象与反射、Bean

| 目标 | 类 | 说明 |
|------|-----|------|
| `:object` | `ObjectUtils` | 对象比较、拷贝（Orika、Protostuff、Guava） |
| `:spring-object` | `SpringObjectUtils` | Spring 集成版对象工具 |
| `:bean` | `BeanUtils` | Bean（hutool） |
| `:reflect` | `ReflectUtils` | 反射 |
| `:convert` | `ConvertUtils` | 类型转换 |

### 5.6 JSON

| 目标 | 类 | 说明 |
|------|-----|------|
| `:json` | `JsonUtils` | Jackson 封装读写（依赖 `standard:jackson`、`constants:date`） |

### 5.7 数值与其它

| 目标 | 类 | 说明 |
|------|-----|------|
| `:number` / `:math` | `NumberUtils` / `MathUtils` | 数值 |
| `:barcode` | `BarcodeUtils` | 条码（ZXing） |
| `:biz-code` | `BizCodeUtils` | 业务编码 |
| `:host` | `HostUtils` | 主机名/IP |
| `:compress` | `CompressUtils` | 压缩 |
| `:exception` | `ExceptionUtils` | 异常链工具 |

### 5.8 Web / 文件 / POI

| 目标 | 类 | 说明 |
|------|-----|------|
| `:spring-file` | `SpringFileUtils` | Spring/Web 下文件与 HTTP 客户端辅助 |
| `:poi` | `poi/CascadeSelectDownloader.java` | **POI**：级联选择导出等（Servlet API） |

## 6. src（根库 `base` 带的「运行时」能力）

与 `java_library` **`base`** 一并编译的 [`src/`](../src) 典型职责：

| 包/类 | 用途 |
|--------|------|
| `context.ApplicationContextHolder` | Spring `ApplicationContext` 访问、按注解扫描 Bean |
| `context.SessionContextHolder` / `ExecutorContextHolder` | 会话与执行上下文 ThreadLocal |
| `config.JacksonConfigurer` / `ObjectCopierConfigurer` / `ThreadPoolConfigurer` + `ThreadPoolProperties` | Jackson、对象拷贝、线程池 **自动配置** |
| `http.HttpConstants` | **跨服务 HTTP 头协议**：`authorization`、`tenant`、`terminal`、`version`、`signature`、`permission` 等 |
| `http.converter.RawInputStreamHttpMessageConverter` / `MappingJackson2YamlHttpMessageConverter` | 消息转换（含 YAML） |
| `interceptor.StringDateConverter` / `StringPeriodConverter` | 请求参数日期/周期转换 |

这些与 `standard`、`utils` 一起构成 **网关—服务—客户端** 共享的协议与基础设施。

## 7. 使用建议（给 Agent / 开发者）

1. **新增依赖**：优先复制**同层服务** `BUILD.bazel` 中已有 `//packages/platform/base/...` 写法；细粒度目标可减少传递依赖膨胀。
2. **错误与响应**：抛异常类型对齐 `standard:exception`；返回体对齐 `model:error-response` 与现有 Controller 约定。
3. **时间与 JSON**：格式常量用 `constants:date`；序列化行为受 `standard:jackson` 与注解约束。
4. **勿把业务模型塞进 base**：领域实体、表映射应留在各业务模块；base 仅保留 **横切协议与可复用无业务语义的载体/工具**。
