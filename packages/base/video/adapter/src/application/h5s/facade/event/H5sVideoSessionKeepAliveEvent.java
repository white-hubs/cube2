package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sVideoSessionKeepAliveRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.BasicResponse;
import lombok.Getter;

@Getter
public class H5sVideoSessionKeepAliveEvent extends ReportEvent<BasicResponse> {

    private final H5sVideoSessionKeepAliveRequest h5sVideoSessionKeepAliveRequest;

    public H5sVideoSessionKeepAliveEvent(Object source, H5sVideoSessionKeepAliveRequest h5sVideoSessionKeepAliveRequest) {
        super(source);
        this.h5sVideoSessionKeepAliveRequest = h5sVideoSessionKeepAliveRequest;
    }
}
