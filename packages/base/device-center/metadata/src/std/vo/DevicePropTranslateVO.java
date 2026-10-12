package com.guhecloud.cube.base.device.center.metadata.std.vo;

import lombok.Data;

@Data
public class DevicePropTranslateVO {

    /**
     * 对外展示的值,例如空调模式，对外展示的是夏季模式或者冬季模式
     * 再例如温度对外展示的就是 温度值，例如23 ，24
     */
    private String name;
    /**
     * 真实的值
     */
    private String value;
}
