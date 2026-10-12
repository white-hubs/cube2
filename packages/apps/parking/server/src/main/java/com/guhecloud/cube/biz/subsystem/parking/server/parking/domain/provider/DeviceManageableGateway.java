package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.provider;

public interface DeviceManageableGateway extends ParkingProviderGateway {

    /**
     * 刷新更新设备订阅
     */
    void refreshDevices();
}
