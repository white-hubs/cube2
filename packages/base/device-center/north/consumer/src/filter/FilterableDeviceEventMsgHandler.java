package com.guhecloud.cube.iot.north.bridge.consumer.filter;

import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceEventMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceEventTriggerMsg;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 设备事件消息可过滤Handler。
 * <p>
 * 支持按设备ID与事件code过滤。
 * 特性：
 * <ul>
 *   <li>设备级过滤：仅处理指定设备ID的事件消息</li>
 *   <li>事件级过滤：仅处理指定事件code的消息</li>
 *   <li>运行时动态管理：支持运行时添加/移除设备ID和事件code，无需重新创建Handler</li>
 * </ul>
 * <p>
 * <b>使用示例</b>：
 * <pre>{@code
 * // 初始化时设置基础过滤条件
 * FilterableDeviceEventMsgHandler handler =
 *     FilterableDeviceEventMsgHandler.builder()
 *         .deviceId("device001")
 *         .eventCodes("alarm", "fault")
 *         .build(msg -> handleEvent(msg));
 *
 * // 运行时动态添加设备
 * handler.addDeviceId("device002");
 *
 * // 运行时动态添加事件code
 * handler.addParamCode("warning");
 *
 * // 运行时移除事件code
 * handler.removeParamCode("fault");
 * }</pre>
 */
public class FilterableDeviceEventMsgHandler
        extends AbstractFilterableHandler<DeviceEventTriggerMsg, DeviceEventMsgHandler>
        implements DeviceEventMsgHandler {

    /**
     * 构造函数。
     *
     * @param criteria 过滤条件
     * @param delegate 被委托的原始事件消息处理器
     */
    public FilterableDeviceEventMsgHandler(FilterCriteria criteria, DeviceEventMsgHandler delegate) {
        super(criteria, delegate);
    }

    /**
     * 创建Builder实例。
     *
     * @return Builder实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 从事件消息中提取设备ID。
     *
     * @param msg 事件消息
     * @return 设备ID
     */
    @Override
    protected String extractDeviceId(DeviceEventTriggerMsg msg) {
        return msg != null ? msg.getDeviceId() : null;
    }

    /**
     * 从事件消息中提取事件code。
     *
     * @param msg 事件消息
     * @return 事件code集合（单个元素）
     */
    @Override
    protected Set<String> extractParams(DeviceEventTriggerMsg msg) {
        if (msg == null || msg.getEventCode() == null) {
            return Collections.emptySet();
        }
        return Collections.singleton(msg.getEventCode());
    }

    /**
     * 处理设备事件消息。
     * <p>
     * 执行过滤逻辑，只有通过过滤的消息才会委托给原始处理器处理。
     *
     * @param msg 设备事件消息
     */
    @Override
    public void handle(DeviceEventTriggerMsg msg) {
        if (!shouldProcess(msg)) {
            return;
        }
        delegate.handle(msg);
    }

    /**
     * 设备事件过滤器Builder。
     */
    public static class Builder {
        /** 关心的设备ID集合 */
        private final Set<String> deviceIds = new HashSet<>();

        /** 关心的事件code集合 */
        private final Set<String> eventCodes = new HashSet<>();

        /**
         * 添加单个设备ID。
         *
         * @param deviceId 设备ID
         * @return Builder
         */
        public Builder deviceId(String deviceId) {
            if (deviceId != null) {
                this.deviceIds.add(deviceId);
            }
            return this;
        }

        /**
         * 批量添加设备ID（数组）。
         *
         * @param deviceIds 设备ID数组
         * @return Builder
         */
        public Builder deviceIds(String... deviceIds) {
            if (deviceIds != null) {
                Collections.addAll(this.deviceIds, deviceIds);
            }
            return this;
        }

        /**
         * 批量添加设备ID（集合）。
         *
         * @param deviceIds 设备ID集合
         * @return Builder
         */
        public Builder deviceIds(Collection<String> deviceIds) {
            if (deviceIds != null) {
                this.deviceIds.addAll(deviceIds);
            }
            return this;
        }

        /**
         * 批量添加事件code（数组）。
         *
         * @param codes 事件code数组
         * @return Builder
         */
        public Builder eventCodes(String... codes) {
            if (codes != null) {
                Collections.addAll(this.eventCodes, codes);
            }
            return this;
        }

        /**
         * 批量添加事件code（集合）。
         *
         * @param codes 事件code集合
         * @return Builder
         */
        public Builder eventCodes(Collection<String> codes) {
            if (codes != null) {
                this.eventCodes.addAll(codes);
            }
            return this;
        }

        /**
         * 构建FilterableDeviceEventMsgHandler实例。
         *
         * @param delegate 被委托的原始事件消息处理器
         * @return FilterableDeviceEventMsgHandler实例
         */
        public FilterableDeviceEventMsgHandler build(DeviceEventMsgHandler delegate) {
            FilterCriteria criteria = FilterCriteria.of(deviceIds, eventCodes);
            return new FilterableDeviceEventMsgHandler(criteria, delegate);
        }
    }
}

