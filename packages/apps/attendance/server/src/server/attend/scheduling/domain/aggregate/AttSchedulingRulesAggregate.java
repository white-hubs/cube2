package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.aggregate;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingRuleClassesParam;
import lombok.Data;

import java.util.List;

@Data
public class AttSchedulingRulesAggregate {
    private Long id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 排班方式(固定班，三班倒)
     */
    private String ways;
    /**
     * 法定节假日是否休息
     */
    private Boolean legalHolidaysRest;
    /**
     * 一个月允许最大连续排班天数
     */
    private int continuousDays;
    /**
     * 一天两个班次最小间隔时间
     */
    private int intervalHour;
    /**
     * 一个月相同班次数据json
     */
    private String sameShift;

    /**
     * 排班规则班次集合
     */
    private List<AttSchedulingRuleClassesParam> rulesClassesList;
}
