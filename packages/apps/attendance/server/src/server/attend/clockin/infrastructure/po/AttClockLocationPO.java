package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@TableName("att_clock_location")
@EqualsAndHashCode(callSuper = true)
public class AttClockLocationPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    /**
     * 规则id
     */
    private Long rulesId;
    /**
     * 地点
     */
    private String address;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 维度
     */
    private String latitude;
    /**
     * 范围
     */
    private Integer ranges;
}
