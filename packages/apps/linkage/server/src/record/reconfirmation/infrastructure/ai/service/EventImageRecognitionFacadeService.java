package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.service;

import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.api.dto.request.ImageRecognitionRequest;
import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.infrastructure.ai.api.dto.response.ChatCompletionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventImageRecognitionFacadeService {

    private final AiImageRecognitionService aiImageRecognitionService;

    public String eventImageRecognition(String cueWord, String eventFileUrl) {
        List<ImageRecognitionRequest.ContentItem> contentItems = getContentItems(cueWord, eventFileUrl);
        ImageRecognitionRequest.Message message = new ImageRecognitionRequest.Message();
        message.setContent(contentItems);
        message.setRole("user");
        ImageRecognitionRequest request = new ImageRecognitionRequest();
        request.setMessages(Collections.singletonList(message));
        request.setStream(Boolean.FALSE);
        request.setThinking(new ImageRecognitionRequest.Thinking("disabled"));
        ChatCompletionResponse chatCompletionResponse = this.aiImageRecognitionService.imageRecognition(request);
        return chatCompletionResponse.getChoices().getFirst().getMessage().getContent();
    }

    private List<ImageRecognitionRequest.ContentItem> getContentItems(String cueWord, String eventFileUrl) {
        ImageRecognitionRequest.ContentItem textContent = new ImageRecognitionRequest.ContentItem();
        textContent.setText(cueWord);
        textContent.setType("text");

        ImageRecognitionRequest.ContentItem.ImageUrl imageUrl = new ImageRecognitionRequest.ContentItem.ImageUrl();
        imageUrl.setUrl(eventFileUrl);
        ImageRecognitionRequest.ContentItem imageContent = new ImageRecognitionRequest.ContentItem();
        imageContent.setType("image_url");
        imageContent.setImageUrl(imageUrl);

        List<ImageRecognitionRequest.ContentItem> contentItems = new ArrayList<>();
        contentItems.add(textContent);
        contentItems.add(imageContent);
        return contentItems;
    }
}
