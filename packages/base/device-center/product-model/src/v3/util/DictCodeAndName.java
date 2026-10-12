package com.guhecloud.cube.base.iot.product.model.v3.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典项的编码和名称
 * 用于快照转换时提供字典项的 code 和 name 信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictCodeAndName {

    /**
     * 字典项编码
     */
    private String code;

    /**
     * 字典项名称
     */
    private String name;

    /**
     * 级联名称
     */
    private String levelName;

}
