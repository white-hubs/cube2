# Change: 重构产品方法模块实现

## Why

产品方法模块当前存在以下问题:
1. 使用了已弃用的Assert工具类(`com.guhecloud.cube.iot.mgt.comm.util.Assert`),需要迁移到标准平台包(`com.guhecloud.platform.base.standard.exception.Assert`)
2. 过度使用`JSONObject`(Fastjson2)暴露在API和VO层,导致代码可维护性差、类型不安全
3. JSON序列化反序列化逻辑分散在多处,缺乏统一的数据转换规范

这些问题影响代码质量、类型安全性和长期可维护性。

## What Changes

- **依赖迁移**: 将所有产品方法相关类从旧Assert迁移到新Assert
- **类型安全增强**: 使用Jackson框架进行JSON处理,减少JSONObject的直接暴露
  - 为输入/输出参数定义明确的Java POJO类
  - 将VO层的`JSONObject`字段替换为具体类型
  - 统一使用Jackson进行序列化/反序列化
- **代码简化**:
  - 移除冗余的JSON转换逻辑
  - 集中数据转换到Service层
  - 保持Controller和VO层的简洁性
- **保持向后兼容**: JSON API格式不变,仅内部实现优化

## Impact

### 受影响的规范
- `product-method` (新增): 产品方法管理能力规范

### 受影响的代码
- **Controller层**: `ProductMethodController.java` - 移除Assert依赖,简化参数校验
- **Service层**: `MdProductMethodService.java`, `MdProductMethodServiceImpl.java` - 更新Assert引用,优化JSON处理
- **VO层**: `ProductMethodVo.java` - 替换JSONObject为明确类型
- **工具类**: `MethodSpecValidator.java` - 更新Assert引用,优化JSON验证逻辑
- **Model层**: `MdProductMethod.java` - 保持不变(数据库映射层)

### 不受影响的模块
- 产品属性(Property)模块
- 产品事件(Event)模块
- 设备管理模块
- 其他物模型管理模块

变更严格限定在产品方法功能范围内,不扩散到其他模块。

## Migration Strategy

1. **渐进式重构**: 逐步替换,先更新类型定义,再迁移使用方
2. **单元测试覆盖**: 确保重构前后行为一致
3. **零业务影响**: REST API接口签名和JSON格式保持不变
