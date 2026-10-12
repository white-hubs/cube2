# Device Center North API: Consumer SDK 开发指南

## 1. 概述

本指南面向外部系统开发者，详细介绍了如何集成和使用 **Device Center North Consumer SDK**。该 SDK 提供了一套安全、高效的底层消息订阅与分发机制，基于 RabbitMQ 为第三方业务系统提供设备全生命周期数据的一手推流。它支持灵活的 API 和各种内置的过滤能力，帮助外部应用轻松捕获和处理大量设备消息。

通过该 SDK，您的业务系统能够轻松订阅以下核心场景的消息：

* **设备属性更新**：设备实时上报的遥测数据和属性。
* **设备状态变更**：设备在线、离线状态的感知及其变化。
* **设备事件告警**：设备上报的业务事件与故障告警。
* **下行命令响应**：云端或业务下发控制指令后的执行结果与响应。
* **规则引擎触发**：设备数据满足特定预设规则后的联动事件触发。

---

## 2. 快速开始

### 2.1 引入依赖

请在您的项目（例如 Maven 或 Gradle 构建系统）中引入相关坐标。由于 SDK 面向外部开发发布，版本号请以您获取的最新发版坐标为准：

```xml
<dependency>
    <groupId>com.guhecloud.cube</groupId>
    <artifactId>device-center-north-consumer</artifactId>
    <version>${version}</version>
</dependency>
```

### 2.2 基础配置

在对接 SDK 之前，外部系统需要准备一套连接到指定 RabbitMQ Broker 的权限配置。以 Spring Boot 项目为例：

```java
import com.guhecloud.cube.iot.north.bridge.consumer.cfg.RabbitSubscribeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IotConsumerConfig {

    @Bean
    public RabbitSubscribeConfig rabbitSubscribeConfig() {
        RabbitSubscribeConfig config = new RabbitSubscribeConfig();
        // 外部消费组标识，必须唯一且具备辨识度。这是 RabbitMQ 生成专属队列的核心后缀。
        config.setConsumerGroup("external_business_system_a");
        config.setTaskQueueSize(1000); // 并发处理任务队列数量，按需调整
        return config;
    }
}
```

### 2.3 初始化订阅容器

SDK 提供了一个基于 RabbitMQ 协议的核心底层通信容器 `RabbitMqSubscribeContainer`，您需要将相关配置注入到容器并启动它。

```java
import com.guhecloud.cube.iot.north.bridge.consumer.RabbitMqSubscribeContainer;
import com.guhecloud.cube.iot.north.bridge.consumer.cfg.SubscribeConfig;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;

@Bean
public RabbitMqSubscribeContainer rabbitMqSubscribeContainer(ConnectionFactory connectionFactory, SubscribeConfig subscribeConfig) {
    // connectionFactory 由 Spring AMQP 提供或由调用方自行创建
    // 容器启动后会自动连接底层设备中心，开始探测各类消息事件
    return new RabbitMqSubscribeContainer(connectionFactory, subscribeConfig);
}
```

---

## 3. 核心 API：消息处理器与注册

SDK 提供了一个统一接口 `SubscribeContainer`，外部应用只需向容器中注册自己关心的 `MsgHandler`（消息处理器接口的实现类）即可完成订阅，所有序列化及并发处理细节均由底层接管。

### 3.1 监听设备属性更新

设备上报最新的环境温度、电量、工作参数等数据时，会通过该通道分发。

**消息模型**：`LivePropertyUpdateMsg`

* `deviceId`: 设备唯一标识。
* `updates`: 属性键值对变化列表，具体结构为 `KeyValueWithTs`，包含了变化的 `key`、新值 `value`、旧值 `preValue` 以及时间戳等。

**基本用法示例：**

```java
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DevicePropMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.LivePropertyUpdateMsg;

public class BasicPropHandler implements DevicePropMsgHandler {
    @Override
    public void handle(LivePropertyUpdateMsg msg) {
        System.out.println("收到设备属性更新：" + msg.getDeviceId());
        msg.getUpdates().forEach(kv -> {
             if (kv.isChanged()) {
                 System.out.printf("属性 [%s] 发生变化：%s -> %s%n", kv.getKey(), kv.getPreValue(), kv.getValue());
             }
        });
    }
}

// 注册到容器中：
// container.registerDevicePropMsgHandler(new BasicPropHandler());
```

### 3.2 监听设备事件触发

设备端或边缘端检测到告警或行为并向上触发事件时，会推送本类型消息。部分复杂事件通过附加消息（AdditionalMsg）传递。

**消息模型**：`DeviceEventTriggerMsg`

* `deviceId`: 设备唯一标识。
* `eventCode`: 事件标识符，例如 `power_failure`, `motion_detected` 等。
* `timestamp`: 事件实际触发时间戳。
* `relatedProp`: 关联的历史关键属性快照。
* `params`: 该事件本身附带的额外参数（Map结构）。

```java
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceEventMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceEventTriggerMsg;

public class MyEventAlarmHandler implements DeviceEventMsgHandler {
    @Override
    public void handle(DeviceEventTriggerMsg msg) {
        System.out.printf("设备 [%s] 触发高优事件 [%s]%n", msg.getDeviceId(), msg.getEventCode());
        // 处理事件参数及业务派单逻辑
        System.out.println("事件参数详情: " + msg.getParams());
    }
}
```

### 3.3 监听设备状态变更

在业务场景中，掌握设备的“在线”和“离线”极为关键。你可以直接实现 `DeviceStatusHandler` 来获得设备实时的每一次交互变动快照。

**消息模型**：`DeviceStatusUpdateMsg`

包含了设备标识及状态变化细节。由于底层可能存在短时间内的网络抖动，推荐阅读下方“高级过滤器”部分的【设备状态变更去重过滤器】。

### 3.4 监听下行命令响应

当业务系统或北向客户端主动下发指令给设备时（如：“打开冷气”），需要一套机制得知设备处理结果。

**消息模型**：`DeviceCommandInfoMsg`
包含内部核心模型 `CommandResponse`：

* `statusCode`: 指令反馈状态，如 `ACCEPT`（设备已接管）, `SUCCESS`（执行成功）, `FAIL`（执行失败或拒绝）, `EXPIRED`（通信超市）。
* `message`: 来自边缘端或设备的文字反馈。

```java
import com.guhecloud.cube.iot.north.bridge.consumer.handler.CommandResponseMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceCommandInfoMsg;

public class CmdResponseHandler implements CommandResponseMsgHandler {
    @Override
    public void handle(DeviceCommandInfoMsg msg) {
        System.out.printf("消息标识符 [%s] 指令调用结束, 状态: %s%n", 
            msg.getMsgId(), msg.getCommandResponse().getStatusCode());
    }
}
```

### 3.5 监听规则引擎触发

若平台侧预设了针对特定业务的复杂联动计算策略（数据计算规则），外部系统也能获取计算最终产生的动作触发。

**消息模型**：`RuleTriggerMsg`

* `ruleId` / `ruleNum`：命中的规则标识。
* `matchSnapshot`：命中规则时的条件快照片段。

---

## 4. 高级：事件与属性智能过滤框架（推荐）

为了降低外部系统对海量冗余报文的处理成本，SDK 内置了强悍的【过滤器】。推荐使用 `SubscribeContainer` 提供的一系列便捷静态工厂，它基于流式 Builder 模式，自动过滤不符预期的报文和属性节点。

### 4.1 按设备ID与属性项裁剪 (FilterableDevicePropMsgHandler)

```java
import com.guhecloud.cube.iot.north.bridge.consumer.filter.FilterableDevicePropMsgHandler;

// 推荐的注册方式
FilterableDevicePropMsgHandler advancedPropHandler = container.devicePropFilter()
        .deviceId("device001")                  // 仅在此处限制单一设备
        .propCodes("temperature", "humidity")   // 只接收温湿度数据
        .build(msg -> {
            // SDK已做过自动化裁剪。若这批报文内除了温湿度还有其他字段，其他字段已被剔除，不会传递到业务侧。
            // 且非 device001 的设备数据将被丢弃
            System.out.println("收到被裁剪过的目标核心数据：" + msg);
        });

container.registerDevicePropMsgHandler(advancedPropHandler);
```

### 4.2 运行时过滤条件的动态管理

外部系统由于有自己的业务流程流转，不需要一直关注所有设备。SDK 的高级过滤器具备无缝、无阻塞热更策略，您可以随时在运行时改变策略。

```java
// 在设备完成初始化建档后，动态向过滤队列注入关注要求：
advancedPropHandler.addDeviceId("device002");         // 运行时追加新关注设备
advancedPropHandler.addParamCode("battery_level");    // 增加电量监控

// 甚至支持批量取消订阅，避免内存及网络IO浪费
advancedPropHandler.removeDeviceId("device001");
```

### 4.3 状态去重与变更捕获 (FilterableDeviceStatusChangeHandler)

常规状态机可能因为弱网频繁下发状态报文，SDK 提供自带防抖去重的状态过滤器，**只有在状态产生确切翻转时**（在线变离线 / 离线变在线）才上报业务回调。

```java
import com.guhecloud.cube.iot.north.bridge.consumer.filter.FilterableDeviceStatusChangeHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceOnlineStatusListener;

FilterableDeviceStatusChangeHandler statusHandler = container.deviceStatusChangeFilter()
        // 若不指定 deviceId ，代表感知全部设备的上下线
        .build((deviceId, isOnline) -> {
            String statusDesc = isOnline ? "在线，连接恢复畅通" : "离线，失去连接";
            System.out.println("检测到网络状态反转, 设备 [" + deviceId + "]: " + statusDesc);
        });

container.registerDeviceStatusChangeMsgHandler(statusHandler);
```

---

## 5. 核心实体 (VO) 字典详解

开发中主要关注的数据载体字典，位于 `com.guhecloud.cube.iot.north.bridge.consumer.vo.*`。

### `LivePropertyUpdateMsg`

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| `deviceId` | String | 设备标识码 |
| `updates` | `List<KeyValueWithTs>` | 发生变化的（或心跳上报的）全量/增量属性键值对 |

### `KeyValueWithTs`

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| `key` | String | 属性标识 Code |
| `value` | Object | 新采集的确切数值 |
| `preValue` | Object | 旧有数值，可用于计算变化率 |
| `changed` | boolean | 标识该记录是否有实质性变化 |
| `ts` | Long | 硬件实际产生该数据的毫秒时间戳 |

### `DeviceEventTriggerMsg`

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| `deviceId` | String | 事件来源设备 |
| `eventCode` | String | 事件唯一系统标识 Code |
| `relatedProp` | `Map<String, Object>` | 事件产生时周边随附的关联监测点快照 |
| `params` | `Map<String, Object>` | 本次事件专属独立抛射出来的参数 |
| `timestamp` | String | 事件确认生成时间 |

### `CommandResponse`

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| `commandHandle` | String | 追踪句柄 |
| `statusCode` | String | `SUCCESS`、`FAIL`、`EXPIRED`、`ACCEPT`、`PROCESSING`。 |
| `message` | String | 透传底层硬件处理说明 |

---

## 6. 注意事项与开发建议

1. **唯一消费标识 (`consumerGroup`)**：
   务必确保您的外部系统在生产环境使用全局唯一的标识。如果与其他微服务使用了相同的名称，将会发生**消费竞争**（仅有随机一部服务能拿到某条消息）。

2. **多实例部署**：
   如果您的系统采用多实例集群分布式部署，请保证所有实例的 `consumerGroup` 保持一致，以此来实现分布式环境下的 **负载均衡**，一条数据只会被处理一次。

3. **慎重在回调内部执行阻塞操作**：
   通过 `Handler` 监听到的数据位于共享线程池中执行。当您的业务计算十分耗时（例如重度 IO 持久化、调用大量微服务链条），建议：
   * 将处理行为扔向您自己维护的异步执行池中。
   * 避免在 SDK 回调中进行阻塞，导致消息积压无法继续消费甚至网络断链。
   * 对于使用高级过滤框架 (`Filterable...Handler`) 的情况，**切勿**在触发内部进行针对过滤器的同步阻塞的结构调整。

4. **处理乱序风险**：
   网络环境和 MQ 环境错综复杂，不同设备的同一项参数更新不一定具有严格的时序性传递保障，请结合 `KeyValueWithTs` 提供的内置 `ts` 毫秒时间戳做业务层面的幂等与覆写校验。
