package com.guhecloud.cube.base.message.sdk.api.channel;

import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message2.client.param.MsgSendTargetParam;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class SmsChannelReceiver implements ChannelReceiver {

    @Override
    public String getChannelCode() {
        return MessageChannelEnum.CHANNEL_SMS.name();
    }

    @Override
    public List<MsgSendTargetParam> getReceiver(List<UserDTO> userDTOS) {
        return userDTOS.stream()
                .map(userDTO ->
                             MsgSendTargetParam.builder()
                                     .userId(userDTO.getId())
                                     .userName(userDTO.getUsername())
                                     .userPhone(userDTO.getMobile())
                                     .receiver(userDTO.getMobile())
                                     .build()
                    ).collect(Collectors.toList());
    }
}
