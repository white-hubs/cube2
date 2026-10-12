package com.guhecloud.cube.base.message2.server.sat.param;

import com.guhecloud.cube.base.message2.server.sat.enumeration.SatMsgSceneReceiveModeEnum;
import lombok.Data;

import java.util.List;

/**
 * 消息场景接收配置参数
 *
 * @author lanchun
 * @date 2023/6/26 15:54
 */
@Data
public class SatMsgSceneReceiveModifyParam {

    /**
     * 接收模式
     */
    private SatMsgSceneReceiveModeEnum receiveMode;

    /**
     * 用户主键集合
     */
    private List<SatMsgSceneReceiveUserModifyParam> users;
}
