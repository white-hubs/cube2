package com.guhecloud.cube.base.message2.server.sat.vo;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 租户消息通道返回参数
 *
 * @author lanchun
 * @date 2023/3/8 9:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SatMsgChannelVO {

    /**
     * 通道编码
     */
    private ChannelEnum channelCode;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 厂家编号
     */
    private ManufacturerCodeEnum manufacturerCode;

    /**
     * 厂家名称
     */
    private String manufacturerName;

    /**
     * 厂家配置信息
     */
    private String config;
}
