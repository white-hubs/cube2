package com.guhecloud.cube.base.bracing.controller;

import com.guhecloud.cube.base.bracing.dto.MessageConfigDTO;
import com.guhecloud.cube.base.bracing.dto.SceneConfigDTO;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.bracing.service.MessageAssemblyNoticeService;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * 业务消息个性化配置
 *
 * @author wql
 * @describe 消息配置
 * @date 2023/9/1 17:55
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageAssemblyNoticeController {
    private final MessageAssemblyNoticeService messageAssemblyNoticeService;

    /**
     * 添加消息配置
     *
     * @Author wql
     * @Description 添加消息配置
     * @Date 9:40 2023/9/4
     **/
    @PostMapping("/message/addConfig")
    public void addMessageConfig(@RequestBody @Validated MessageConfigDTO messageConfig) {
        messageAssemblyNoticeService.addMessageConfig(messageConfig);
    }

    /**
     * 查询消息配置
     *
     * @Author wql
     * @Description 查询消息配置
     * @Date 9:40 2023/9/4
     **/
    @GetMapping("/message/getConfigInfo")
    public List<SceneConfigDTO> getMessageConfigInfo(@NotBlank String businessType, String label) {
        return messageAssemblyNoticeService.getMessageConfigInfo(businessType, label);
    }

    /**
     * 查询消息场景信息
     *
     * @Author wql
     * @Description 查询消息发送配置
     * @Date 16:56 2023/9/4
     **/
    @PostMapping("/message/getSendConfig")
    public List<MessageSendConfig> getSendMsgConfig(@RequestBody @Validated MessageSendSceneQueryPram queryPram) {
        return messageAssemblyNoticeService.getSendMsgConfig(queryPram);
    }

    /**
     * 消息发送
     *
     * @Author wql
     * @Description 消息发送
     * @Date 15:54 2023/9/4
     **/
    @PostMapping("/message/send")
    public MessageSendResponse sendMsg(@RequestBody @Validated MessageSendParam sendParam) {
        return messageAssemblyNoticeService.sendMsg(sendParam);
    }

}
