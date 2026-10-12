package com.guhecloud.cube.biz.fusion.communicate.server.gps.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadGpsParam {

    /**
     * 方向
     */
    private Integer direction;

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    private Double lng;

    /**
     * 纬度
     */
    @NotNull(message = "纬度不能为空")
    private Double lat;

    /**
     * 位置信息
     */
    private String address;

    /**
     * 高度
     */
    private Float height;

    /**
     * 速度
     */
    private Float speed;
}
