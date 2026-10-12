package com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.dto;

import lombok.Data;

@Data
public class PunchCardWayDTO {

    /**
     * 考勤机名称
     */
    private String deviceName;

    /**
     * 考勤机型号
     */
    private String deviceModel;

    /**
     * 考勤机编码
     */
    private String deviceCode;

    /**
     * 考勤机类型
     */
    private String deviceGenre;

    private Integer tenant;

}
