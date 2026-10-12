package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result;

import lombok.Data;
import com.guhecloud.platform.base.util.StringUtils;

/**
 * 班次详情返回对象
 */
@Data
public class AttClassesDetailResult {
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 别称
     */
    private String nickName;

    /**
     * 上班时间
     */
    private String startWorkTime;

    /**
     * 上班打卡开始时间
     */
    private String startClockBeforeTime;

    /**
     * 上班打卡结束时间
     */
    private String startClockAfterTime;

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
     * （班还是休的）标志(是否必要)
     * 枚举
     */
    private String mark;

    /**
     * 班次类型
     * 枚举，（如果有需求可拓展成班次类型模块）
     */
    private String classesType;

    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;

    /**
     * 建议应用范围(部门id)
     */
    private String deptIds;

    /**
     * 建议应用范围数组
     */
    private String[] deptIdList;

    public String[] getDeptIdList() {
        if (StringUtils.isNotBlank(this.deptIds)) {
            return deptIds.split(",");
        }
        return new String[0];
    }
}
