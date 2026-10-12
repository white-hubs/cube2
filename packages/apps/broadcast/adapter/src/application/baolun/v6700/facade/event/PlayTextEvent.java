package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.request.PlayTextRequestAbstract;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.response.PlayTextResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public class PlayTextEvent extends ApplicationEvent implements Supplier<PlayTextResponse>, Consumer<PlayTextResponse> {

    private final PlayTextRequestAbstract playTextRequest;

    private PlayTextResponse playTextResponse;

    public PlayTextEvent(Object source, PlayTextRequestAbstract playTextRequest) {
        super(source);
        this.playTextRequest = playTextRequest;
    }

    @Override
    public void accept(PlayTextResponse playTextResponse) {
        this.playTextResponse = playTextResponse;
    }

    @Override
    public PlayTextResponse get() {
        return this.playTextResponse;
    }
}
