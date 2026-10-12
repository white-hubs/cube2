# device-center/mgt/north 下行命令逻辑文档

> 整理范围：`packages/base/device-center/mgt/src/north` 及相关核心模块  
> 更新时间：2026-04-01

---

## 一、总体架构

所有下行命令均通过 **`SouthInnerBus`** 投递，底层实现为 **RocketMQ**。

```
HTTP API (north/controller)
        │
        ▼
   Service 层 (north/server/impl)
        │
        │  ① 前置校验（设备状态、属性可写性、方法有效性等）
        │  ② 写入命令日志三件套:
        │       CommandInfoService.startCmd()         → iot_command_info
        │       CommandRecordService.startCmdRecord() → iot_command_record
        │       CommandLogService.logCmd()            → iot_command_log
        │  ③ 构建消息体
        ▼
   SouthInnerBus.process(msg)
        │
        ▼  RocketMQ
   iot-core-access-runtime (南向接入运行时)
```

---

## 二、SouthInnerBus 接口总览

**接口路径**：`com.guhecloud.cube.iot.mgt.core.innerbus.SouthInnerBus`  
**实现类**：`SouthInnerBusImpl`（基于 `RocketMQContextHolder.send(topic, msg)`）

| 方法签名 | MQ Topic | 说明 |
|---|---|---|
| `process(PreDevicePropSetMsg)` | `TO_IOT_RUNTIME_PRE_DEVICE_PROP_SET` | 属性设置命令 |
| `process(PreDeviceResetMsg)` | `TO_IOT_RUNTIME_PRE_DEVICE_RESET` | 设备重置命令（属性/事件） |
| `process(DeviceInfoSyncMsg)` | `TO_IOT_RUNTIME_DEVICE_INFO_SYNC` | 设备信息同步命令 |
| `process(MockPropReport)` | `TO_IOT_RUNTIME_DEV_PROP_MOCK_REPORT` | 模拟属性上报 |
| `process(MockDevEventReport)` | `TO_IOT_RUNTIME_DEV_EVENT_MOCK_REPORT` | 模拟事件上报 |
| `process(PreDeviceMethodInvokeMsg)` | `TO_IOT_RUNTIME_METHOD_INVOKE` | 方法调用命令 |
| `deviceRemove(List<DeviceRemove>)` | `TO_IOT_RUNTIME_DEVICE_REMOVE` | 设备删除通知 |

---

## 三、命令类型定义

**枚举**：`DownCmd`

| 枚举值 | code | 说明 |
|---|---|---|
| `DEV_PROP_SET` | DEV_PROP_SET | 设备属性设置 |
| `DEV_PROP_RESET` | DEV_PROP_RESET | 设备属性重置 |
| `DEV_EVENT_RESET` | DEV_EVENT_RESET | 设备事件重置 |
| `DEV_INFO_SYNC` | DEV_INFO_SYNC | 设备信息同步 |
| `DEV_LINKAGE_TASK_PROP_DOWN` | DEV_LINKAGE_TASK_PROP_DOWN | 联动任务属性下发 |
| `METHOD_INVOKE` | METHOD_INVOKE | 设备方法调用 |

---

## 四、各下行命令详细逻辑

---

### 4.1 属性设置命令 `DEV_PROP_SET`

**触发入口**：  
- `POST /north/device/command/issue` — 单条属性设置  
- `POST /north/device/command/issue/batch` — 批量属性设置

**Service**：`NorthCommandService.commandIssue(CommandRequest)`

#### 请求参数 `CommandRequest`

| 字段 | 类型 | 说明 |
|---|---|---|
| `commandSeq` | String | 命令序列号（可选，空时系统自动生成雪花 ID） |
| `deviceId` | String | 设备 ID |
| `propCode` | String | 属性 Code |
| `propValue` | String | 属性值 |

#### 处理流程

```
1. 查询设备摘要信息 DeviceDigestVo（缓存）
2. 前置校验：
   a. 设备状态必须不为 NO_ACTIVE / DISABLED
   b. 属性存在性检查（isDeviceEventThreshold 的特殊属性跳过此检查）
   c. 属性可写性检查（dataWrite != 0）
3. 生成 commandSeq（若未提供）
4. 写入命令日志三件套：
   - CommandInfo:   DownCmd.DEV_PROP_SET, translatedContent
   - CommandRecord: commandSeq, translatedContent, rawCmdContent
   - CommandLog:    "消息投递到物联平台"
5. 构建 PreDevicePropSetMsg 并投递到 SouthInnerBus
6. 返回 CommandResponse（状态 PROCESSING）
```

#### 消息体 `PreDevicePropSetMsg`

| 字段 | 说明 |
|---|---|
| `commandSeq` | 命令序列号 |
| `instanceId` | 实例 ID（等于 commandSeq） |
| `tenant` | 租户 ID |
| `deviceId` | 设备 ID |
| `deviceName` | 设备名称 |
| `deviceNumber` | 设备编号 |
| `parentDeviceId` | 父设备 ID（可空） |
| `parentDeviceNumber` | 父设备编号（可空） |
| `propCode` | 属性 Code |
| `propValue` | 属性值（由 PreDevicePropSetMsgHandler 在 runtime 侧做值规范化） |
| `ts` | 时间戳 |

---

### 4.2 属性重置命令 `DEV_PROP_RESET`

**触发入口**：  
- `POST /north/device/command/reset/issues`（`commandResetEnum = RESET_DEVICE_PROP`）

**Service**：`NorthCommandService.commandDeviceResetIssue(CommandRestParam)`

#### 请求参数 `CommandRestParam`

| 字段 | 类型 | 说明 |
|---|---|---|
| `commandSeq` | String | 命令序列号（可选） |
| `deviceId` | String | 设备 ID |
| `commandResetEnum` | `CommandResetEnum` | `RESET_DEVICE_PROP` |
| `resetTarget` | String | 要重置的属性 Code |

#### 处理流程

```
1. 查询设备摘要信息及设备状态校验
2. 查询属性缓存，获取属性默认值 defaultValue
3. 若属性不存在，返回 FAIL
4. 写入命令日志三件套（DownCmd.DEV_PROP_RESET）
5. 构建 PreDeviceResetMsg（commandResetEnum=RESET_DEVICE_PROP, resetValue=defaultValue）
6. 投递 SouthInnerBus
7. 返回 CommandResponse（状态 PROCESSING）
```

---

### 4.3 事件重置命令 `DEV_EVENT_RESET`

**触发入口**：  
- `POST /north/device/command/reset/issues`（`commandResetEnum = RESET_DEVICE_EVENT`）

**Service**：`NorthCommandService.commandDeviceResetIssue(CommandRestParam)`

#### 特殊逻辑（事件规格判断）

```
1. 查询产品事件定义 MdProductEvent
2. 若事件不存在 → 返回 FAIL
3. 判断事件触发类型 TriggerType：
   ├── TriggerType.SIMPLE（基于单属性阈值的模拟事件）：
   │     取出触发属性 propCode 及属性默认值
   │     → 转为 RESET_DEVICE_PROP 属性重置下发
   └── 其他类型：
         → 按 RESET_DEVICE_EVENT 下发原生事件重置
4. 写入命令日志三件套
5. 构建 PreDeviceResetMsg 投递 SouthInnerBus
```

#### 消息体 `PreDeviceResetMsg`

| 字段 | 说明 |
|---|---|
| `commandSeq` | 命令序列号 |
| `instanceId` | 实例 ID |
| `tenantId` | 租户 ID |
| `deviceId/deviceName/deviceNumber` | 设备信息 |
| `parentDeviceId/parentDeviceNumber` | 父设备信息 |
| `prodId` | 产品 ID |
| `protocolId` | 协议 ID |
| `commandResetEnum` | `RESET_DEVICE_PROP` 或 `RESET_DEVICE_EVENT` |
| `resetTarget` | 属性 Code 或事件 Code |
| `resetValue` | 重置值（属性重置时为 defaultValue，事件重置为 null） |

---

### 4.4 方法调用命令 `METHOD_INVOKE`

**触发入口**：  
- `POST /north/product-method/invoke` — 单设备方法调用  
- `POST /north/product-method/invoke/batch` — 批量方法调用

**Service**：`NorthProductMethodService.invokeMethod(MethodInvokeRequest)`

#### 请求参数 `MethodInvokeRequest`

| 字段 | 类型 | 说明 |
|---|---|---|
| `commandSeq` | String | 命令序列号（可选） |
| `deviceId` | String | 设备 ID（必填） |
| `methodCode` | String | 方法 Code（必填） |
| `inputParams` | `Map<String, Object>` | 输入参数（JSON 格式） |

#### 处理流程

```
1. 查询设备摘要信息 + 设备状态校验
2. 通过 methodCode + prodId 查询 MdProductMethod
3. 查询方法详情 ProductMethodMgtVo
4. 校验：
   a. 方法必须属于该设备的产品（prodId 匹配）
   b. 方法为启用状态（status != 0）
5. 使用 MethodSpecValidator 校验 inputParams（基于 DB 方法规格定义）
   → 校验失败立即返回 REJECT，不写入命令日志
6. 生成 commandSeq
7. 写入命令日志三件套（DownCmd.METHOD_INVOKE）
8. 构建 PreDeviceMethodInvokeMsg 并投递 SouthInnerBus
9. 返回 MethodInvokeResponse（状态 PROCESSING）
```

#### 消息体 `PreDeviceMethodInvokeMsg`

| 字段 | 说明 |
|---|---|
| `commandSeq/instanceId` | 命令标识 |
| `tenantId` | 租户 ID |
| `deviceId/deviceName/deviceNumber` | 设备信息 |
| `parentDeviceId/parentDeviceNumber` | 父设备信息 |
| `prodId` | 产品 ID |
| `protocolId` | 协议 ID |
| `methodCode` | 方法编码 |
| `methodName` | 方法名称 |
| `methodType` | 方法类型（sync/async） |
| `inputParams` | 输入参数 Map |
| `timeoutMs` | 超时时间（毫秒，来自方法定义） |
| `ts` | 时间戳 |

#### 状态查询

`GET /north/product-method/invoke-status?instanceId=xxx`  
→ `CommandRecordMapper.getMethodInvokeStatus(instanceId, "METHOD_INVOKE")`  
→ 返回 `MethodRawCmdContent`（包含原始命令内容和输出参数，outputParams 由外部程序补充）

---

### 4.5 设备信息同步命令 `DEV_INFO_SYNC`

**触发场景**：设备编码或名称批量修改时，需同步通知南向 iot-runtime

**触发入口**（非 north 直接对外，由内部业务调用）：  
- `DevDeviceServiceImpl.batchUpdate(BatchUpdateDeviceParam)` — 批量修改设备信息

#### 处理流程

```
1. 执行数据库更新（devDeviceMapper.batchUpdate）
2. 清空 Redis 中设备属性缓存和状态缓存
3. 发布 IOT_DEVICE_NUMBER_NAME_CHANGE 内部消息
4. 按父设备（网关/直连设备）分组构建 DeviceInfoSyncMsg
5. 写入命令日志三件套（DownCmd.DEV_INFO_SYNC）
   → 每组一个 instanceId，共享同一个 cmdSeq
6. 逐组投递 SouthInnerBus
```

#### 消息体 `DeviceInfoSyncMsg`

| 字段 | 说明 |
|---|---|
| `cmdSeq` | 命令任务 ID（批次共享） |
| `instanceId` | 本次同步实例 ID |
| `tenantId` | 租户 ID |
| `syncSelfInfo` | 网关/直连设备的同步信息 |
| `syncChildInfo` | 子设备同步信息列表 |

每个 `DeviceInfo` 包含：`devNum`（旧编号）、`devId`、`edgeDevIdentify`、`config.newDevNum`、`config.newDevName`

---

### 4.6 模拟设备属性上报 `MockPropReport`

> 注意：此命令为**上行模拟**，不是真正的下行命令，但均通过 SouthInnerBus 投递。

**触发入口**：`POST /public/device/mock/prop`

**Controller**：`DeviceMockPropController.mockProp(MockPropReport)`

#### 处理流程

```
1. 通过 deviceId 查询设备 DeviceDigestVo（缓存，不存在则返回 false）
2. 从缓存补齐设备信息（deviceNumber, parentDeviceId, productId, tenantId）
3. 直接投递 SouthInnerBus（不写命令日志）
```

---

### 4.7 模拟设备事件上报 `MockDevEventReport`

> 注意：同 4.6，为上行模拟，通过 SouthInnerBus 投递。

**触发入口**：`POST /public/device/mock/event`

**Controller**：`DeviceMockPropController.mockEvent(MockDevEventReport)`

#### 处理流程

```
1. 通过 deviceId 查询设备 DeviceDigestVo（缓存，不存在则返回 false）
2. 从缓存补齐（productId, tenantId）
3. 直接投递 SouthInnerBus（不写命令日志）
```

---

### 4.8 设备删除通知 `deviceRemove`

**触发场景**：设备批量删除时通知 iot-runtime 清理南向资源

**触发入口**：`DevDeviceServiceImpl.batchDel(List<Long>)`

#### 处理流程

```
1. 逐设备软删除及 Redis 缓存清理
2. 收集所有非网关设备的 DeviceRemove 对象
3. 所有批次数据库操作完成后，统一调用 southInnerBus.deviceRemove(allDeviceRemoves)
4. SouthInnerBus 封装为 DeviceOperate{operate="del", deviceRemoves} 发送 MQ
```

---

## 五、命令状态查询

| 查询接口 | Service 方法 | 说明 |
|---|---|---|
| `GET /north/device/command/issue-status?instanceId=xxx` | `NorthCommandService.commandStatus(instanceId)` | 查询属性设置/重置命令状态 |
| `GET /north/product-method/invoke-status?instanceId=xxx` | `NorthProductMethodService.queryInvokeStatus(instanceId)` | 查询方法调用状态及输入输出参数 |

命令状态枚举 `CommandExecStatusEnum`：

| 状态 | 含义 |
|---|---|
| `PROCESSING` | 执行中（初始投递后状态） |
| `ACCEPT` | 命令已被设备侧接收（异步场景） |
| `SUCCESS` | 命令执行成功 |
| `FAIL` | 命令执行失败 |
| `REJECT` | 命令被拒绝（参数校验失败等） |
| `ERROR` | 系统未找到相关命令记录 |

---

## 六、关键设计要点

1. **属性重置 vs 事件重置的自动转换**：当事件由 `TriggerType.SIMPLE` 的单属性阈值模拟时，事件重置会被自动转换为对应属性的默认值下发（`RESET_DEVICE_PROP`），而非发 `RESET_DEVICE_EVENT`。

2. **命令日志三件套**：所有真实下行命令（DEV_PROP_SET/DEV_PROP_RESET/DEV_EVENT_RESET/METHOD_INVOKE/DEV_INFO_SYNC）在投递前均必须写入三张命令日志表；模拟上报（Mock）和设备删除通知跳过此步骤。

3. **设备状态前置校验**：`NO_ACTIVE`（未激活）和 `DISABLED`（禁用）的设备会直接抛出 `ServiceException`，拒绝所有命令下发。

4. **方法调用参数校验**：`MethodSpecValidator.validateInputParams()` 基于数据库中存储的方法规格定义在 mgt 侧完成，参数校验失败时直接返回 `REJECT`，不写入命令日志，不投递 MQ。

5. **commandSeq 生成策略**：调用方可以指定 `commandSeq`（幂等 Key），若不指定，由系统使用 MyBatis-Plus `IdWorker.getId()` 雪花算法自动生成。

6. **`DEV_LINKAGE_TASK_PROP_DOWN`（联动属性下发）**：该 `DownCmd` 枚举值存在，但当前在 `mgt/north` 层未直接暴露 HTTP 接口，由联动规则运行时（iot-runtime）在联动触发时自行生成，不走 mgt 入口。
