package com.guhecloud.cube.iot.mgt.core.data.msg.notify;

/**
 * 消息分类
 */
public enum NotifyMsgType {
    //设备状态
    DEVICE_STATUS,
    //设备事件
    DEVICE_EVENT,
    //设备事件附件，不能直接订阅，通过DEVICE_EVENT进行订阅
    DEVICE_EVENT_ADDITION,
    //设备属性
    DEVICE_PROP,
    //设备命令
    DEVICE_COMMAND,
    //设备重置
    DEVICE_COMMAND_RESET,
    //规则触发事件
    RULE_TRIGGER_EVENT
}