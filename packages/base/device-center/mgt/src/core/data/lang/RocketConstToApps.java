package com.guhecloud.cube.iot.mgt.core.data.lang;

/**
 * RocketMQ 消息常量
 * <p>
 * 用于向各个应用发送消息的主题名称
 * </p>
 * <p>
 * 注意：这些常量是为了与其他应用进行通信而设计的，确保在使用时遵循相应的消息格式和协议。
 * </p>
 */
public class RocketConstToApps {

    /**
     * 三方设备ID发现
     */
    public static final String IOT_THIRD_PARTY_ID_BIND = "IOT_THIRD_PARTY_ID_BIND";

    /**
     * 设备配置绑定
     */
    public static final String IOT_DEVICE_CFG_BID = "IOT_DEVICE_CFG_BID";

    /**
     * 摄像头设备确认
     */
    public static final String IOT_CAMERA_DEVICE_CONFIRMED = "IOT_CAMERA_DEVICE_CONFIRMED";

    /**
     * 设备编码和名称变更
     */
    public static final String IOT_DEVICE_NUMBER_NAME_CHANGE = "IOT_DEVICE_NUMBER_NAME_CHANGE";
    /**
     * 内部数据变更<br/>
     * eg: 三方id绑定事件
     */
    public static final String IOT_INNER_DATA_CHANGE = "IOT_INNER_DATA_CHANGE";


    /**
     * 内部数据变更-代理<br/>
     * 设备创建无法直接通知到serverOne 中的deviceCenter,通过MGT中的EventListener 转发！（在不改动DeviceCenter的情况下）
     */
    public static final String IOT_INNER_DATA_CHANGE_PROXY = "IOT_INNER_DATA_CHANGE_PROXY";

    /**
     * 设备注册成功
     */
    public static final String IOT_DEVICE_REG_SUCCEED = "IOT_DEVICE_REG_SUCCEED";


    private RocketConstToApps() {
    }
}
