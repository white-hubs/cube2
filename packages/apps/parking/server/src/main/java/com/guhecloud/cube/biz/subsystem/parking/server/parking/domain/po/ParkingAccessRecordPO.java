package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 车辆通行记录
 *
 * @author lanchun
 * @date 2022/10/13 13:41
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("parking_access_record")
public class ParkingAccessRecordPO extends BasicEnhancedTenantEntity<Long> {

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
     * 图片路径，长度500（OSS URL 等）
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
