package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade;

import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade.request.TTsPlayTextRequest;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade.response.TTsPlayTextResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * 保伦itcV7700API接口
 *
 * @author lanchun
 * @date 2024/11/7 11:05
 */
@Slf4j
@Service
public class BaoLunV7700ApiService {

    /**
     * ttx文本播放
     *
     * @param request
     *
     * @return
     */
    public TTsPlayTextResponse ttsPlayText(TTsPlayTextRequest request) {
        ResponseEntity<TTsPlayTextResponse> response = request.requester().exchange(
                request.getUrl(),
                HttpMethod.PUT,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<TTsPlayTextResponse>() {
                });
        log.debug("保伦ITC V7700tts文本播放响应信息{}", response);
        Assert.isTrue(Objects.nonNull(response.getBody()), "保伦ITC V7700tts文本播放失败");
        Assert.isTrue(200 == response.getBody().getResult(), "保伦ITC V7700tts文本播放失败");
        return response.getBody();
    }
}
