package com.guhecloud.cube.srv.gen.product.market.server.pricing.entity;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration.PublishTypeEnum;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Appet
 * @Description 商品采价计划
 * @date 2023-05-16 15:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsPricingPlan extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //计划名称
    private String name;
    //紧急程度
    private String urgency;
    /**
     * 任务有效期
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

    private String xxlTaskId;

    private String createUserName;

    private String status;

    public String getCron() {
        String minite = "00 ";
        String hour = "00 ";

        if (StringUtils.isNotBlank(publishTime)) {
            String[] split = publishTime.split(":");
            minite = split[1] + " ";
            hour = split[0] + " ";
        }
        if (publishType != null) {
            if (PublishTypeEnum.EVERY_DAY.name().equals(publishType.name())) {
                cron = "00 " + minite + hour + "* * ?";
            } else if (PublishTypeEnum.EVERY_WEEK.name().equals(publishType.name()) && StringUtils.isNotBlank(publishPeriod)) {
                //在cron表达式中 1是星期天 7是星期六
                int week = Integer.valueOf(publishPeriod) < 7
                           ? Integer.valueOf(publishPeriod) + 1
                           : Integer.valueOf(publishPeriod) - 6;
                cron = "00 " + minite + hour + "? * " + week;
            } else if (PublishTypeEnum.EVERY_MONTH.name().equals(publishType.name()) && StringUtils.isNotBlank(publishPeriod)) {
                cron = "00 " + minite + hour + publishPeriod + " * ?";
            }
        }
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public LocalDateTime getTaskValidityTime(LocalDateTime time) {
        if (StringUtils.isNotBlank(this.taskValidity)) {
            String[] taskValidityArray = taskValidity.split(",");
            if (taskValidityArray.length >= 3) {
                var printTime = time == null ? LocalDateTime.now() : time;
                printTime = DateUtils.offsetDay(printTime, Integer.parseInt(taskValidityArray[0]));
                printTime = DateUtils.offset(printTime, ChronoUnit.HOURS, Integer.parseInt(taskValidityArray[1]));
                return DateUtils.offset(printTime, ChronoUnit.MINUTES, Integer.parseInt(taskValidityArray[2]));
            }
        }
        return null;
    }
}