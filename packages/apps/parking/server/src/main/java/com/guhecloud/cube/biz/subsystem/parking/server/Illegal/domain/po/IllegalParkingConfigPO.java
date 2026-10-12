package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.enumeration.IllegalLevelEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 16:04
 * @Description: 违停配置
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("illegal_parking_config")
public class IllegalParkingConfigPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 统计周期
     */
    private String timeCycle;

    /**
     * 违停程度
     */
    private IllegalLevelEnum illegalLevel;

    /**
     * 扣分值
     */
    private BigDecimal score;
}
