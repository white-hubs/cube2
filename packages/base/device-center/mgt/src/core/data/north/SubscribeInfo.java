package com.guhecloud.cube.iot.mgt.core.data.north;

import com.guhecloud.cube.iot.mgt.core.data.base.Pair;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.NotifyMsgType;
import com.guhecloud.cube.iot.mgt.core.data.north.condition.SubscribeCondition;
import com.guhecloud.cube.iot.mgt.core.data.north.outparam.WebSocketParam;
import lombok.Data;

import java.util.List;

@Data
public class SubscribeInfo {
    // 如何处理重复订阅
    // 不处理重复订阅，那么前一次订阅与本次订阅将导致多次推送

    /**
     * 缓存的数据如下 {@link MsgEventSubscribeBo}
     */
    public static final String V2_NOTIFY_SUBSCRIBE_CACHE_KEY = "iot:subscribe:north:";

    /**
     * 输出消息必要参数
     */
    private WebSocketParam outParam;

    /**
     * 订阅的消息类型
     */
    private List<Pair<NotifyMsgType, SubscribeCondition>> subscribeMsgTypes;

    /**
     * 订阅有效期<br/>
     * 单位 秒
     */
    private long subscribeTTS;

}