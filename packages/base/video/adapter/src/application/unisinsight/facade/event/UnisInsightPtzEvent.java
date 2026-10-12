package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request.UnisInsightPtzRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightBaseResponse;
import lombok.Getter;

@Getter
public class UnisInsightPtzEvent extends ReportEvent<UnisInsightBaseResponse<?>> {

    private final UnisInsightPtzRequest unisInsightPtzRequest;

    public UnisInsightPtzEvent(Object source, UnisInsightPtzRequest unisInsightPtzRequest) {
        super(source);
        this.unisInsightPtzRequest = unisInsightPtzRequest;
    }
}
