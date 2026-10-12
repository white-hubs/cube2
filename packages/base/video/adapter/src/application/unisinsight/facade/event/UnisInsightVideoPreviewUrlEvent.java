package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request.UnisInsightVideoPreviewUrlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightBaseResponse;
import lombok.Getter;

@Getter
public class UnisInsightVideoPreviewUrlEvent extends ReportEvent<UnisInsightBaseResponse<String>> {

    private final UnisInsightVideoPreviewUrlRequest unisInsightVideoPreviewUrlRequest;

    public UnisInsightVideoPreviewUrlEvent(Object source, UnisInsightVideoPreviewUrlRequest unisInsightVideoPreviewUrlRequest) {
        super(source);
        this.unisInsightVideoPreviewUrlRequest = unisInsightVideoPreviewUrlRequest;
    }
}
