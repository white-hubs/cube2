package com.guhecloud.cube.biz.mdata.server.stall.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 档位扩展信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StallExpandModifyParam {

    /**
     * 经纬度信息
     */
    private String lntAndLat;

    /**
     * 位置描述信息
     */
    private String locationDes;
}
