package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.enumeration.VehicleNoColorEnum;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 17:19
 * @Description:
 */
@Data
public class VehicleInfoDetailVO {
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
     * 文件
     */
    private List<FileVO> fileVOList;
}
