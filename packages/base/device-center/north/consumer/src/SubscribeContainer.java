package com.guhecloud.cube.iot.north.bridge.consumer;

import com.guhecloud.cube.iot.north.bridge.consumer.filter.FilterableDeviceEventMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.filter.FilterableDevicePropMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.filter.FilterableDeviceStatusChangeHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.CommandResponseMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceEventAdditionalMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceEventMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DevicePropMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceStatusHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.MsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.RuleEventMsgHandler;

import java.util.Collections;
import java.util.List;

/**
 * 消息订阅容器接口。
 * <p>
 * 提供统一的消息处理器注册入口，支持多种类型的设备消息订阅，包括：
 * <ul>
 *   <li>设备属性上报消息</li>
 *   <li>设备事件消息</li>
 *   <li>设备命令响应消息</li>
 *   <li>设备状态变更消息</li>
 *   <li>规则触发事件消息</li>
 * </ul>
 * 同时提供便捷的过滤器构建方法，支持按设备ID、属性code、事件code等维度进行消息过滤。
 */
public interface SubscribeContainer {

    /**
     * 注册消息处理器列表。
     *
     * @param msgHandlers 消息处理器列表
     */
    void registerMsgHandlers(List<MsgHandler> msgHandlers);

    /**
     * 便捷入口：创建设备属性过滤Builder。
     * <p>
     * 用于构建支持按设备ID和属性code过滤的属性消息处理器。
     *
     * @return 设备属性过滤器构建器
     */
    default FilterableDevicePropMsgHandler.Builder devicePropFilter() {
        return FilterableDevicePropMsgHandler.builder();
    }

    /**
     * 便捷入口：创建设备事件过滤Builder。
     * <p>
     * 用于构建支持按设备ID和事件code过滤的事件消息处理器。
     *
     * @return 设备事件过滤器构建器
     */
    default FilterableDeviceEventMsgHandler.Builder deviceEventFilter() {
        return FilterableDeviceEventMsgHandler.builder();
    }

    /**
     * 便捷入口：创建设备状态变更过滤Builder。
     * <p>
     * 用于订阅设备在线/离线状态变更，只有状态真正发生变化时才触发回调。
     *
     * @return 设备状态变更过滤器构建器
     */
    default FilterableDeviceStatusChangeHandler.Builder deviceStatusChangeFilter() {
        return FilterableDeviceStatusChangeHandler.builder();
    }

    /**
     * 注册设备属性消息处理器。
     *
     * @param msgHandler 设备属性消息处理器
     */
    default void registerDevicePropMsgHandler(DevicePropMsgHandler msgHandler) {
        registerMsgHandlers(Collections.singletonList(msgHandler));
    }

    /**
     * 注册可过滤的设备属性消息处理器。
     * <p>
     * 支持按设备ID和属性code进行过滤。
     *
     * @param msgHandler 可过滤的设备属性消息处理器
     */
    default void registerDevicePropMsgHandler(FilterableDevicePropMsgHandler msgHandler) {
        registerMsgHandlers(Collections.singletonList(msgHandler));
    }

    /**
     * 注册设备状态消息处理器。
     *
     * @param msgHandler 设备状态消息处理器
     */
    default void registerDeviceStatusMsgHandler(DeviceStatusHandler msgHandler) {
        registerMsgHandlers(Collections.singletonList(msgHandler));
    }

    /**
     * 注册设备状态变更消息处理器。
     * <p>
     * 用于订阅设备在线/离线状态变更。
     *
     * @param msgHandler 设备状态变更处理器
     */
    default void registerDeviceStatusChangeMsgHandler(FilterableDeviceStatusChangeHandler msgHandler) {
        registerMsgHandlers(Collections.singletonList(msgHandler));
    }

    /**
     * 注册设备事件消息处理器。
     *
     * @param msgHandler 设备事件消息处理器
     */
    default void registerDeviceEventMsgHandler(DeviceEventMsgHandler msgHandler) {
        registerMsgHandlers(Collections.singletonList(msgHandler));
    }

    /**
     * 注册可过滤的设备事件消息处理器。
     * <p>
     * 支持按设备ID和事件code进行过滤。
     *
     * @param msgHandler 可过滤的设备事件消息处理器
     */
    default void registerDeviceEventMsgHandler(FilterableDeviceEventMsgHandler msgHandler) {
        registerMsgHandlers(Collections.singletonList(msgHandler));
    }

    /**
     * 注册设备事件附加消息处理器。
     *
     * @param msgHandler 设备事件附加消息处理器
     */
    default void registerDeviceEventAdditionalMsgHandler(DeviceEventAdditionalMsgHandler msgHandler) {
        registerMsgHandlers(Collections.singletonList(msgHandler));
    }

    /**
     * 注册设备命令响应消息处理器。
     *
     * @param msgHandler 设备命令响应消息处理器
     */
    default void registerDeviceCommandMsgHandler(CommandResponseMsgHandler msgHandler) {
        registerMsgHandlers(Collections.singletonList(msgHandler));
    }

    /**
     * 注册规则触发事件消息处理器。
     *
     * @param msgHandler 规则触发事件消息处理器
     */
    default void registerRuleTriggerEventMsgHandler(RuleEventMsgHandler msgHandler) {
        registerMsgHandlers(Collections.singletonList(msgHandler));
    }

}
