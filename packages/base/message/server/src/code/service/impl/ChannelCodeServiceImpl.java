package com.guhecloud.cube.base.message2.server.code.service.impl;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.code.holder.CodeServiceHolder;
import com.guhecloud.cube.base.message2.server.code.service.CodeService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ChannelCodeServiceImpl implements CodeService {
    @Autowired
    protected CodeServiceHolder codeServiceHolder;

    @PostConstruct
    protected void init() {
        codeServiceHolder.putCodeServiceMap(getChannel(), this);
    }

    abstract ChannelEnum getChannel();
}
