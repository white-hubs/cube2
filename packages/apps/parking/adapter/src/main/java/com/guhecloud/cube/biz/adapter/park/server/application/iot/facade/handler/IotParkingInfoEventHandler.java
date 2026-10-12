package com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.handler;

import com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.event.IotParkingInfoEvent;
import com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.request.IotParkingInfoRequest;
import com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.response.IotBasicResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.response.IotDeviceResponse;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class IotParkingInfoEventHandler extends IotAbstractSynchronizedHandler<IotParkingInfoEvent, IotParkingInfoRequest> {

    public List<IotDeviceResponse> getIotParkingInfo(IotParkingInfoEvent iotParkingInfoEvent) {
        IotBasicResponse<List<IotDeviceResponse>> basicResponse = super.handle(iotParkingInfoEvent);
        log.info("请求物联网平台停车场信息结果:{}", basicResponse);
        if (basicResponse.isSuccess()) {
            List<IotDeviceResponse> data = basicResponse.getData();
            return data;
        } else {
            throw new ServiceException("请求物联网平台停车场信息结果失败");
        }
    }

    @Override
    public IotParkingInfoRequest buildRequest(IotParkingInfoEvent t) {
        IotParkingInfoRequest iotParkingInfoRequest = new IotParkingInfoRequest(t.getProjectId()
                                                                                        .toString(), t.getProjectId(), t.getProdCode(), true, true);
        return iotParkingInfoRequest;
    }

    @Override
    public IotBasicResponse requesting(IotParkingInfoRequest iotParkingInfoRequest) {

        ResponseEntity<IotBasicResponse<List<IotDeviceResponse>>> exchange = requester().exchange(
                iotParkingInfoRequest.getUrl(),
                HttpMethod.GET,
                iotParkingInfoRequest.getHttpEntityNoWithBody(),
                new ParameterizedTypeReference<IotBasicResponse<List<IotDeviceResponse>>>() {
                },
                iotParkingInfoRequest.getParamForGet()
                                                                                                 );

        IotBasicResponse<List<IotDeviceResponse>> body = exchange.getBody();
        return body;
    }

}
