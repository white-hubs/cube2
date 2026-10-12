package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event.handler;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event.PlayTextStateEvent;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.request.PlayTextStateRequestAbstract;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.response.PlayTextStateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PlayTextStateEventHandler extends AbstractSynchronizedHandler<PlayTextStateEvent, PlayTextStateRequestAbstract> {

    @EventListener
    public void getPlayTextState(final PlayTextStateEvent playTextStateEvent) {
        PlayTextStateResponse playTextStateResponse = this.handle(playTextStateEvent);
        playTextStateEvent.accept(playTextStateResponse);
    }

    @Override
    public PlayTextStateRequestAbstract buildRequest(PlayTextStateEvent t) {
        return t.getPlayTextStateRequest();
    }

    @Override
    public PlayTextStateResponse requesting(PlayTextStateRequestAbstract playTextStateRequest) {
        ResponseEntity<PlayTextStateResponse> responseResponseEntity = requester().exchange(
                playTextStateRequest.getUrl(),
                HttpMethod.POST,
                playTextStateRequest.getHttpEntity(playTextStateRequest),
                new ParameterizedTypeReference<PlayTextStateResponse>() {
                });
        return responseResponseEntity.getBody();
    }
}
