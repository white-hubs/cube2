package com.guhecloud.cube.base.message2.server.sat.entity;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 租户通道厂商配置
 *
 * @author lanchun
 * @date 2023/3/7 10:47
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SatMsgChannel extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 通道编号
     */
    private ChannelEnum channelCode;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 供应商配置主键
     */
    private Long satMsgManufacturerId;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
