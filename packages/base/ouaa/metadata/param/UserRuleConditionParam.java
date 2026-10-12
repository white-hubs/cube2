package com.guhecloud.cube.biz.ouaa.metadata.param;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/19 11:04
 * @Description:
 */
public class UserRuleConditionParam {

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 是否在岗
     */
    private Boolean isOnDuty;

    /**
     * 条件信息
     */
    private List<ConditionParam> participateParams;

    /**
     * 异常时发放用户
     */
    private List<Long> abnormalUserIdList;

    public UserRuleConditionParam() {

    }

    public UserRuleConditionParam(Long ruleId, Boolean isOnDuty, List<ConditionParam> participateParams, List<Long> abnormalUserIdList) {
        this.ruleId = ruleId;
        this.isOnDuty = isOnDuty;
        this.participateParams = participateParams;
        this.abnormalUserIdList = abnormalUserIdList;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Boolean getOnDuty() {
        return isOnDuty;
    }

    public void setOnDuty(Boolean onDuty) {
        isOnDuty = onDuty;
    }

    public List<ConditionParam> getParticipateParams() {
        return participateParams;
    }

    public void setParticipateParams(List<ConditionParam> participateParams) {
        this.participateParams = participateParams;
    }

    public List<Long> getAbnormalUserIdList() {
        return abnormalUserIdList;
    }

    public void setAbnormalUserIdList(List<Long> abnormalUserIdList) {
        this.abnormalUserIdList = abnormalUserIdList;
    }
}
