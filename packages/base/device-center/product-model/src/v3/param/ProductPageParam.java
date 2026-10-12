package com.guhecloud.cube.base.iot.product.model.v3.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductPageParam {

    /**
     * 页码(从1开始)
     */
    private Integer pageNumber = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 产品名称(模糊查询)
     */
    private String prodName;

    /**
     * 产品编号
     */
    private String prodNumber;

    /**
     * 产品名称拼音首字母(前缀匹配)
     */
    private String prodNamePinyin;

    /**
     * 产品品类ID
     */
    private Long prodCategoryId;

    /**
     * 节点类型
     * 使用 {@link com.guhecloud.cube.base.iot.product.model.v3.enums.NodeTypeEnum}
     */
    private Integer nodeType;

    /**
     * 产品品牌ID
     */
    private Long prodBrandId;

    /**
     * 消息协议ID
     * 使用 {@link com.guhecloud.cube.base.iot.product.model.v3.enums.ProtocolTypeEnum}
     */
    private Long protocolId;

    /**
     * 产品状态: 1正常 0禁用
     */
    private Integer status;
}
