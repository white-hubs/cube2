package com.guhecloud.cube.srv.prepaid.server.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 日用水实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("water_use_day")
@EqualsAndHashCode(callSuper = true)
public class WaterUseDayPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 水表编号
     */
    private String meterId;
    /**
     * 日期
     */
    private LocalDateTime useMonth;
    /**
     * 日期
     */
    private LocalDateTime useDate;
    /**
     * 剩余水量
     */
    private String powerRemain;
    /**
     * 本段时间内的用水量
     */
    private String pv;
    /**
     * 当前抄表用水量
     */
    private String currentPv;
    /**
     * 上次抄表用水量
     */
    private String lastPv;
}
