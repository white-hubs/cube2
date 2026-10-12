package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.service;

import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.api.AiImageRecognitionApi;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.api.dto.request.ImageRecognitionRequest;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.api.dto.response.ChatCompletionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AiImageRecognitionService {

    private final AiImageRecognitionApi aiImageRecognitionApi;

    public ChatCompletionResponse imageRecognition(ImageRecognitionRequest request) {
        request.setModel(request.getRequestConfig().getModel());
        return aiImageRecognitionApi.imageRecognition(request);
    }
}
