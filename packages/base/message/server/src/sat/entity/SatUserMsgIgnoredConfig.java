package com.guhecloud.cube.base.message2.server.sat.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SatUserMsgIgnoredConfig extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 是否为次日
     */
    private Boolean tomorrows;

    /**
     * 忽略的通道
     */
    private String channels;

}
