package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sVideoSessionKeepAliveRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.BasicResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.H5sVideoSessionKeepAliveEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class H5sVideoSessionKeepAliveEventHandler extends AbstractSynchronizedHandler<H5sVideoSessionKeepAliveEvent, H5sVideoSessionKeepAliveRequest> {

    @EventListener
    public void h5sVideoSessionKeepAliveEvent(final H5sVideoSessionKeepAliveEvent event) {
        BasicResponse basicResponse = handle(event);
        event.accept(basicResponse);
    }

    @Override
    public H5sVideoSessionKeepAliveRequest buildRequest(H5sVideoSessionKeepAliveEvent t) {
        return t.getH5sVideoSessionKeepAliveRequest();
    }

    @Override
    public BasicResponse requesting(H5sVideoSessionKeepAliveRequest h5sVideoSessionKeepAliveRequest) {
        ResponseEntity<BasicResponse> exchange = this.requester().exchange(
                h5sVideoSessionKeepAliveRequest.getUrl(),
                HttpMethod.GET,
                h5sVideoSessionKeepAliveRequest.getHttpEntityNoWithBody(),
                new ParameterizedTypeReference<BasicResponse>() {
                },
                h5sVideoSessionKeepAliveRequest.getParamForGet()
                                                                          );
        return exchange.getBody();
    }
}
