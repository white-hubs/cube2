package com.guhecloud.cube.base.message2.server.handler.handler.send;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.service.MsgRecordService;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.exeception.MessageExceptionConstant;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

/**
 * 消息发送各个渠道的处理器
 *
 * @author lanchun
 * @date 2023/3/13 14:29
 */
@Slf4j
public abstract class BaseMsgSender implements MsgSender {

    @Autowired
    private MsgSendHandlerHolder msgSendHandlerHolder;

    @Autowired
    private ManufacturerHolder manufacturerHolder;

    @Autowired
    protected MsgRecordService msgRecordService;

    protected ManufacturerSender manufacturerSender;

    protected ChannelEnum channelCode;

    @PostConstruct
    private void initHandler() {
        this.msgSendHandlerHolder.putHandler(channelCode, this);
    }

    @Override
    public MessageSendResponse send(MessageInfo messageInfo) {
        MessageSendResponse sendResponse;
        String verificationErrorMsg = this.verification(messageInfo);
        if (StringUtils.isNotEmpty(verificationErrorMsg)) {
            sendResponse = MessageSendResponse
                    .builder()
                    .id(messageInfo.getId())
                    .succeed(false)
                    .failReceivers(messageInfo.getReceiver()
                                           .stream()
                                           .map(phone -> new MessageSendResponse.Receiver(phone, verificationErrorMsg))
                                           .collect(Collectors.toList()))
                    .build();
        } else {
            //获取通道发送器
            this.manufacturerSender = this.manufacturerHolder.getHandler(
                    messageInfo.getChannelCode().name() + "-" + messageInfo.getManufacturerCode().name());
            sendResponse = this.doSend(messageInfo);
        }
        //发送结果入库
        this.saveSendResult(messageInfo, sendResponse);
        return sendResponse;
    }

    public void saveSendResult(MessageInfo messageInfo, MessageSendResponse messageSendResponse) {
        //发送状态入库
        try {
            SessionContextHolder.requestTenant(messageInfo.getTenant());
            this.msgRecordService.updateMsgRecordStatus(messageInfo.getId(), messageSendResponse.getSucceed(), DateUtils.now(), messageSendResponse.getFailReceivers());
        } catch (Exception e) {
            log.error("消息任务为{}已发送,但写入数据库失败,发送响应为:{},失败原因:{}", messageInfo.getId(), messageSendResponse, e);
        }
    }

    public abstract MessageSendResponse doSend(MessageInfo messageInfo);

    public String verification(MessageInfo messageInfo) {
        if (StringUtils.isEmpty(messageInfo.getSendAccountInfo())) {
            return MessageExceptionConstant.MANUFACTURER_NOT_CONFIGURED;
        }
        return StringUtils.EMPTY_STRING;
    }
}
