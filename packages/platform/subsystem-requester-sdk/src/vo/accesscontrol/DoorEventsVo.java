package com.guhecloud.subsystem.requester.sdk.vo.accesscontrol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoorEventsVo {
    //事件id
    private String eventId;
    //事件名称
    private String eventName;
    //事件产生时间
    private String eventTime;
    //事件方式
    private String eventWay;
    //事件结果
    private String eventResult;
    //卡号
    private String cardNo;
    //抓拍图片文件ID
    private Long pictureFileId;
    //人员Id
    private String personId;
    //人员名称
    private String personName;
    //门禁点名称
    private String doorName;
    //门禁点编号
    private String doorIndexCode;
    //事件类型
    private String eventType;
    //进出类型
    private Integer inAndOutType;

}
