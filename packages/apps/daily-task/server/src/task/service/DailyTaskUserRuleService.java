package com.guhecloud.cube.biz.daily.task.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyParticipateConditionResult;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanSuperintendentPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserRulePO;
import com.guhecloud.cube.biz.daily.task.enums.DailyDomainTypeEnum;
import com.guhecloud.cube.biz.daily.task.param.DailyParticipateConditionParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTargetStandardResultParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/17 11:52
 * @Description:
 */
public interface DailyTaskUserRuleService extends IService<DailyTaskUserRulePO> {
    void saveRuleBindUser(Long refId, List<DailyParticipateConditionParam> participateList,
                          List<DailyTargetStandardResultParam> targetResultList,
                          List<DailyTaskUserPO> dailyTaskUserPOS,
                          List<DailyPlanSuperintendentPO> dailyPlanSuperintendentPOS
                         );

    /**
     * 根据关联id获取规则及规则对应用户配置
     */
    List<DailyParticipateConditionResult> getRuleByRefId(Long refId, DailyDomainTypeEnum typeEnum);

    Map<Long, List<DailyParticipateConditionResult>> getRuleByRefIds(Set<Long> refIds, DailyDomainTypeEnum typeEnum);

    Map<Long, List<DailyParticipateConditionParam>> getRulesForTaskParam(Set<Long> refIds, DailyDomainTypeEnum typeEnum
                                                                        );
}
