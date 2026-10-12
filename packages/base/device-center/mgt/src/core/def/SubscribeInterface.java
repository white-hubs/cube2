package com.guhecloud.cube.iot.mgt.core.def;

import com.guhecloud.cube.iot.mgt.core.data.north.SubscribeInfo;
import com.guhecloud.cube.iot.mgt.core.data.north.outparam.WebSocketParam;

import java.util.Set;

/**
 * 设备订阅服务支持,面向所有北向订阅
 */
public interface SubscribeInterface {

    /**
     * 进行消息订阅
     *
     * @param subscribeInfo 订阅详情
     * @return 订阅ID
     */
    String subscribe(SubscribeInfo subscribeInfo);

    /**
     * 取消订阅
     *
     * @param subscribeId 订阅ID
     * @return SubscribeInfo nullable here
     */
    SubscribeInfo unSubscribe(String subscribeId);

    /**
     * 查询订阅信息
     *
     * @param deviceId 设备ID
     * @param propCode 属性代码
     * @return 订阅的WebSocket参数集合
     */
    Set<WebSocketParam> queryDeviceProp(String deviceId, String propCode);

    /**
     * 查询设备事件订阅
     *
     * @param deviceId  设备ID
     * @param eventCode 事件代码
     * @return 订阅的WebSocket参数集合
     */
    Set<WebSocketParam> queryDeviceEvent(String deviceId, String eventCode);

    /**
     * 查询设备状态订阅
     *
     * @param deviceId 设备ID
     * @return 订阅的WebSocket参数集合
     */
    Set<WebSocketParam> queryDeviceStatus(String deviceId);

    /**
     * 查询设备命令
     *
     * @param commandId 设备ID
     * @return 订阅的WebSocket参数集合
     */
    Set<WebSocketParam> queryCommand(String commandId);

    /**
     * 查询规则事件订阅
     *
     * @param ruleId 规则ID
     * @return 订阅的WebSocket参数集合
     */
    Set<WebSocketParam> queryRule(String ruleId);
}
