package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 15:49
 * @Description: 车牌扣分记录
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("vehicle_penalty_record")
public class VehiclePenaltyRecordPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 关联违停记录ID
     */
    private Long illegalRecordId;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 违停程度
     */
    private String illegalLevel;

    /**
     * 违停扣分分数
     */
    private BigDecimal violationScore;

    /**
     * 违停时间
     */
    private LocalDateTime illegalTime;

}
