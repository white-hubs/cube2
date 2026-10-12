package com.guhecloud.cube.base.message2.server.sat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.sat.dao.SatMsgSceneReceiveChannelMapper;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceiveChannel;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgSceneReceiveChannelService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SatMsgSceneReceiveChannelServiceImpl extends ServiceImpl<SatMsgSceneReceiveChannelMapper, SatMsgSceneReceiveChannel> implements SatMsgSceneReceiveChannelService {

    @Override
    public List<SatMsgSceneReceiveChannel> getChannelsBySceneReceiveIds(Collection<Long> satMsgSceneReceiveIds) {
        return CollectionUtils.isEmpty(satMsgSceneReceiveIds) ? Collections.emptyList() :
               this.lambdaQuery()
                       .in(SatMsgSceneReceiveChannel::getSatMsgSceneReceiveId, satMsgSceneReceiveIds)
                       .list();
    }

    @Override
    public SatMsgSceneReceiveChannel getSatMsgSceneReceiveChannel(@NonNull Long satMsgSceneReceiveId,
                                                                  @NonNull ChannelEnum channelCode
                                                                 ) {
        return this.baseMapper.selectOne(Wrappers.<SatMsgSceneReceiveChannel>lambdaQuery()
                                                 .eq(SatMsgSceneReceiveChannel::getSatMsgSceneReceiveId, satMsgSceneReceiveId)
                                                 .eq(SatMsgSceneReceiveChannel::getChannelCode, channelCode));
    }

    @Override
    public Long getCountByChannel(@NonNull ChannelEnum ChannelEnum) {
        return this.baseMapper.selectCount(Wrappers.<SatMsgSceneReceiveChannel>lambdaQuery()
                                                   .eq(SatMsgSceneReceiveChannel::getChannelCode, ChannelEnum));
    }
}
