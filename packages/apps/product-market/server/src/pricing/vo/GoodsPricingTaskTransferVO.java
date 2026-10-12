package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 采价任务转移
 * @date 2023-06-05 15:08:36
 */
@Data
public class GoodsPricingTaskTransferVO {

    //
    private Long id;
    //移交人
    private Long fromUserId;
    //移交人姓名
    private String fromUserName;
    //被移交人
    private String toUserIds;
    //被移交人姓名
    private String toUserNames;
    //状态
    private String status;
    //备注
    private String remark;

    private LocalDateTime createTime;
}