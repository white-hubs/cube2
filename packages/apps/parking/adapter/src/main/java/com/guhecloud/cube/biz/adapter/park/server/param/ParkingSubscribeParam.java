package com.guhecloud.cube.biz.adapter.park.server.param;

import com.guhecloud.cube.biz.adapter.park.server.enumeration.ParkingSubscribeTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 停车场订阅参数
 *
 * @author lanchun
 * @date 2022/10/8 17:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
