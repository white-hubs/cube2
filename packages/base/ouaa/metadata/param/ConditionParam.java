package com.guhecloud.cube.biz.ouaa.metadata.param;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.ConditionTypeEnum;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/19 11:18
 * @Description:
 */
public class ConditionParam {
    /**
     * 对象类型 ConditionTypeEnum
     */
    private ConditionTypeEnum conditionTypeEnum;
    /**
     * 参与对象的id(例如ConditionTypeEnum类型为USER时是人员id,为POST时是岗位id
     */
    private Long targetId;

    public ConditionParam() {
    }

    public ConditionParam(ConditionTypeEnum conditionTypeEnum, Long targetId) {
        this.conditionTypeEnum = conditionTypeEnum;
        this.targetId = targetId;
    }

    public ConditionTypeEnum getConditionTypeEnum() {
        return conditionTypeEnum;
    }

    public void setConditionTypeEnum(ConditionTypeEnum conditionTypeEnum) {
        this.conditionTypeEnum = conditionTypeEnum;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
