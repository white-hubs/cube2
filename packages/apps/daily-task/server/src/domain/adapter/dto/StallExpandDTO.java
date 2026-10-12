package com.guhecloud.cube.biz.daily.task.domain.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 档位扩展信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StallExpandDTO {

    private Long id;

    /**
     * 档位主键
     */
    private Long stallId;

    /**
     * 经纬度信息
     */
    private String lntAndLat;

    /**
     * 位置描述信息
     */
    private String locationDes;
}
