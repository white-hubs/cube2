# product-method-north-api Specification

## Purpose
为北向接口提供产品方法的完整访问能力,包括查询产品方法定义、调用设备方法、追踪方法执行状态等功能。北向接口作为对外统一的标准API层,将产品方法能力标准化地暴露给第三方系统、前端应用和其他微服务。

## ADDED Requirements

### Requirement: 查询产品方法列表
系统SHALL通过北向接口提供根据产品ID查询该产品下所有方法定义的能力。

#### Scenario: 根据产品ID查询方法列表
- **WHEN** 用户通过北向接口请求`/north/product-method/list?productId={productId}`
- **AND** 产品ID有效且属于当前租户
- **THEN** 系统应当返回该产品下所有未删除的方法列表
- **AND** 返回数据格式与物模型管理接口保持一致(使用`ProductMethodVo`)
- **AND** 按创建时间倒序排列
- **AND** 包含方法的基本信息:方法名称、方法编码、方法类型、输入输出参数定义等

#### Scenario: 产品不存在或无权限
- **WHEN** 用户请求的产品ID不存在或不属于当前租户
- **THEN** 系统应当返回空列表或友好的错误提示
- **AND** 不泄露其他租户的数据信息

### Requirement: 查询方法详情
系统SHALL通过北向接口提供根据方法ID查询单个方法完整定义的能力。

#### Scenario: 根据方法ID查询详情
- **WHEN** 用户通过北向接口请求`/north/product-method/detail?methodId={methodId}`
- **AND** 方法ID有效且属于当前租户
- **THEN** 系统应当返回该方法的完整信息
- **AND** 包含格式化的输入参数定义(`input_params`)
- **AND** 包含格式化的输出参数定义(`output_params`)
- **AND** 包含扩展配置信息(`extended_config`)
- **AND** 包含方法类型、超时时间、重试配置等执行控制参数

#### Scenario: 方法不存在或无权限
- **WHEN** 用户请求的方法ID不存在或不属于当前租户
- **THEN** 系统应当返回404错误或友好的错误提示
- **AND** 不泄露其他租户的数据信息

### Requirement: 调用设备方法
系统SHALL通过北向接口提供向指定设备下发方法调用指令的能力,支持同步和异步两种调用模式,并支持批量调用。

#### Scenario: 同步调用设备方法
- **WHEN** 用户通过北向接口POST请求`/north/product-method/invoke`
- **AND** 提供有效的设备ID、方法ID和输入参数
- **AND** 方法类型为同步(`sync`)
- **AND** 输入参数格式符合方法定义
- **THEN** 系统应当向设备下发方法调用指令
- **AND** 等待设备响应或超时
- **AND** 返回`MethodInvokeResponse`,包含:
  - 命令实例ID(`instanceId`)
  - 执行状态(`statusCode`): SUCCESS/TIMEOUT/REJECT等
  - 输出参数(`output`)
  - 错误信息(如果有)
- **AND** 记录命令调用日志到`CommandRecord`

#### Scenario: 异步调用设备方法
- **WHEN** 用户通过北向接口调用方法
- **AND** 方法类型为异步(`async`)
- **THEN** 系统应当立即返回命令受理状态
- **AND** 返回`MethodInvokeResponse`,包含命令实例ID(`instanceId`)供后续查询状态
- **AND** 状态码为`PENDING`或`ACCEPTED`
- **AND** 后台异步处理命令执行

#### Scenario: 批量调用设备方法
- **WHEN** 用户通过北向接口POST请求`/north/product-method/invoke/batch`
- **AND** 提供包含多个调用请求的列表`List<MethodInvokeParam>`
- **THEN** 系统应当逐个处理每个调用请求
- **AND** 返回`List<MethodInvokeResponse>`,每个响应对应一个请求
- **AND** 确保部分请求失败不影响其他请求的执行
- **AND** 失败的请求返回错误信息,状态码为`REJECT`
- **AND** 参考`NorthCommandController.projectCommandIssue(Set<CommandRequest>)`的实现模式

#### Scenario: 输入参数验证失败
- **WHEN** 用户提供的输入参数格式不符合方法定义
- **OR** 必填参数缺失
- **OR** 参数类型不匹配
- **THEN** 系统应当拒绝调用
- **AND** 返回参数验证错误信息
- **AND** 状态码为`REJECT`
- **AND** 不向设备下发指令

#### Scenario: 设备离线或不支持方法
- **WHEN** 目标设备离线
- **OR** 设备产品不包含指定的方法
- **THEN** 系统应当返回错误状态
- **AND** 提示设备状态或方法不匹配的原因
- **AND** 状态码为`REJECT`

### Requirement: 查询方法调用历史
系统SHALL通过北向接口提供查询设备方法调用记录的能力,支持按设备、方法、时间等维度筛选。

#### Scenario: 分页查询调用历史
- **WHEN** 用户通过北向接口请求`/north/product-method/invoke-history`
- **AND** 提供分页参数(pageNum、pageSize)
- **THEN** 系统应当返回分页的调用历史记录
- **AND** 每条记录包含:
  - 调用时间(`invoke_time`)
  - 设备信息(设备ID、设备名称)
  - 方法信息(方法ID、方法名称、方法编码)
  - 输入参数(`input_params`)
  - 执行状态(`status`)
  - 输出结果(`output_params`,如果有)
  - 错误信息(`error_message`,如果有)
- **AND** 按调用时间倒序排列

#### Scenario: 按设备ID筛选
- **WHEN** 用户在查询参数中指定设备ID
- **THEN** 系统应当只返回该设备的方法调用记录
- **AND** 遵循租户隔离规则

#### Scenario: 按方法ID筛选
- **WHEN** 用户在查询参数中指定方法ID
- **THEN** 系统应当只返回该方法的调用记录
- **AND** 包含所有设备对该方法的调用

#### Scenario: 按时间范围筛选
- **WHEN** 用户指定开始时间和结束时间
- **THEN** 系统应当只返回该时间范围内的调用记录
- **AND** 支持跨天、跨月查询

### Requirement: 租户隔离与权限控制
系统SHALL确保所有北向方法接口严格遵循多租户隔离和权限控制规则。

#### Scenario: 租户数据隔离
- **WHEN** 租户A通过北向接口查询产品方法或调用历史
- **THEN** 系统应当只返回租户A的数据
- **AND** 不能看到或操作租户B的产品和设备
- **AND** 所有查询自动添加租户过滤条件

#### Scenario: 方法调用权限验证
- **WHEN** 用户尝试调用设备方法
- **THEN** 系统应当验证用户是否有权限操作该设备
- **AND** 验证设备是否属于用户的租户
- **AND** 记录操作人信息到调用日志

### Requirement: 接口风格一致性
系统SHALL确保产品方法北向接口与现有北向接口(设备、属性、事件)保持一致的设计风格和规范。

#### Scenario: URL命名规范
- **WHEN** 设计北向方法接口URL
- **THEN** 应当遵循`/north/product-method/*`的命名规范
- **AND** 与`/north/device/*`、`/north/product/*`等现有接口保持风格一致
- **AND** 使用RESTful风格(GET查询、POST操作)

#### Scenario: 错误处理统一
- **WHEN** 接口发生错误
- **THEN** 应当返回统一的错误格式
- **AND** 包含错误码、错误消息、时间戳等标准字段
- **AND** 与现有北向接口的错误格式保持一致

#### Scenario: 返回数据格式统一
- **WHEN** 接口返回数据
- **THEN** 应当使用标准的响应包装格式(如果有)
- **AND** 分页查询使用`Pagination<T>`格式
- **AND** 时间格式、枚举值等与现有接口保持一致

### Requirement: 命令系统集成
系统SHALL复用现有的命令下发和追踪机制,不引入新的方法调用路径。

#### Scenario: 复用CommandInfo记录
- **WHEN** 用户调用设备方法
- **THEN** 系统应当创建`CommandInfo`记录
- **AND** 命令类型标识为"METHOD_INVOKE"或类似标识
- **AND** 命令内容包含方法ID和输入参数
- **AND** 利用现有的命令下发服务向设备发送指令

#### Scenario: 复用CommandRecord追踪
- **WHEN** 查询方法调用历史
- **THEN** 系统应当从`CommandRecord`表查询
- **AND** 筛选命令类型为"METHOD_INVOKE"的记录
- **AND** 复用现有的命令状态定义(PENDING、SUCCESS、TIMEOUT等)

#### Scenario: 命令状态查询
- **WHEN** 用户通过命令实例ID查询状态
- **THEN** 应当能够复用现有的命令状态查询接口
- **AND** 返回实时的执行状态和结果
