package com.guhecloud.cube.srv.gen.maintain.server.electron.param.plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatrolConditionParticipateParam {
    /**
     * 条件类型 PatrolParticipateTypeEnum
     */
    private String conditionType;
    /**
     * 巡更人员集合
     */
    private List<PatrolParticipateParam> participateList;
    /**
     * 异常时发放人员id集合
     */
    private List<PatrolParticipateParam> abnormalUserList;
}
