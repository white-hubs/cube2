package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TaskCheckInParam {
    /**
     * 经度
     */
    private BigDecimal longitude;
    ;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 地点名称
     */
    private String locationName;

    /**
     * 打开的图片文件（没有可以不传）
     */
    private String files;

}
