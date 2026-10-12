package com.guhecloud.cube.iot.north.bridge.consumer.filter;

import com.guhecloud.cube.iot.north.bridge.consumer.handler.DevicePropMsgHandler;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.KeyValueWithTs;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.LivePropertyUpdateMsg;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 设备属性消息可过滤Handler。
 * <p>
 * 支持按设备ID与属性code过滤，并在通过过滤后只保留关心的updates字段。
 * 特性：
 * <ul>
 *   <li>设备级过滤：仅处理指定设备ID的属性消息</li>
 *   <li>属性级过滤：仅保留关心的属性code</li>
 *   <li>字段裁剪：自动裁剪消息的updates字段，只保留过滤条件中的属性</li>
 *   <li>运行时动态管理：支持运行时添加/移除设备ID和属性code，无需重新创建Handler</li>
 * </ul>
 * <p>
 * <b>使用示例</b>：
 * <pre>{@code
 * // 初始化时设置基础过滤条件
 * FilterableDevicePropMsgHandler handler =
 *     FilterableDevicePropMsgHandler.builder()
 *         .deviceId("device001")
 *         .propCodes("temperature", "humidity")
 *         .build(msg -> processMsg(msg));
 *
 * // 运行时动态添加设备
 * handler.addDeviceId("device002");
 * handler.addDeviceIds(Arrays.asList("device003", "device004"));
 *
 * // 运行时动态添加属性
 * handler.addParamCode("pressure");
 *
 * // 运行时移除设备
 * handler.removeDeviceId("device001");
 *
 * // 清空所有过滤条件（监听所有设备的所有属性）
 * handler.clearDeviceIds();
 * handler.clearParamCodes();
 * }</pre>
 */
public class FilterableDevicePropMsgHandler
        extends AbstractFilterableHandler<LivePropertyUpdateMsg, DevicePropMsgHandler>
        implements DevicePropMsgHandler {

    /**
     * 构造函数。
     *
     * @param criteria 过滤条件
     * @param delegate 被委托的原始属性消息处理器
     */
    public FilterableDevicePropMsgHandler(FilterCriteria criteria, DevicePropMsgHandler delegate) {
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
     * 从属性消息中提取设备ID。
     *
     * @param msg 属性消息
     * @return 设备ID
     */
    @Override
    protected String extractDeviceId(LivePropertyUpdateMsg msg) {
        return msg != null ? msg.getDeviceId() : null;
    }

    /**
     * 从属性消息中提取所有属性code。
     *
     * @param msg 属性消息
     * @return 属性code集合
     */
    @Override
    protected Set<String> extractParams(LivePropertyUpdateMsg msg) {
        if (msg == null || msg.getUpdates() == null) {
            return java.util.Collections.emptySet();
        }
        return msg.getUpdates().stream()
                .filter(Objects::nonNull)
                .map(KeyValueWithTs::getKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * 处理设备属性消息。
     * <p>
     * 执行过滤逻辑，并在通过过滤后对消息的updates字段进行裁剪，只保留关心的属性。
     *
     * @param msg 设备属性消息
     */
    @Override
    public void handle(LivePropertyUpdateMsg msg) {
        if (!shouldProcess(msg)) {
            return;
        }

        // 字段级过滤：只保留关心的属性
        if (!paramFilter.isEmpty() && msg != null && msg.getUpdates() != null) {
            List<KeyValueWithTs> filtered = msg.getUpdates().stream()
                    .filter(kv -> kv != null && paramFilter.contains(kv.getKey()))
                    .collect(Collectors.toList());
            msg.setUpdates(filtered);
        }

        delegate.handle(msg);
    }

    /**
     * 设备属性过滤器Builder。
     */
    public static class Builder {
        /** 关心的设备ID集合 */
        private final Set<String> deviceIds = new HashSet<>();

        /** 关心的属性code集合 */
        private final Set<String> propCodes = new HashSet<>();

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
                for (String id : deviceIds) {
                    if (id != null) {
                        this.deviceIds.add(id);
                    }
                }
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
                for (String id : deviceIds) {
                    if (id != null) {
                        this.deviceIds.add(id);
                    }
                }
            }
            return this;
        }

        /**
         * 批量添加属性code（数组）。
         *
         * @param codes 属性code数组
         * @return Builder
         */
        public Builder propCodes(String... codes) {
            if (codes != null) {
                for (String code : codes) {
                    if (code != null) {
                        this.propCodes.add(code);
                    }
                }
            }
            return this;
        }

        /**
         * 批量添加属性code（集合）。
         *
         * @param codes 属性code集合
         * @return Builder
         */
        public Builder propCodes(Collection<String> codes) {
            if (codes != null) {
                for (String code : codes) {
                    if (code != null) {
                        this.propCodes.add(code);
                    }
                }
            }
            return this;
        }

        /**
         * 构建FilterableDevicePropMsgHandler实例。
         *
         * @param delegate 被委托的原始属性消息处理器
         * @return FilterableDevicePropMsgHandler实例
         */
        public FilterableDevicePropMsgHandler build(DevicePropMsgHandler delegate) {
            FilterCriteria criteria = FilterCriteria.of(deviceIds, propCodes);
            return new FilterableDevicePropMsgHandler(criteria, delegate);
        }
    }
}
