package com.guhecloud.cube.base.flow.flowable.common.message;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author pengjian
 * @date 2022/9/2 9:14
 * @description
 */
@Getter
@AllArgsConstructor
public class NotifyUser {
    private Long userId;
    private Set<ChannelEnum> channelTypes;

    public NotifyUser(Long userId) {
        this.userId = userId;
        this.channelTypes = initDefaultChannelTypes();
    }

    private Set<ChannelEnum> initDefaultChannelTypes() {
        Set<ChannelEnum> ChannelEnums = new HashSet<>();
        Collections.addAll(ChannelEnums, ChannelEnum.CHANNEL_APP, ChannelEnum.CHANNEL_SITE, ChannelEnum.CHANNEL_SMS);
        return ChannelEnums;
    }
}
