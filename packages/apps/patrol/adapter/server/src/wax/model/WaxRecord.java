package com.guhecloud.cube.srv.gen.adapter.patrol.server.wax.model;

import lombok.Data;

@Data
public class WaxRecord {
    private Long startTime;//开始时间
    private Long endTime;//结束时间
    private Long fixedStartTime;//固定开始时间(带毫秒的时间戳)
    private Long fixedEndTime;//固定结束时间(带毫秒的时间戳)
    private Long arriveTime;//巡检时间(带毫秒的时间戳)
    private int arriveType;//状态：-1 漏检 0 准时 1 早到 2 晚到
    private Long orderNum;//排序号
    private Boolean fixedTime;//是否固定时间
    private String lineId;//线路ID
    private String lineName;//线路名称
    private String checkpointId;//巡检点ID
    private String checkpointName;//巡检点名称
    private String checkpointCard;//巡检点卡号
    private String patrolmanId;//巡检员ID
    private String patrolmanName;//巡检员名称
    private String patrolmanCard;//巡检员卡号
    private String areaId;//区域ID
    private String areaName;//区域名称
    private String planJobId;//任务班次ID
    private String planId;//计划ID
}
