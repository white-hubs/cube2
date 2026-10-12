package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.enumeration.VehicleNoColorEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleInfoBatchParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleInfoQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleInfoDetailVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleInfoVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 车辆服务
 */
public interface VehicleInfoService extends IService<VehicleInfo> {
    Pagination<VehicleInfoVO> getPage(VehicleInfoQueryParam param);

    List<VehicleInfoVO> getAllVehicleEntity();

    void saveOrUpdateBatch(VehicleInfoBatchParam param);

    Boolean saveOrUpdate(VehicleInfoVO param);

    VehicleInfoDetailVO getVehicleInfo(Long id);

    VehicleInfoDetailVO getVehicleInfoByCode(String vehicleNo);

    void saveNotExist(VehicleInfoVO param);

    /**
     * 保存或更新车牌颜色信息。
     * 如果车牌号不存在则新建车辆信息；如果已存在且车牌颜色为空，则更新车牌颜色。
     *
     * @param vehicleNo      车牌号
     * @param vehicleNoColor 车牌颜色
     */
    void saveOrUpdateVehicleNoColor(String vehicleNo, VehicleNoColorEnum vehicleNoColor);
}
