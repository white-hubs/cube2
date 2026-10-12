package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo;

import lombok.Data;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组打卡方式
 */
@Data
public class AttendanceGroupPunchCardWayVO {

    private Long id;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 方式
     */
    private String way;

    /**
     * wifi名称
     */
    private String wifi;

    /**
     * mac地址
     */
    private String mac;

    /**
     * GPS考勤地址
     */
    private String address;

    /**
     * 经度
     */
    private String latitude;

    /**
     * 纬度
     */
    private String longitude;

    /**
     * 考勤范围(单位：米)
     */
    private Integer effectiveRange;

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

}