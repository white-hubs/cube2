package com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 人流设备小时人流记录
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("traffic_detection_hourly_person_flow")
public class TrafficDetectionHourlyPersonFlowPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 记录时间，精确到小时
     */
    private LocalDateTime recordTime;

    /**
     * 人数
     */
    private Integer personCount;
}
