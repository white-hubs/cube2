package com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model;

import lombok.Data;

@Data
public class WaxAlarm {
    private String id;//报警ID
    private String areaId;//区域ID
    private String areaName;//区域名称
    private String uuid;//图片视频 UUID
    private int type;//类型 0 图片 1 视频
    private String patrolmanId;//巡检员ID
    private String patrolmanName;//巡检
    private String patrolmanCard;//巡检员卡号
    private String deviceId;//巡检器ID
    private String deviceName;//巡检器名称
    private String deviceCode;//巡检器编号
    private Boolean proc;//是否处理
    private String userId;//处理人ID
    private String userName;//处理人名称
    private Long procTime;//处理时间
    private String remark;
    private Long createTime;//上报时间
}
