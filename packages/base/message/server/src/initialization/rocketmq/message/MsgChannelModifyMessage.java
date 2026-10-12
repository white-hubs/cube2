package com.guhecloud.cube.base.message2.server.initialization.rocketmq.message;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MsgChannelModifyMessage {

    public final static String TOPIC = "MSG_CHANNEL_MANUFACTURER_MODIFY";

    private Long satMsgManufacturerId;

    private ChannelEnum channelCode;

    private String manufacturerCode;

    private String config;
}
