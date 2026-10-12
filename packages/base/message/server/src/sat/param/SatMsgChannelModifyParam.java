package com.guhecloud.cube.base.message2.server.sat.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import lombok.Data;

/**
 * 租户通道厂商配置新增参数
 *
 * @author lanchun
 * @date 2023/3/7 10:47
 */
@Data
public class SatMsgChannelModifyParam {

    /**
     * 通道编号
     */
    private ChannelEnum channelCode;

    /**
     * 厂家编号
     */
    private ManufacturerCodeEnum manufacturerCode;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 通道厂家配置
     */
    private String config;
}
