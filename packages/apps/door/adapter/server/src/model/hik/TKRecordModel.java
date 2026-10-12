package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik;

import lombok.Data;

/**
 * 梯控记录
 */
@Data
public class TKRecordModel {
    private String eventId;//用于存放第三方的ID
    private String eventType;//事件类型
    private String eventName;//事件名称
    private String eventTime;//事件时间
    private String personId;//人员ID
    private String personName;//人员姓名
    private String cardNo;//卡号
    private String deviceName;//设备名称
    private String deviceCode;//设备编号
    private String orgId;//组织ID
    private String orgName;//组织名称
    private String liftName;//电梯名称
}
