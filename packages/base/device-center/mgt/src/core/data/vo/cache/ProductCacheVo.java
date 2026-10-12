package com.guhecloud.cube.iot.mgt.core.data.vo.cache;

import lombok.Data;

@Data
public class ProductCacheVo {
    /**
     * 产品ID
     */
    private Long id;

    /**
     * 产品版本号<br/>
     * 每当产品进行更新时，版本号自动递增
     */
    private String prodVersion;

    /**
     * 产品名称
     */
    private String prodName;

    /**
     * 是否自定义品类<br/>
     * 1:是<br/>
     * 0:否
     */
    private Integer isCustom;

    /**
     * 产品品类ID
     */
    private Long prodCategoryId;

    /**
     * 节点类型: 1直连设备 2网关设备 3网关子设备
     */
    private Long nodeType;

    /**
     * 产品品牌id
     */
    private Long prodBrandId;

    /**
     * 接入方式Id
     */
    private Long accessTypeId;

    /**
     * 消息协议ID（非字典表）
     */
    private Long protocolId;

    /**
     * 产品扩展字段,更多描述的可能
     */
    private String extendConfig;

    private long adminId;
}
