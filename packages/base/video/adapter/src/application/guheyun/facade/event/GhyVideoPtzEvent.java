package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoPtzRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyBaseResponse;
import lombok.Getter;

@Getter
public class GhyVideoPtzEvent extends ReportEvent<GhyBaseResponse<?>> {

    private final GhyVideoPtzRequest ghyVideoPtzRequest;

    public GhyVideoPtzEvent(Object source, GhyVideoPtzRequest ghyVideoPtzRequest) {
        super(source);
        this.ghyVideoPtzRequest = ghyVideoPtzRequest;
    }
}
