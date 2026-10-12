package com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan;

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
public class PatrolParticipateParam {

    /**
     * 对象类型 ConditionTypeEnum
     */
    private String participateType;
    /**
     * 参与对象的id(例如superintendentType类型为USER时是人员id,为POST时是岗位id
     */
    private Long targetId;
}
