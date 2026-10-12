package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event.handler;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request.IscVideoPtzControlRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.BasicResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.event.IscVideoPtzControlEvent;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IscVideoPtzControlEventHandler extends AbstractSynchronizedHandler<IscVideoPtzControlEvent, IscVideoPtzControlRequest> {

    @EventListener
    public void iscVideoPtzControl(final IscVideoPtzControlEvent event) {
        BasicResponse response = this.handle(event);
        event.accept(response);
    }

    @Override
    public IscVideoPtzControlRequest buildRequest(IscVideoPtzControlEvent event) {
        return event.getIscVideoPtzControlRequest();
    }

    @Override
    public BasicResponse requesting(IscVideoPtzControlRequest iscVideoPtzControlRequest) {
        String result = ArtemisHttpUtil.doPostStringArtemis(
                iscVideoPtzControlRequest.getUrl(),
                iscVideoPtzControlRequest.getHttpEntity(iscVideoPtzControlRequest),
                null,
                null,
                iscVideoPtzControlRequest.getContentType(),
                null);// post请求application/json类型参数
        if (StringUtils.isNotEmpty(result)) {
            return JsonUtils.deserialize(result, BasicResponse.class);
        }
        return null;
    }
}
