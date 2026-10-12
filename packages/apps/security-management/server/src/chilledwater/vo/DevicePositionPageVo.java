package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

@Data
public class DevicePositionPageVo {
    //设备名称
    private String deviceName;
    //设备id
    private Long deviceId;
    //品类
    private String deviceProdCategoryName;

    //设备状态
    private Boolean deviceStatus;

    //电磁阀
    private String solenoidValve;
    //区域
    private String area;
    //档位编码
    private String gearPositionCode;

    private String code;

    private List<FileVO> images;
    //品类映射code
    private String mapCode;
}
