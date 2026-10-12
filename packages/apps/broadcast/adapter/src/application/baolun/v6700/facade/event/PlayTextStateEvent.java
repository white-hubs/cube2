package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.request.PlayTextStateRequestAbstract;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.response.PlayTextStateResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public class PlayTextStateEvent extends ApplicationEvent implements Supplier<PlayTextStateResponse>, Consumer<PlayTextStateResponse> {

    private final PlayTextStateRequestAbstract playTextStateRequest;

    private PlayTextStateResponse playTextStateResponse;

    public PlayTextStateEvent(Object source, PlayTextStateRequestAbstract playTextStateRequest) {
        super(source);
        this.playTextStateRequest = playTextStateRequest;
    }

    @Override
    public void accept(PlayTextStateResponse playTextStateResponse) {
        this.playTextStateResponse = playTextStateResponse;
    }

    @Override
    public PlayTextStateResponse get() {
        return this.playTextStateResponse;
    }
}
