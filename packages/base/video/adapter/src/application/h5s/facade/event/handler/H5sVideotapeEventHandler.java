package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sVideotapeRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.H5sVideotapeResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.H5sVideotapeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class H5sVideotapeEventHandler extends AbstractSynchronizedHandler<H5sVideotapeEvent, H5sVideotapeRequest> {

    @EventListener
    public void h5sVideotapeEvent(final H5sVideotapeEvent event) {
        H5sVideotapeResponse response = handle(event);
        event.accept(response);
    }

    @Override
    public H5sVideotapeRequest buildRequest(H5sVideotapeEvent t) {
        return t.getVideotapeRequest();
    }

    @Override
    public H5sVideotapeResponse requesting(H5sVideotapeRequest h5sVideotapeRequest) {
        ResponseEntity<H5sVideotapeResponse> exchange = this.requester().exchange(
                h5sVideotapeRequest.getUrl(),
                HttpMethod.GET,
                h5sVideotapeRequest.getHttpEntityNoWithBody(),
                new ParameterizedTypeReference<H5sVideotapeResponse>() {
                },
                h5sVideotapeRequest.getParamForGet()
                                                                                 );
        return exchange.getBody();
    }
}
