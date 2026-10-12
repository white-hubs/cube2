package com.guhecloud.cube.biz.adapter.park.server.facade.event.handler;

import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInfoEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInfoRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.ParkingInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ParkingInfoEventHandler extends AbstractSynchronizedHandler<ParkingInfoEvent, ParkingInfoRequest> {

    private final static String REQUEST_CODE = "200101";

    public List<ParkingInfoResponse> getParkingInfoResponses(ParkingInfoEvent t) {
        BasicResponse<List<ParkingInfoResponse>> response = this.handle(t);
        return response.getBody();
    }

    @Override
    public ParkingInfoRequest buildRequest(ParkingInfoEvent t) {
        return new ParkingInfoRequest();
    }

    @Override
    public BasicResponse<List<ParkingInfoResponse>> requesting(ParkingInfoRequest parkingInfoRequest) {
        ResponseEntity<BasicResponse<List<ParkingInfoResponse>>> responseResponseEntity = requester().exchange(
                parkingInfoRequest.getUrl(),
                HttpMethod.POST,
                parkingInfoRequest.getHttpEntity(REQUEST_CODE),
                new ParameterizedTypeReference<BasicResponse<List<ParkingInfoResponse>>>() {
                });
        return responseResponseEntity.getBody();
    }
}
