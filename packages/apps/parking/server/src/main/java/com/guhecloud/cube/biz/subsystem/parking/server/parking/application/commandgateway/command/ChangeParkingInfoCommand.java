package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingSourceEnum;
import lombok.Data;

import java.util.Set;

@Data
public class ChangeParkingInfoCommand {

    private Long id;

    /**
     * 车场编号
     */
    private String code;

    /**
     * 车场名称
     */
    private String name;

    /**
     * 模型区域
     */
    private String modelArea;

    /**
     * 车位总数
     */
    private Integer totalNum;

    /**
     * 车场来源
     */
    private ParkingSourceEnum source;

    /**
     * 所在区域
     */
    private String areaId;

    /**
     * 车场地址
     */
    private String address;

    /**
     * 车场备注
     */
    private String remarks;

    /**
     * 车场图片
     */
    private Set<Long> files;

    /**
     * 车场关联设备
     */
    private ModifyParkingInfoDeviceCommand refDevice;

    /**
     * 车场可视化配置
     */
    private ModifyParkingInfoVisualCommand visual;
}
