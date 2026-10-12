package com.guhecloud.cube.base.message2.client;

import com.guhecloud.cube.base.message2.client.param.SatMsgSiteSendParam;
import com.guhecloud.cube.base.message2.client.param.SatSiteBroadcastSendParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "message2")
public interface SatMsgSiteSendClient {

    /**
     * 站内信发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-msg-site/send")
    Boolean sendSite(@Valid @RequestBody SatMsgSiteSendParam param);

    /**
     * 站内广播发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-msg-site/broadcast/send")
    Boolean sendBroadcastSite(@Valid @RequestBody SatSiteBroadcastSendParam param);

    /**
     * 站内广播自定义发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-msg-site/customer/broadcast/send")
    Boolean sendCustomerBroadcastSite(@Valid @RequestBody SatSiteBroadcastSendParam param);

    /**
     * 终端用户站内发送
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/sat-msg-site/terminal-user/send")
    Boolean sendByTerminal(@Valid @RequestBody SatMsgSiteSendParam param);
}
