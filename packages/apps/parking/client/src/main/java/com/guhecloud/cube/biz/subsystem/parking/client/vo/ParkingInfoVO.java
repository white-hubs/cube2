package com.guhecloud.cube.biz.subsystem.parking.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车位信息返回参数
 *
 * @author lanchun
 * @date 2022/10/13 14:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingInfoVO {

    private Long id;

    /**
     * 车场编号
     */
    private String code;

    /**
     * 车场名称
     */
    private String name;

    /**
     * 车位总数
     */
    private Integer totalNum;

    /**
     * 车位使用总数
     */
    private Integer totalInNum;

    /**
     * 剩余总数
     */
    private Integer totalSpaceNum;
}
