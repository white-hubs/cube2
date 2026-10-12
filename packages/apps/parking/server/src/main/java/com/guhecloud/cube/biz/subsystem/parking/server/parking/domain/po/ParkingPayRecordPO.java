package com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po;

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
 * 缴费记录
 *
 * @author lanchun
 * @date 2022/10/13 13:42
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("parking_pay_record")
public class ParkingPayRecordPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 通行唯一标识
     */
    private String identifyId;

    /**
     * 缴费时间
     */
    private LocalDateTime payTime;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 停车时长
     */
    private Integer parkingTimes;

    /**
     * 收费方式
     */
    private String payType;

    /**
     * 应收金额
     */
    private BigDecimal receivableAmount;

    /**
     * 实收金额
     */
    private BigDecimal freeAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
}
