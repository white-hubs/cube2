package com.guhecloud.cube.srv.gen.maintain.server.common.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/8/1 17:48
 * @Description:
 */

@Data
@Builder
public class OrderHomePageVO {

    /**
     * 整改单数
     */
    private Long orderCount;

    /**
     * 环比
     */
    private Float orderRatio;

    /**
     * 进行中
     */
    private Long processOrder;

    /**
     * 已完成
     */
    private Long finishOrder;
}
