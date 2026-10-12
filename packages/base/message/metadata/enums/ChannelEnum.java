package com.guhecloud.base.message.metadata.enums;

public enum ChannelEnum {
    /**
     * 站内信消息
     */
    CHANNEL_SITE("站内通知"),

    /**
     * app消息
     */
    CHANNEL_APP("app通知"),

    /**
     * 短信消息
     */
    CHANNEL_SMS("短信通知"),

    /**
     * 语音通知
     */
    CHANNEL_VOICE("语音通知"),

    /**
     * 企业微信通知
     */
    CHANNEL_WE_COM("企业微信通知"),

    /**
     * 微信公众服务号通知
     */
    CHANNEL_WE_PUBLIC_SERVICE_ACCOUNT("微信公众服务号"),

    /**
     * 邮箱通知
     */
    CHANNEL_EMAIL("邮箱");

    ChannelEnum(String description) {this.description = description;}

    private final String description;

    public String getDescription() {
        return description;
    }
}
