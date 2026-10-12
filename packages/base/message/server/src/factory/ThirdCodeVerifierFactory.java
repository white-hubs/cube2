package com.guhecloud.cube.base.message2.server.factory;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.handler.handler.verifier.CodeVerify;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ThirdCodeVerifierFactory {
    @Autowired
    protected ThirdCodeVerifierFactoryHolder thirdCodeVerifierFactoryHolder;

    public abstract CodeVerify getVerifier();

    @PostConstruct
    protected void init() {
        thirdCodeVerifierFactoryHolder.putThirdCodeVerifierFactoryMap(getChannel(), this);
    }

    protected abstract ChannelEnum getChannel();
}
