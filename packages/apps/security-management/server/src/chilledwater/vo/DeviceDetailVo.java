package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

@Data
public class DeviceDetailVo {
    //设备名称
    private String deviceName;
    //设备id
    private Long deviceId;
    //设备code
    private String code;
    //设备类型
    private String genreName;
    //区域
    private String areaName;
    //冷媒体单位流量
    private String unitTraffic;

    private List<FileVO> images;

    private String gearPositionCode;
    //批发商档主
    private String shiftMaster;

}
