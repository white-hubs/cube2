package com.guhecloud.cube.base.message2.server.sat.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import lombok.Data;

/**
 * 消息厂家配置参数
 *
 * @author lanchun
 * @date 2023/6/21 14:22
 */
@Data
public class SatMsgManufacturerModifyParam {

    /**
     * 通道编号
     */
    private ChannelEnum channelCode;

    /**
     * 厂家编号
     */
    private ManufacturerCodeEnum manufacturerCode;

    /**
     * 通道厂家配置
     */
    private String config;
}
