package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.config.H5sClientHttpConfig;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sVideoLoginRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.H5sVideoLoginResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event.H5sVideoLoginEvent;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.DigestUtils;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class H5sVideoLoginEventHandler extends AbstractSynchronizedHandler<H5sVideoLoginEvent, H5sVideoLoginRequest> {

    @EventListener
    public void h5sVideoLoginEvent(final H5sVideoLoginEvent event) {
        H5sVideoLoginResponse response = handle(event);
        event.accept(response);
    }

    @Override
    public H5sVideoLoginRequest buildRequest(H5sVideoLoginEvent t) {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        H5sClientHttpConfig h5sClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(subsystemRedisUtils
                                                                                                    .getSubAdapterRefreshConfig()
                                                                                                    .getManufacturerJson()), H5sClientHttpConfig.class);
        H5sVideoLoginRequest h5sVideoLoginRequest = new H5sVideoLoginRequest();
        h5sVideoLoginRequest.setUser(h5sClientHttpConfig.getAccount());
        h5sVideoLoginRequest.setPassword(DigestUtils.md5Hex(h5sClientHttpConfig.getPwd()).toLowerCase());
        return h5sVideoLoginRequest;
    }

    @Override
    public H5sVideoLoginResponse requesting(H5sVideoLoginRequest h5sVideoLoginRequest) {
        ResponseEntity<H5sVideoLoginResponse> exchange = this.requester().exchange(
                h5sVideoLoginRequest.getUrl(),
                HttpMethod.GET,
                h5sVideoLoginRequest.getHttpEntityNoWithBody(),
                new ParameterizedTypeReference<H5sVideoLoginResponse>() {
                },
                h5sVideoLoginRequest.getParamForGet()
                                                                                  );
        return exchange.getBody();
    }
}
