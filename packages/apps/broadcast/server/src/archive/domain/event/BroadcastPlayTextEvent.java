package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.event;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.BroadcastPlayTextResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public class BroadcastPlayTextEvent extends ApplicationEvent implements Supplier<BroadcastPlayTextResponse>, Consumer<BroadcastPlayTextResponse> {

    private final String content;

    private final Set<String> devices;

    private final Integer playTime;

    private BroadcastPlayTextResponse broadcastPlayTextResponse;

    public BroadcastPlayTextEvent(Object source, String content, Set<String> devices, Integer playTime) {
        super(source);
        this.content = content;
        this.devices = devices;
        this.playTime = playTime;
    }

    @Override
    public void accept(BroadcastPlayTextResponse broadcastPlayTextResponse) {
        this.broadcastPlayTextResponse = broadcastPlayTextResponse;
    }

    @Override
    public BroadcastPlayTextResponse get() {
        return this.broadcastPlayTextResponse;
    }
}
