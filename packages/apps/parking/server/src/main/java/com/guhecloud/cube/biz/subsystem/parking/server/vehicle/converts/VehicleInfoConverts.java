package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.converts;

import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleInfoDetailVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleInfoVO;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 17:12
 * @Description:
 */

public final class VehicleInfoConverts {
    public static void convert(VehicleInfoVO param, VehicleInfo vehicleInfo) {
        vehicleInfo.setVehicleNo(param.getVehicleNo());
        vehicleInfo.setVehicleTypeCode(param.getVehicleTypeCode());
        vehicleInfo.setVehicleTypeName(param.getVehicleTypeName());
        vehicleInfo.setCustomerName(param.getCustomerName());
        vehicleInfo.setCustomerMobile(param.getCustomerMobile());
        vehicleInfo.setVehicleNoColor(param.getVehicleNoColor());
    }

    public static VehicleInfoDetailVO convertToDetailVO(VehicleInfo vehicleInfo) {
        VehicleInfoDetailVO infoDetailVO = new VehicleInfoDetailVO();
        infoDetailVO.setId(vehicleInfo.getId());
        infoDetailVO.setVehicleNo(vehicleInfo.getVehicleNo());
        infoDetailVO.setVehicleTypeCode(vehicleInfo.getVehicleTypeCode());
        infoDetailVO.setVehicleTypeName(vehicleInfo.getVehicleTypeName());
        infoDetailVO.setCustomerName(vehicleInfo.getCustomerName());
        infoDetailVO.setCustomerMobile(vehicleInfo.getCustomerMobile());
        infoDetailVO.setVehicleNoColor(vehicleInfo.getVehicleNoColor());
        return infoDetailVO;
    }
}
