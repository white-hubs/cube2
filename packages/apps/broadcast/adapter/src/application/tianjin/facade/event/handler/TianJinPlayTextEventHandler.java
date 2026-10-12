package com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.facade.event.handler;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.facade.event.TianJinPlayTextEvent;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.tianjin.facade.request.TianJinPlayTextRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TianJinPlayTextEventHandler extends AbstractTianJinSynchronizedHandler<TianJinPlayTextEvent, TianJinPlayTextRequest> {

    @EventListener
    public void playText(final TianJinPlayTextEvent playTextEvent) {
        String playTextResponse = this.handle(playTextEvent);
        playTextEvent.accept(playTextResponse);
    }

    @Override
    public TianJinPlayTextRequest buildRequest(TianJinPlayTextEvent t) {
        return t.getTianJinPlayTextRequest();
    }

    @Override
    public String requesting(TianJinPlayTextRequest playTextRequest) {
        ResponseEntity<String> responseResponseEntity = requester().exchange(
                playTextRequest.getUrl(),
                HttpMethod.GET,
                playTextRequest.getHttpEntity(playTextRequest),
                new ParameterizedTypeReference<String>() {
                });
        log.info("天津广播文本播放返回内容{}", responseResponseEntity);
        return responseResponseEntity.getBody();
    }
}
