package com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.facade.event.handler;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.facade.event.LiuZhouPlayTextEvent;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.facade.request.LiuZhouPlayTextRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LiuZhouPlayTextEventHandler extends AbstractLiuZhouSynchronizedHandler<LiuZhouPlayTextEvent, LiuZhouPlayTextRequest> {

    @EventListener
    public void playText(final LiuZhouPlayTextEvent playTextEvent) {
        String playTextResponse = this.handle(playTextEvent);
        playTextEvent.accept(playTextResponse);
    }

    @Override
    public LiuZhouPlayTextRequest buildRequest(LiuZhouPlayTextEvent t) {
        return t.getLiuZhouPlayTextRequest();
    }

    @Override
    public String requesting(LiuZhouPlayTextRequest playTextRequest) {
        ResponseEntity<String> responseResponseEntity = requester().exchange(
                playTextRequest.getUrl(),
                HttpMethod.POST,
                playTextRequest.getHttpEntity(playTextRequest),
                new ParameterizedTypeReference<String>() {
                });
        log.info("柳州广播文本播放返回内容{}", responseResponseEntity);
        return responseResponseEntity.getStatusCode().value() + "";
    }
}
