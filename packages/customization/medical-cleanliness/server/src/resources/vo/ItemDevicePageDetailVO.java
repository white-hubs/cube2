package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo;

import lombok.Data;

@Data
public class ItemDevicePageDetailVO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 尺寸
     */
    private String size;
    /**
     * 数量
     */
    private String number;
    /**
     * 厂家
     */
    private String manufacturer;
    /**
     * 型号
     */
    private String model;
}
