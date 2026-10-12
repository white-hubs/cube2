package com.guhecloud.cube.iot.north.bridge.consumer.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author mioopp
 * @create 2021-11-26 15:48
 */
@Data
public class DeviceEventTriggerMsg {

    private String deviceId;

    private String eventCode;

    private String instanceId;

    private String timestamp;

    private String delayEventTime;

    private List<CommandRespEventMsg> matchSnapshot;

    /**
     * 事件关联的属性
     */
    private Map<String, Object> relatedProp;

    /**
     * 事件参数
     */
    private Map<String, Object> params;

    private String notifyStatus;

    private String notifyStatusTs;

    //暂未实现
    private String urgencyLvl;

}
