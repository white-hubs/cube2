package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingProviderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车场对接配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingInfoDockConfig {

    /**
     * 厂家编码
     */
    private ParkingProviderEnum providerCode;

    /**
     * 车场对接配置
     */
    private String config;

    /**
     * 创建对接配置
     */
    public static ParkingInfoDockConfig create(ParkingProviderEnum providerCode, String config) {
        return ParkingInfoDockConfig.builder()
                .providerCode(providerCode)
                .config(config)
                .build();
    }
}
