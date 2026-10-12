# 实施任务清单

## 1. 准备工作
- [ ] 1.1 确认所有依赖的Assert类导入路径
- [ ] 1.2 确认Jackson已经在项目依赖中(已在Tech Stack中)
- [ ] 1.3 创建测试用例覆盖现有功能

## 2. 类型定义重构
- [ ] 2.1 创建`MethodParamDTO`类,替代JSONObject表示方法参数
- [ ] 2.2 创建`MethodParamsWrapper`类,包装输入/输出参数列表
- [ ] 2.3 更新`ProductMethodVo`,替换JSONObject字段为新类型
  - [ ] 2.3.1 替换`inputParams`字段类型
  - [ ] 2.3.2 替换`outputParams`字段类型
  - [ ] 2.3.3 替换`extendConfig`字段类型(使用Map<String, Object>)

## 3. Assert类迁移
- [ ] 3.1 更新`ProductMethodController`
  - [ ] 3.1.1 替换Assert导入语句
  - [ ] 3.1.2 验证Assert方法调用兼容性
- [ ] 3.2 更新`MdProductMethodServiceImpl`
  - [ ] 3.2.1 替换Assert导入语句
  - [ ] 3.2.2 验证Assert方法调用兼容性
- [ ] 3.3 更新`MethodSpecValidator`
  - [ ] 3.3.1 替换Assert导入语句
  - [ ] 3.3.2 验证Assert方法调用兼容性

## 4. JSON处理重构
- [ ] 4.1 更新`MethodSpecValidator`
  - [ ] 4.1.1 移除Fastjson2 JSONObject依赖
  - [ ] 4.1.2 使用新的POJO类进行参数验证
  - [ ] 4.1.3 更新`validateAndNormalize`方法签名
  - [ ] 4.1.4 使用Jackson进行JSON序列化/反序列化
- [ ] 4.2 更新`MdProductMethodServiceImpl`
  - [ ] 4.2.1 更新`convert2ProductMethod`方法,使用Jackson序列化
  - [ ] 4.2.2 更新`convert2ProductMethodVo`方法,使用Jackson反序列化
  - [ ] 4.2.3 移除`parseJsonObject`方法,使用统一的Jackson处理
- [ ] 4.3 更新`ProductMethodController`
  - [ ] 4.3.1 验证请求参数自动绑定到新VO类型
  - [ ] 4.3.2 移除任何显式的JSON转换代码

## 5. 代码简化与优化
- [ ] 5.1 移除MethodSpecValidator中的冗余方法
- [ ] 5.2 统一异常处理和错误信息
- [ ] 5.3 添加必要的JavaDoc注释(中文)
- [ ] 5.4 确保常量定义使用枚举或常量类

## 6. 测试与验证
- [ ] 6.1 运行单元测试,确保所有测试通过
- [ ] 6.2 手动测试REST API接口
  - [ ] 6.2.1 创建产品方法
  - [ ] 6.2.2 更新产品方法
  - [ ] 6.2.3 查询产品方法详情
  - [ ] 6.2.4 查询产品方法列表
  - [ ] 6.2.5 删除产品方法
- [ ] 6.3 验证JSON序列化/反序列化格式保持不变
- [ ] 6.4 验证参数校验逻辑正确性

## 7. 代码审查与清理
- [ ] 7.1 检查是否有遗漏的旧Assert引用
- [ ] 7.2 检查是否有遗漏的JSONObject直接暴露
- [ ] 7.3 代码格式化和风格检查
- [ ] 7.4 确认没有引入新的技术债务

## 验收标准

- ✅ 所有产品方法相关类使用新的平台Assert
- ✅ VO层不直接暴露JSONObject(除非必要的Map<String,Object>)
- ✅ 所有JSON处理统一使用Jackson
- ✅ REST API接口行为和JSON格式保持不变
- ✅ 代码简洁,类型安全,易于维护
- ✅ 所有测试通过,无回归问题
