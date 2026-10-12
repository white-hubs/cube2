package com.guhecloud.cube.base.message2.server.base.entity;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通道接入厂家
 *
 * @author lanchun
 * @date 2023/3/7 9:51
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MsgChannelManufacturer implements Entity<Long> {

    private Long id;

    /**
     * 通道编码
     */
    private ChannelEnum channelCode;

    /**
     * 厂家编码
     */
    @Unique(composites = { "channelCode" }, message = "通道下已存在此厂家")
    private ManufacturerCodeEnum manufacturerCode;

    /**
     * 账号规则
     */
    private String accountRule;

    /**
     * 账号规则描述
     */
    private String accountRuleRemark;
}
