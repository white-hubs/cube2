package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 采价任务
 * @date 2023-05-16 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsPricingTask extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //采价计划ID
    private Long planId;
    //任务名称
    private String name;
    //紧急程度
    private String urgency;
    /**
     * 截止时间
     */
    private LocalDateTime deadline;
    /**
     * 任务有效期
     */
    private String taskValidity;
    //任务描述
    private String taskDesc;
    //cron表达式
    private String cron;
    //任务状态 wait 未完成 exec 执行中 over 已完成
    private String status;
    /**
     * 超时未完成通知时间
     */
    private LocalDateTime incompleteNoticeTime;
    //备注
    private String remark;

    private String createUserName;

}