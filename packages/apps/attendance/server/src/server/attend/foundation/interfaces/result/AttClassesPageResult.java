package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result;

import lombok.Data;

@Data
public class AttClassesPageResult {

    private Long id;

    /**
     * 班次标识，code相同为同一个班次
     */
    private Long code;

    /**
     * 名称
     */
    private String name;

    /**
     * 别称
     */
    private String nickName;

    /**
     * 上班打卡开始时间
     */
    private String startClockBeforeTime;

    /**
     * 上班打卡结束时间
     */
    private String startClockAfterTime;

    /**
     * 上班时间
     */
    private String startWorkTime;

    /**
     * 下班时间
     */
    private String endWorkTime;
    /**
     * 下班打卡开始时间
     */
    private String endClockBeforeTime;

    /**
     * 下班打卡结束时间
     */
    private String endClockAfterTime;

    /**
     * 休息开始时间
     */
    private String restStartTime;

    /**
     * 休息结束时间
     */
    private String restEndTime;

    /**
     * 班次类型
     * 枚举AttClassesTypeEnum，（如果有需求可拓展成班次类型模块）
     */
    private String classesType;

    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;
    /**
     * 建议应用班次
     */
    private String deptIds;

    /**
     * 建议应用班次名称
     */
    private String deptNames;
}
