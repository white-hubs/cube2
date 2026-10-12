package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DaHuaRealtimeRequest<T extends AbstractDaHuaRequest> extends AbstractDaHuaRequest {

    private T data;

    @Override
    public String getRequestUrl() {
        return data.getRequestUrl();
    }
}
