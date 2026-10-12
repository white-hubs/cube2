package com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.event;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.model.ProviderConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
@Getter
public class ProviderConfigChangeEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    private final ProviderConfig providerConfig;

    private final ProviderConfig newProviderConfig;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ProviderConfigChangeEvent(Object source, ProviderConfig providerConfig, ProviderConfig newProviderConfig) {
        super(source);
        this.providerConfig = providerConfig;
        this.newProviderConfig = newProviderConfig;
    }
}
