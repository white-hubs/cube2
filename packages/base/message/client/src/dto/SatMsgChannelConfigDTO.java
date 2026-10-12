package com.guhecloud.cube.base.message2.client.dto;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.Data;

/**
 * 平台消息通道配置参数
 *
 * @author lanchun
 * @date 2021/11/10 17:23
 */
@Data
public class SatMsgChannelConfigDTO {

    /**
     * 通道类型
     */
    private ChannelEnum channelType;

    /**
     * 访问端编号
     */
    private String terminal;

}
