package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo;

import lombok.Data;

/**
 * @author lanchun
 * @date 2023/10/25 15:24
 */
@Data
public class CarInfoVO {

    private Long id;

    /**
     * 车牌号
     */
    private String plateNo;

    /**
     * 车主
     */
    private String carOwnerName;

    /**
     * 车辆类型(车卡)
     */
    private String cardName;

    /**
     * 联系电话
     */
    private String phone;
}
