package com.guhecloud.cube.base.message.sdk.api.channel;

import com.guhecloud.cube.base.message2.client.param.MsgSendTargetParam;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;

import java.util.List;

public interface ChannelReceiver {

    String getChannelCode();

    List<MsgSendTargetParam> getReceiver(List<UserDTO> userDTOS);
}
