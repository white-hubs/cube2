package com.guhecloud.cube.customization.xialing.server.liangcang.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 海康门禁远程核验回调 JSON。
 *
 * @param ipAddress 设备 IP 地址
 * @param portNo 设备端口
 * @param protocol 通信协议
 * @param macAddress 设备 MAC 地址
 * @param channelId 通道 ID
 * @param dateTime 事件时间
 * @param activePostCount 当前主动上报次数
 * @param eventType 事件类型
 * @param eventState 事件状态
 * @param eventDescription 事件描述
 * @param shortSerialNumber 设备短序列号
 * @param accessControllerEvent 门禁事件明细
 */
public record HikRemoteCheckRequest(
        String ipAddress,
        Integer portNo,
        String protocol,
        String macAddress,
        @JsonProperty("channelID") Integer channelId,
        String dateTime,
        Integer activePostCount,
        String eventType,
        String eventState,
        String eventDescription,
        String shortSerialNumber,
        @JsonProperty("AccessControllerEvent") HikRemoteCheckAccessControllerEventRequest accessControllerEvent
) {
}
