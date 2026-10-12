package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoSnapRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyCaptureResponse;
import lombok.Getter;

@Getter
public class GhyVideoSnapEvent extends ReportEvent<GhyBaseResponse<GhyCaptureResponse>> {

    private final GhyVideoSnapRequest ghyVideoSnapRequest;

    public GhyVideoSnapEvent(Object source, GhyVideoSnapRequest ghyVideoSnapRequest) {
        super(source);
        this.ghyVideoSnapRequest = ghyVideoSnapRequest;
    }
}
