package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.event;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request.UnisInsightChannelListRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightBaseResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightChannelResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.response.UnisInsightPageResponse;
import lombok.Getter;

@Getter
public class UnisInsightChannelListEvent
        extends ReportEvent<UnisInsightBaseResponse<UnisInsightPageResponse<UnisInsightChannelResponse>>> {

    private final UnisInsightChannelListRequest unisInsightChannelListRequest;

    public UnisInsightChannelListEvent(Object source, UnisInsightChannelListRequest unisInsightChannelListRequest) {
        super(source);
        this.unisInsightChannelListRequest = unisInsightChannelListRequest;
    }
}
