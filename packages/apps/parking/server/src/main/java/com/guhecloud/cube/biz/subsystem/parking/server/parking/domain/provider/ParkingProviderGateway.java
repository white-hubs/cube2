package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.provider;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingProviderEnum;

public interface ParkingProviderGateway {

    /**
     * 获取车场接入方式
     *
     * @return 车场接入方式
     */
    ParkingProviderEnum getProvider();

    /**
     * 激活车场接入
     *
     * @param parkingInfo 车厂配置（包含接入方式等参数）
     */
    void activate(ParkingInfo parkingInfo);

    /**
     * 刷新车场信息
     *
     * @param parkingInfo
     */
    void refreshParkingInfo(ParkingInfo parkingInfo);

    /**
     * 停用车场接入
     */
    void deactivate(ParkingInfo parkingInfo);
}
