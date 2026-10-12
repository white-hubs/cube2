package com.guhecloud.cube.base.bracing.api;

import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author wql
 * @describe 消息配置
 * @date 2023/9/5 10:24
 */
@FeignClient("messageBracing")
public interface AssemblyNoticeClient {

    //获取消息发送场景
    @PostMapping("/message/getSendConfig")
    List<MessageSendConfig> getSendMsgConfig(@RequestBody @Validated MessageSendSceneQueryPram queryPram);

    //消息发送
    @PostMapping("/message/send")
    MessageSendResponse sendMsg(@RequestBody @Validated MessageSendParam sendParam);
}
