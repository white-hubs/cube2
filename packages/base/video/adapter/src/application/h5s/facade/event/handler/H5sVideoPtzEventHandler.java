package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.BasicResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.H5sVideoPtzEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class H5sVideoPtzEventHandler extends AbstractSynchronizedHandler<H5sVideoPtzEvent, H5sPtzControlRequest> {

    @EventListener
    public void h5sVideoPtzEvent(final H5sVideoPtzEvent event) {
        BasicResponse basicResponse = handle(event);
        event.accept(basicResponse);
    }

    @Override
    public H5sPtzControlRequest buildRequest(H5sVideoPtzEvent t) {
        return t.getH5sPtzControlRequest();
    }

    @Override
    public BasicResponse requesting(H5sPtzControlRequest h5sPtzControlRequest) {
        ResponseEntity<BasicResponse> exchange = this.requester().exchange(
                h5sPtzControlRequest.getUrl(),
                HttpMethod.GET,
                h5sPtzControlRequest.getHttpEntityNoWithBody(),
                new ParameterizedTypeReference<BasicResponse>() {
                },
                h5sPtzControlRequest.getParamForGet()
                                                                          );
        return exchange.getBody();
    }
}
