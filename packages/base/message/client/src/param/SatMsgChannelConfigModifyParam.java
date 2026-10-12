package com.guhecloud.cube.base.message2.client.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/4/20 14:45
 * @description
 */
@Data
public class SatMsgChannelConfigModifyParam {
    /**
     * 通道类型
     */
    private ChannelEnum channelType;

    /**
     * 推送终端编号(app与站内信必传,否则无法成功推送)
     */
    private String terminal;

    /**
     * 推送终端名称
     */
    private String terminalName;
}
