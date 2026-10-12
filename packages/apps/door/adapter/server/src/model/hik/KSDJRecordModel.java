package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik;

import lombok.Data;

/**
 * 可视对讲记录
 */
@Data
public class KSDJRecordModel {
    private String eventId;//用于存放第三方的ID
    private String eventType;//事件类型
    private String eventName;//事件名称
    private String eventTime;//事件时间
    private String personId;//人员ID
    private String personName;//人员姓名
    private String cardNo;//卡号
    private String deviceName;//设备名称
    private String deviceIndexCode;//设备编号
    private String orgIndexCode;//组织ID
    private String orgName;//组织名称
    private String picUrl;//抓拍图片地址
}
