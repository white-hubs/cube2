package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

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
public class ParkingInfoDockConfigVO {

    /**
     * 厂家编码
     */
    private ParkingProviderEnum providerCode;

    /**
     * 车场对接配置
     */
    private String config;

}
