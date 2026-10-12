package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoPreviewUrlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyPreviewUrlResponse;
import lombok.Getter;

@Getter
public class GhyVideoPreviewUrlEvent extends ReportEvent<GhyBaseResponse<GhyPreviewUrlResponse>> {

    private final GhyVideoPreviewUrlRequest ghyVideoPreviewUrlRequest;

    public GhyVideoPreviewUrlEvent(Object source, GhyVideoPreviewUrlRequest ghyVideoPreviewUrlRequest) {
        super(source);
        this.ghyVideoPreviewUrlRequest = ghyVideoPreviewUrlRequest;
    }
}
