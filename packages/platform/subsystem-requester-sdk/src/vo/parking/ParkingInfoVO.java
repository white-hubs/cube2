package com.guhecloud.subsystem.requester.sdk.vo.parking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车场信息返回参数
 *
 * @author lanchun
 * @date 2022/9/28 17:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingInfoVO {

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
