package com.guhecloud.subsystem.requester.sdk.vo.car;

import lombok.Data;

@Data
public class CarInfoVO {

    private Long id;
    //车牌号
    private String carNo;

    private String carTypeId;

    private String carTypeName;

    private String driverName;

    private String phone;
}
