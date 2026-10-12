package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sVideoPhotographRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.H5sVideoPhotographResponse;
import lombok.Getter;

@Getter
public class H5sVideoPhotographEvent extends ReportEvent<H5sVideoPhotographResponse> {

    private final H5sVideoPhotographRequest h5sVideoPhotographRequest;

    public H5sVideoPhotographEvent(Object source, H5sVideoPhotographRequest h5sVideoPhotographRequest) {
        super(source);
        this.h5sVideoPhotographRequest = h5sVideoPhotographRequest;
    }
}
