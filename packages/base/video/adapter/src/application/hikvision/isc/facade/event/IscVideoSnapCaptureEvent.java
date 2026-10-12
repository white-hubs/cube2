package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request.IscVideoSnapCaptureRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.BasicResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.IscVideoSnapCaptureResponse;
import lombok.Getter;

@Getter
public class IscVideoSnapCaptureEvent extends ReportEvent<BasicResponse<IscVideoSnapCaptureResponse>> {

    private final IscVideoSnapCaptureRequest iscVideoSnapCaptureRequest;

    public IscVideoSnapCaptureEvent(Object source, IscVideoSnapCaptureRequest iscVideoSnapCaptureRequest) {
        super(source);
        this.iscVideoSnapCaptureRequest = iscVideoSnapCaptureRequest;
    }
}
