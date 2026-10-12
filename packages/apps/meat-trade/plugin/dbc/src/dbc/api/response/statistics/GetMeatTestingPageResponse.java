package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response.statistics;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class GetMeatTestingPageResponse {

    /**
     * 检测详情id
     */
    private String oid;

    private String meatTallyBatch;

    /**
     * 抽检批次号
     */
    private String meatTallyBatchNumber;

    /**
     * 检测项目
     */
    private String meatTestingItemCode;

    /**
     * 检测项目
     */
    private String meatTestingItemName;

    /**
     * 商家编号
     */
    private String sellerId;

    /**
     * 商家名称
     */
    private String sellerName;

    /**
     * 商品品类名称
     */
    private String productTypeName;

    /**
     * 检测结果值
     */
    private String factor;

    /**
     * 检测比例
     */
    private BigDecimal checkRate;

    /**
     * 检测数量
     */
    private Integer checkNum;

    /**
     * 检测结果
     */
    private String status;

    /**
     * 抽检人
     */
    private String createName;
}
