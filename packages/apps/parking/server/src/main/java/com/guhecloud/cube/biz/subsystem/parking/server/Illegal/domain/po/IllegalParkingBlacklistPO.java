package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 15:54
 * @Description: 违停黑名单
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("illegal_parking_blacklist")
public class IllegalParkingBlacklistPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 车主
     */
    private String owner;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 加入原因
     */
    private String reason;

    /**
     * 车场编号
     */
    private String parkingLot;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
}
