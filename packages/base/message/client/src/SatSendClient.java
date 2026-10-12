package com.guhecloud.cube.base.message2.client;

import com.guhecloud.cube.base.message2.client.param.SatMsgSendParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "message2")
public interface SatSendClient {

    /**
     * 消息发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-msg-send/send")
    Boolean sendMsg(@Valid @RequestBody SatMsgSendParam param);
}
