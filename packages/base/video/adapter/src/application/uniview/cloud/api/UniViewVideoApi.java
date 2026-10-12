package com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api;

import com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.request.UnViewLiveVideoRequest;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.response.UniViewLiveVideoResponse;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.response.UniViewResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@Slf4j
@Service
public class UniViewVideoApi {

    /**
     * 获取播放地址
     *
     * @param request
     *
     * @return
     */
    public UniViewResponse<UniViewLiveVideoResponse> getLiveVideo(UnViewLiveVideoRequest request) {
        ResponseEntity<UniViewResponse<UniViewLiveVideoResponse>> getLiveVideoRes = request.requester().exchange(
                request.getRequestUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<UniViewResponse<UniViewLiveVideoResponse>>() {
                });
        log.debug("获取到的宇视云播放地址响应信息{}", getLiveVideoRes);
        Assert.isTrue(Objects.nonNull(getLiveVideoRes.getBody()), "获取到的宇视云播放地址响应信息失败");
        return getLiveVideoRes.getBody();
    }
}
