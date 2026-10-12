package com.guhecloud.cube.biz.meattrade.server.order.param;

import lombok.Data;

/**
 * @author lzy
 * @date 2021/12/8
 */
@Data
public class OrderDetailModifyParam {

    /**
     * 理货数据详情主键
     */
    private Long porkArrangeDetailId;

    /**
     * 单价
     */
    private Double unitPrice;

}
