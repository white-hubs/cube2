package com.guhecloud.cube.biz.adapter.park.server.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 车场车位停车记录
 *
 * @author lanchun
 * @date 2024/12/17 9:48
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ParkingPlaceInRecordMessage {

    public static final String TOPIC = "PARKING_PLACE_RECORD";

    private Long id;

    /**
     * 停车唯一标识
     */
    private String identifyId;

    /**
     * 摄像机id
     */
    private String cameraId;

    /**
     * 发生时间
     */
    private LocalDateTime occurrenceTime;

    /**
     * 楼层id
     */
    private String floorId;

    /**
     * 标识停入车位or离开车位
     */
    private String type;

    /**
     * 停车场id
     */
    private String parkingId;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 车位id
     */
    private String placeId;

    /**
     * 车牌号
     */
    private String vehicleNo;
}
