# Project Context

## Purpose
物模型（Product Model）服务是 Cube2 设备中心的核心子模块，负责管理IoT设备的物理模型定义，包括：
- **产品管理**：定义IoT设备产品的基本信息、品类、品牌、型号等
- **属性管理**：定义设备的属性（properties），如温度、湿度等数据点
- **事件管理**：定义设备事件（events），如告警、状态变化等
- **方法管理**：定义设备方法（methods），如远程控制指令
- **物模型版本管理**：支持物模型的版本快照，便于版本对比和回滚
- **导入导出**：支持物模型的JSON格式导入导出，便于跨环境迁移

本模块为设备中心的其他模块（设备管理、数据采集、告警等）提供设备元数据支持。

## Tech Stack
- **Java 17+**：主要开发语言
- **Spring Boot 3.x**：应用框架
- **Spring Cloud**：微服务框架（Feign、Nacos等）
- **MyBatis Plus 3.x**：ORM框架，简化数据库操作
- **MySQL 8.0+**：关系型数据库
- **Redis**：缓存和分布式锁
- **RocketMQ**：消息队列
- **Bazel**：构建工具
- **Liquibase**：数据库版本管理
- **Lombok**：减少样板代码
- **Jackson**：JSON序列化/反序列化
- **Hibernate Validator**：参数校验
- **Guava**：Google工具库
- **Nacos**：服务注册与配置中心

## Project Conventions

### Code Style
1. **命名规范**：
    - 类名：大驼峰（PascalCase），如 `MdProduct`、`ProductServiceImpl`
    - 变量/方法名：小驼峰（camelCase），如 `prodName`、`getUserById`
    - 常量：全大写下划线分隔，如 `DEFAULT_STATUS`
    - 包名：全小写，如 `com.guhecloud.cube.iot.mgt.base`
    - 数据库表名：小写下划线分隔，格式 `{module}_{entity}`，如 `iot_md_product`
    - 数据库字段名：小写下划线分隔，如 `prod_name`、`create_time`

2. **类命名约定**：
    - 实体类：`{Entity}` 或 `{Entity}PO`，如 `MdProduct`
    - DTO：`{Entity}DTO`
    - VO：`{Entity}VO`
    - Param：`{Entity}{Action}Param`，如 `ProductPageParam`
    - Service接口：`{Entity}Service`
    - Service实现：`{Entity}ServiceImpl`
    - Mapper：`{Entity}Mapper`
    - Controller：`{Entity}Controller`

3. **一个文件仅一个public类**：严格遵循Java规范，每个.java文件只包含一个public类

4. **注释规范**：
    - 所有注释、文档和说明必须使用中文
    - 类和公共方法必须有JavaDoc注释
    - 复杂逻辑需要添加行内注释说明
    - 数据库表和字段必须有COMMENT说明

5. **代码格式**：
    - 使用Lombok简化getter/setter/toString等代码
    - 依赖注入优先使用构造器注入（`@RequiredArgsConstructor`）
    - 使用Stream API进行集合操作
    - 使用`@Slf4j`进行日志记录

### Architecture Patterns
1. **分层架构**（从上到下）：
    - **Controller层**：REST API接口，负责接收HTTP请求、参数校验、调用Service、返回响应
    - **Service层**：业务逻辑层，负责事务管理、业务逻辑实现、调用Mapper
    - **Mapper层**：数据访问层，使用MyBatis Plus，只负责数据库操作
    - **Model层**：数据模型，包括Entity（PO）、DTO、VO、Param等

2. **标准目录结构**：
   ```
   src/v2/
   ├── controller/      # REST控制器
   ├── server/          # 业务服务（Service）
   │   └── impl/       # Service实现
   ├── mapper/          # MyBatis Mapper
   ├── model/           # 数据实体（PO）
   ├── param/           # 请求参数
   ├── vo/              # 视图对象（返回前端）
   ├── dto/             # 数据传输对象
   └── ...
   ```

3. **设计模式应用**：
    - **策略模式**：不同的数据类型处理策略
    - **模板方法模式**：抽象通用的业务流程
    - **工厂模式**：创建不同类型的对象
    - **建造者模式**：使用Lombok的`@Builder`构建复杂对象

4. **依赖原则**：
    - Controller只依赖Service，不直接调用Mapper
    - Service可以依赖其他Service和Mapper
    - 跨模块调用使用Feign Client
    - 避免循环依赖

5. **事务管理**：
    - 事务在Service层使用`@Transactional(rollbackFor = Exception.class)`
    - 避免在事务中进行远程调用或长时间操作
    - 复杂业务使用事件驱动（Spring Event或RocketMQ）

### Testing Strategy
1. **单元测试**：
    - 使用JUnit 5和Spring Boot Test
    - Service层必须编写单元测试
    - 测试覆盖率目标：核心业务逻辑 > 80%

2. **集成测试**：
    - 使用`@SpringBootTest`进行集成测试
    - 测试REST API接口
    - 测试数据库事务

3. **测试命名**：
    - 测试类：`{Class}Test`
    - 测试方法：`test{MethodName}_{Scenario}`

### Git Workflow
1. **分支策略**：
    - `main`：主分支，生产环境代码
    - `develop`：开发分支
    - `feature/*`：功能分支，如 `feature/add-product-export`
    - `bugfix/*`：bug修复分支
    - `hotfix/*`：紧急修复分支

2. **Commit规范**：
    - 格式：`<type>(<scope>): <subject>`
    - type类型：
        - `feat`：新功能
        - `fix`：bug修复
        - `refactor`：代码重构
        - `docs`：文档更新
        - `style`：代码格式调整
        - `test`：测试相关
        - `chore`：构建/工具相关
    - 示例：`feat(product-model): 添加物模型导出功能`

## Domain Context

### 物模型核心概念
1. **产品（Product）**：
    - IoT设备的抽象模型，定义设备的类型、品牌、型号等
    - 一个产品可以对应多个实际设备实例
    - 关键字段：`prod_number`（产品编号，唯一）、`prod_name`、`prod_category_id`、`node_type`

2. **节点类型（Node Type）**：
    - 1：直连设备（直接连接到平台）
    - 2：网关设备（可以管理子设备）
    - 3：网关子设备（通过网关连接）

3. **物模型属性（Property）**：
    - 设备的数据点，如温度、湿度、电量等
    - 支持的数据类型：int、long、float、double、string、boolean、date、enum等
    - 关键字段：`prop_code`（属性编码）、`data_type`、`data_config`（数据配置，JSON格式）
    - `data_write`：是否可写（false=只读属性，true=可读写属性）

4. **物模型事件（Event）**：
    - 设备上报的事件，如告警、状态变化等
    - 关键字段：`event_code`（事件编码）、`category_name`（事件类别）、`config`（事件配置，JSON格式）

5. **物模型方法（Method）**：
    - 设备支持的远程调用方法/指令
    - 关键字段：`method_code`（方法编码）、`method_type`（同步/异步）、`input_params`（输入参数）、`output_params`（输出参数）

6. **物模型版本**：
    - 每次修改物模型时，都会生成一个完整的JSON快照
    - JSON包含：产品信息 + 所有属性 + 所有事件 + 所有方法
    - 版本快照便于版本对比、回滚和跨环境迁移

### 业务规则
1. **多租户隔离**：所有表都有`tenant`字段，数据按租户隔离
2. **逻辑删除**：使用`deleted`字段标记删除，不物理删除数据
3. **审计字段**：所有表包含`create_time`、`update_time`、`create_by`、`update_by`
4. **唯一性约束**：
    - 产品编号`prod_number`全局唯一
    - 属性编码`prop_code`在同一产品内唯一
    - 事件编码`event_code`在同一产品内唯一
    - 方法编码`method_code`在同一产品内唯一

### 物模型JSON格式
- 自描述的JSON格式，包含完整的物模型快照
- **屏蔽ID字段**：不包含数据库自增ID，便于跨环境迁移
- **使用业务code**：通过`prod_number`、`prop_code`、`event_code`等业务编码标识实体
- **字典字段用name替代code**：使用`prod_category_name`而非`prod_category_code`，导入时动态查询匹配
- **嵌套结构**：properties、events、methods直接嵌套在product下
- 详见：`docs/物模型版本JSON格式示例.md`

## Important Constraints

0. **总体约束**：
    - 参考[代码规范与最佳实践.md](../docs/%E4%BB%A3%E7%A0%81%E8%A7%84%E8%8C%83%E4%B8%8E%E6%9C%80%E4%BD%B3%E5%AE%9E%E8%B7%B5.md) 代码规范与最佳实践.md
    - 代码设计遵循简洁、高质量、可维护的原则。
    - 优先使用枚举（Enum）来表示固定集合的常量。
    - 为所有方法添加清晰的中文注释。
    - Java 文件遵循单一 `public class` 规范。
    - 默认不提供单元测试、工作量评估和性能分析，除非用户明确要求。

1. **技术约束**：
    - Java版本：必须使用Java 17或更高版本
    - Spring Boot版本：3.x
    - 数据库：MySQL 8.0+，字符集utf8mb4
    - 构建工具：Bazel（不使用Maven或Gradle）

2. **业务约束**：
    - 多租户系统：所有数据操作必须带租户过滤
    - 逻辑删除：禁止物理删除数据，必须使用逻辑删除
    - 数据隔离：不同租户的数据严格隔离
    - 版本管理：物模型的修改必须生成版本快照

3. **性能约束**：
    - 分页查询：列表查询必须分页，避免一次性加载大量数据
    - 缓存策略：高频查询的字典数据需要缓存
    - 索引优化：频繁查询的字段必须建立索引

4. **安全约束**：
    - SQL注入防护：必须使用参数化查询
    - 密码加密：敏感信息必须加密存储
    - 权限校验：所有接口必须进行权限验证
    - 数据脱敏：日志中不能输出敏感信息

5. **代码规范约束**：
    - 一个Java文件只能有一个public类
    - 所有注释和文档必须使用中文
    - 代码保持简洁，不做过度设计
    - 遵循团队的代码规范和最佳实践（详见`docs/代码规范与最佳实践.md`）

## External Dependencies

1. **内部依赖**：
    - `platform/base`：平台基础工具库（工具类、常量、标准接口）
    - `platform/database`：数据库访问框架（MyBatis Plus增强）
    - `platform/security`：安全认证框架（JWT、Token管理）
    - `platform/redis`：Redis缓存支持
    - `platform/rocketmq`：RocketMQ消息队列
    - `platform/event`：事件驱动框架
    - `packages/base/device-center/mgt`：设备管理模块
    - `packages/base/device-center/north/consumer`：北向接口消费者
    - `packages/base/file/client`：文件管理服务
    - `packages/base/ouaa/client`：用户认证授权服务

2. **外部依赖**：
    - **数据库**：MySQL 8.0+（主存储）
    - **缓存**：Redis（缓存、分布式锁）
    - **消息队列**：RocketMQ（异步消息、事件驱动）
    - **注册中心**：Nacos（服务注册、配置管理）
    - **监控**：XXL-Job（定时任务）

3. **第三方库**：
    - MyBatis Plus：简化MyBatis操作
    - Lombok：减少样板代码
    - Jackson：JSON处理
    - Guava：Google工具库
    - Hibernate Validator：参数校验
    - EasyExcel / EasyPoi：Excel导入导出
    - Fastjson2：JSON处理（阿里巴巴）

4. **开发工具**：
    - Bazel：构建工具
    - Liquibase：数据库版本管理
    - Git：版本控制
    - OpenSpec：变更管理工具（用于规范化的变更提案和实施）

## Additional Notes

1. **项目位置**：
    - 完整路径：`packages/base/device-center/product-model`
    - 所属模块：设备中心（device-center）的子模块
    - 层级定位：Base层（基础业务层）

2. **相关文档**：
    - 系统架构：`docs/系统架构设计.md`
    - 代码规范：`docs/代码规范与最佳实践.md`
    - 物模型JSON格式：`docs/物模型版本JSON格式示例.md`
    - OpenSpec指引：`openspec/AGENTS.md`

3. **开发建议**：
    - 新功能开发前，建议先查看现有代码结构和命名规范
    - 涉及重大变更时，使用OpenSpec创建变更提案（`/openspec:proposal`）
    - 保持代码简洁，避免过度设计
    - 遵循现有的架构模式和代码风格
