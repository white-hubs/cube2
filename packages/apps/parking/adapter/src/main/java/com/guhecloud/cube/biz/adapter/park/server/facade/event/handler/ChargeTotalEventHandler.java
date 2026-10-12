package com.guhecloud.cube.biz.adapter.park.server.facade.event.handler;

import com.guhecloud.cube.biz.adapter.park.server.facade.event.ChargeTotalQueryEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ChargeTotalRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.ChargeTotalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 收费统计处理
 *
 * @author lanchun
 * @date 2022/10/8 14:44
 */
@Slf4j
@Component
public class ChargeTotalEventHandler extends AbstractSynchronizedHandler<ChargeTotalQueryEvent, ChargeTotalRequest> {

    private final static String REQUEST_CODE = "200179";

    public List<ChargeTotalResponse> getChargeTotalResponses(final ChargeTotalQueryEvent chargeTotalQueryEvent) {
        BasicResponse<List<ChargeTotalResponse>> response = this.handle(chargeTotalQueryEvent);
        return response.getBody();
    }

    @Override
    public ChargeTotalRequest buildRequest(ChargeTotalQueryEvent t) {
        return t.getChargeTotalRequest();
    }

    @Override
    public BasicResponse<List<ChargeTotalResponse>> requesting(ChargeTotalRequest chargeTotalRequest) {
        ResponseEntity<BasicResponse<List<ChargeTotalResponse>>> responseEntity = this.requester().exchange(
                chargeTotalRequest.getUrl(),
                HttpMethod.POST,
                chargeTotalRequest.getHttpEntity(REQUEST_CODE, chargeTotalRequest),
                new ParameterizedTypeReference<BasicResponse<List<ChargeTotalResponse>>>() {
                }
                                                                                                           );
        return responseEntity.getBody();
    }
}
