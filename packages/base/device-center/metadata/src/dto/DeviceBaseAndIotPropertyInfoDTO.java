package com.guhecloud.cube.base.device.center.metadata.dto;

import com.guhecloud.cube.base.device.center.metadata.lang.AttributePermissionEnum;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

/**
 * 设备基础信息与iot属性信息
 *
 * @author lanchun
 * @date 2024/1/24 13:38
 */
@Data
public class DeviceBaseAndIotPropertyInfoDTO {

    private Long id;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备iot属性列表
     */
    private Set<IotPropertyValue> iotProperties;

    @Data
    @Builder
    public static class IotPropertyValue {

        /**
         * 属性编号
         */
        private String code;

        /**
         * 属性名称
         */
        private String name;

        /**
         * 读写权限
         */
        private AttributePermissionEnum permission;

        /**
         * 数据类型
         */
        private String dataType;

        /**
         * 属性单位
         */
        private String attributeUnitName;

        /**
         * 当前值
         */
        private Object originalValue;

        /**
         * 业务平台翻译后的值
         */
        private String translatedValue;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            IotPropertyValue that = (IotPropertyValue) o;
            return code.equals(that.code);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code);
        }

    }
}
