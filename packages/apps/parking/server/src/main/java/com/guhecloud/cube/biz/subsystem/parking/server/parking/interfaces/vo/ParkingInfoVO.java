package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

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
     * 模型区域
     */
    private String modelArea;

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

    /**
     * 区域id
     */
    private String areaId;

    /**
     * 区域名称
     */
    private String areaName;
}
