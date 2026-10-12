package com.guhecloud.cube.base.device.center.metadata.std.param;

import lombok.Data;

import java.util.Objects;
import java.util.Set;

/**
 * @author pengjian
 * @date 2024/1/10 11:06
 * @description 属性写值对象
 */
@Data
public class DevicePropWriteCmd {
    private Long deviceId;
    /**
     * 命令id, 如果不传，系统会自动生成
     */
    private Long commandSeqOptional;

    /**
     * 属性写值对象
     */
    private Set<PropWriteValue> propWriteValues;

    @Data
    public static class PropWriteValue {
        private String code;
        /**
         * 写入前的值，不传会读取当前值
         */
        private String originalValue;
        /**
         * 期望写的值
         */
        private String expectedValue;

        //复写equals和hashcode方法， code不一致，对象就不一致
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PropWriteValue that = (PropWriteValue) o;
            return code.equals(that.code);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code);
        }
    }
}
