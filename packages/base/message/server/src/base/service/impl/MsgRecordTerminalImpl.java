package com.guhecloud.cube.base.message2.server.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.base.dao.MsgRecordTerminalMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecordTerminal;
import com.guhecloud.cube.base.message2.server.base.service.MsgRecordTerminalService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgRecordTerminalImpl extends ServiceImpl<MsgRecordTerminalMapper, MsgRecordTerminal> implements MsgRecordTerminalService {
    @Override
    public List<MsgRecordTerminal> listMsgRecordTerminal(Long recordId) {
        return this.lambdaQuery().eq(MsgRecordTerminal::getMsgRecordId, recordId).list();
    }

    @Override
    public List<MsgRecordTerminal> listMsgRecordTerminal(Collection<Long> recordIds) {
        return this.lambdaQuery().in(MsgRecordTerminal::getMsgRecordId, recordIds).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatchByMsgRecordIds(@NonNull Set<Long> recordIds) {
        this.lambdaUpdate()
                .set(MsgRecordTerminal::getDeleted, System.currentTimeMillis())
                .set(MsgRecordTerminal::getUpdateTime, new Date())
                .in(MsgRecordTerminal::getMsgRecordId, recordIds)
                .update();

    }
}
