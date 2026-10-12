package com.guhecloud.cube.base.iot.product.model.v3.constant;

import lombok.Getter;

/**
 * 物模型常量类
 * <p>
 * 定义物模型相关的业务常量,避免魔术字符串
 */
public final class ModelConstants {

    private ModelConstants() {
        throw new UnsupportedOperationException("工具类不允许实例化");
    }

    /**
     * 导入导出相关常量
     */
    public static final class ImportExport {
        /**
         * 导出版本
         */
        public static final String EXPORT_VERSION = "v3.0.0";

        private ImportExport() {
        }
    }
}
