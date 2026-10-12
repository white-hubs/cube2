package com.guhecloud.cube.iot.mgt.north.websocket.msg;

import com.guhecloud.cube.iot.mgt.core.data.base.Pair;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.SubscribeCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MsgInSubscribe extends MsgIn {

    private List<Pair<NotifyMsgType, SubscribeCondition>> subscribeMsgTypes;
    private long subscribeTTSWithSecond;
}
