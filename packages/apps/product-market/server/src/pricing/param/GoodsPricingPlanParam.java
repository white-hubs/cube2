package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PublishTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Appet
 * @Description 商品采价计划
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingPlanParam {

    //计划名称
    private String name;
    //紧急程度
    private String urgency;
    /**
     * 任务有效期(天,时,分 逗号隔开)
     */
    private String taskValidity;
    //计划描述
    private String planDesc;
    //开始时间
    private LocalDateTime startTime;
    //结束时间
    private LocalDateTime endTime;
    //发布类型
    private PublishTypeEnum publishType;
    //时间段
    private String publishPeriod;
    //时间点
    private String publishTime;
    //cron表达式
    private String cron;
    //是否启用
    private Boolean enabled;
    //备注
    private String remark;

    //品类列表
    List<Long> categoryIds;

    //人员列表
    List<Long> userIds;
}