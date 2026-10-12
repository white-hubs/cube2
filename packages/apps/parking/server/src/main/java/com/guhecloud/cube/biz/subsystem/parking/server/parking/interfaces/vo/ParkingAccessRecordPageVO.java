package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 车辆通行记录分页返回参数
 *
 * @author lanchun
 * @date 2022/10/13 14:10
 */
@Data
public class ParkingAccessRecordPageVO {

    private Long id;

    /**
     * 通行唯一标识
     */
    private String identifyId;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 出入标识
     */
    private ParkingAccessTypeEnum accessType;

    /**
     * 通道号
     */
    private String channelNo;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 通行时间
     */
    private LocalDateTime accessTime;

    /**
     * 图片路径
     */
    private String imageUrl;

    /**
     * 图片base64
     */
    private String imageBase;

    /**
     * 车场编号
     */
    private String parkingInfoCode;

    /**
     * 车场名称
     */
    private String parkingInfoName;
}
