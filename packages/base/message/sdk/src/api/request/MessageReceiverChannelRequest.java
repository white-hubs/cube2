package com.guhecloud.cube.base.message.sdk.api.request;

import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class MessageReceiverChannelRequest {

    /**
     * 模板id
     */
    private Long tmpId;

    /**
     * 通道编号
     */
    private MessageChannelEnum channelCode;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 扩展信息
     */
    private String extendConfig;

    /**
     * 接受则信息(可以是手机号,邮箱等，根据上面通道而定)
     */
    private Set<String> receiver;
}
