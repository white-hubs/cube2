package com.guhecloud.cube.base.message2.server.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecordTarget;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MsgRecordTargetService extends IService<MsgRecordTarget> {

    MsgRecordTarget getByRecordId(Long msgRecordId, Long userId);

    List<MsgRecordTarget> failList(ChannelEnum channelType);

    List<MsgRecordTarget> listByMsgRecordIds(Collection<Long> msgRecordIds);

    Long unReadTotal(ChannelEnum channelType);

    void readAll(ChannelEnum ChannelEnum);

    void readAllMsgType(ChannelEnum ChannelEnum, String messageTypeEnum);

    void readAllApplicationMsg(ChannelEnum ChannelEnum, String applicationCode);

    void readBatch(List<Long> msgRecordIds);

    void remove(Long msgRecordId);

    void removeBatch(List<Long> msgRecordIds);

    void removeBatchByBusinessIds(Set<String> businessIds);
}
