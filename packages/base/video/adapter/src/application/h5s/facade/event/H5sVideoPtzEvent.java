package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.BasicResponse;
import lombok.Getter;

@Getter
public class H5sVideoPtzEvent extends ReportEvent<BasicResponse> {

    private final H5sPtzControlRequest h5sPtzControlRequest;

    public H5sVideoPtzEvent(Object source, H5sPtzControlRequest h5sPtzControlRequest) {
        super(source);
        this.h5sPtzControlRequest = h5sPtzControlRequest;
    }
}
