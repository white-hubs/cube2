package com.guhecloud.cube.base.message2.client.dto;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.client.enumeration.SatMsgSceneReceiveModeEnum;
import lombok.Data;

import java.util.List;

@Data
public class SatMsgSceneReceiveDTO {

    private Long id;

    /**
     * 场景编号
     */
    private String msgSceneCode;

    /**
     * 场景名称
     */
    private String msgSceneName;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 通道编号列表
     */
    private List<ChannelEnum> channelCodes;

    /**
     * 接收模式
     */
    private SatMsgSceneReceiveModeEnum receiveMode;

    /**
     * 用户列表信息
     */
    private List<SatMsgSceneReceiveUserDTO> users;
}
