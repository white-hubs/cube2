# 实现任务清单

## 1. 查询产品方法列表接口
- [ x ] 创建`NorthProductMethodController`,添加`/north/product-method/list`接口
- [ x ] 创建`NorthProductMethodService`,实现产品方法列表查询逻辑
- [ x ] 复用`MdProductMethodService`的查询能力,调用`listByProductId`方法
- [ x ] 返回`List<ProductMethodVo>`,按创建时间倒序排列
- [ x ] 测试:验证根据产品ID正确返回方法列表
- [ x ] 测试:验证租户隔离(不同租户看不到彼此的数据)

## 2. 查询方法详情接口
- [ x ] 在`NorthProductMethodController`添加`/north/product-method/detail`接口
- [ x ] 在`NorthProductMethodService`实现方法详情查询逻辑
- [ x ] 复用`MdProductMethodService`的`getDetail`方法
- [ x ] 返回`ProductMethodVo`,包含完整的方法定义(输入输出参数等)
- [ x ] 测试:验证根据方法ID正确返回详情
- [ x ] 测试:验证不存在的方法ID返回友好错误

## 3. 调用设备方法接口
- [ x ] 在`NorthProductMethodController`添加`/north/product-method/invoke`接口
- [ x ] 创建请求参数类`MethodInvokeParam`,包含:设备ID、方法ID、输入参数
- [ x ] 创建响应类`MethodInvokeResponse`,包含:命令实例ID、执行状态、输出参数、错误信息
- [ x ] 在`NorthProductMethodService`实现方法调用逻辑
- [ x ] 集成现有命令下发系统:
  - [ x ] 查询方法定义,获取方法类型(同步/异步)
  - [ x ] 验证输入参数格式是否符合方法定义
  - [ x ] 创建`CommandInfo`记录,设置命令类型为"METHOD_INVOKE"
  - [ x ] 调用命令下发服务,向设备发送方法调用指令
  - [ x ] 返回命令实例ID(`instanceId`)和初始状态
- [ x ] 测试:验证同步方法调用返回执行结果
- [ x ] 测试:验证异步方法调用返回命令受理状态
- [ x ] 测试:验证参数格式不匹配时返回错误

## 3.1 批量调用设备方法接口
- [ x ] 在`NorthProductMethodController`添加`/north/product-method/invoke/batch`接口
- [ x ] 接收`List<MethodInvokeParam>`作为请求参数
- [ x ] 在`NorthProductMethodService`实现批量调用逻辑
- [ x ] 循环处理每个调用请求,捕获异常确保部分失败不影响其他请求
- [ x ] 返回`List<MethodInvokeResponse>`,每个响应对应一个请求
- [ x ] 参考现有的`NorthCommandController.projectCommandIssue(Set<CommandRequest>)`实现
- [ x ] 测试:验证批量调用多个方法
- [ x ] 测试:验证部分失败场景(某些请求失败,其他成功)

## 4. 查询方法调用历史接口
- [ x ] 在`NorthProductMethodController`添加`/north/product-method/invoke-history`接口
- [ x ] 创建查询参数类`MethodInvokeHistoryParam`,支持按设备ID、方法ID、时间范围筛选
- [ x ] 在`NorthProductMethodService`实现调用历史查询逻辑
- [ x ] 查询`CommandRecord`表,筛选命令类型为"METHOD_INVOKE"的记录
- [ x ] 返回分页结果`Pagination<MethodInvokeHistoryVo>`,包含:
  - [ x ] 调用时间
  - [ x ] 设备信息
  - [ x ] 方法信息
  - [ x ] 输入参数
  - [ x ] 执行状态
  - [ x ] 输出结果(如果有)
- [ x ] 测试:验证分页查询正确返回历史记录
- [ x ] 测试:验证按设备ID筛选
- [ x ] 测试:验证按方法ID筛选

## 5. 补充VO类和参数类
- [ x ] 创建`MethodInvokeParam.java` - 方法调用请求参数(设备ID、方法ID、输入参数)
- [ x ] 创建`MethodInvokeResponse.java` - 方法调用响应(命令实例ID、状态、输出参数、错误信息)
- [ x ] 创建`MethodInvokeHistoryParam.java` - 调用历史查询参数(支持设备ID、方法ID、时间范围筛选)
- [ x ] 创建`MethodInvokeHistoryVo.java` - 调用历史返回对象
- [ x ] 所有类添加完整的中文注释
- [ x ] 使用Lombok简化代码
- [ x ] 添加参数校验注解(`@NotNull`、`@NotBlank`等)

## 6. 文档和验证
- [ x ] 为所有接口添加清晰的中文JavaDoc注释
- [ x ] 确保代码符合项目规范(命名、格式、分层等)
- [ x ] 运行现有测试,确保没有破坏现有功能
- [ x ] 更新产品方法规范文档(如需要)

## 注意事项
- 所有接口都需要经过租户隔离校验
- 方法调用需要验证设备是否在线、是否支持该方法
- 方法调用涉及设备控制,需要记录操作日志
- 保持与现有北向接口的风格一致(URL命名、错误处理、返回格式)
- 复用现有的命令下发和追踪机制,不要引入新的调用路径
