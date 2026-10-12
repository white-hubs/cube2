package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result;

import lombok.Data;

/**
 * 考勤组列表返回对象
 */
@Data
public class AttGroupPageResult {
    private Long id;
    /**
     * 考勤组名称
     */
    private String name;
    /**
     * 打卡规则id
     */
    private Long clockRuleId;
    /**
     * 排班规则id
     */
    private Long schedulingRuleId;
    /**
     * 考勤组范围(所有人，部分人。枚举AttGroupRangesEnum）
     */
    private String ranges;
    /**
     * 考勤组人数
     */
    private long staffNumber;
}
