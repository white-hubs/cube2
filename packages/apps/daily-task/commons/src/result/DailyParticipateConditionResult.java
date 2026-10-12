package com.guhecloud.cube.biz.daily.task.commons.result;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailySuperintendentDTO;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/17 16:59
 * @Description:
 */
@Data
public class DailyParticipateConditionResult {

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 关联ID
     */
    private Long refId;
    /**
     * 参与对象类型 DailySuperintendentTypeEnum
     */
    private String conditionType;

    /**
     * 条件信息
     */
    private List<DailySuperintendentDTO> participateParams;

    /**
     * 异常时发放用户
     */
    private List<DailySuperintendentDTO> abnormalUserList;
}
