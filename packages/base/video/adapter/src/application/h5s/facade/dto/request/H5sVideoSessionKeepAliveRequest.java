package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request;

import lombok.Data;

@Data
public class H5sVideoSessionKeepAliveRequest extends BasicRequest {

    @Override
    public String getUrl() {
        return getUrl("/api/v1/Keepalive")
                .append("?session={session}")
                .toString();
    }
}
