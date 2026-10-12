package com.guhecloud.cube.biz.smartcard.server.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRecordVo {
    private Long id;
    //事件名称
    private String eventName;
    //事件id
    private String eventId;
    //事件时间
    private LocalDateTime eventTime;
    //人员Id
    private String personId;
    //人员名称
    private String personName;
    //门禁点编码
    private String doorIndexCode;
    //门禁名称
    private String doorName;
    //事件类型
    private String eventType;
    //门禁方式
    private String eventWay;
    //进出类型
    private String inAndOutType;
    //事件结果
    private String eventResult;
    //卡号
    private String cardNo;
}
