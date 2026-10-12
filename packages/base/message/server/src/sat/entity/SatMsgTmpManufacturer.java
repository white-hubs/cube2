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
 * 模板厂家信息
 *
 * @author lanchun
 * @date 2023/6/26 14:26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SatMsgTmpManufacturer extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 模板主键
     */
    private Long satMsgTmpId;

    /**
     * 通道编号
     */
    private ChannelEnum channelCode;

    /**
     * 租户厂商主键
     */
    private Long satMsgManufacturerId;

    /**
     * 租户模板配置
     */
    private String manufacturerTmpConfig;

    /**
     * 厂家编号
     */
    private String manufacturerCode;

    /**
     * 状态
     */
    private String status;

    /**
     * 状态描述
     */
    private String statusDesc;
}
