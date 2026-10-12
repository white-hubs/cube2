package com.guhecloud.cube.biz.linkage.client;

import com.guhecloud.apps.linkage.metadata.dto.MessageTargetNotifyDTO;
import com.guhecloud.apps.linkage.metadata.param.EventMessageSendParam;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "linkage")
public interface LinkageMessageClient {

    /**
     * 联动消息立即通知
     *
     * @param eventMessageSendParam 发送参数
     */
    @PostMapping("/linage/message/immediately/notify")
    void sendLinkageMessage(@RequestBody @Valid EventMessageSendParam eventMessageSendParam);

    /**
     * 构造消息发送人信息
     *
     * @param targetNotifies 对象通知配置
     *
     * @return 消息发送人信息
     */
    @PostMapping("/linage/message/notify/targets")
    List<MessageNotifyTarget> buildMessageNotifyTargets(@RequestBody List<MessageTargetNotifyDTO> targetNotifies);
}
