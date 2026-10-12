package com.guhecloud.cube.iot.north.bridge.consumer.handler;

/**
 * 设备在线状态变更监听器。
 * <p>
 * 客户端只需关注设备ID和在线状态，无需理解底层消息格式。
 * <p>
 * 使用示例：
 * <pre>{@code
 * FilterableDeviceStatusChangeHandler handler =
 *     FilterableDeviceStatusChangeHandler.builder()
 *         .deviceIds("device001", "device002")
 *         .build((deviceId, isOnline) -> {
 *             if (isOnline) {
 *                 log.info("设备 {} 上线了", deviceId);
 *             } else {
 *                 log.info("设备 {} 离线了", deviceId);
 *             }
 *         });
 * }</pre>
 */
@FunctionalInterface
public interface DeviceOnlineStatusListener {
    /**
     * 当设备在线/离线状态变化时回调。
     *
     * @param deviceId 设备ID
     * @param isOnline true=在线, false=离线
     */
    void onStatusChanged(String deviceId, boolean isOnline);
}
