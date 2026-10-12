package com.guhecloud.cube.base.message.sdk.api.request;

import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageChannelRequest {

    /**
     * 模板id
     */
    private Long tmpId;

    /**
     * 通道编号
     */
    private MessageChannelEnum channelCode;

    /**
     * 扩展配置
     */
    private String extendConfig;

    /**
     * 租户编号
     */
    private Integer tenant;
}
