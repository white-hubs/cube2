package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 资产数据采集配置
 * @date 2023-09-13 15:08:36
 */
@Data
public class PropertyFiling {

    //资产id
    private Long id;
    //资产名称
    private String propertyName;
    //资产类别
    private String propertyType;
    //入库时间
    private LocalDateTime putInTime;
    //维保到期时间
    private LocalDateTime expirationTime;
    //资产状态
    private String status;
    //管理员id
    private Long managerId;
    //管理员名称
    private String managerName;
    //区域id
    private String areaId;
    //区域名称
    private String areaName;
    //使用人
    private Long userId;
    //使用人
    private String username;
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

    private String propertySubType;

    private Integer num;

    /**
     * 资产编码
     */
    private String serialNumber;
    /**
     * 使用部门
     */
    private String receiveDepName;
    /**
     * 维护部门
     */
    private String maintenanceDepName;
    //
    private String propertySubTypeName;
    /**
     * 模型编码
     */
    private String bimCode;
    /**
     * 属性
     */
    private String attribute;
    /**
     * 档位名称
     */
    private String stallName;
    /**
     * 现场点名称
     */
    private String sitePointName;
    private String address;
    /**
     * 电动车类型
     */
    private String electronMobileType;
}