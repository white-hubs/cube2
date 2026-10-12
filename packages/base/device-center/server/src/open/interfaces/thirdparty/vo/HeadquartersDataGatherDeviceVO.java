package com.guhecloud.cube.base.device.center.open.interfaces.thirdparty.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HeadquartersDataGatherDeviceVO {
    private Long id;
    private String status;
    private String iotDeviceProdCategoryName;
    private String deviceName;
    private String iotDeviceProdName;
    private String applicationCode;
    private String areaId;
    private String areaName;
    private String departmentId;
    private String departmentName;

    private Integer tenant;
    private Boolean deleted;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private String applicationTag;
}
