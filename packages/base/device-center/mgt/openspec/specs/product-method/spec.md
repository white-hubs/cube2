# product-method Specification

## Purpose
TBD - created by archiving change refactor-product-method-implementation. Update Purpose after archive.
## Requirements
### Requirement: 产品方法管理
系统SHALL提供产品方法的完整生命周期管理能力,包括创建、更新、查询、删除操作,支持同步和异步方法类型。

#### Scenario: 创建产品方法
- **WHEN** 用户提供有效的产品ID、方法名称、方法编码、方法类型等信息
- **AND** 输入参数和输出参数定义符合规范
- **AND** 方法编码在产品内唯一
- **THEN** 系统应当成功创建产品方法
- **AND** 自动生成唯一的方法ID
- **AND** 更新产品版本号

#### Scenario: 更新产品方法
- **WHEN** 用户提供有效的方法ID和更新信息
- **AND** 方法存在且未被删除
- **AND** 新的方法编码在产品内唯一(排除自身)
- **THEN** 系统应当成功更新产品方法
- **AND** 更新产品版本号

#### Scenario: 查询产品方法详情
- **WHEN** 用户提供有效的方法ID
- **THEN** 系统应当返回完整的方法信息
- **AND** 包含格式化的输入输出参数定义
- **AND** 包含扩展配置信息

#### Scenario: 查询产品方法列表
- **WHEN** 用户提供有效的产品ID
- **THEN** 系统应当返回该产品下所有未删除的方法列表
- **AND** 按创建时间倒序排列

#### Scenario: 删除产品方法
- **WHEN** 用户提供有效的方法ID
- **AND** 方法存在
- **THEN** 系统应当执行逻辑删除
- **AND** 更新产品版本号

### Requirement: 方法参数定义规范
系统SHALL支持结构化的方法参数定义,包括输入参数和输出参数,每个参数MUST包含名称、标识码、数据类型等必要信息。

#### Scenario: 验证参数定义格式
- **WHEN** 用户提交方法参数定义
- **THEN** 系统应当验证参数必须包含params数组
- **AND** 每个参数必须包含name(名称)、param_code(标识)、data_type(数据类型)
- **AND** 参数名称和标识不能为空

#### Scenario: 支持空参数定义
- **WHEN** 用户创建无输入参数或无输出参数的方法
- **THEN** 系统应当接受空参数定义
- **AND** 使用标准的空参数结构(包含空params数组)

### Requirement: 类型安全与数据转换
系统SHALL使用强类型的数据结构进行方法参数定义,避免直接暴露通用JSON对象,提高代码可维护性和类型安全性。

#### Scenario: 参数定义使用POJO类
- **WHEN** 系统处理方法参数定义
- **THEN** 应当使用明确的Java POJO类表示参数结构
- **AND** 使用Jackson框架进行JSON序列化和反序列化
- **AND** 在VO层暴露类型安全的对象

#### Scenario: 扩展配置使用Map类型
- **WHEN** 系统处理方法扩展配置
- **THEN** 应当使用`Map<String, Object>`类型
- **AND** 避免直接暴露JSONObject到VO层

### Requirement: 标准异常处理
系统SHALL使用平台标准的异常和断言机制,确保错误处理的一致性。

#### Scenario: 使用平台标准Assert
- **WHEN** 系统进行参数校验或状态断言
- **THEN** 应当使用`com.guhecloud.platform.base.standard.exception.Assert`
- **AND** 不使用已弃用的`com.guhecloud.cube.iot.mgt.comm.util.Assert`

#### Scenario: 参数校验错误信息
- **WHEN** 参数校验失败
- **THEN** 系统应当抛出清晰的中文错误信息
- **AND** 指明具体的校验失败原因

### Requirement: 方法编码唯一性约束
系统SHALL确保方法编码(method_code)在同一产品内唯一,便于跨环境方法映射。

#### Scenario: 创建时检查编码唯一性
- **WHEN** 用户创建新方法时提供方法编码
- **AND** 该编码在产品内已存在
- **THEN** 系统应当拒绝创建
- **AND** 返回错误信息"方法标识[{methodCode}]重复"

#### Scenario: 更新时检查编码唯一性
- **WHEN** 用户更新方法时修改方法编码
- **AND** 新编码已被其他方法使用
- **THEN** 系统应当拒绝更新
- **AND** 返回错误信息"方法标识[{methodCode}]重复"

### Requirement: 方法类型与超时配置
系统SHALL支持同步和异步两种方法类型,并提供超时、重试等执行控制配置。

#### Scenario: 方法类型验证
- **WHEN** 用户创建或更新方法时指定方法类型
- **THEN** 系统应当验证方法类型必须为"sync"(同步)或"async"(异步)
- **AND** 如果未指定则默认为"sync"

#### Scenario: 超时配置默认值
- **WHEN** 用户创建方法时未指定超时时间
- **THEN** 系统应当使用默认超时时间10000毫秒
- **AND** 超时时间必须大于0

#### Scenario: 重试配置
- **WHEN** 用户创建方法时指定retryable为true
- **THEN** 系统应当允许配置最大重试次数(maxRetries)
- **AND** maxRetries必须大于等于0
- **WHEN** retryable为false或未指定
- **THEN** 系统应当将maxRetries设置为0

### Requirement: 产品版本联动
系统SHALL在方法的创建、更新、删除操作时自动更新产品版本号,支持物模型版本管理。

#### Scenario: 创建方法更新版本
- **WHEN** 成功创建产品方法
- **THEN** 系统应当自动递增产品版本号

#### Scenario: 更新方法更新版本
- **WHEN** 成功更新产品方法
- **THEN** 系统应当自动递增产品版本号

#### Scenario: 删除方法更新版本
- **WHEN** 成功删除产品方法
- **THEN** 系统应当自动递增产品版本号

