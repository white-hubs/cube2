package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 采价任务转移
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingTaskTransferParam {
    //采价任务ID
    private Long taskId;

    private Long fromUserId;
    //移交人
    private String fromUserName;

    private String toUserIds;
    //被移交人
    private String toUserNames;
    //时间段
    private Period period;
    //状态
    private String status;
    //备注
    private String remark;

}