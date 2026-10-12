# 北向接口新增产品方法能力

## Metadata
- **Change ID**: `add-product-method-north-api`
- **Status**: Draft
- **Created**: 2025-12-02
- **Author**: AI Assistant

## Why
产品物模型管理模块现已支持产品方法的完整生命周期管理(创建、更新、查询、删除),但北向接口(`/north/*`)目前尚未提供产品方法相关的查询和调用能力。

北向接口作为对外统一的标准API层,需要向第三方系统、前端应用和其他微服务提供完整的物模型访问能力。目前产品属性、事件已经通过北向接口暴露,而产品方法作为物模型的核心组成部分,也需要通过北向接口提供标准化的访问能力。

## What
在北向接口(`src/north`)模块中新增产品方法相关的REST API,提供以下能力:

### 主要功能
1. **查询产品方法列表**: 根据产品ID查询该产品下的所有方法定义
2. **查询方法详情**: 根据方法ID查询单个方法的完整信息
3. **调用设备方法**: 向指定设备下发方法调用指令(同步/异步),支持单个和批量调用
4. **查询方法调用历史**: 分页查询设备方法的调用记录和状态,支持按设备、方法、时间筛选

### 技术实现
- **复用现有VO**: 直接使用`ProductMethodVo`等现有VO类,保持数据格式一致性
- **新建专用VO**: 创建`MethodInvokeParam`(调用请求)、`MethodInvokeResponse`(调用响应)、`MethodInvokeHistoryVo`(历史记录)等专用类
- **仅REST接口**: 不提供Feign客户端,仅提供标准HTTP REST接口
- **分层架构**: 遵循项目标准分层,创建`NorthProductMethodController`、`NorthProductMethodService`
- **权限校验**: 集成现有的租户隔离和权限验证机制
- **集成现有命令系统**: 方法调用复用现有的`CommandInfo`、`CommandRecord`等命令下发和追踪机制

## Impact

### Benefits
- **完整的物模型北向能力**: 补齐产品方法在北向接口的缺失,形成完整的物模型对外访问能力
- **统一的接口规范**: 通过北向接口统一对外,避免直接暴露内部管理接口
- **第三方系统集成**: 便于第三方系统查询产品能力并进行设备控制
- **前端应用支持**: 为前端应用提供标准化的方法查询和调用接口

### Risks
- **接口设计一致性**: 需要确保与现有北向接口(设备、属性、事件)的风格保持一致
- **权限控制**: 方法调用涉及设备控制,需要严格的权限校验
- **命令系统复用**: 需要理解和正确集成现有的命令下发机制,避免引入新的调用路径

### Migration
无需数据迁移,仅新增接口能力,对现有系统无影响。

## Affected Capabilities
- **新增**: `product-method-north-api` - 产品方法北向接口能力

## Open Questions
- [x] 北向接口的功能范围? → 已确认:查询产品方法列表、查询方法详情、调用设备方法、查询方法调用历史
- [x] 是否需要Feign客户端? → 已确认:仅提供REST接口
- [x] 数据格式是否需要定制? → 已确认:复用现有VO
- [x] 方法调用是否需要支持批量调用? → 已确认:第一期实现批量调用,参考现有命令接口设计
- [x] 方法调用历史查询是否需要支持分页? → 已确认:必须支持分页,符合项目规范要求
- [x] 是否需要提供方法执行状态的实时推送(WebSocket)? → 已确认:不在本期范围,可通过轮询命令状态接口查询结果

## References
- 物模型管理: `src/things/controller/ProductMethodController.java`
- 现有北向设备接口: `src/north/controller/NorthDeviceController.java`
- 现有命令下发系统: `src/north/controller/NorthCommandController.java`
- 产品方法规范: `openspec/specs/product-method/spec.md`
- 项目约定: `openspec/project.md`
