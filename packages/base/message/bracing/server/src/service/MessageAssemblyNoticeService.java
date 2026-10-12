package com.guhecloud.cube.base.bracing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.bracing.dto.MessageConfigDTO;
import com.guhecloud.cube.base.bracing.dto.SceneConfigDTO;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.bracing.po.MessageAssemblyNotice;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;

import java.util.List;

/**
 * @author wql
 * @describe 通知配置
 * @date 2023/9/1 17:48
 */
public interface MessageAssemblyNoticeService extends IService<MessageAssemblyNotice> {
    /**
     * @Author wql
     * @Description 添加消息配置
     * @Date 10:24 2023/9/4
     **/
    void addMessageConfig(MessageConfigDTO messageConfig);

    /**
     * @Author wql
     * @Description 查询消息配置
     * @Date 11:01 2023/9/4
     **/
    List<SceneConfigDTO> getMessageConfigInfo(String businessType, String label);

    /**
     * @Author wql
     * @Description 消息发送
     * @Date 15:55 2023/9/4
     **/
    MessageSendResponse sendMsg(MessageSendParam sendParam);

    /**
     * @Author wql
     * @Description 查询消息发送配置
     * @Date 17:00 2023/9/4
     **/
    List<MessageSendConfig> getSendMsgConfig(MessageSendSceneQueryPram queryPram);
}
