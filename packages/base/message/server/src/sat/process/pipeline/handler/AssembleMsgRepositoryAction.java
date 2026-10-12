package com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler;

import com.guhecloud.cube.base.message2.server.base.entity.MsgRecord;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecordTarget;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecordTerminal;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.service.MsgRecordService;
import com.guhecloud.cube.base.message2.server.base.service.MsgRecordTargetService;
import com.guhecloud.cube.base.message2.server.base.service.MsgRecordTerminalService;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.site.dto.SiteTerminal;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.BusinessProcess;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.ProcessContext;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.context.MsgSendContext;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssembleMsgRepositoryAction implements BusinessProcess<MsgSendContext> {

    private final MsgRecordService msgRecordService;

    private final MsgRecordTargetService msgRecordTargetService;

    private final MsgRecordTerminalService msgRecordTerminalService;

    @Override
    public void process(ProcessContext<MsgSendContext> context) {
        List<MsgRecord> msgRecords = new ArrayList<>(context.getProcessModel().getMessageRecordInfos().size());
        List<MsgRecordTarget> msgRecordTargets = new ArrayList<>();
        List<MsgRecordTerminal> msgRecordTerminals = new ArrayList<>();
        context.getProcessModel().getMessageRecordInfos().forEach(messageRecordInfo -> {
            MsgRecord msgRecord = ObjectUtils.copying(messageRecordInfo, MsgRecord.class);
            msgRecords.add(msgRecord);
            messageRecordInfo.getMessageRecordTargetInfos().forEach(messageRecordTargetInfo ->
                                                                            msgRecordTargets.add(MsgRecordTarget.builder()
                                                                                                         .id(messageRecordTargetInfo.getId())
                                                                                                         .channelCode(messageRecordInfo.getChannelCode())
                                                                                                         .msgRecordId(messageRecordInfo.getId())
                                                                                                         .userId(messageRecordTargetInfo.getUserId())
                                                                                                         .userName(messageRecordTargetInfo.getUserName())
                                                                                                         .userPhone(messageRecordTargetInfo.getUserPhone())
                                                                                                         .userEmail(messageRecordTargetInfo.getUserEmail())
                                                                                                         .receiver(messageRecordTargetInfo.getReceiver())
                                                                                                         .sourceTenant(messageRecordTargetInfo.getSourceTenant())
                                                                                                         .sendTime(DateUtils.now())
                                                                                                         .sendTimes(1)
                                                                                                         .build())
                                                                   );
            //站内信按端拆分
            if (ChannelEnum.CHANNEL_SITE == messageRecordInfo.getChannelCode() &&
                StringUtils.isNotEmpty(messageRecordInfo.getTmpConfig())) {
                //拆分终端
                SiteTerminal siteTerminal = JsonUtils.deserialize(messageRecordInfo.getTmpConfig(), SiteTerminal.class);
                siteTerminal.getTerminals().forEach(terminal -> {
                    MsgRecordTerminal msgRecordTerminal = new MsgRecordTerminal();
                    msgRecordTerminal.setMsgRecordId(messageRecordInfo.getId());
                    msgRecordTerminal.setTerminal(terminal);
                    msgRecordTerminals.add(msgRecordTerminal);
                });
            }
        });
        SessionContextHolder.requestTenant(msgRecords.get(0).getTenant());
        this.msgRecordService.saveBatch(msgRecords);
        this.msgRecordTargetService.saveBatch(msgRecordTargets);
        if (!CollectionUtils.isEmpty(msgRecordTerminals)) {
            this.msgRecordTerminalService.saveBatch(msgRecordTerminals);
        }
        context.setNeedBreak(false);
    }
}
