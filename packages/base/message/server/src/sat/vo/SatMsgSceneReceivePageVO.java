package com.guhecloud.cube.base.message2.server.sat.vo;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.sat.enumeration.SatMsgSceneReceiveModeEnum;
import lombok.Data;

import java.util.List;

@Data
public class SatMsgSceneReceivePageVO {

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
}
