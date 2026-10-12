package com.guhecloud.cube.iot.mgt.things.vo;

import com.guhecloud.cube.iot.mgt.base.vo.DictItemVo;
import lombok.Data;

@Data
public class ProductPropVo {
    private Long id;

    /**
     * 产品ID
     */
    private Long prodId;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性Code,产品下唯一
     */
    private String propCode;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据类型名称
     */
    private String dataTypeName;

    /**
     * 数据精度
     */
    private Integer precision;

    /**
     * 数据单位id
     */
    private String unitId;

    private DictItemVo unitItem;

    /**
     * 数据单位名称
     */
    private String unitCode;

    /**
     * 数据单位名称
     */
    private String unitName;

    /**
     * 读写类型:0只读:1读写
     */
    private int dataWrite;

    /**
     * json数组[0,100] 或 [{"value":"0","label":"开"},{"value":"1","label":"关"}]
     */
    private String dataConfig;

    /**
     * 属性描述
     */
    private String remark;

    private String createTimeFormat;

    private String defaultValue;
}
