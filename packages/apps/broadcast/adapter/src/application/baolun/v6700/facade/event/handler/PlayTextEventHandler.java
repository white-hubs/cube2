package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event.handler;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.event.PlayTextEvent;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.request.PlayTextRequestAbstract;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.response.PlayTextResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PlayTextEventHandler extends AbstractSynchronizedHandler<PlayTextEvent, PlayTextRequestAbstract> {

    @EventListener
    public void playText(final PlayTextEvent playTextEvent) {
        PlayTextResponse playTextResponse = this.handle(playTextEvent);
        playTextEvent.accept(playTextResponse);
    }

    @Override
    public PlayTextRequestAbstract buildRequest(PlayTextEvent t) {
        return t.getPlayTextRequest();
    }

    @Override
    public PlayTextResponse requesting(PlayTextRequestAbstract playTextRequest) {
        ResponseEntity<PlayTextResponse> responseResponseEntity = requester().exchange(
                playTextRequest.getUrl(),
                HttpMethod.POST,
                playTextRequest.getHttpEntity(playTextRequest),
                new ParameterizedTypeReference<PlayTextResponse>() {
                });
        return responseResponseEntity.getBody();
    }
}
