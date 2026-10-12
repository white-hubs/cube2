package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response.H5sVideoLoginResponse;
import lombok.Getter;

@Getter
public class H5sVideoLoginEvent extends ReportEvent<H5sVideoLoginResponse> {

    public H5sVideoLoginEvent(Object source) {
        super(source);
    }
}
