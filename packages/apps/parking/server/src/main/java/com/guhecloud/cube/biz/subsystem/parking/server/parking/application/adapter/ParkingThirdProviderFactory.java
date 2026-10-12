package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.adapter;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingProviderEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.provider.ParkingProviderGateway;

import java.util.Map;

public class ParkingThirdProviderFactory {

    private final Map<ParkingProviderEnum, ParkingProviderGateway> providerMap;

    public ParkingThirdProviderFactory(Map<ParkingProviderEnum, ParkingProviderGateway> providerMap) {
        this.providerMap = providerMap;
    }

    /**
     * 获取第三方车场接入网关
     *
     * @param provider 车场接入方式
     *
     * @return 车场接入网关
     */
    public ParkingProviderGateway getProvider(ParkingProviderEnum provider) {
        return providerMap.get(provider);
    }
}
