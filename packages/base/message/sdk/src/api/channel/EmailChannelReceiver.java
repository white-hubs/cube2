package com.guhecloud.cube.base.message.sdk.api.channel;

import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message2.client.param.MsgSendTargetParam;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class EmailChannelReceiver implements ChannelReceiver {
    @Override
    public String getChannelCode() {
        return MessageChannelEnum.CHANNEL_EMAIL.name();
    }

    @Override
    public List<MsgSendTargetParam> getReceiver(List<UserDTO> userDTOS) {
        return userDTOS.stream()
                .filter(userDTO -> StringUtils.isNotBlank(userDTO.getEmail()))
                .map(userDTO ->
                             MsgSendTargetParam.builder()
                                     .userId(userDTO.getId())
                                     .userName(userDTO.getUsername())
                                     .userPhone(userDTO.getMobile())
                                     .receiver(userDTO.getEmail())
                                     .build()
                    ).collect(Collectors.toList());
    }
}
