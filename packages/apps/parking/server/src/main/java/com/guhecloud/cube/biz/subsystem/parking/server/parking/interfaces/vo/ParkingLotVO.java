package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import lombok.Data;

/**
 * 车位信息返回参数
 *
 * @author lanchun
 * @date 2022/12/23 14:05
 */
@Data
public class ParkingLotVO {

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

    public ParkingLotVO(Integer totalNum, Integer totalInNum, Integer totalSpaceNum) {
        this.totalNum = totalNum;
        this.totalInNum = totalInNum;
        this.totalSpaceNum = totalSpaceNum;
    }
}
