package com.guhecloud.cube.base.message2.server.sat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceiveChannel;

import java.util.Collection;
import java.util.List;

public interface SatMsgSceneReceiveChannelService extends IService<SatMsgSceneReceiveChannel> {

    List<SatMsgSceneReceiveChannel> getChannelsBySceneReceiveIds(Collection<Long> satMsgSceneReceiveIds);

    SatMsgSceneReceiveChannel getSatMsgSceneReceiveChannel(Long satMsgSceneReceiveId, ChannelEnum channelCode);

    Long getCountByChannel(ChannelEnum ChannelEnum);
}
