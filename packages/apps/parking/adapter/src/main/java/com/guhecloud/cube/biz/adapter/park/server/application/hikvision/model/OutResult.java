package com.guhecloud.cube.biz.adapter.park.server.application.hikvision.model;

import lombok.Data;

/**
 * @author wql
 * @describe 事件出场结果
 * @date 2024/3/6 9:17
 */
@Data
public class OutResult {
    /**
     * 账单编号
     */
    private String chargeIndex;
    /**
     * 出场实收金额
     */
    private float realCost;
    /**
     * 放行结果信息
     */
    private RlsResult rlsResult;
    /**
     * 收费员id
     */
    private String tollerId;
    /**
     * 出场应收金额
     */
    private float totalCost;
}
