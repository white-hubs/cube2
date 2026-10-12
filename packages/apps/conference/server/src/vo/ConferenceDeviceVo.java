package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import lombok.Data;

@Data
public class ConferenceDeviceVo {

    private Long id;
    //会议室id
    private Long conference_id;
    //设备id
    private Long device_id;
    //设备名称
    private String name;
    //产品分类
    private String type;
    private String deviceCode;
    //设备型号
    private String deviceModel;
    //区域id
    private Long areaId;
    //区域名称
    private String areaName;
}
