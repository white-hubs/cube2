package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sVideoPhotographRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.H5sVideoPhotographResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.H5sVideoPhotographEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class H5sVideoPhotographEventHandler extends AbstractSynchronizedHandler<H5sVideoPhotographEvent, H5sVideoPhotographRequest> {

    @EventListener
    public void h5sVideoPhotographEvent(final H5sVideoPhotographEvent event) {
        H5sVideoPhotographResponse response = handle(event);
        event.accept(response);
    }

    @Override
    public H5sVideoPhotographRequest buildRequest(H5sVideoPhotographEvent t) {
        return t.getH5sVideoPhotographRequest();
    }

    @Override
    public H5sVideoPhotographResponse requesting(H5sVideoPhotographRequest h5sVideoPhotographRequest) {
        ResponseEntity<H5sVideoPhotographResponse> exchange = this.requester().exchange(
                h5sVideoPhotographRequest.getUrl(),
                HttpMethod.GET,
                h5sVideoPhotographRequest.getHttpEntityNoWithBody(),
                new ParameterizedTypeReference<H5sVideoPhotographResponse>() {
                },
                h5sVideoPhotographRequest.getParamForGet()
                                                                                       );
        return exchange.getBody();
    }
}
