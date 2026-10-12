package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

import java.util.List;

/**
 * 排班规则操作日志字段
 */
@Data
public class SchedulingRuleOperationVO {
    @DiffLogField(name = "规则id")
    private Long id;
    /**
     * 规则名称
     */
    @DiffLogField(name = "名称")
    private String name;
    /**
     * 排班方式(固定班，三班倒)
     */
    @DiffLogField(name = "排班方式", function = "SCHEDULING_WAYS")
    private String ways;
    /**
     * 法定节假日是否休息
     */
    @DiffLogField(name = "法定节假日是否休息", function = "BOOLEAN")
    private Boolean legalHolidaysRest;
    /**
     * 一个月允许最大连续排班天数
     */
    @DiffLogField(name = "最大连续排班天数")
    private int continuousDays;
    /**
     * 一天两个班次最小间隔时间
     */
    @DiffLogField(name = "最小间隔时间")
    private int intervalHour;
    /**
     * 一个月相同班次数据json
     */
    private String sameShift;

    @DiffLogField(name = "规则班次", function = "DIFF_LIST")
    private List<String> classes;
    @DiffLogField(name = "相同班次数", function = "DIFF_LIST")
    private List<String> sameShiftList;
}
