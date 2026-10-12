package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request.IscVideoPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.BasicResponse;
import lombok.Getter;

@Getter
public class IscVideoPtzControlEvent extends ReportEvent<BasicResponse> {

    private final IscVideoPtzControlRequest iscVideoPtzControlRequest;

    public IscVideoPtzControlEvent(Object source, IscVideoPtzControlRequest iscVideoPtzControlRequest) {
        super(source);
        this.iscVideoPtzControlRequest = iscVideoPtzControlRequest;
    }
}
