package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request.IscVideoPreviewUrlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.BasicResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.IscVideoPreviewUrlResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event.IscVideoPreviewUrlEvent;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IscVideoPreviewUrlEventHandler extends AbstractSynchronizedHandler<IscVideoPreviewUrlEvent, IscVideoPreviewUrlRequest> {

    @EventListener
    public void iscVideoPreviewUrl(IscVideoPreviewUrlEvent event) {
        BasicResponse<IscVideoPreviewUrlResponse> responseBasicResponse = this.handle(event);
        event.accept(responseBasicResponse);
    }

    @Override
    public IscVideoPreviewUrlRequest buildRequest(IscVideoPreviewUrlEvent event) {
        return event.getIscVideoPreviewUrlRequest();
    }

    @Override
    public BasicResponse<IscVideoPreviewUrlResponse> requesting(IscVideoPreviewUrlRequest iscVideoPreviewUrlRequest) {
        String result = ArtemisHttpUtil.doPostStringArtemis(
                iscVideoPreviewUrlRequest.getUrl(),
                iscVideoPreviewUrlRequest.getHttpEntity(iscVideoPreviewUrlRequest),
                null,
                null,
                iscVideoPreviewUrlRequest.getContentType(),
                null);// post请求application/json类型参数
        if (StringUtils.isNotEmpty(result)) {
            return JsonUtils.deserialize(result, new TypeReference<BasicResponse<IscVideoPreviewUrlResponse>>() {
            });
        }
        return null;
    }
}
