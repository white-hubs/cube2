package com.guhecloud.cube.biz.emergency.server.facade.iot.subscribe.dto;

import com.guhecloud.cube.iot.north.bridge.consumer.vo.CommandRespEventMsg;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author pengjian
 * @date 2022/7/6 17:49
 * @description
 */
@Data
public class EventTriggerDTO {
    private String deviceId;
    private String eventCode;
    private String instanceId;
    private String timestamp;
    private String delayEventTime;
    private List<CommandRespEventMsg> matchSnapshot;
    private Map<String, Object> relatedProp;
    private Map<String, Object> params;
    private String notifyStatus;
    private String notifyStatusTs;
    //暂未实现
    private String urgencyLvl;
}
