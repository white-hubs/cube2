package com.guhecloud.platform.redis.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Set;

@Getter
public class TokenRemoveEvent extends ApplicationEvent {

    private final String account;

    private final String terminal;

    private final Set<String> removeTokens;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public TokenRemoveEvent(Object source, String account, String terminal, Set<String> removeTokens) {
        super(source);
        this.account = account;
        this.terminal = terminal;
        this.removeTokens = removeTokens;
    }
}
