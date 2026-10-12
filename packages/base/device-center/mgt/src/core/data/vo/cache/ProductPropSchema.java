package com.guhecloud.cube.iot.mgt.core.data.vo.cache;

import lombok.Data;

import java.io.Serializable;

/**
 * 设备属性的实时值
 */
@Data
public class ProductPropSchema implements Serializable {

    /**
     * 属性Code,产品下唯一
     */
    private String propCode;

    /**
     * 属性名称
     */
    private String propName;

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 属性单位
     */
    private String unitName;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据精度
     */
    private Integer dataPrecision;

    /**
     * 读写类型:0只读:1读写
     */
    private int dataWrite;

    /**
     * json数组[0,100] 或 [{"value":"0","label":"开"},{"value":"1","label":"关"}]
     */
    private String dataConfig;
}
