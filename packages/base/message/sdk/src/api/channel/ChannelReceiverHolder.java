package com.guhecloud.cube.base.message.sdk.api.channel;

import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;

import java.util.HashMap;
import java.util.Map;

public class ChannelReceiverHolder {

    private static final Map<String, ChannelReceiver> CHANNELS = new HashMap<>();

    static {
        CHANNELS.put(MessageChannelEnum.CHANNEL_SITE.name(), new SiteChannelReceiver());
        CHANNELS.put(MessageChannelEnum.CHANNEL_APP.name(), new AppChannelReceiver());
        CHANNELS.put(MessageChannelEnum.CHANNEL_SMS.name(), new SmsChannelReceiver());
        CHANNELS.put(MessageChannelEnum.CHANNEL_VOICE.name(), new VoiceChannelReceiver());
        CHANNELS.put(MessageChannelEnum.CHANNEL_WE_COM.name(), new WeComChannelReceiver());
        CHANNELS.put(MessageChannelEnum.CHANNEL_WE_PUBLIC_SERVICE_ACCOUNT.name(), new WePublicServiceChannelReceiver());
        CHANNELS.put(MessageChannelEnum.CHANNEL_EMAIL.name(), new EmailChannelReceiver());
    }

    public static ChannelReceiver getChannel(final String channelName) {
        return CHANNELS.get(channelName);
    }
}
