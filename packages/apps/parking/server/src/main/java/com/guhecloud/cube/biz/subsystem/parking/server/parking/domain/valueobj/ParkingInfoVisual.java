package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车场视角配置值对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingInfoVisual {

    /**
     * 中心位置
     */
    private String centerAddress;

    /**
     * 视角参数
     */
    private String visualConfig;

    /**
     * 隐藏楼栋
     */
    private String hiddenBuilding;

    /**
     * 创建默认视角配置
     */
    public static ParkingInfoVisual createDefault() {
        return ParkingInfoVisual.builder()
                .centerAddress("0,0")
                .visualConfig("{}")
                .hiddenBuilding("")
                .build();
    }

    /**
     * 创建自定义视角配置
     */
    public static ParkingInfoVisual create(String centerAddress, String visualConfig, String hiddenBuilding) {
        return ParkingInfoVisual.builder()
                .centerAddress(centerAddress)
                .visualConfig(visualConfig)
                .hiddenBuilding(hiddenBuilding)
                .build();
    }
}