package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.api;

import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.api.dto.request.ImageRecognitionRequest;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.api.dto.response.ChatCompletionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@Slf4j
@Service
public class AiImageRecognitionApi {

    /**
     * AI图像识别请求
     *
     * @param request
     *
     * @return
     */
    public ChatCompletionResponse imageRecognition(ImageRecognitionRequest request) {
        ResponseEntity<ChatCompletionResponse> imageRecognitionRes = request.requester().exchange(
                request.getRequestUrl(),
                HttpMethod.POST,
                request.getHttpEntity(request),
                new ParameterizedTypeReference<>() {
                });
        log.debug("AI图像识别结果响应信息{}", imageRecognitionRes);
        Assert.isTrue(Objects.nonNull(imageRecognitionRes.getBody()), "AI图像识别结果响应信息失败");
        return imageRecognitionRes.getBody();
    }
}
