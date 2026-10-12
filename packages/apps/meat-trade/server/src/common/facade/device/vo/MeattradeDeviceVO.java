package com.guhecloud.cube.biz.meattrade.server.common.facade.device.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeattradeDeviceVO {
    //设备id
    private Long id;
    //编码
    private String code;
    //设备名称
    private String name;
}