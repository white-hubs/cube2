package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request.IscVideoSnapCaptureRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.BasicResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.IscVideoSnapCaptureResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event.IscVideoSnapCaptureEvent;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IscVideoSnapCaptureEventHandler extends AbstractSynchronizedHandler<IscVideoSnapCaptureEvent, IscVideoSnapCaptureRequest> {

    @EventListener
    public void iscVideoSnapCaptureEvent(final IscVideoSnapCaptureEvent iscVideoSnapCaptureEvent) {
        BasicResponse<IscVideoSnapCaptureResponse> iscVideoSnapCaptureResponseBasicResponse = this.handle(iscVideoSnapCaptureEvent);
        iscVideoSnapCaptureEvent.accept(iscVideoSnapCaptureResponseBasicResponse);
    }

    @Override
    public IscVideoSnapCaptureRequest buildRequest(IscVideoSnapCaptureEvent event) {
        return event.getIscVideoSnapCaptureRequest();
    }

    @Override
    public BasicResponse<IscVideoSnapCaptureResponse> requesting(IscVideoSnapCaptureRequest iscVideoSnapCaptureRequest) {
        String result = ArtemisHttpUtil.doPostStringArtemis(
                iscVideoSnapCaptureRequest.getUrl(),
                iscVideoSnapCaptureRequest.getHttpEntity(iscVideoSnapCaptureRequest),
                null,
                null,
                iscVideoSnapCaptureRequest.getContentType(),
                null);// post请求application/json类型参数
        if (StringUtils.isNotEmpty(result)) {
            return JsonUtils.deserialize(result, new TypeReference<BasicResponse<IscVideoSnapCaptureResponse>>() {
            });
        }
        return null;
    }
}
