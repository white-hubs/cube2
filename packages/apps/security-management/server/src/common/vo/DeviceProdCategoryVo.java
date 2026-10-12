package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeviceProdCategoryVo {

    /**
     * 产品品类编号
     */
    private String deviceProdCategoryCode;

    /**
     * 产品品类编号序列号
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 产品品类名称
     */
    private String deviceProdCategoryName;
    //数量
    private Integer num;
    //本粉笔
    private BigDecimal proportion;
}
