package com.guhecloud.cube.biz.daily.task.param;

import com.guhecloud.cube.biz.ouaa.metadata.enumeration.ConditionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参与对象入参
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyParticipateParam {

    /**
     * 参与对象类型 DailySuperintendentTypeEnum
     */
    private ConditionTypeEnum participateType;
    /**
     * 参与对象的id(例如superintendentType类型为USER时是人员id,为POST时是岗位id
     */
    private Long targetId;

    /**
     * 用类型（执行人、负责人）
     */
    private String taskUserType;
}
