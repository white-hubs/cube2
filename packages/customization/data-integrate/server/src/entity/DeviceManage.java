package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 设备数据采集
 * @date 2023-09-13 15:08:36
 */
@Data
public class DeviceManage {

    //设备id
    private Long id;
    //设备状态
    private String status;
    //iot产品分类名称new
    private String iotDeviceProdCategoryName;
    //设备名称
    private String deviceName;
    //iot产品名称new
    private String iotDeviceProdName;
    //应用code
    private String applicationCode;
    //区域iD
    private String areaId;
    //区域名称
    private String areaName;
    //部门id
    private String departmentId;
    //部门名称
    private String departmentName;

    //市场租户
    private Integer marketTenant;
    //市场名称
    private String marketName;

    private Integer tenant;

    private String createBy;

    private String creatorId;

    private ActorTypeEnum creatorType;

    private LocalDateTime createTime;

    private String updateBy;

    private String updaterId;

    private ActorTypeEnum updaterType;

    private LocalDateTime updateTime;

    private Boolean deleted;

}