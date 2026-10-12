package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity;

import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.enumeration.VehicleBottomMonitoringAutoEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 车底监测记录
 *
 * @author lanchun
 * @date 2024/12/12 16:06
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class VehicleBottomMonitorRecord extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 来访事由名称
     */
    private String reason;

    /**
     * 来访事由编码
     */
    private String reasonType;

    /**
     * 记录编码
     */
    private String recordCode;

    /**
     * 出入标识
     */
    private ParkingAccessTypeEnum accessType;

    /**
     * 出入时间
     */
    private LocalDateTime accessTime;

    /**
     * 车底照片
     */
    private String underBodyPhoto;

    /**
     * 车身抓拍照
     */
    private String bodyPhoto;

    /**
     * 车牌抓拍照
     */
    private String platePhoto;

    /**
     * 是否自动识别
     */
    private VehicleBottomMonitoringAutoEnum isAuto;

    /**
     * 登记地点编码
     */
    private String orgCode;

    /**
     * 抓拍设备编码
     */
    private String deviceCode;

    /**
     * 抓拍设备类型
     */
    private String deviceType;

}
