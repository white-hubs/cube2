package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result;

import lombok.Data;

/**
 * 考勤组班次返回对象
 */
@Data
public class AttGroupClassResult {
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
     * 建议应用班次
     */
    private String deptIds;
    /**
     * 建议应用班次名称
     */
    private String deptNames;
}
