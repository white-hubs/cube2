package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 采价任务
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingTaskQueryParam extends Paging {

    //采价计划ID
    private Long planId;
    //任务名称
    private String name;
    //紧急程度
    private String urgency;
    //采价人ID
    private String userId;
    //采价人姓名
    private String userName;
    //任务状态
    private String status;
    //时间段
    private Period period;
    //我接受的
    private Boolean received;
    //接收人ID
    private String receiverId;

}