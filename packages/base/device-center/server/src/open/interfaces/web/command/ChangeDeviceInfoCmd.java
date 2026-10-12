package com.guhecloud.cube.base.device.center.open.interfaces.web.command;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * @author pengjian
 * @date 2024/1/11 14:33
 * @description
 */
@Data
public class ChangeDeviceInfoCmd {
    @NotNull(message = "参数错误，设备不能为空")
    private Long deviceId;

    /**
     * 三维模型编码
     */
    private String deviceCode;

    /**
     * 最后一级
     */
    private Long areaId;

    /**
     * 区域信息，级联
     */
    private List<Long> areaIds;

    /**
     * 设备安装图片
     */
    private Long picture;

    /**
     * 部门ID
     */
    private Long departmentId;

    /**
     * 管理人员
     */
    private Set<Long> managerIds;

    /**
     * 经纬度
     */
    private String lntAndLat;

    /**
     * 位置描述
     */
    private String locationDes;

    /**
     * 采购单价
     */
    private Integer unitPrice;

    /**
     * 采购日期
     */
    private LocalDate purchaseDate;

    /**
     * 生产日期
     */
    private LocalDate productionDate;

    /**
     * 使用寿命，单位（年）
     */
    private Integer usefulLife;

    /**
     * 空间坐标信息
     */
    private String spatialCoordinates;
}
