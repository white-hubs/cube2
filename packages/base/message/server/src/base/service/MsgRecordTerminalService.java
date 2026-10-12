package com.guhecloud.cube.base.message2.server.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecordTerminal;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface MsgRecordTerminalService extends IService<MsgRecordTerminal> {

    List<MsgRecordTerminal> listMsgRecordTerminal(Long recordId);

    List<MsgRecordTerminal> listMsgRecordTerminal(Collection<Long> recordIds);

    void removeBatchByMsgRecordIds(Set<Long> recordIds);
}
