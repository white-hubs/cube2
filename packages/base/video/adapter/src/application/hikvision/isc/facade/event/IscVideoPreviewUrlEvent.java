package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request.IscVideoPreviewUrlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.BasicResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.IscVideoPreviewUrlResponse;
import lombok.Getter;

@Getter
public class IscVideoPreviewUrlEvent extends ReportEvent<BasicResponse<IscVideoPreviewUrlResponse>> {

    private final IscVideoPreviewUrlRequest iscVideoPreviewUrlRequest;

    public IscVideoPreviewUrlEvent(Object source, IscVideoPreviewUrlRequest iscVideoPreviewUrlRequest) {
        super(source);
        this.iscVideoPreviewUrlRequest = iscVideoPreviewUrlRequest;
    }
}
