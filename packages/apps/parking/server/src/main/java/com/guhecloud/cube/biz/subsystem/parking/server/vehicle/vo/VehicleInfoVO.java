package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo;

import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.enumeration.VehicleNoColorEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 车辆信息
 */
@Data
public class VehicleInfoVO {
    private Long id;
    /**
     * 车牌号码
     */
    private String vehicleNo;
    /**
     * 车辆类型编号
     */
    private String vehicleTypeCode;
    /**
     * 车辆类型名称
     */
    private String vehicleTypeName;
    /**
     * 车主姓名
     */
    private String customerName;
    /**
     * 车主联系手机号
     */
    private String customerMobile;

    /**
     * 车牌颜色
     */
    private VehicleNoColorEnum vehicleNoColor;

    /**
     * 是否加入黑名单
     */
    private Boolean isBlacklist;

    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    private Boolean deleted;
    private Integer tenant;

    private List<Long> fileIdList;
}
