package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request.H5sVideotapeRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.H5sVideotapeResponse;
import lombok.Getter;

@Getter
public class H5sVideotapeEvent extends ReportEvent<H5sVideotapeResponse> {

    private final H5sVideotapeRequest videotapeRequest;

    public H5sVideotapeEvent(Object source, H5sVideotapeRequest videotapeRequest) {
        super(source);
        this.videotapeRequest = videotapeRequest;
    }
}
