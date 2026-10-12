package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result;

import lombok.Data;

import java.util.List;

/**
 * 排班规则详情返回对象
 */
@Data
public class AttSchedulingRuleDetailResult {
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
    //    /**
    //     * 相同班次数量
    //     */
    //    private int sameShiftNumber;
    //    /**
    //     * 相同班次类型
    //     */
    //    private String sameShiftType;
    /**
     * 排班规则班次集合
     */
    private List<AttSchedulingRuleClassesResult> rulesClassesList;
}
