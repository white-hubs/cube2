package com.guhecloud.cube.srv.gen.product.market.server.pricing.param;

import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author Appet
 * @Description 采价任务
 * @date 2023-05-16 15:08:36
 */
@Data
public class GoodsPricingTaskParam {

    //采价计划ID
    private Long planId;
    //任务名称
    private String name;
    //紧急程度
    private String urgency;
    /**
     * 任务有效期
     */
    private String taskValidity;
    //任务描述
    private String taskDesc;
    //cron表达式
    private String cron;
    //备注
    private String remark;
    //品类列表
    List<Long> categoryIds;

    //人员列表
    List<Long> userIds;

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