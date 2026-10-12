package com.guhecloud.cube.iot.north.bridge.consumer.filter;

import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceOnlineStatusListener;
import com.guhecloud.cube.iot.north.bridge.consumer.handler.DeviceStatusHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.util.CallbackUtil;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceStatusUpdateMsg;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 设备状态变更可过滤Handler。
 * <p>
 * 专门用于监听设备在线/离线状态变更，支持按设备ID过滤。
 * 客户端只需实现简化的 {@link DeviceOnlineStatusListener} 接口。
 * <p>
 * <b>特性</b>：
 * <ul>
 *   <li>设备级过滤：仅监听指定设备ID的状态变更</li>
 *   <li>运行时动态管理：支持运行时添加/移除设备ID，无需重新创建Handler</li>
 * </ul>
 * <p>
 * <b>使用示例</b>：
 * <pre>{@code
 * // 监听特定设备
 * FilterableDeviceStatusChangeHandler handler =
 *     FilterableDeviceStatusChangeHandler.builder()
 *         .deviceIds("device001", "device002", "device003")
 *         .build((deviceId, isOnline) -> {
 *             if (isOnline) {
 *                 log.info("设备 {} 上线了", deviceId);
 *             } else {
 *                 log.info("设备 {} 离线了", deviceId);
 *             }
 *         });
 *
 * // 运行时动态添加设备
 * handler.addDeviceId("device004");
 * handler.addDeviceIds(Arrays.asList("device005", "device006"));
 *
 * // 运行时移除设备
 * handler.removeDeviceId("device001");
 *
 * // 监听所有设备（不指定deviceId）
 * FilterableDeviceStatusChangeHandler handler =
 *     FilterableDeviceStatusChangeHandler.builder()
 *         .build((deviceId, isOnline) -> {
 *             updateDeviceStatusInUI(deviceId, isOnline);
 *         });
 * }</pre>
 */
public class FilterableDeviceStatusChangeHandler implements DeviceStatusHandler {

    /**
     * 关注的设备ID集合（空集合表示关注所有设备）
     */
    private final Set<String> deviceIdFilter;

    /**
     * 用户的状态变更监听器
     */
    private final DeviceOnlineStatusListener listener;

    private FilterableDeviceStatusChangeHandler(
            Set<String> deviceIdFilter,
            DeviceOnlineStatusListener listener) {
        this.deviceIdFilter = Collections.unmodifiableSet(new HashSet<>(deviceIdFilter));
        this.listener = listener;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 处理底层设备状态消息（框架回调）。
     * <p>
     * 执行设备过滤、状态变更检测，并转换为简化的回调。
     *
     * @param msg 设备状态更新消息
     */
    @Override
    public void handle(DeviceStatusUpdateMsg msg) {
        // 1. 提取设备ID
        String deviceId = msg != null ? msg.getDeviceId() : null;
        if (deviceId == null) {
            return;
        }

        // 2. 设备ID过滤
        if (!deviceIdFilter.isEmpty() && !deviceIdFilter.contains(deviceId)) {
            return;
        }

        // 3. 判断当前在线状态
        boolean currentOnline = CallbackUtil.isOnlineSingle(msg);

        listener.onStatusChanged(deviceId, currentOnline);
    }

    public static class Builder {
        private final Set<String> deviceIds = new HashSet<>();

        /**
         * 添加单个关注的设备ID。
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
         * 批量添加关注的设备ID（数组）。
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
         * 批量添加关注的设备ID（集合）。
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
         * 构建 FilterableDeviceStatusChangeHandler 实例。
         * <p>
         * 如果未指定任何设备ID，则默认监听所有设备的状态变更。
         *
         * @param listener 状态变更监听器
         * @return FilterableDeviceStatusChangeHandler实例
         */
        public FilterableDeviceStatusChangeHandler build(DeviceOnlineStatusListener listener) {
            return new FilterableDeviceStatusChangeHandler(deviceIds, listener);
        }
    }
}
