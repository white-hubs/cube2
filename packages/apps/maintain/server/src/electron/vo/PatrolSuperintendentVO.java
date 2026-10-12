package com.guhecloud.cube.srv.gen.maintain.server.electron.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatrolSuperintendentVO {
    /**
     * 对象类型 ConditionTypeEnum
     */
    private String participateType;
    /**
     * 参与对象的id(例如participateType类型为USER时是人员id,为POST时是岗位id
     */
    private Long targetId;
    private String targetName;
}
