package com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.context;

import com.guhecloud.cube.base.message2.server.sat.param.SatMsgSendParam;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.ProcessModel;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageRecordInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 消息发送上下文信息
 *
 * @author lanchun
 * @date 2023/5/8 15:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MsgSendContext implements ProcessModel {

    /**
     * 消息参数
     */
    private SatMsgSendParam satMsgSendParam;

    /**
     * 待发送的信息
     */
    private List<MessageInfo> messageInfos;

    /**
     * 消息记录信息
     */
    private List<MessageRecordInfo> messageRecordInfos;
}
