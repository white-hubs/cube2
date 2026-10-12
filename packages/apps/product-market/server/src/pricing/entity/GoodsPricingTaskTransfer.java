package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 采价任务转移
 * @date 2023-06-05 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsPricingTaskTransfer extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //任务ID
    private Long taskId;
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

}