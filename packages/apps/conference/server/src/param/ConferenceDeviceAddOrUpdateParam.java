package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import lombok.Data;

@Data
public class ConferenceDeviceAddOrUpdateParam {

    private Long conferenceId;
    //设备id
    private Long deviceId;
    //设备名称
    private String name;
    //产品分类
    private String type;
    //设备编码
    private String deviceCode;
    //设备型号
    private String deviceModel;
    //区域id
    private Long areaId;
    //区域名称
    private String areaName;
}
