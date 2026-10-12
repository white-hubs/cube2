package com.guhecloud.cube.base.dataintegrate.client.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/12 10:37
 * @Description:
 */

@Data
public class MarketConfigQueryParam {

    /**
     * 市场租户id
     */
    private Long marketTenant;

    /**
     * 市场租户id集合
     */
    private List<String> marketTenantIds;
    //市场名称
    private String keyword;

    /**
     * 是否按照状态排序
     */
    private Boolean statusOrder;
}
