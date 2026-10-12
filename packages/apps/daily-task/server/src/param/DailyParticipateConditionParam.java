package com.guhecloud.cube.biz.daily.task.param;

import com.guhecloud.cube.biz.daily.task.enums.DailyParticipateTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/17 10:57
 * @Description: 日常任务发放用户条件
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyParticipateConditionParam {

    /**
     * 规则id
     */
    private Long ruleId;
    /**
     * 参与对象类型 DailySuperintendentTypeEnum
     */
    private DailyParticipateTypeEnum conditionType;

    /**
     * 条件信息
     */
    private List<DailyParticipateParam> participateParams;

    /**
     * 异常时发放用户
     */
    private List<Long> abnormalUserIdList;
}
