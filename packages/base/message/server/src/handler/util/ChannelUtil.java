package com.guhecloud.cube.base.message2.server.handler.util;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 通道
 *
 * @author lanchun
 * @date 2023/3/10 10:19
 */
public class ChannelUtil {

    /**
     * 获取所有通道类型
     *
     * @return
     */
    public static List<String> getAllChannels() {
        List<String> channelCodes = new ArrayList<>();
        for (ChannelEnum channelCode : ChannelEnum.values()) {
            channelCodes.add(channelCode.name());
        }
        return channelCodes;
    }
}
