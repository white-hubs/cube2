package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request.GhyVideoCaptureRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.response.GhyCaptureResponse;
import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/21 14:58
 * @Description:
 */
@Getter
public class GhyVideoCaptureEvent extends ReportEvent<GhyBaseResponse<GhyCaptureResponse>> {

    private final GhyVideoCaptureRequest ghyVideoCaptureRequest;
    public GhyVideoCaptureEvent(Object source, GhyVideoCaptureRequest ghyVideoCaptureRequest) {
        super(source);
        this.ghyVideoCaptureRequest = ghyVideoCaptureRequest;
    }
}
