package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import lombok.Data;

import java.util.List;

@Data
public class AlarmHandlerSituationAnalysisVO {

    /**
     * 市场租户编号
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    private String marketName;

    /**
     * 状态分组信息
     */
    private List<AlarmStatusGroupVO> statusGroups;
}
