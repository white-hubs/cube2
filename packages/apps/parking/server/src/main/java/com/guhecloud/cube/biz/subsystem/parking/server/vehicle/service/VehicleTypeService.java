package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleTypePO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleTypeParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleTypeQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleTypeVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 16:37
 * @Description:
 */

public interface VehicleTypeService extends IService<VehicleTypePO> {
    Boolean saveOrUpdate(VehicleTypeParam param);

    Pagination<VehicleTypeVO> getVehicleTypPage(VehicleTypeQueryParam param);

    List<VehicleTypeVO> getVehicleTypList(VehicleTypeQueryParam param);
}
