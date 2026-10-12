package com.guhecloud.cube.base.message2.server.factory;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ThirdCodeVerifierFactoryHolder {
    private final Map<ChannelEnum, ThirdCodeVerifierFactory> thirdCodeVerifierFactoryMap = new HashMap<>();

    public ThirdCodeVerifierFactory getFactory(ChannelEnum channelCode) {
        return thirdCodeVerifierFactoryMap.get(channelCode);
    }

    public void putThirdCodeVerifierFactoryMap(ChannelEnum channelCode, ThirdCodeVerifierFactory thirdCodeVerifierFactory) {
        thirdCodeVerifierFactoryMap.put(channelCode, thirdCodeVerifierFactory);
    }
}
