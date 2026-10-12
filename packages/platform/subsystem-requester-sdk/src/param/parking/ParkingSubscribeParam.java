package com.guhecloud.subsystem.requester.sdk.param.parking;

import com.guhecloud.subsystem.requester.sdk.enumeration.parking.ParkingSubscribeTypeEnum;
import lombok.Data;

/**
 * 停车场订阅参数
 *
 * @author lanchun
 * @date 2022/10/8 17:09
 */
@Data
public class ParkingSubscribeParam {

    /**
     * 停车场订阅类型
     */
    private ParkingSubscribeTypeEnum parkingSubscribeType;

    /**
     * 推送主题
     */
    private String topic;
}
