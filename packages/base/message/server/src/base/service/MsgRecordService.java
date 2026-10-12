package com.guhecloud.cube.base.message2.server.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecord;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.param.MsgRecordQueryByTenantParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgRecordQueryParam;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordApplicationTotalVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordPageVO;
import com.guhecloud.cube.base.message2.server.base.vo.MsgRecordTypeTotalVO;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface MsgRecordService extends IService<MsgRecord> {

    void updateMsgRecordStatus(Long id, Boolean sendStatus, LocalDateTime sendTime, List<MessageSendResponse.Receiver> failReceiverInfos);

    void updateMsgRecordTargetStatus(Long id, Boolean sendStatus, LocalDateTime sendTime, String errorDescribe);

    @Override
    MsgRecord getById(Serializable id);

    List<MsgRecord> listByBusinessId(String businessId);

    List<MsgRecordTypeTotalVO> msgTypeUnreadTotal(ChannelEnum channelType);

    List<MsgRecordApplicationTotalVO> msgApplicationUnreadTotal(ChannelEnum channelType);

    Page<MsgRecordPageVO> page(MsgRecordQueryParam param);

    Page<MsgRecordPageVO> page(MsgRecordQueryByTenantParam param);

    void removeExpireDel();

    void remove(Long id);

    void removeBatchByBusinessIds(Set<String> businessIds);
}
